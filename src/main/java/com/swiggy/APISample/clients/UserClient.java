package com.swiggy.APISample.clients;

import com.swiggy.APISample.utils.RestAssuredSpecifications;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UserClient {

    private static final String CREATE_USERS_PATH = "/api/users";
    private static final String FETCH_USER_PATH = "/api/users/{id}";

    /**
     * Create User (POST)
     */
    public Response createUserData(Object createUserRequest) {

        return RestAssured
                .given()
                .spec(RestAssuredSpecifications.getRequestSpecification())
                .body(createUserRequest)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .post(CREATE_USERS_PATH)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Fetch User by ID (GET)
     */
    public Response fetchUserById(String id) {

        return RestAssured
                .given()
                .spec(RestAssuredSpecifications.getRequestSpecification())
                .pathParam("id", id)
                .log().all()
                .when()
                .get(FETCH_USER_PATH)
                .then()
                .log().all()
                .extract().response();
    }
}
