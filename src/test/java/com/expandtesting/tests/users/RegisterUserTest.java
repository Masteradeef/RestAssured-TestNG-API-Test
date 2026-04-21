package com.expandtesting.tests.users;

import com.expandtesting.base.BaseTest;
import com.expandtesting.clients.UserClient;
import com.expandtesting.models.request.RegisterRequest;
import com.expandtesting.utils.DataGenerator;
import com.expandtesting.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests for User Registration endpoint.
 */
public class RegisterUserTest extends BaseTest {

    private UserClient userClient;
    private String registeredEmail;
    private String registeredPassword;
    private String registeredToken;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        userClient = new UserClient();
    }

    @Test(groups = {"smoke"}, description = "Register a new user with valid data should return 201")
    public void testRegisterWithValidData() {
        String name = DataGenerator.randomName();
        registeredEmail = DataGenerator.randomEmail();
        registeredPassword = DataGenerator.randomPassword();

        RegisterRequest request = new RegisterRequest(name, registeredEmail, registeredPassword);
        Response response = userClient.register(request);

        log.info("Register response: {}", response.getBody().asString());

        ResponseValidator.assertSuccessResponse(response, 201);
        ResponseValidator.assertMessage(response, "User account created successfully");
        ResponseValidator.assertFieldNotNull(response, "data.id");
        ResponseValidator.assertFieldEquals(response, "data.email", registeredEmail);
        ResponseValidator.assertFieldEquals(response, "data.name", name);

        // Cleanup: login then delete
        Response loginResp = userClient.login(
                new com.expandtesting.models.request.LoginRequest(registeredEmail, registeredPassword));
        String token = loginResp.jsonPath().getString("data.token");
        if (token != null) {
            userClient.deleteAccount(token);
        }
    }

    @Test(groups = {"regression"}, description = "Register with missing name should return 400")
    public void testRegisterWithMissingName() {
        RegisterRequest request = new RegisterRequest(null, DataGenerator.randomEmail(), DataGenerator.randomPassword());
        Response response = userClient.register(request);

        log.info("Register (missing name) response: {}", response.getBody().asString());
        ResponseValidator.assertStatusCode(response, 400);
    }

    @Test(groups = {"regression"}, description = "Register with invalid email format should return 400")
    public void testRegisterWithInvalidEmail() {
        RegisterRequest request = new RegisterRequest(DataGenerator.randomName(), "not-an-email", DataGenerator.randomPassword());
        Response response = userClient.register(request);

        log.info("Register (invalid email) response: {}", response.getBody().asString());
        ResponseValidator.assertStatusCode(response, 400);
    }

    @Test(groups = {"regression"}, description = "Register with short password should return 400")
    public void testRegisterWithShortPassword() {
        RegisterRequest request = new RegisterRequest(DataGenerator.randomName(), DataGenerator.randomEmail(), "123");
        Response response = userClient.register(request);

        log.info("Register (short password) response: {}", response.getBody().asString());
        ResponseValidator.assertStatusCode(response, 400);
    }

    @Test(groups = {"regression"}, description = "Register with duplicate email should return 409")
    public void testRegisterWithDuplicateEmail() {
        String name = DataGenerator.randomName();
        String email = DataGenerator.randomEmail();
        String password = DataGenerator.randomPassword();

        RegisterRequest request = new RegisterRequest(name, email, password);

        // First registration
        Response firstResponse = userClient.register(request);
        Assert.assertEquals(firstResponse.getStatusCode(), 201, "First registration should succeed");

        // Duplicate registration
        Response duplicateResponse = userClient.register(request);
        log.info("Duplicate register response: {}", duplicateResponse.getBody().asString());
        ResponseValidator.assertStatusCode(duplicateResponse, 409);

        // Cleanup
        Response loginResp = userClient.login(new com.expandtesting.models.request.LoginRequest(email, password));
        String token = loginResp.jsonPath().getString("data.token");
        if (token != null) {
            userClient.deleteAccount(token);
        }
    }
}

