package com.serenitydojo.playwright.pageObject;

import com.microsoft.playwright.Page;

import java.util.List;

public class ProductList {
    private final Page page;

    ProductList(Page page) {
        this.page = page;
    }


    public List<String> getProductNames() {
        return page.getByTestId("product-name").allInnerTexts();
    }

    public void viewProductDetails(String productName) {
        page.locator(".card").getByText(productName).click();
    }
}
