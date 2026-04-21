package com.expandtesting.clients;

import com.expandtesting.config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Base API client providing a shared RequestSpecification.
 */
public class BaseApiClient {

    protected final RequestSpecification baseSpec;

    public BaseApiClient() {
        ConfigManager config = ConfigManager.getInstance();
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);

        if (config.isLogAllRequests()) {
            builder.log(LogDetail.ALL);
        }

        baseSpec = builder.build();
    }

    protected RequestSpecification getBaseSpec() {
        return baseSpec;
    }
}

