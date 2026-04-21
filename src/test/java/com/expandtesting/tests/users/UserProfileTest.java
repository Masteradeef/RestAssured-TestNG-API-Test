package com.expandtesting.tests.users;

import com.expandtesting.base.BaseTest;
import com.expandtesting.clients.UserClient;
import com.expandtesting.models.request.UpdateProfileRequest;
import com.expandtesting.utils.AuthHelper;
import com.expandtesting.utils.DataGenerator;
import com.expandtesting.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests for User Profile endpoints (GET & PATCH).
 */
public class UserProfileTest extends BaseTest {

    private UserClient userClient;
    private AuthHelper authHelper;
    private String token;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        userClient = new UserClient();
        authHelper = new AuthHelper();
        token = authHelper.registerAndLogin();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (authHelper != null) {
            authHelper.deleteAccount();
        }
    }

    @Test(groups = {"smoke"}, description = "Get profile with a valid token should return 200 and user data")
    public void testGetProfileWithValidToken() {
        Response response = userClient.getProfile(token);
        log.info("Get profile response: {}", response.getBody().asString());

        ResponseValidator.assertSuccessResponse(response, 200);
        ResponseValidator.assertFieldNotNull(response, "data.id");
        ResponseValidator.assertFieldEquals(response, "data.email", authHelper.getRegisteredEmail());
    }

    @Test(groups = {"regression"}, description = "Get profile with invalid token should return 401")
    public void testGetProfileWithInvalidToken() {
        Response response = userClient.getProfile("invalid_token_xyz");
        log.info("Get profile (invalid token) response: {}", response.getBody().asString());

        ResponseValidator.assertStatusCode(response, 401);
    }

    @Test(groups = {"regression"}, description = "Update profile with valid data should return 200")
    public void testUpdateProfile() {
        String newName = DataGenerator.randomName();
        String newPhone = DataGenerator.randomPhone();
        String newCompany = DataGenerator.randomCompany();

        UpdateProfileRequest request = new UpdateProfileRequest(newName, newPhone, newCompany);
        Response response = userClient.updateProfile(token, request);
        log.info("Update profile response: {}", response.getBody().asString());

        ResponseValidator.assertSuccessResponse(response, 200);
        ResponseValidator.assertFieldEquals(response, "data.name", newName);
        ResponseValidator.assertFieldEquals(response, "data.phone", newPhone);
        ResponseValidator.assertFieldEquals(response, "data.company", newCompany);
    }

    @Test(groups = {"regression"}, description = "Update profile without auth token should return 401")
    public void testUpdateProfileWithoutToken() {
        UpdateProfileRequest request = new UpdateProfileRequest(
                DataGenerator.randomName(), DataGenerator.randomPhone(), DataGenerator.randomCompany());
        Response response = userClient.updateProfile("", request);
        log.info("Update profile (no token) response: {}", response.getBody().asString());

        ResponseValidator.assertStatusCode(response, 401);
    }
}

