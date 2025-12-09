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
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        Page page = browser.newPage();
        page.navigate("https://www.practicesoftwaretesting.com/");
        System.out.println(page.title());
        Assert.assertEquals(page.title(),"Practice Software Testing - Toolshop - v5.0");

        browser.close();
        playwright.close();
    }

    @Test
    void shouldSearchByKeyword() throws InterruptedException {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        Page page = browser.newPage();
        page.navigate("https://www.practicesoftwaretesting.com/");
        System.out.println(page.title());

        page.locator("#search-query").fill("Pliers"); //ID Locator
        Thread.sleep(1000); // HardCoded wait
        Assert.assertEquals(page.title(),"Practice Software Testing - Toolshop - v5.0");
        page.locator("//*[@id='filters']/form[2]/div/button[2]").click();
        Thread.sleep(2000);
        int totalNoOfResults = page.locator(".card-body").count();
        Assert.assertEquals(totalNoOfResults,4);
        browser.close();
        playwright.close();
    }


}
