package com.expandtesting.tests.health;

import com.expandtesting.base.BaseTest;
import com.expandtesting.clients.HealthClient;
import com.expandtesting.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests for the /health-check endpoint.
 */
public class HealthCheckTest extends BaseTest {

    private HealthClient healthClient;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        healthClient = new HealthClient();
    }

    @Test(groups = {"smoke"}, description = "Verify API health-check returns 200 and success:true")
    public void testHealthCheckReturns200() {
        Response response = healthClient.checkHealth();
        log.info("Health Check response: {}", response.getBody().asString());

        ResponseValidator.assertSuccessResponse(response, 200);
        ResponseValidator.assertMessage(response, "Notes API is Running");
    }
}

