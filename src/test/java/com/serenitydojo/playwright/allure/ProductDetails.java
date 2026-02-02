package com.serenitydojo.playwright.allure;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Prodcut Details")
@Feature("Product Details")
public class ProductDetails {
    private final Page page;

    ProductDetails(Page page) {
        this.page = page;
    }

    @DisplayName("Increase Quanity")
    @Story("Increasing Quantity")
    @Step("Increasing quantity")
    public void increaseQuanityBy(int increment) {
        for (int i = 1; i <= increment; i++) {
            page.getByTestId("increase-quantity").click();
        }
    }

    @DisplayName("Adding to cart")
    @Story("Add to cart")
    @Step("Add to cart")
    public void addToCart() {
        page.waitForResponse(
                response -> response.url().contains("/carts") && response.request().method().equals("POST"),
                () -> {
                    page.getByText("Add to cart").click();
                    page.getByRole(AriaRole.ALERT).click();
                }
        );
    }
}
