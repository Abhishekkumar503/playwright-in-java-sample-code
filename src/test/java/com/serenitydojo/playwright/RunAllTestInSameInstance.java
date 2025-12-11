package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Arrays;


public class RunAllTestInSameInstance {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;
    Page page;

    @BeforeClass
    public static void setUpBrowser()
    {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.
                LaunchOptions().
                setHeadless(false).
                setArgs(Arrays.asList("--no-sandbox",
                        "--disable-gpu",
                        "--disable-extensions"))
        );
        browserContext = browser.newContext();
    }

    @BeforeMethod
    public void setUp()
    {
        page = browserContext.newPage();
    }
    @AfterClass
    public void setdown()
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
    void shouldSearchByKeyword() throws InterruptedException {
        page.navigate("https://www.practicesoftwaretesting.com/");
        System.out.println(page.title() + " 2");
        page.locator("#search-query").fill("Pliers"); //ID Locator
        Assert.assertEquals(page.title(),"Practice Software Testing - Toolshop - v5.0");
        page.locator("//*[@id='filters']/form[2]/div/button[2]").click();
        Thread.sleep(1500);
        int totalNoOfResults = page.locator(".card-body").count();
        Assert.assertEquals(totalNoOfResults,4);
    }


}
