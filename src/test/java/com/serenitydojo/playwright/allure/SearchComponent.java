package com.serenitydojo.playwright.allure;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Feature;
import io.qameta.allure.Features;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Searching Products")
@Feature("Searching Product")
public class SearchComponent {
    private final Page page;

    SearchComponent(Page page) {
        this.page = page;
    }

    @DisplayName("Search by Search Option")
    @Story("Searching by Search Option")
    @Step("Searching by keyword")
    public void searchBy(String keyword) {
            page.waitForResponse("**/products/search?q=" + keyword, () -> {
            page.getByPlaceholder("Search").fill(keyword);
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        });
    }
}
