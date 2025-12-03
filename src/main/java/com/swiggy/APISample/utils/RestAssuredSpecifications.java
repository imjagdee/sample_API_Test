package com.swiggy.APISample.utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * REST Assured Specifications Builder
 * Centralized configuration for request and response specifications
 */
@Component
public class RestAssuredSpecifications {

    @Autowired
    private UserUtils userUtils;

    @Value("${baseUrl}")
    private String baseUrl;

    /**
     * Build a common request specification
     */
    public RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .addHeaders(userUtils.getXpiHeaders().entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                java.util.Map.Entry::getKey,
                                e -> e.getValue().toString()
                        )))
                .setContentType(ContentType.JSON)
                .build();
    }

    /**
     * Build response specification for successful responses
     */
    public ResponseSpecification getSuccessResponseSpecification(int expectedStatusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .expectContentType(ContentType.JSON)
                .build();
    }

    /**
     * Build response specification for JSON responses
     */
    public ResponseSpecification getJsonResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();
    }
}
