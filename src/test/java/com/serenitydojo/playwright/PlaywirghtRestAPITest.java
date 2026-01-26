package com.serenitydojo.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import com.microsoft.playwright.junit.UsePlaywright;
import com.serenitydojo.playwright.browserSetup.Base;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


@UsePlaywright(Base.class)
public class PlaywirghtRestAPITest {
    /**
     * 1. Prepare MockResponse for single item
     * 2. Route the API response to what you want to check.
     * 3. Then navigate to the URL (if you naviage to first then Route will not work).
     * 4. Declare all code after that
     */

    private Page page;

    @BeforeEach
    public void setPage(Page page) {
        this.page = page;
    }

    @Nested
    class MockingAPIResponses
    {

        @Test
        void whenASingleItemIsFound()
        {
            // https://api.practicesoftwaretesting.com/products?page=0&sort=name,asc&between=price,1,100&is_rental=false
            page.route("**/products**",route -> {
                route.fulfill(
                        new Route.FulfillOptions()
                                .setBody(MockingSearchResponse.RESPONSE_WITH_A_SINGLE_ENTRY)
                                .setStatus(200)
                                .setContentType("application/json")
                );
            });
            page.navigate("https://www.practicesoftwaretesting.com/");
            page.getByPlaceholder("Search").fill("Hammer");
            page.getByPlaceholder("Search").press("Enter");

            //Assert
            System.out.println(page.locator(".card-title").textContent().trim() + " : " + page.locator(".card-title").count());
            Assertions.assertThat(page.locator(".card-title").count()).isEqualTo(1);
            Assertions.assertThat(page.locator(".card-title").first().textContent().trim()).isEqualTo("Thor Hammer");

        }

        @Test
        void whenNoItemAreFound()
        {
            page.route("**/products**",route -> {
                route.fulfill(
                        new Route.FulfillOptions()
                                .setBody(MockingSearchResponse.RESPONSE_WITH_NO_ENTRIES)
                                .setStatus(200)
                                .setContentType("application/json")
                );
            });
            page.navigate("https://www.practicesoftwaretesting.com/");
            page.getByPlaceholder("Search").fill("udai");
            page.getByPlaceholder("Search").press("Enter");

            Assertions.assertThat(page.getByTestId("search_completed").count()).isEqualTo(0);
            Assertions.assertThat(page.getByTestId("search_completed").first().textContent().trim()).isEqualTo("There are no products found.");
        }
    }

}
