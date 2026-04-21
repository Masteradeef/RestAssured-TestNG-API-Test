package com.expandtesting.base;

import com.expandtesting.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeSuite;

/**
 * Base test class: configures RestAssured defaults before the suite runs.
 */
public class BaseTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        ConfigManager config = ConfigManager.getInstance();

        RestAssured.baseURI = config.getBaseUrl();
        RestAssured.config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", config.getRequestTimeout())
                        .setParam("http.socket.timeout", config.getRequestTimeout()));

        if (config.isLogAllRequests()) {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        }

        log.info("RestAssured configured. Base URI: {}", config.getBaseUrl());
    }
}

