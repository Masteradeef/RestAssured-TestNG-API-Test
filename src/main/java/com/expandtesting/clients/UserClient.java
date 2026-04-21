package com.expandtesting.clients;

import com.expandtesting.config.EndpointConstants;
import com.expandtesting.models.request.*;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * API client for User endpoints.
 */
public class UserClient extends BaseApiClient {

    public Response register(RegisterRequest request) {
        return given()
                .spec(baseSpec)
                .body(request)
                .when()
                .post(EndpointConstants.USERS_REGISTER)
                .then()
                .extract().response();
    }

    public Response login(LoginRequest request) {
        return given()
                .spec(baseSpec)
                .body(request)
                .when()
                .post(EndpointConstants.USERS_LOGIN)
                .then()
                .extract().response();
    }

    public Response getProfile(String token) {
        return given()
                .spec(baseSpec)
                .header(EndpointConstants.AUTH_TOKEN_HEADER, token)
                .when()
                .get(EndpointConstants.USERS_PROFILE)
                .then()
                .extract().response();
    }

    public Response updateProfile(String token, UpdateProfileRequest request) {
        return given()
                .spec(baseSpec)
                .header(EndpointConstants.AUTH_TOKEN_HEADER, token)
                .body(request)
                .when()
                .patch(EndpointConstants.USERS_PROFILE)
                .then()
                .extract().response();
    }

    public Response changePassword(String token, ChangePasswordRequest request) {
        return given()
                .spec(baseSpec)
                .header(EndpointConstants.AUTH_TOKEN_HEADER, token)
                .body(request)
                .when()
                .patch(EndpointConstants.USERS_CHANGE_PASSWORD)
                .then()
                .extract().response();
    }

    public Response deleteAccount(String token) {
        return given()
                .spec(baseSpec)
                .header(EndpointConstants.AUTH_TOKEN_HEADER, token)
                .when()
                .delete(EndpointConstants.USERS_DELETE_ACCOUNT)
                .then()
                .extract().response();
    }

    public Response logout(String token) {
        return given()
                .spec(baseSpec)
                .header(EndpointConstants.AUTH_TOKEN_HEADER, token)
                .when()
                .delete(EndpointConstants.USERS_LOGOUT)
                .then()
                .extract().response();
    }
}

