package com.expandtesting.utils;

import io.restassured.response.Response;
import org.testng.Assert;

/**
 * Reusable assertion helpers for API responses.
 */
public class ResponseValidator {

    private ResponseValidator() {}

    public static void assertStatusCode(Response response, int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode,
                "Expected status code " + expectedStatusCode + " but got " + response.getStatusCode()
                        + ". Response body: " + response.getBody().asString());
    }

    public static void assertSuccess(Response response) {
        boolean success = response.jsonPath().getBoolean("success");
        Assert.assertTrue(success, "Expected 'success' to be true. Response: " + response.getBody().asString());
    }

    public static void assertMessage(Response response, String expectedMessage) {
        String actualMessage = response.jsonPath().getString("message");
        Assert.assertEquals(actualMessage, expectedMessage,
                "Message mismatch. Response: " + response.getBody().asString());
    }

    public static void assertFieldNotNull(Response response, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        Assert.assertNotNull(value, "Expected field '" + jsonPath + "' to be non-null. Response: "
                + response.getBody().asString());
    }

    public static void assertFieldEquals(Response response, String jsonPath, Object expected) {
        Object actual = response.jsonPath().get(jsonPath);
        Assert.assertEquals(actual, expected,
                "Field '" + jsonPath + "' mismatch. Response: " + response.getBody().asString());
    }

    public static void assertSuccessResponse(Response response, int expectedStatusCode) {
        assertStatusCode(response, expectedStatusCode);
        assertSuccess(response);
    }
}

