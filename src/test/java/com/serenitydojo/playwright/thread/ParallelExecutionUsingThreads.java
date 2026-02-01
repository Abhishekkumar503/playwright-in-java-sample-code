package com.serenitydojo.playwright.thread;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Execution(ExecutionMode.CONCURRENT)
public class ParallelExecutionUsingThreads {

    // Setup threadLocal based Playwright and browser
    private static ThreadLocal<Playwright> playwright
            =ThreadLocal.withInitial(
            () -> {
                Playwright  playwright = Playwright.create();
                playwright.selectors().setTestIdAttribute("data-test");
                return playwright;
            }
    );
    private static ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
        playwright.get().chromium().launch(new BrowserType.
                LaunchOptions().
                setHeadless(false).
                setArgs(Arrays.asList("--no-sandbox",
                        "--disable-gpu",
                        "--disable-extensions",
                        "--screen-maximize"))
        )
    );

    private  BrowserContext browserContext; // no need for use static
    Page page;


    @BeforeEach
    public void setUp()
    {
        browserContext = browser.get().newContext(); // use get() to get the particular browser context
        page = browserContext.newPage();
        System.out.println("Thread: " + Thread.currentThread().getName());
    }

    @AfterEach
    public void setdown()
    {
        // Use get() to process paricular instance and make sure instance removed as well
        browser.get().close();
        browser.remove();
        playwright.get().close();
        playwright.remove();
    }
//    @Test
//    void shouldShowThePageTitle() {
//
//        page.navigate("https://www.practicesoftwaretesting.com/");
//        System.out.println(page.title());
//        Assertions.assertEquals(page.title(),"Practice Software Testing - Toolshop - v5.0");
//
//    }


    // 🔥 SAME TEST RUNS 4 TIMES IN PARALLEL
    @ParameterizedTest(name = "Parallel Run {index}")
    @ValueSource(ints = {1, 2, 3, 4})
//    @Test
    void shouldSearchByKeyword() throws InterruptedException {
        page.navigate("https://www.practicesoftwaretesting.com/");
        System.out.println(page.title() + " 2");
        page.locator("#search-query").fill("Pliers"); //ID Locator
        Assertions.assertEquals(page.title(),"Practice Software Testing - Toolshop - v5.0");
        page.locator("[data-test='search-submit']").click();
        page.waitForSelector("[data-test='search_completed']");
        int totalNoOfResults = page.locator(".card-body").count();
        Assertions.assertEquals(totalNoOfResults,4);
    }


}
