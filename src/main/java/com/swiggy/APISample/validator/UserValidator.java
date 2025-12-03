package com.swiggy.APISample.validator;

import com.swiggy.APISample.dto.response.CreateUserResponse;
import com.swiggy.APISample.dto.response.GetUserResponse;
import io.restassured.response.Response;
import org.testng.Assert;

/**
 * REST Assured Validator for User API Responses
 * Provides validation methods for API response verification
 */
public class UserValidator {

    /**
     * Validate the response for user creation
     *
     * @param expectedStatusCode The expected HTTP status code
     * @param response           The API response object
     */
    public void validateCreateUser(int expectedStatusCode, Response response) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, 
            "Status code mismatch for user creation");

        // Parse response body to CreateUserResponse object
        CreateUserResponse createUserResponse = response.getBody().as(CreateUserResponse.class);
        Assert.assertNotNull(createUserResponse, "Response body should not be null");
    }

    /**
     * Validate the response for fetching user data
     *
     * @param expectedStatusCode The expected HTTP status code
     * @param response           The API response object
     */
    public void validateFetchUser(int expectedStatusCode, Response response) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, 
            "Status code mismatch for fetch user");

        // Parse response body to GetUserResponse object
        GetUserResponse getUserResponse = response.getBody().as(GetUserResponse.class);
        Assert.assertNotNull(getUserResponse, "Response body should not be null");
    }

    /**
     * Validate HTTP status code
     *
     * @param response              The API response object
     * @param expectedStatusCode    The expected HTTP status code
     */
    public void validateStatusCode(Response response, int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode,
            String.format("Expected status code %d, but got %d", 
                expectedStatusCode, response.getStatusCode()));
    }

    /**
     * Validate content type of response
     *
     * @param response                The API response object
     * @param expectedContentType     The expected content type
     */
    public void validateContentType(Response response, String expectedContentType) {
        String actualContentType = response.getContentType();
        Assert.assertTrue(actualContentType.contains(expectedContentType),
            String.format("Expected content type to contain %s, but got %s", 
                expectedContentType, actualContentType));
    }

    /**
     * Validate response time
     *
     * @param response        The API response object
     * @param maxTimeInMs     Maximum allowed response time in milliseconds
     */
    public void validateResponseTime(Response response, long maxTimeInMs) {
        long actualTime = response.getTime();
        Assert.assertTrue(actualTime <= maxTimeInMs,
            String.format("Response time %d ms exceeded max allowed time %d ms", 
                actualTime, maxTimeInMs));
    }
}
