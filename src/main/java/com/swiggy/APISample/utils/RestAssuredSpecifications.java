package com.swiggy.APISample.utils;

import com.swiggy.APISample.commons.UserConstants;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

/**
 * REST Assured Specifications Builder
 * Plain Java version (No Spring Boot)
 */
public class RestAssuredSpecifications {

    private static final String baseUrl = ConfigReader.get("baseUrl");;
    private static final String apiKey = ConfigReader.get("x.api.value");


    /**
     * Build a common Request Specification
     */
    public static RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .addHeader(UserConstants.xApiKey, apiKey)
                .setContentType(ContentType.JSON)
                .build();
    }

    /**
     * Response spec for success responses
     */
    public static ResponseSpecification getSuccessResponseSpecification(int expectedStatusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .expectContentType(ContentType.JSON)
                .build();
    }

    /**
     * Response spec for JSON content type
     */
    public static ResponseSpecification getJsonResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();
    }
}
