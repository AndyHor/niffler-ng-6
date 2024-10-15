package guru.qa.niffler.test.myweb;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.myannotations.meta.WebTest;
import guru.qa.niffler.mypages.LoginPage;
import guru.qa.niffler.mypages.MainPage;
import guru.qa.niffler.mypages.RegisterPage;
import org.junit.jupiter.api.Test;

import java.util.Random;

@WebTest
public class OperationsWithUserWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void shouldRegisterNewUser() {
        RegisterPage registerPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .checkLoginPage()
                .gotoRegisterNewUser()
                .checkRegisterPage();

        int add = new Random().nextInt(500);
        registerPage.setUserName("kat" + add);
        registerPage.setUserPassword("12345");
        registerPage.setUserPasswordSubmit("12345");
        registerPage.submitRegistration();

        new LoginPage().checkLoginPage();
    }

    @Test
    void shouldNotRegisterNewUserWithExistingUserName() {
        RegisterPage registerPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .checkLoginPage()
                .gotoRegisterNewUser()
                .checkRegisterPage();

        registerPage.setUserName("duck");
        registerPage.setUserPassword("12345");
        registerPage.setUserPasswordSubmit("12345");
        registerPage.clickRegistration();

        registerPage.checkErrorMessageContainsExpectedMessage("Username `duck` already exists");
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        RegisterPage registerPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .checkLoginPage()
                .gotoRegisterNewUser()
                .checkRegisterPage();

        int add = new Random().nextInt(500);
        registerPage.setUserName("kat" + add);
        registerPage.setUserPassword("1234");
        registerPage.setUserPasswordSubmit("12345");
        registerPage.clickRegistration();

        registerPage.checkErrorMessageContainsExpectedMessage("Passwords should be equal");
    }

    @Test
    void mainPageShouldBedisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .checkLoginPage()
                .login("duck", "12345");
        MainPage mainPage = new MainPage();
        mainPage.checkMainPage();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadcredentials() {
        LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
        loginPage.login("duck", "1234");
        loginPage.checkErrorText("Неверные учетные данные пользователя");
        loginPage.checkLoginPage();
    }
}

