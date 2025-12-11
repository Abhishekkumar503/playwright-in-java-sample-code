package com.serenitydojo.playwright;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

@UsePlaywright(OptionFactory.MyOption.class)
public class OptionFactory {
    // In this just declare Page page as method param and this will work with JUNIT only Not supported with Testng

    public static class MyOption implements OptionsFactory
    {

        @Override
        public Options getOptions() {
            return new Options().setHeadless(false)
                    .setLaunchOptions( new BrowserType.LaunchOptions().setArgs(
                            Arrays.asList("--no-sandbox",
                                    "--disable-gpu",
                                    "--disable-extensions"))
                    );
        }
    }
    @Test
    void shouldShowThePageTitle(Page page) {

        page.navigate("https://www.practicesoftwaretesting.com/");
        System.out.println(page.title());
        Assert.assertEquals(page.title(),"Practice Software Testing - Toolshop - v5.0");

    }

    @Test
    void shouldSearchByKeyword(Page page) throws InterruptedException {
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
