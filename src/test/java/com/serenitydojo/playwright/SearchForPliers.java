package com.serenitydojo.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;

public class SearchForPliers extends LaunchBrowser {

/*
    Requirments :-
    Search for Plier
    count of plier
    check how many are out of stock
 */

    @Test
    public void search()
    {
        page.locator("#search-query").fill("Pliers");
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Search ")).click();
        page.locator("[data-test='search_completed']").waitFor();
        int countOfPliers = page.locator(".card").count();
        System.out.println("Total Pliers : " + countOfPliers);
    }
}
