package com.serenitydojo.playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;

import java.sql.Struct;
import java.util.List;

public class SearchOutOfStockPliers extends LaunchBrowser {

/*
    Requirments :-
    Search for Plier
    count of plier
    check how many are out of stock
 */

    @Test
    public void search() throws InterruptedException {
        page.locator("#search-query").fill("Pliers");
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Search")).click();
        page.locator("[data-test='search_completed']").waitFor();
        int countOfPliers = page.locator(".card").count();
        System.out.println("Total Pliers : " + countOfPliers);
        Locator outOfStockList = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                .locator(".card-title");
        List<String> nameAndCount = outOfStockList.allTextContents();
        System.out.println("Number Of Out of Stock records : " + outOfStockList.count());
        for (String name : nameAndCount)
        {
            System.out.println("Product Name : " + name);
        }
    }
}
