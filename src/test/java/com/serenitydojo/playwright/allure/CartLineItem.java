package com.serenitydojo.playwright.allure;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

@Feature("Items in Cart")
@DisplayName("Cart Line Items")
public record CartLineItem(String title, int quantity, double price, double total) {


}
