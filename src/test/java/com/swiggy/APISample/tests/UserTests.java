package com.swiggy.APISample.tests;

import com.swiggy.APISample.ApiSampleApplicationTests;
import com.swiggy.APISample.commons.FilePathConstatns;
import com.swiggy.APISample.dto.request.CreateUserRequest;
import com.swiggy.APISample.dto.request.testData.UserTestData;
import com.swiggy.APISample.validator.UserValidator;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

/**
 * REST Assured API Tests for User Management
 * Demonstrates comprehensive API testing using:
 * - REST Assured for HTTP requests
 * - TestNG DataProvider for parameterized testing
 * - POJO/DTO objects for request/response serialization
 * - Custom validator for response validation
 * - Spring Boot dependency injection
 */
public class UserTests extends ApiSampleApplicationTests {

    private static final UserValidator userValidator = new UserValidator();

    /**
     * Data Provider for user creation test cases
     * Loads test data from JSON file and creates parameterized test iterations
     */
    @DataProvider(name = "userCases")
    public Object[][] fetchTestCases() throws IOException {
        List<UserTestData> userTestDataList = dataMapper.userList(
            FilePathConstatns.userFilePath,
            UserTestData.class
        );
        Object[][] data = new Object[userTestDataList.size()][1];
        for (int i = 0; i < userTestDataList.size(); i++) {
            data[i][0] = userTestDataList.get(i);
        }
        return data;
    }

    /**
     * Test Case 1: Create Users with Parameterized Data
     * 
     * This test demonstrates:
     * - Using POJO (UserTestData) to load test cases
     * - Converting POJO to CreateUserRequest using Jackson
     * - Making REST Assured API call
     * - Validating response using custom validator
     */
    @Test(
        dataProvider = "userCases",
        description = "Create users with various test cases from JSON test data"
    )
    public void testCreateUsersWithDataProvider(UserTestData userTestData) throws IOException {
        // Step 1: Convert test POJO to request POJO using Jackson ObjectMapper
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        String json = mapper.writeValueAsString(userTestData);
        CreateUserRequest createUserRequest = mapper.readValue(json, CreateUserRequest.class);

        // Step 2: Log test input
        System.out.println("=== Test Input ===");
        System.out.println("Name: " + userTestData.name);
        System.out.println("Job: " + userTestData.job);
        System.out.println("Expected Status Code: " + userTestData.expectedStatusCode);

        // Step 3: Execute REST Assured API call
        Response response = userClient.createUserData(createUserRequest);

        // Step 4: Log response
        System.out.println("=== Response ===");
        System.out.println("Actual Status Code: " + response.getStatusCode());
        System.out.println("Response Time: " + response.getTime() + "ms");

        // Step 5: Validate response using custom validator
        userValidator.validateCreateUser(userTestData.expectedStatusCode, response);
        userValidator.validateContentType(response, "application/json");
        userValidator.validateResponseTime(response, 5000);

        System.out.println("✓ Test PASSED\n");
    }

    /**
     * Test Case 2: Fetch User Details
     * 
     * This test demonstrates:
     * - Loading test data from JSON file using POJO
     * - Extracting specific values from POJO
     * - Making REST Assured API call with path parameters
     * - Validating response structure
     */
    @Test(description = "Fetch user details and validate response structure")
    public void testFetchUserDetails() throws IOException {
        // Step 1: Load test data from JSON file using POJO
        List<UserTestData> userTestDataList = dataMapper.userList(
            FilePathConstatns.fetchUserFilePath,
            UserTestData.class
        );

        if (userTestDataList.isEmpty()) {
            throw new IllegalArgumentException("Test data not found in " + FilePathConstatns.fetchUserFilePath);
        }

        UserTestData testData = userTestDataList.get(0);

        // Step 2: Log test input
        System.out.println("=== Test Input ===");
        System.out.println("User ID: " + testData.id);
        System.out.println("Expected Status Code: " + testData.expectedStatusCode);

        // Step 3: Execute REST Assured API call with path parameter
        Response response = userClient.fetchUserData(testData.id);

        // Step 4: Log response
        System.out.println("=== Response ===");
        System.out.println("Actual Status Code: " + response.getStatusCode());
        System.out.println("Response Time: " + response.getTime() + "ms");

        // Step 5: Validate response using custom validator
        userValidator.validateFetchUser(testData.expectedStatusCode, response);
        userValidator.validateContentType(response, "application/json");
        userValidator.validateStatusCode(response, testData.expectedStatusCode);
        userValidator.validateResponseTime(response, 5000);

        System.out.println("✓ Test PASSED\n");
    }
}
