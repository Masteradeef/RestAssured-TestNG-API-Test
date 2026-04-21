package com.expandtesting.utils;

import com.github.javafaker.Faker;

import java.util.Locale;

/**
 * Generates random test data using JavaFaker.
 */
public class DataGenerator {

    private static final Faker faker = new Faker(Locale.ENGLISH);
    private static final String[] NOTE_CATEGORIES = {"Home", "Work", "Personal"};

    private DataGenerator() {}

    public static String randomName() {
        return faker.name().fullName();
    }

    public static String randomEmail() {
        return faker.internet().emailAddress()
                .replace("'", "")      // strip chars that may break API validation
                .replace("+", "")
                .toLowerCase();
    }

    public static String randomPassword() {
        // Must be at least 6 chars; mix upper, lower, digit, special
        return "Test@" + faker.number().digits(4) + "!";
    }

    public static String randomNoteTitle() {
        return faker.lorem().sentence(3).replace(".", "");
    }

    public static String randomNoteDescription() {
        return faker.lorem().sentence(8);
    }

    public static String randomNoteCategory() {
        return NOTE_CATEGORIES[faker.random().nextInt(NOTE_CATEGORIES.length)];
    }

    public static String randomPhone() {
        return faker.numerify("##########"); // 10-digit number string
    }

    public static String randomCompany() {
        return faker.company().name().replace(",", "").replace("'", "");
    }
}

