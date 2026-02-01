package com.serenitydojo.playwright.Tracing;

import com.microsoft.playwright.*;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

public class TracingAndDebugging {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;
    Page page;

    LocalDateTime now = LocalDateTime.now();


    @BeforeAll
    public static void setUpBrowser()
    {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.
                LaunchOptions().
                setHeadless(false).
                setArgs(Arrays.asList("--no-sandbox",
                        "--disable-gpu",
                        "--disable-extensions",
                        "--start-maximized"))
        );
        browserContext = browser.newContext();
    }

    @BeforeEach
    void setUpTrace()
    {
        browserContext.tracing().start(
                new Tracing.StartOptions()
                        .setSnapshots(true)
                        .setScreenshots(true)
                        .setSources(true)

        );
        page = browserContext.newPage();
    }

    @AfterEach
    public void RecordTrace(TestInfo testInfo)
    {
        String name = testInfo.getTestMethod()
                .map(method -> method.getName())
                .orElse("unknown-test");

        browserContext.tracing().stop(
                new Tracing.StopOptions()
                        .setPath(Paths.get("target/Trace/trace-" +name+"-"+now+".zip"))
        );
        page.close();
    }


    @AfterAll
     public static void setdown()
    {
        browser.close();
        playwright.close();
    }
    @Test
    void shouldShowThePageTitle() {

        page.navigate("https://www.practicesoftwaretesting.com/");
        System.out.println(page.title());
        Assert.assertEquals(page.title(),"Practice Software Testing - Toolshop - v5.0");

    }

    @Test
    void shouldSearchByKeyword() {
        page.navigate("https://www.practicesoftwaretesting.com/");
        System.out.println(page.title() + " 2");
        page.locator("#search-query").fill("Pliers"); //ID Locator
        Assert.assertEquals(page.title(),"Practice Software Testing - Toolshop - v5.0");
        page.locator("[data-test='search-submit']").click();
        page.waitForSelector("[data-test='search_completed']");
        int totalNoOfResults = page.locator(".card-body").count();
        Assert.assertEquals(totalNoOfResults,4);
    }



}
