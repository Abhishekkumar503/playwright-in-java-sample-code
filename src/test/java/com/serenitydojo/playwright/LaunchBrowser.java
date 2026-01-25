package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.Arrays;

class LaunchBrowser {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;
    Page page;

    @BeforeSuite(alwaysRun = true)
    public static void setUpBrowser()
    {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.
                LaunchOptions().
                setHeadless(true).
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
        page.navigate("https://www.practicesoftwaretesting.com/");
        System.out.println("Browser launched Successfully!");
    }

    @AfterSuite(alwaysRun = true)
    public void setdown()
    {
        browser.close();
        playwright.close();
    }
}
