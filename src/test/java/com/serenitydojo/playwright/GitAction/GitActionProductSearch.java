package com.serenitydojo.playwright.GitAction;

import com.microsoft.playwright.Page;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

@Feature("GitHub Actions Product Filtering")
@DisplayName("Product Filtering in GitAction")
public class GitActionProductSearch {
    private final Page page;

    GitActionProductSearch(Page page) {
        this.page = page;
    }

    @DisplayName("Sort Products")
    @Story("Sorting Products")
    @Step("Sorting products by")
    public void sortProducts(String sortOption) {
        try {
            page.locator("[data-test='product-sort-container']").selectOption(sortOption);
            page.waitForLoadState();
            page.waitForTimeout(1000);
        } catch (Exception e) {
            System.out.println("Error during sort: " + e.getMessage());
        }
    }

    @DisplayName("Get All Product Names")
    @Story("Retrieving Product List")
    @Step("Getting all product names")
    public List<String> getProductNames() {
        try {
            page.waitForTimeout(1000);
            return page.locator("[data-test='inventory-item-name']").allInnerTexts();
        } catch (Exception e) {
            System.out.println("Error getting products: " + e.getMessage());
            return List.of();
        }
    }

    @DisplayName("Verify Products Exist")
    @Step("Checking if products exist")
    public boolean hasProducts() {
        try {
            var results = page.locator("[data-test='inventory-item-name']").all();
            return !results.isEmpty();
        } catch (Exception e) {
            System.out.println("Error checking products: " + e.getMessage());
            return false;
        }
    }

    @DisplayName("Select Product by Name")
    @Story("Product Selection")
    @Step("Clicking on product")
    public void selectProduct(String productName) {
        try {
            page.locator("[data-test='inventory-item-name']").getByText(productName).click();
            page.waitForLoadState();
        } catch (Exception e) {
            System.out.println("Error selecting product: " + e.getMessage());
        }
    }
}





