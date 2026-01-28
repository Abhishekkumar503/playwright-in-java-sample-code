package com.serenitydojo.playwright.login;


import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.serenitydojo.playwright.Users;
import com.serenitydojo.playwright.browserSetup.Base;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@UsePlaywright(Base.class)
public class LoginWithRegisteredUserTest {

    /**
     * @param page
     *
     * 1. Create USER then Register user
     * 2. Navigate to loginPage and Login with new USER
     * 3. Validate you are navigate to a correct Account
     */

    @Test
        @DisplayName("Should be login in login page")
        void should_Be_Login_With_User(Page page)
    {
        // Register for new User
        Users.User user = Users.User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);

        // Login to the page via login page
        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs(user);

        // Check that we are right Account page
        Assertions.assertThat(loginPage.title()).isEqualTo("My account");
    }

}
