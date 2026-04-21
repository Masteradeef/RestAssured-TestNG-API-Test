package com.expandtesting.tests.users;

import com.expandtesting.base.BaseTest;
import com.expandtesting.clients.UserClient;
import com.expandtesting.models.request.LoginRequest;
import com.expandtesting.utils.AuthHelper;
import com.expandtesting.utils.DataGenerator;
import com.expandtesting.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests for User Login endpoint.
 */
public class LoginUserTest extends BaseTest {

    private UserClient userClient;
    private AuthHelper authHelper;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        userClient = new UserClient();
        authHelper = new AuthHelper();
        authHelper.registerAndLogin();  // creates a user we can test with
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (authHelper != null) {
            authHelper.deleteAccount();
        }
    }

    @Test(groups = {"smoke"}, description = "Login with valid credentials should return 200 and a token")
    public void testLoginWithValidCredentials() {
        LoginRequest request = new LoginRequest(authHelper.getRegisteredEmail(), authHelper.getRegisteredPassword());

        // Re-login to get a fresh token
        Response response = userClient.login(request);
        log.info("Login response: {}", response.getBody().asString());

        ResponseValidator.assertSuccessResponse(response, 200);
        ResponseValidator.assertMessage(response, "Login successful");
        ResponseValidator.assertFieldNotNull(response, "data.token");

        String token = response.jsonPath().getString("data.token");
        Assert.assertFalse(token.isEmpty(), "Token should not be empty");

        // Update the stored token so teardown can delete the account
        // (the previous token is still valid, so this is fine)
    }

    @Test(groups = {"regression"}, description = "Login with wrong password should return 401")
    public void testLoginWithWrongPassword() {
        LoginRequest request = new LoginRequest(authHelper.getRegisteredEmail(), "WrongPassword@99!");
        Response response = userClient.login(request);

        log.info("Login (wrong password) response: {}", response.getBody().asString());
        ResponseValidator.assertStatusCode(response, 401);
    }

    @Test(groups = {"regression"}, description = "Login with non-existent email should return 401")
    public void testLoginWithNonExistentEmail() {
        LoginRequest request = new LoginRequest("nonexistent_" + DataGenerator.randomEmail(), DataGenerator.randomPassword());
        Response response = userClient.login(request);

        log.info("Login (non-existent email) response: {}", response.getBody().asString());
        ResponseValidator.assertStatusCode(response, 401);
    }

    @Test(groups = {"regression"}, description = "Login with missing email should return 400")
    public void testLoginWithMissingEmail() {
        LoginRequest request = new LoginRequest(null, DataGenerator.randomPassword());
        Response response = userClient.login(request);

        log.info("Login (missing email) response: {}", response.getBody().asString());
        ResponseValidator.assertStatusCode(response, 400);
    }
}

