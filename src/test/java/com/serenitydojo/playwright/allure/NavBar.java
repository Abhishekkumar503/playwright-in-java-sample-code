package com.serenitydojo.playwright.allure;

import com.microsoft.playwright.Page;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;

@Feature("Navigation")
@DisplayName("Navigation")
public class NavBar {
    private final Page page;

    NavBar(Page page) {
        this.page = page;
    }

    @DisplayName("Opening the Cart")
    @Step("Cart Opened")
    public void openCart() {
        page.getByTestId("nav-cart").click();
    }

    @Story("Home Page Opened")
    @Step("Navigation to Home Page")
    public void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }
}
