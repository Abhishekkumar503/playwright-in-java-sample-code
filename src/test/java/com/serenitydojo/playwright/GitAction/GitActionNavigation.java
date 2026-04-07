package com.serenitydojo.playwright.GitAction;

import com.microsoft.playwright.Page;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;

@Feature("GitHub Actions Navigation")
@DisplayName("GitHub Actions Navigation")
public class GitActionNavigation {
    private final Page page;

    GitActionNavigation(Page page) {
        this.page = page;
    }

    @DisplayName("Navigate to Sauce Demo")
    @Story("Opening Home Page")
    @Step("Navigating to home page")
    public void navigateToHomePage() {
        page.navigate("https://www.saucedemo.com");
    }

    @DisplayName("Navigate to Inventory Page")
    @Story("Product Navigation")
    @Step("Navigating to inventory page")
    public void navigateToProductPage() {
        page.navigate("https://www.saucedemo.com/inventory.html");
    }

    @DisplayName("Verify Page Title")
    @Step("Verifying page title")
    public String getPageTitle() {
        return page.title();
    }

    @DisplayName("Wait for Page Load")
    @Step("Waiting for page to load")
    public void waitForPageLoad() {
        page.waitForLoadState();
    }
}


