package VersionCheck;

import com.microsoft.playwright.*;

public class PlaywrightVersionCheck {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            System.out.println("Playwright initialized successfully!");

            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            System.out.println("Chromium browser launched successfully!");

            browser.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}