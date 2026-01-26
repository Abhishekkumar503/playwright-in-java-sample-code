package com.serenitydojo.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.impl.WaitableNever;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.browserSetup.Base;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Replace;

import java.util.Comparator;
import java.util.List;


@UsePlaywright(Base.class)
public class Waits {
    private Page page;

    @BeforeEach
    public void setPage(Page page) {
        this.page = page;
    }

    @Nested
    class WaitingForState{

        @BeforeEach
        void openHomePage()
        {
            page.navigate("https://www.practicesoftwaretesting.com/");
            page.waitForSelector("[data-test='product-name']");
        }

        @Test
        void shouldShowAllProductName() {
            List<String> productName = page.locator(".card-title").allInnerTexts();
            Assertions.assertThat(productName).contains("Pliers", "Bolt Cutters");
        }

    }

    @Nested
    class AutomaticWait{

        @BeforeEach
        void openHomePage() {
            page.navigate("https://www.practicesoftwaretesting.com/");
        }

        @Test
        void shouldShowAllProductName() {
            var screwdriverFilter = page.getByLabel(" Screwdriver ");
            screwdriverFilter.click();
            Assertions.assertThat(screwdriverFilter.isChecked());
        }

    }

    @Nested
    class WaitforSelectorMethod{

        @BeforeEach
        void openHomePage() {
            page.navigate("https://www.practicesoftwaretesting.com/");
        }

        @Test
        void shouldShowAllProductName() {
            page.getByRole(AriaRole.MENUBAR).getByText(" Categories ").click();
            page.getByRole(AriaRole.MENUBAR).getByText("Power Tools").click();
            page.waitForSelector(".card");
            var filteredProdcut = page.getByTestId("product-name").allInnerTexts();
            filteredProdcut.forEach(System.out::println);
            Assertions.assertThat(filteredProdcut)
                    .contains("Sheet Sander" ,
                            "Belt Sander" ,
                            "Circular Saw" ,
                            "Random Orbit Sander" ,
                            "Cordless Drill 20V" ,
                            "Cordless Drill 24V" ,
                            "Cordless Drill 18V" ,
                            "Cordless Drill 12V"
                    );
        }
    }

    @Nested
    class WaitingForElementsToAppearAndDisappear{

        @BeforeEach
        void openHomePage() {
            page.navigate("https://www.practicesoftwaretesting.com/");
        }

        @Test
        void toastMessage() {
            page.getByText(" Slip Joint Pliers ").click();
            page.getByText("Add to cart").click();

            System.out.println();
            Assertions.assertThat(page.getByRole(AriaRole.ALERT).textContent()).isEqualTo(" Product added to shopping cart. ");

            // this is used for hidden Alert ( wait till hidden ends up )
            page.waitForCondition(() -> page.getByRole(AriaRole.ALERT).isHidden());
        }

    }

    @Nested
    class WaitTillCartCountupdate{

        @BeforeEach
        void openHomePage() {
            page.navigate("https://www.practicesoftwaretesting.com/");
        }

        @Test
        void cartCount() {
            page.getByText(" Slip Joint Pliers ").click();
            page.getByText("Add to cart").click();

            // Count Add to cart
            System.out.println(page.getByTestId("cart-quantity").count());

            // Above code return 0 OR [] due to cart not update faster so overcome from use wait
            page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("1"));

            // Now below line return 1
            System.out.println(page.getByTestId("cart-quantity").count());

            /**
             * Output
             * 0
             * 1
             */
        }

    }

    @Nested
    class WaitingForAPIResponses{

        @BeforeEach
        void openHomePage() {
            page.navigate("https://www.practicesoftwaretesting.com/");
        }

        @Test
        void APIResponseWait() {


            // https://api.practicesoftwaretesting.com/products?page=0&sort=price,asc&between=price,1,100&is_rental=false
            // Shorting in ASC order
            page.waitForResponse("**/products?page=0&sort=price**",
                    () -> {
                        page.selectOption(".form-select","Price (Low - High)");
                    });

            // Find all price of product in page
            var prodPrice = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream()
                    .map(price -> Double.parseDouble(price.replace("$","")))
                    .toList();
            prodPrice.forEach(System.out::println);
            Assertions.assertThat(prodPrice).isSortedAccordingTo(Comparator.reverseOrder());
        }

    }
}
