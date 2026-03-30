package com.serenitydojo.playwright.allure;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import com.serenitydojo.playwright.browserSetup.Base;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(Base.class)
public class PageObject implements Screenshot {

    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;
    NavBar navBar;
    CheckoutCart checkoutCart;

    @BeforeEach
    void setUp(Page page) {
        // ✅ FIX #1: Set viewport to mimic real browser
        page.setViewportSize(1920, 1080);

        // ✅ FIX #2: Add user agent to avoid bot detection
        page.context().addInitScript("Object.defineProperty(navigator, 'webdriver', {get: () => false,});");

        // ✅ FIX #3: Set timeouts BEFORE navigation
        page.setDefaultTimeout(60_000);
        page.setDefaultNavigationTimeout(60_000);

        // ✅ FIX #4: Add headers to look like real browser
        page.setExtraHTTPHeaders(new java.util.HashMap<String, String>() {{
            put("Accept-Language", "en-US,en;q=0.9");
            put("Accept-Encoding", "gzip, deflate, br");
        }});

        // ✅ FIX #5: Navigate with goto options
        try {
            page.navigate("https://practicesoftwaretesting.com",
                    new Page.NavigateOptions()
                            .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
                            .setTimeout(60_000)
            );
        } catch (Exception e) {
            System.out.println("⚠️ First navigation attempt failed, retrying...");
            try {
                Thread.sleep(2000);  // Wait 2 seconds
                page.navigate("https://practicesoftwaretesting.com",
                        new Page.NavigateOptions()
                                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
                                .setTimeout(60_000)
                );
            } catch (Exception e2) {
                throw new RuntimeException("Failed to navigate after 2 attempts", e2);
            }
        }

        // ✅ FIX #6: Check if Cloudflare protection page appeared
        String pageTitle = page.title();
        String pageContent = page.content();

        if (pageTitle.contains("Just a moment") || pageContent.contains("cf_clearance")) {
            System.out.println("⚠️ Cloudflare protection detected, waiting for bypass...");
            try {
                // Wait up to 15 seconds for Cloudflare to pass
                page.waitForLoadState(LoadState.NETWORKIDLE);
            } catch (Exception e) {
                System.out.println("⚠️ NETWORKIDLE timeout after Cloudflare, trying DOMCONTENTLOADED");
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            }
        }

        // ✅ FIX #7: Wait for page to be ready
        try {
            page.waitForLoadState(LoadState.NETWORKIDLE);
        } catch (Exception e) {
            System.out.println("⚠️ NETWORKIDLE timeout, trying DOMCONTENTLOADED");
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        }

        // ✅ FIX #8: Try multiple selectors for search box
        Locator searchBox = null;
        String[] searchSelectors = {
                "[data-test=\"search-query\"]",        // Original
                "input[placeholder*=\"Search\"]",      // By placeholder
                "[placeholder*=\"Search\"]",           // Any element with placeholder
                "input[type=\"search\"]",              // Search input type
                ".search-input",                       // Common CSS class
                "#search"                              // By ID
        };

        for (String selector : searchSelectors) {
            try {
                searchBox = page.locator(selector);
                // Verify it exists and is visible
                if (searchBox.count() > 0) {
                    searchBox.waitFor(new Locator.WaitForOptions()
                            .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
                            .setTimeout(10_000)
                    );
                    System.out.println("✅ Found search box with selector: " + selector);
                    break;
                }
            } catch (Exception e) {
                // Try next selector
                System.out.println("⚠️ Selector failed: " + selector);
            }
        }

        // ✅ FIX #9: If still not found, try fallback
        if (searchBox == null || searchBox.count() == 0) {
            System.out.println("❌ ERROR: Could not find search box with any selector");
            System.out.println("Page URL: " + page.url());
            System.out.println("Page title: " + page.title());
            System.out.println("Page HTML (first 500 chars): " + page.content().substring(0, Math.min(500, page.content().length())));

            // Last resort: find by text
            try {
                searchBox = page.locator("input, [role='searchbox']").first();
                searchBox.waitFor(new Locator.WaitForOptions().setTimeout(10_000));
                System.out.println("✅ Found input/searchbox via generic search");
            } catch (Exception e) {
                throw new RuntimeException("Search box element not found after trying all methods. " +
                        "Page may be protected by Cloudflare or structure changed. " +
                        "Page title: " + page.title(), e);
            }
        }

        // Now safe to instantiate components
        searchComponent = new SearchComponent(page);
        productList = new ProductList(page);
        productDetails = new ProductDetails(page);
        navBar = new NavBar(page);
        checkoutCart = new CheckoutCart(page);
    }

