package com.serenitydojo.playwright.GitAction;

import com.microsoft.playwright.Page;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;

@Feature("GitHub Actions Cart Operations")
@DisplayName("Cart Operations in GitAction")
public class GitActionCartActions {
    private final Page page;

    GitActionCartActions(Page page) {
        this.page = page;
    }

    @DisplayName("Add Product to Cart")
    @Story("Add to Cart")
    @Step("Adding product to shopping cart")
    public void addToCart() {
        try {
            // Try to find add to cart button on inventory page
            var buttons = page.locator("button[class*='btn-primary']").all();
            if (!buttons.isEmpty()) {
                buttons.get(0).click();
                page.waitForTimeout(500);
            }
        } catch (Exception e) {
            System.out.println("Unable to add to cart: " + e.getMessage());
        }
    }

    @DisplayName("Remove Product from Cart")
    @Story("Remove from Cart")
    @Step("Removing product from shopping cart")
    public void removeFromCart() {
        try {
            page.locator("[data-test='remove']").first().click();
            page.waitForTimeout(500);
        } catch (Exception e) {
            System.out.println("Unable to remove from cart: " + e.getMessage());
        }
    }

    @DisplayName("Get Cart Quantity")
    @Story("Cart Information")
    @Step("Retrieving current cart quantity")
    public int getCartQuantity() {
        try {
            String quantity = page.locator("[data-test='shopping-cart-badge']").textContent();
            return Integer.parseInt(quantity != null ? quantity.trim() : "0");
        } catch (Exception e) {
            return 0;
        }
    }

    @DisplayName("Open Shopping Cart")
    @Story("Cart Navigation")
    @Step("Opening shopping cart page")
    public void openCart() {
        try {
            page.locator("[data-test='shopping-cart-link']").click();
            page.waitForLoadState();
        } catch (Exception e) {
            System.out.println("Unable to open cart: " + e.getMessage());
        }
    }

    @DisplayName("Checkout")
    @Story("Checkout Process")
    @Step("Proceeding to checkout")
    public void proceedToCheckout() {
        try {
            page.locator("[data-test='checkout']").click();
            page.waitForLoadState();
        } catch (Exception e) {
            System.out.println("Unable to proceed to checkout: " + e.getMessage());
        }
    }

    @DisplayName("Continue Shopping")
    @Story("Shopping Navigation")
    @Step("Continuing shopping")
    public void continueShopping() {
        try {
            page.locator("[data-test='continue-shopping']").click();
            page.waitForLoadState();
        } catch (Exception e) {
            System.out.println("Unable to continue shopping: " + e.getMessage());
        }
    }
}



