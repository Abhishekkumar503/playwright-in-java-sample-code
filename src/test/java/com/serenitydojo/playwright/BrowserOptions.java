package com.serenitydojo.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Arrays;


public class BrowserOptions {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeTest
    private void setup()
    {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.
                LaunchOptions().
                setHeadless(false).
                setArgs(Arrays.asList("--no-sandbox",
                        "--disable-gpu",
                        "--disable-extensions"))
        );
        page = browser.newPage();
    }
    @AfterTest
    private void setdown()
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
        System.out.println(page.title());

        page.locator("#search-query").fill("Pliers"); //ID Locator
        Thread.sleep(1000); // HardCoded wait
        Assert.assertEquals(page.title(),"Practice Software Testing - Toolshop - v5.0");
        page.locator("//*[@id='filters']/form[2]/div/button[2]").click();
        Thread.sleep(2000);
        int totalNoOfResults = page.locator(".card-body").count();
        Assert.assertEquals(totalNoOfResults,4);
    }


}
