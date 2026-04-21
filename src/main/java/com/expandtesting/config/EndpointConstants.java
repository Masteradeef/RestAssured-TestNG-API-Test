package com.expandtesting.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Centralized API endpoint path constants.
 */
public final class EndpointConstants {

    private EndpointConstants() {}

    private static final Map<String, String> ENDPOINTS = loadEndpoints();

    // Base path
    public static final String BASE_PATH = get("BASE_PATH");

    // Health
    public static final String HEALTH_CHECK = get("HEALTH_CHECK");

    // Users
    public static final String USERS_REGISTER = get("USERS_REGISTER");
    public static final String USERS_LOGIN = get("USERS_LOGIN");
    public static final String USERS_PROFILE = get("USERS_PROFILE");
    public static final String USERS_FORGOT_PASSWORD = get("USERS_FORGOT_PASSWORD");
    public static final String USERS_CHANGE_PASSWORD = get("USERS_CHANGE_PASSWORD");
    public static final String USERS_DELETE_ACCOUNT = get("USERS_DELETE_ACCOUNT");
    public static final String USERS_LOGOUT = get("USERS_LOGOUT");

    // Notes
    public static final String NOTES = get("NOTES");
    public static final String NOTES_BY_ID = get("NOTES_BY_ID");

    // Headers
    public static final String AUTH_TOKEN_HEADER = get("AUTH_TOKEN_HEADER");

    private static String get(String key) {
        String value = ENDPOINTS.get(key);
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException("Missing endpoint key in endpoints.json: " + key);
        }
        return value;
    }

    private static Map<String, String> loadEndpoints() {
        try (InputStream is = EndpointConstants.class.getClassLoader().getResourceAsStream("endpoints.json")) {
            if (is == null) {
                throw new IllegalStateException("endpoints.json not found in classpath");
            }
            return new ObjectMapper().readValue(is, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load endpoints.json", e);
        }
    }
}

