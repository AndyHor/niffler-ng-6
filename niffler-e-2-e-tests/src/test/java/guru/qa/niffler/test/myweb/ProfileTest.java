package guru.qa.niffler.test.myweb;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.myannotations.Category;
import guru.qa.niffler.jupiter.myannotations.Spending;
import guru.qa.niffler.jupiter.myannotations.User;
import guru.qa.niffler.jupiter.myannotations.meta.WebTest;
import guru.qa.niffler.mymodel.CategoryJson;
import guru.qa.niffler.mypages.LoginPage;
import guru.qa.niffler.mypages.MainPage;
import guru.qa.niffler.mypages.ProfilePage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {

    private static final Config CFG = Config.getInstance();

    @User(
            username = "duck2",
            categories = {@Category(
                    archived = false
            )},
            spendings = {
                    @Spending(
                            category = "Milk15",
                            description = "Ammm",
                            amount = 354
                    )
            }
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck2", "12345");
        MainPage mainPage = new MainPage();
        mainPage.checkMainPage();
        ProfilePage profilePage = mainPage.selectProfile();
        profilePage.checkCategory(categoryJson.name());
    }

    @User(
            username = "duck2",
            categories = {@Category(
                    archived = true
            )},
            spendings = {
                    @Spending(
                            category = "Milk16",
                            description = "Ammm",
                            amount = 354
                    )
            }
    )
    @Test
    void acrchivedCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck2", "12345");
        MainPage mainPage = new MainPage();
        mainPage.checkMainPage();
        ProfilePage profilePage = mainPage.selectProfile();
        profilePage.showArchived()
                .checkCategory(categoryJson.name());
    }
}
