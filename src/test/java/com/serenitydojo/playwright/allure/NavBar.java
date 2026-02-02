package com.serenitydojo.playwright.allure;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class NavBar {
    private final Page page;

    NavBar(Page page) {
        this.page = page;
    }

    @Step("Opening the cart")
    public void openCart() {
        page.getByTestId("nav-cart").click();
    }

    @Step("Opening Home Page")
    public void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }
}
