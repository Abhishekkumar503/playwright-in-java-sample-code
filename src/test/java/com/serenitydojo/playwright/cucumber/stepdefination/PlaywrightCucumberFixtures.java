package com.serenitydojo.playwright.cucumber.stepdefination;

import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;

import java.util.Arrays;

public class PlaywrightCucumberFixtures {

    /*
     * Thread used to run multiple testcases in different browsers (execute in seperate process )
     */
    private static final ThreadLocal<Playwright> playwright
            = ThreadLocal.withInitial(() -> {
                Playwright playwright = Playwright.create();
                playwright.selectors().setTestIdAttribute("data-test");
                return playwright;
            }
    );

    /*
     * Thread used to run multiple testcases in different browsers (execute in seperate process )
     */
    private static final ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            playwright.get().chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(true)
                            .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
            )
    );

    /*
     * Making it private because we are calling in child class ProductCatalogStepDefinations.java
     * Static : call with out object ( Direct call )
     * final : Unmodifable
     */
    private static final ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();

    /*
     * Making it private because we are calling in child class ProductCatalogStepDefinations.java
     * Static : call with out object ( Direct call )
     * final : Unmodifable
     */
    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    /**
     *     if order = 100 not declare then compiler call first before setting up new.
     *     Then It will throw page = null error
      */
    @Before(order = 100)
    public void setUpBrowserContext() {
        browserContext.set(browser.get().newContext());
        page.set(browserContext.get().newPage());
    }

    @After(order = 100)
    public void closeContext() {
        browserContext.get().close();
    }

    /*
    get() is used to close/remove specific thread ( like Thread 1 or Thread 2 )
     */
    @AfterAll
    public static void tearDown() {
        browser.get().close();
        browser.remove();

        playwright.get().close();
        playwright.remove();
    }

    // getpage() is used to get the page in differnt class.
    public static Page getPage() {
        return page.get();
    }

    public static BrowserContext getBrowserContext() {
        return browserContext.get();
    }


}
