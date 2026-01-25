package com.serenitydojo.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.LoadState;
import com.serenitydojo.playwright.browserSetup.Base;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

@UsePlaywright(Base.class)
public class AssertJExample {

    @BeforeEach
    void openHomepage(Page page){
        page.navigate("https://www.practicesoftwaretesting.com/");
        page.waitForCondition(()-> page.locator(".card").count() >= 9);
    }

    @Test
    void findPriceOfAllProducts(Page page)
    {
        List<Double> price = page.locator("[data-test='product-price']")
                .allTextContents()
                .stream()
                .map(amount -> Double.parseDouble(amount.replace("$","")))
                .toList();
        System.out.println(price.size());
        price.forEach(System.out::println);

        Assertions.assertThat(price)
                .isNotEmpty()
                .allMatch(p-> p > 0)
                .doesNotContain(0.0)
                .allMatch(p -> p < 1000);
    }

    @Test
    void shortInAlpabeticalOrder(Page page)
    {
        page.selectOption(".form-select","Name (A - Z)");
        page.locator("[data-test='sorting_completed']").first().waitFor();
       List<String> productName = page.locator(".card-title").allInnerTexts().stream().map(String::trim).toList();
       Assertions.assertThat(productName).isSorted();
       productName.forEach(System.out::println);
    }

    @Test
    void shortInDecendingOrder(Page page)
    {
        page.selectOption(".form-select","Name (Z - A)");
        page.locator("[data-test='sorting_completed']").first().waitFor();
        List<String> productName = page.locator(".card-title").allInnerTexts().stream().map(String::trim).toList();
        Assertions.assertThat(productName).isSortedAccordingTo(Comparator.reverseOrder());
        productName.forEach(System.out::println);
    }
}
