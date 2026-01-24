package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.Arrays;

public class CSSLocator {
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
    void contactPage() {
        page.navigate("https://www.practicesoftwaretesting.com/");
        page.getByText("Contact").click();
        page.locator("#first_name").fill("Abhishek");
        page.locator("#last_name").fill("Kumar");
        page.getByPlaceholder("Your email *").fill("test@test.com");
        page.selectOption("#subject","Payments");
        page.locator("#message").fill("Maa ka bharosa, dentist ka Sujhaya. Colgate!");
        page.locator(".btnSubmit").click();
    }
}
