package com.serenitydojo.playwright.login;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.Users;

public class LoginPage {

   private Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void open()
    {
        page.navigate("https://practicesoftwaretesting.com/auth/login");
    }

    public void loginAs(Users.User user) {
        page.getByTestId("email").fill(user.email());
        page.getByTestId("password").fill("Az@10IN1");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();

    }

    public String title() {
        return page.getByTestId("page-title").textContent();
    }
}
