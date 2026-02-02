package com.serenitydojo.playwright.allure;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class SearchComponent {
    private final Page page;

    SearchComponent(Page page) {
        this.page = page;
    }

    @Step("Searching by keyword")
    public void searchBy(String keyword) {
            page.waitForResponse("**/products/search?q=" + keyword, () -> {
            page.getByPlaceholder("Search").fill(keyword);
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        });
    }
}
