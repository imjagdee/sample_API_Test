package com.swiggy.APISample.tests;

import com.swiggy.APISample.clients.UserClient;
import com.swiggy.APISample.commons.FilePathConstants;
import com.swiggy.APISample.dto.request.CreateUserRequest;
import com.swiggy.APISample.dto.request.testData.UserTestData;
import com.swiggy.APISample.dto.response.CreateUserResponse;
import com.swiggy.APISample.dto.response.GetUserResponse;
import com.swiggy.APISample.utils.DataMapper;
import com.swiggy.APISample.validator.UserValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class UserTests {

    private final UserClient userClient = new UserClient();
    private final UserValidator userValidator = new UserValidator();
    private final DataMapper dataMapper = new DataMapper();

    /**
     * Test case: Create User
     * Reads test data from userData.json using DataMapper.userList()
     */
    @Test(description = "Create user using JSON test data")
    public void createUser() throws IOException {

        // 1. Read JSON test data -> List<UserTestData>
        List<UserTestData> testDataList =
                dataMapper.userList(FilePathConstants.userFilePath, UserTestData.class);

        // Use the first JSON object for this test (no DataProvider)
        UserTestData testData = testDataList.get(0);

        // 2. Prepare request using Lombok builder (no getters/setters)
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .name(testData.getName())
                .job(testData.getJob())
                .build();

        // 3. Execute API using UserClient
        Response response = userClient.createUserData(createUserRequest);

        // 4. Validate status & content type using validator
        userValidator.validateCreateUser(testData.getExpectedStatusCode(), response);
        userValidator.validateContentType(response, "application/json");

        // 5. Validate body if 201 Created
        if (response.getStatusCode() == 201) {
            CreateUserResponse createUserResponse =
                    response.getBody().as(CreateUserResponse.class);

            Assert.assertNotNull(createUserResponse.getId(), "ID should not be null");
            Assert.assertNotNull(createUserResponse.getCreatedAt(), "createdAt should not be null");
        }
    }

    /**
     * Test case: Fetch User by ID
     * Reads test data from fetchUserData.json using DataMapper.userList()
     */
    @Test(description = "Fetch user using JSON test data")
    public void fetchUser() throws IOException {

        // 1. Load fetchUser test data
        List<UserTestData> testDataList =
                dataMapper.userList(FilePathConstants.fetchUserFilePath, UserTestData.class);

        // Use the first record
        UserTestData testData = testDataList.get(0);

        // 2. Call API using UserClient
        Response response = userClient.fetchUserById(testData.getId());

        // 3. Validate status & content type
        userValidator.validateFetchUser(testData.getExpectedStatusCode(), response);
        userValidator.validateContentType(response, "application/json");

        // 4. If success, deserialize body using your DTO
        }
    }

