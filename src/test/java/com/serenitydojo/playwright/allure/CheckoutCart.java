package com.serenitydojo.playwright.allure;

import com.microsoft.playwright.Page;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

@DisplayName("Check Out Cart")
@Feature("Check Out Cart")
public class CheckoutCart {
    private final Page page;
    CheckoutCart(Page page) {
        this.page = page;
    }

    @Story("Getting Line Items")
    @Step("Getting Line Items")
    public List<CartLineItem> getLineItems() {
        page.locator("app-cart tbody tr").first().waitFor();
        return page.locator("app-cart tbody tr")
                .all()
                .stream()
                .map(
                        row -> {
                            String title = trimmed(row.getByTestId("product-title").innerText());
                            int quantity = Integer.parseInt(row.getByTestId("product-quantity").inputValue());
                            double price = Double.parseDouble(price(row.getByTestId("product-price").innerText()));
                            double linePrice = Double.parseDouble(price(row.getByTestId("line-price").innerText()));
                            return new CartLineItem(title, quantity, price, linePrice);
                        }
                ).toList();


    }

    @Step("replacing all symbol to non space")
    private String trimmed(String s) {
        return s.strip().replace("\u00A0","");
    }

    @Step("Replace the $ symbol to non space")
    private String price(String value) {
        return value.replace("$","");
    }
    public double total() {
        String totalAmount = page.locator("[data-test='cart-total']").innerText();
        return Double.parseDouble(String.valueOf(totalAmount.replace("$", "").trim()));
    }

}



