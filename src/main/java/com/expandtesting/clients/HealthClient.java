package com.expandtesting.clients;

import com.expandtesting.config.EndpointConstants;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * API client for Health Check endpoints.
 */
public class HealthClient extends BaseApiClient {

    public Response checkHealth() {
        return given()
                .spec(baseSpec)
                .when()
                .get(EndpointConstants.HEALTH_CHECK)
                .then()
                .extract().response();
    }
}

