package com.serenitydojo.playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.browserSetup.Base;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

@UsePlaywright(Base.class)
public class TestingMandatoryFields {

    private Page page;

    @BeforeEach
    public void setPage(Page page) {
        this.page = page;
    }

    @DisplayName("Mandatory Fields")
    @ParameterizedTest
    @ValueSource(strings = {"First name","Last name","Email","Message"})
    public void mandatoryFields(String fieldName)
    {
        page.navigate("https://www.practicesoftwaretesting.com/");
        page.getByText("Contact").click();
        page.getByLabel(fieldName).fill("");
        page.locator(".btnSubmit").click();
        page.locator(".ng-submitted").waitFor();
        Locator mandatoryFields = page.getByRole(AriaRole.ALERT);
        List<String> errorMessage = mandatoryFields.allTextContents();
        errorMessage.forEach(System.out::println);

    }
}
