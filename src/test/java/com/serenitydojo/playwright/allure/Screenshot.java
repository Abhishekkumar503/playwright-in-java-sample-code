package com.serenitydojo.playwright.allure;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayInputStream;

public interface Screenshot {
    @AfterEach
    default void screenshotManager(Page page)
    {
        System.out.println("After Each");
        var screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final screenshot", new ByteArrayInputStream(screenshot));    }
}
