package com.serenitydojo.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.serenitydojo.playwright.browserSetup.Base;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@UsePlaywright(Base.class)
public class ContactPage {

    @Test
    public void contact(Page page) throws URISyntaxException {
        page.navigate("https://www.practicesoftwaretesting.com/");
        page.getByText("Contact").click();
        page.locator("#first_name").fill("Abhishek");
        page.locator("#last_name").fill("Kumar");
        page.getByPlaceholder("Your email *").fill("test@test.com");
        page.selectOption("#subject","Payments");
        page.locator("#message").fill("aa ka bharosa, dentist ka sujhaya – Colgate se muskaan chamkao!");
        var upload = page.locator("#attachment");

        // Locate file in resources
        Path uploadAttachment = Paths.get(ClassLoader.getSystemResource("Files/test.txt").toURI());
        // upload the file
        page.setInputFiles("#attachment",uploadAttachment);

        String fileName = upload.inputValue();
        System.out.println("File uplaoded : "+ fileName.endsWith("test.txt"));
        page.locator(".btnSubmit").click();

    }
}
