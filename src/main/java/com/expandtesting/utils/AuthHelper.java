package com.expandtesting.utils;

import com.expandtesting.clients.UserClient;
import com.expandtesting.models.request.LoginRequest;
import com.expandtesting.models.request.RegisterRequest;
import io.restassured.response.Response;

/**
 * Helper that registers a brand-new user and returns their auth token.
 * Useful in @BeforeClass / @BeforeMethod to provide isolated test users.
 */
public class AuthHelper {

    private final UserClient userClient = new UserClient();

    private String registeredEmail;
    private String registeredPassword;
    private String registeredName;
    private String token;
    private String userId;

    /**
     * Registers a fresh random user, logs in, and returns the auth token.
     */
    public String registerAndLogin() {
        registeredName = DataGenerator.randomName();
        registeredEmail = DataGenerator.randomEmail();
        registeredPassword = DataGenerator.randomPassword();

        RegisterRequest registerRequest = new RegisterRequest(registeredName, registeredEmail, registeredPassword);
        Response registerResponse = userClient.register(registerRequest);

        if (registerResponse.getStatusCode() != 201) {
            throw new RuntimeException("Registration failed: " + registerResponse.getBody().asString());
        }

        userId = registerResponse.jsonPath().getString("data.id");

        LoginRequest loginRequest = new LoginRequest(registeredEmail, registeredPassword);
        Response loginResponse = userClient.login(loginRequest);

        if (loginResponse.getStatusCode() != 200) {
            throw new RuntimeException("Login failed: " + loginResponse.getBody().asString());
        }

        token = loginResponse.jsonPath().getString("data.token");
        return token;
    }

    public void deleteAccount() {
        if (token != null) {
            userClient.deleteAccount(token);
            token = null;
        }
    }

    public String getToken() { return token; }
    public String getRegisteredEmail() { return registeredEmail; }
    public String getRegisteredPassword() { return registeredPassword; }
    public String getRegisteredName() { return registeredName; }
    public String getUserId() { return userId; }
}