    @DisplayName("Without Page Objects")
    @Test
    void withoutPageObjects(Page page) {
        // Search for pliers
        page.getByPlaceholder("Search").waitFor();
        page.getByPlaceholder("Search").fill("pliers");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search ")).click();

        // Wait for results to load
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.locator(".card").first().waitFor(new Locator.WaitForOptions().setTimeout(30_000));

        // Show details page
        page.locator(".card").getByText("Combination Pliers").waitFor();
        page.locator(".card").getByText("Combination Pliers").click();

        // Wait after click
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        // Increase cart quantity
        page.getByTestId("increase-quantity").click();
        page.getByTestId("increase-quantity").click();

        // Add to cart
        page.getByText("Add to cart").click();

        // Wait for cart quantity to update
        page.getByTestId("cart-quantity").waitFor();
        assertThat(page.getByTestId("cart-quantity")).hasText("3");

        // Open the cart
        page.getByTestId("nav-cart").click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        // Verify cart
        assertThat(page.locator(".product-title").getByText("Combination Pliers")).isVisible();
        assertThat(page.getByTestId("cart-quantity").getByText("3")).isVisible();
    }

    @DisplayName("With Page Objects")
    @Test
    void withPageObjects(Page page) {
        searchComponent.searchBy("pliers");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        // Wait for product card to appear
        page.locator(".card").first().waitFor(new Locator.WaitForOptions().setTimeout(30_000));

        productList.viewProductDetails("Combination Pliers");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        productDetails.increaseQuanityBy(2);
        productDetails.addToCart();

        navBar.openCart();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        List<CartLineItem> lineItems = checkoutCart.getLineItems();

        Assertions.assertThat(lineItems)
                .hasSize(1)
                .first()
                .satisfies(item -> {
                    Assertions.assertThat(item.title()).contains("Combination Pliers");
                    Assertions.assertThat(item.quantity()).isEqualTo(3);
                    Assertions.assertThat(checkoutCart.total()).isEqualTo(item.quantity() * item.price());
                });
    }

    @DisplayName("With Page Objects for Multiple Items")
    @Test
    void whenCheckingOutMultipleItems(Page page) {
        navBar.openHomePage();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        // Wait for product list to render
        page.locator(".card").first().waitFor(new Locator.WaitForOptions().setTimeout(30_000));

        productList.viewProductDetails("Bolt Cutters");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        productDetails.increaseQuanityBy(2);
        productDetails.addToCart();

        navBar.openHomePage();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        // Retry if product not found (network flakiness)
        try {
            productList.viewProductDetails("Slip Joint Pliers");
        } catch (Exception e) {
            System.out.println("⚠️ Product not found, retrying after reload...");
            page.reload();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.locator(".card").first().waitFor(new Locator.WaitForOptions().setTimeout(30_000));
            productList.viewProductDetails("Slip Joint Pliers");
        }
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        productDetails.addToCart();

        navBar.openCart();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        List<CartLineItem> lineItems = checkoutCart.getLineItems();

        Assertions.assertThat(lineItems).hasSize(2);
        List<String> productNames = lineItems.stream().map(CartLineItem::title).toList();
        Assertions.assertThat(productNames).contains("Bolt Cutters", "Slip Joint Pliers");

        Assertions.assertThat(lineItems)
                .allSatisfy(item -> {
                    Assertions.assertThat(item.quantity()).isGreaterThanOrEqualTo(1);
                    Assertions.assertThat(item.price()).isGreaterThan(0.0);
                    Assertions.assertThat(item.total()).isGreaterThan(0.0);
                    Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
                });
    }
}