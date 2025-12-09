package com.serenitydojo.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;


public class ASimplePlaywrightTest {

    private static final Logger log = LoggerFactory.getLogger(ASimplePlaywrightTest.class);

    @Test
    void shouldShowThePageTitle() {
        // TODO: Write your first playwright test here

        Playwright playwright = Playwright.create();

//        This is for headless browser
//        Browser browser =  playwright.chromium().launch();

//        This used for UI ( false to headless browser )
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();
        page.navigate("https://www.google.com/");
        System.out.println(page.title());
        Assert.assertEquals(page.title(),"Google");

        browser.close();
        playwright.close();
    }
}
