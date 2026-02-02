package com.serenitydojo.playwright.allure;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

@DisplayName("Get All List Of Products")
@Story("Getting All Product List")
public class ProductList {
    private final Page page;

    ProductList(Page page) {
        this.page = page;
    }


    @Step("Getting Products")
    public List<String> getProductNames() {
        return page.getByTestId("product-name").allInnerTexts();
    }

    @Step("Displaying Product Details")
    public void viewProductDetails(String productName) {
        page.locator(".card").getByText(productName).click();
    }
}
