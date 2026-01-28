package com.serenitydojo.playwright.login;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.RequestOptions;
import com.serenitydojo.playwright.Users;

public class UserAPIClient {
    private Page page;

    private static final String REQUEST_URL = "https://api.practicesoftwaretesting.com/users/register";

    public UserAPIClient(Page page) {
        this.page = page;
    }

    public void registerUser(Users.User user) {
        var response = page.request().post(
                REQUEST_URL,
                RequestOptions.create()
                        .setData(user)
                        .setHeader("Content-Type","application/json")
                        .setHeader("Accept","application/json")

        );
        if (response.status() != 201)
        {
            try {
                throw new IllegalAccessException("USER NOT CREATED, PLEASE CHCEK!!" + response.text());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
