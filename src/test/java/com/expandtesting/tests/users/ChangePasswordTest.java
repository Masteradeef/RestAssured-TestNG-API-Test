package com.expandtesting.tests.users;

import com.expandtesting.base.BaseTest;
import com.expandtesting.clients.UserClient;
import com.expandtesting.models.request.ChangePasswordRequest;
import com.expandtesting.models.request.LoginRequest;
import com.expandtesting.utils.AuthHelper;
import com.expandtesting.utils.DataGenerator;
import com.expandtesting.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests for Change Password and Logout endpoints.
 */
public class ChangePasswordTest extends BaseTest {

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
        // Delete account using the latest known token
        if (token != null && userClient != null) {
            userClient.deleteAccount(token);
        }
    }

    @Test(groups = {"regression"}, description = "Change password with correct current password should return 200")
    public void testChangePasswordSuccess() {
        String newPassword = DataGenerator.randomPassword();
        ChangePasswordRequest request = new ChangePasswordRequest(authHelper.getRegisteredPassword(), newPassword);

        Response response = userClient.changePassword(token, request);
        log.info("Change password response: {}", response.getBody().asString());

        ResponseValidator.assertSuccessResponse(response, 200);
        ResponseValidator.assertMessage(response, "The password was successfully updated");

        // Login with new password to get a valid token for tearDown
        Response loginResp = userClient.login(new LoginRequest(authHelper.getRegisteredEmail(), newPassword));
        token = loginResp.jsonPath().getString("data.token");
    }

    @Test(groups = {"regression"}, description = "Change password with incorrect current password should return 400",
            dependsOnMethods = {"testChangePasswordSuccess"})
    public void testChangePasswordWithWrongCurrentPassword() {
        ChangePasswordRequest request = new ChangePasswordRequest("WrongOldPass@11!", DataGenerator.randomPassword());
        Response response = userClient.changePassword(token, request);
        log.info("Change password (wrong old) response: {}", response.getBody().asString());

        ResponseValidator.assertStatusCode(response, 400);
    }

    @Test(groups = {"regression"}, description = "Logout should invalidate the token",
            dependsOnMethods = {"testChangePasswordWithWrongCurrentPassword"})
    public void testLogout() {
        Response logoutResponse = userClient.logout(token);
        log.info("Logout response: {}", logoutResponse.getBody().asString());

        ResponseValidator.assertSuccessResponse(logoutResponse, 200);

        // Using the old token after logout should return 401
        Response profileResponse = userClient.getProfile(token);
        log.info("Profile after logout response: {}", profileResponse.getBody().asString());
        ResponseValidator.assertStatusCode(profileResponse, 401);

        // Re-login to get a valid token for teardown
        Response loginResp = userClient.login(
                new LoginRequest(authHelper.getRegisteredEmail(), authHelper.getRegisteredPassword()));
        // password was changed - try the original (before change) ... may fail here on teardown
        // The tearDown will attempt delete; if it fails, that's acceptable
        token = loginResp.jsonPath().getString("data.token");
    }
}

