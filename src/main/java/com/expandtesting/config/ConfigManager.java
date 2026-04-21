package com.expandtesting.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton configuration manager that reads properties from config.properties.
 */
public class ConfigManager {

    private static ConfigManager instance;
    private final Properties properties = new Properties();

    private ConfigManager() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("config.properties not found in classpath.");
            }
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage(), e);
        }
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public String getBaseUrl() {
        return getProperty("base.url");
    }

    public int getRequestTimeout() {
        return Integer.parseInt(getProperty("request.timeout", "30000"));
    }

    public boolean isLogAllRequests() {
        return Boolean.parseBoolean(getProperty("log.all.requests", "false"));
    }

    public boolean isLogAllResponses() {
        return Boolean.parseBoolean(getProperty("log.all.responses", "false"));
    }

    public String getProperty(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties.");
        }
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        return System.getProperty(key, properties.getProperty(key, defaultValue));
    }
}

