package com.serenitydojo.playwright.GitAction;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;

@Feature("GitAction Browser Configuration")
@DisplayName("GitAction Browser Setup")
public class GitActionBase implements OptionsFactory {

    @Override
    public Options getOptions() {
        return new Options().setLaunchOptions(new BrowserType.
                LaunchOptions().
                setHeadless(true).
                setArgs(Arrays.asList("--no-sandbox",
                        "--disable-gpu",
                        "--disable-extensions"))
        ).setTestIdAttribute("data-test");
    }
}



