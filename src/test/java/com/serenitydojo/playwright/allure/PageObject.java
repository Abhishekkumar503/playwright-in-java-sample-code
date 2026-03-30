package com.serenitydojo.playwright.allure;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
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
        // ✅ FIX #1: Set timeout BEFORE navigation
        page.setDefaultTimeout(60_000);
        page.setDefaultNavigationTimeout(60_000);

        // ✅ FIX #2: Navigate to page
        page.navigate("https://www.practicesoftwaretesting.com/");

        // ✅ FIX #3: CRITICAL - Wait for page to fully load
        // Try NETWORKIDLE first, fall back to DOMCONTENTLOADED
        try {
            page.waitForLoadState(LoadState.NETWORKIDLE);
        } catch (Exception e) {
            System.out.println("⚠️ NETWORKIDLE timeout, trying DOMCONTENTLOADED");
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        }

        // ✅ FIX #4: Verify search box is visible before proceeding
        Locator searchBox = page.getByPlaceholder("Search");
        try {
            searchBox.waitFor(new Locator.WaitForOptions().setTimeout(30_000));
        } catch (Exception e) {
            System.err.println("❌ ERROR: Search box not found after 30 seconds");
            System.err.println("Page URL: " + page.url());
            System.err.println("Page title: " + page.title());
            throw new RuntimeException("Search box element not found. Page may not have loaded properly.", e);
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

        // ✅ FIX: Wait for results to load
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.locator(".card").first().waitFor(new Locator.WaitForOptions().setTimeout(30_000));

        // Show details page
        page.locator(".card").getByText("Combination Pliers").waitFor();
        page.locator(".card").getByText("Combination Pliers").click();

        // ✅ FIX: Wait after click
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

        // ✅ FIX: Wait for product card to appear
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

        // ✅ FIX: Wait for product list to render
        page.locator(".card").first().waitFor(new Locator.WaitForOptions().setTimeout(30_000));

        productList.viewProductDetails("Bolt Cutters");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        productDetails.increaseQuanityBy(2);
        productDetails.addToCart();

        navBar.openHomePage();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        // ✅ FIX: Retry if product not found (network flakiness)
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