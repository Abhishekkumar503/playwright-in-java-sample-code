package com.serenitydojo.playwright;

import net.datafaker.Faker;

public class Users {

    public record Address(
            String street,
            String city,
            String state,
            String country,
            String postal_code
    ) {}

    public record User(
            String first_name,
            String last_name,
            Address address,
            String phone,
            String dob,
            String password,
            String email
    ) {
        public static User randomUser() {
            Faker fake = new Faker();
            return new User(
                    fake.name().firstName(),
                    fake.name().lastName(),
                    new Address(
                            fake.address().streetName(),
                            fake.address().city(),
                            fake.address().state(),
                            fake.address().country(),
                            fake.address().postcode()
                    ),
                    fake.number().digits(10),
                    "1995-01-26",
                    "Az@10IN1",
                    fake.internet().emailAddress()
            );
        }


        public User withPassword(String password) {
            return new User(
                    first_name,
                    last_name,
                    address,
                    phone,
                    dob,
                    password,
                    email);
        }

        public User withFirstName(String first_name) {
            return new User(first_name, last_name, address, phone, dob, password, email);
        }
    }

}