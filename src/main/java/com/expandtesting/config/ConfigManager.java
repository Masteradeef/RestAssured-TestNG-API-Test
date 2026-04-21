package com.expandtesting.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Singleton configuration manager that reads JSON config with properties fallback.
 */
public class ConfigManager {

    private static ConfigManager instance;
    private final Map<String, String> config = new HashMap<>();

    private ConfigManager() {
        if (!loadJsonConfig()) {
            loadPropertiesConfig();
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
        String value = System.getProperty(key, config.get(key));
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.json/config.properties.");
        }
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        return System.getProperty(key, config.getOrDefault(key, defaultValue));
    }

    private boolean loadJsonConfig() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.json")) {
            if (is == null) {
                return false;
            }
            Map<String, Object> raw = new ObjectMapper().readValue(is, new TypeReference<Map<String, Object>>() {});
            for (Map.Entry<String, Object> entry : raw.entrySet()) {
                config.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.json: " + e.getMessage(), e);
        }
    }

    private void loadPropertiesConfig() {
        Properties properties = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("config.json and config.properties were not found in classpath.");
            }
            properties.load(is);
            for (String key : properties.stringPropertyNames()) {
                config.put(key, properties.getProperty(key));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage(), e);
        }
    }
}

