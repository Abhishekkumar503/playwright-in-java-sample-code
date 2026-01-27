package com.serenitydojo.playwright;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.RequestOptions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@UsePlaywright
public class RegisterUserAPITest {

    /**
     * 1. Setup base url
     * 2. Hit POST API and check the reponse code is 200.
     * 3. Close the request
     */

    private APIRequestContext request;

    Gson gson = new Gson();

    // 1. setup base URL
    @BeforeEach
    void setup(Playwright playwright)
    {
        request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://api.practicesoftwaretesting.com")
        );
    }

    // close the request
    @AfterEach
    void tearDown()
    {
        if(request != null)
        {
            request.dispose();
        }
    }

    // 2. Hitting user register API and Part-1  Checking Normal case
    @Test
     void should_Register_User()
    {
        // create random user with Users class i.e just invoke
        Users.User validUser = Users.User.randomUser();

        System.out.println(validUser.toString());

        // Hittimn the post user.register API with body as above random user and get back reponse
        var response = request.post("/users/register", RequestOptions.create()
                .setHeader("Content-Type","application/json")
                .setData(validUser));

        Assertions.assertThat(response.status()).isEqualTo(201);

        // Convert  JSON to USER Class
        String reponse = response.text();

        Users.User createdUser = gson.fromJson(reponse,Users.User.class);
        // To get Single field from the JSON
        JsonObject jsonObject = gson.fromJson(reponse,JsonObject.class);

        // you can add assertions as well
        System.out.println(jsonObject.get("id").getAsString());

        //  you comapare createdUser with validUser without password OR id. TDC

        assertSoftly(softly -> {
            softly.assertThat(response.status())
                    .as("Registration should return 201 created status code")
                    .isEqualTo(201);

            softly.assertThat(createdUser)
                    .as("Created user should match the specified user without the password")
                    .isEqualTo(validUser.withPassword(null));

            softly.assertThat(jsonObject.has("password"))
                    .as("No password should be returned")
                    .isFalse();

            softly.assertThat(jsonObject.get("id").getAsString())
                    .as("Registered user should have an id")
                    .isNotEmpty();

            softly.assertThat(response.headers().get("content-type")).contains("application/json");
        });





    }

    // 2. Part-2 testing API EDGE Case
    @Test
    void first_name_is_mandatory() {
        Users.User userWithNoName = Users.User.randomUser().withFirstName(null);

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(userWithNoName)
        );

        assertSoftly(softly -> {
            softly.assertThat(response.status()).isEqualTo(422);
            JsonObject responseObject = gson.fromJson(response.text(), JsonObject.class);
            softly.assertThat(responseObject.has("first_name")).isTrue();
            String errorMessage = responseObject.get("first_name").getAsString();
            softly.assertThat(errorMessage).isEqualTo("The first name field is required.");
        });
    }
}
