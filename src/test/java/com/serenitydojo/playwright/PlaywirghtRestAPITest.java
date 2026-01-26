package com.serenitydojo.playwright;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.UsePlaywright;
import com.serenitydojo.playwright.browserSetup.Base;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;


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

    @Nested
    class MakingAPICall
    {

        /**
         *
         * @param name
         * @param price
         *
         * Class: MakingAPICall
         * * Purpose: Fetch products from API and verify they appear correctly on the UI.
         * * Inner record: product(String name, Double price) → simple data holder for each product.
         *
         * 1. setupRequestContext()
         * * Annotation: @BeforeAll → runs once before all tests.
         * * Function: Creates an APIRequestContext to make HTTP calls.
         * * Key points:
         *     * Sets API base URL: "https://api.practicesoftwaretesting.com"
         *     * Adds JSON headers: Accept: application/json
         *
         * 2. products()
         * * Annotation: @MethodSource → supplies data for @ParameterizedTest.
         * * Function: Calls /products?page=2 API, parses JSON, converts to product objects.
         * * Key points:
         *     * Asserts response status is 200.
         *     * Uses Gson to extract name and price.
         *     * Returns Stream<product> for tests.
         *
         * 3. checkKnownProducts(product products)
         * * Annotation: @ParameterizedTest → runs for each product from products().
         * * Function: Searches product on UI and verifies name & price.
         * * Steps:
         *     1. Navigate to homepage: page.navigate(...)
         *     2. Fill search input: page.fill(...)
         *     3. Click search: page.click(...)
         *     4. Filter product cards by matching name and price using .locator().filter().
         *
         * Flow summary:
         * 1. Setup API context → setupRequestContext()
         * 2. Fetch products → products()
         * 3. Verify each product in UI → checkKnownProducts()
         *
         */
        record product (String name, Double price){}

        private static APIRequestContext requestContext;

        // 1. setup Client Request
        @BeforeAll
        public static void setupRequestContext()
        {
            requestContext = Playwright.create().request().newContext(
                    new APIRequest.NewContextOptions()
                            .setBaseURL("https://api.practicesoftwaretesting.com")
                            .setExtraHTTPHeaders(new HashMap<>(){{
                                put("Accept","application/json");
                            }})
            );
        }

        // 3. Validation product in UI with API response values
        @DisplayName("Check presence of know data")
        @ParameterizedTest(name = "Checking product {0}")
        @MethodSource("products")
        void checkKnownProducts(product products)
        {
            page.navigate("https://www.practicesoftwaretesting.com/");
            page.fill("[placeholder='Search']",products.name);
            page.click("button:has-text('Search')");

//            Check that the product appears with the correct name and price

            Locator productCard = page.locator(".card")
                    .filter(
                            new Locator.FilterOptions().
                                    setHasText(products.name)
                                    .setHasText(Double.toString(products.price))
                    );

        }

        // 2. fetching name and price form API response
        static Stream<product> products(){
            APIResponse response = requestContext.get("/products?page=2");
            Assertions.assertThat(response.status()).isEqualTo(200);

            JsonObject JsonObject = new Gson().fromJson(response.text(), JsonObject.class);
            JsonArray data = JsonObject.getAsJsonArray("data");

            return data.asList().stream()
                    .map(jsonElement -> {
                        JsonObject productJson = jsonElement.getAsJsonObject();
                        return new product(
                                productJson.get("name").getAsString(),
                                productJson.get("price").getAsDouble()
                        );
                    });
        }
    }



}
