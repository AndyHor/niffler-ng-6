package guru.qa.niffler.test.myweb;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.myannotations.meta.WebTest;
import guru.qa.niffler.mypages.FriendsPage;
import guru.qa.niffler.mypages.LoginPage;
import guru.qa.niffler.mypages.MainPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.myextensions.UsersQueueExtension.StaticUserExtended;
import static guru.qa.niffler.jupiter.myextensions.UsersQueueExtension.UserTypeExtended;
import static guru.qa.niffler.jupiter.myextensions.UsersQueueExtension.UserTypeExtended.Type.*;

@WebTest
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void friendShouldBePresentInFriendsTable(@UserTypeExtended(WITH_FRIENDS) StaticUserExtended user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        MainPage mainPage = new MainPage();
        mainPage.checkMainPage();
        FriendsPage friendsPage = mainPage.selectFriends();
        friendsPage.checkFriendsPage();
        friendsPage.checkFriendNameInFriends(user.friend());
    }

    @Test
    void friendsTableShouldBeEmptyForNewUser(@UserTypeExtended(EMPTY) StaticUserExtended user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        MainPage mainPage = new MainPage();
        mainPage.checkMainPage();
        FriendsPage friendsPage = mainPage.selectFriends();
        friendsPage.checkFriendsPage();
        friendsPage.checkFriendsTableIsEmpty();
    }

    @Test
    void incomeInvitationBePresentInFriendsTable(@UserTypeExtended(WITH_INCOME_FRIEND_REQUEST) StaticUserExtended user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        MainPage mainPage = new MainPage();
        mainPage.checkMainPage();
        FriendsPage friendsPage = mainPage.selectFriends();
        friendsPage.checkFriendsPage();
        friendsPage.checkFriendIncomeRequestExistInTable(user.incomeFriendRequest());
    }

    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(@UserTypeExtended(WITH_OUTCOME_FRIEND_REQUEST) StaticUserExtended user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        MainPage mainPage = new MainPage();
        mainPage.checkMainPage();
        FriendsPage friendsPage = mainPage.selectFriends();
        friendsPage.checkFriendsPage();
        friendsPage.selectAllPeople();
        friendsPage.checkFriendsPage();
        friendsPage.checkFriendOutcomeRequestExistInTable(user.outcomeFriendRequest());
    }
}
