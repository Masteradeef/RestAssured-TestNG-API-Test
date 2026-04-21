package com.expandtesting.config;

/**
 * Centralized API endpoint path constants.
 */
public final class EndpointConstants {

    private EndpointConstants() {}

    // Base path
    public static final String BASE_PATH = "/notes/api";

    // Health
    public static final String HEALTH_CHECK = BASE_PATH + "/health-check";

    // Users
    public static final String USERS_REGISTER       = BASE_PATH + "/users/register";
    public static final String USERS_LOGIN          = BASE_PATH + "/users/login";
    public static final String USERS_PROFILE        = BASE_PATH + "/users/profile";
    public static final String USERS_FORGOT_PASSWORD = BASE_PATH + "/users/forgot-password";
    public static final String USERS_CHANGE_PASSWORD = BASE_PATH + "/users/change-password";
    public static final String USERS_DELETE_ACCOUNT  = BASE_PATH + "/users/delete-account";
    public static final String USERS_LOGOUT         = BASE_PATH + "/users/logout";

    // Notes
    public static final String NOTES            = BASE_PATH + "/notes";
    public static final String NOTES_BY_ID      = BASE_PATH + "/notes/{id}";

    // Headers
    public static final String AUTH_TOKEN_HEADER = "x-auth-token";
}

