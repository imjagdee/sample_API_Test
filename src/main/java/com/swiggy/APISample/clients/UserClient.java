package com.swiggy.APISample.clients;

import com.swiggy.APISample.dto.request.CreateUserRequest;
import com.swiggy.APISample.utils.UserUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserClient {

    @Autowired
    UserUtils userUtils;

    @Value("${baseUrl}")
    private String baseUrl;

    private static final String CREATE_USERS_RESOURCE_PATH = "/api/users";
    private static final String FETCH_USER_RESOURCE_PATH = "/api/users/{id}";

    /**
     * Build a common RequestSpecification with base configuration
     */
    private RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .headers(userUtils.getXpiHeaders())
                .contentType(ContentType.JSON)
                .log().all();
    }

    /**
     * Create a new user with the provided request data
     */
    public Response createUserData(Object createUserRequest) {
        return getRequestSpec()
                .body(createUserRequest)
                .when()
                .post(CREATE_USERS_RESOURCE_PATH)
                .then()
                .log().all()
                .extract()
                .response();
    }

    /**
     * Fetch user data by ID
     */
    public Response fetchUserData(String id) {
        return getRequestSpec()
                .pathParam("id", id)
                .when()
                .get(FETCH_USER_RESOURCE_PATH)
                .then()
                .log().all()
                .extract()
                .response();
    }
}
