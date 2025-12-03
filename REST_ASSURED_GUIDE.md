# REST Assured API Testing - Setup & Usage Guide

This project has been enhanced with **REST Assured** for comprehensive API testing using Java.

## 📋 Project Structure

```
src/
├── main/
│   ├── java/com/swiggy/APISample/
│   │   ├── clients/
│   │   │   └── UserClient.java          # API client using REST Assured
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   │   └── CreateUserRequest.java
│   │   │   └── response/
│   │   │       ├── CreateUserResponse.java
│   │   │       └── GetUserResponse.java
│   │   ├── utils/
│   │   │   ├── UserUtils.java           # Header utilities
│   │   │   ├── DataMapper.java          # JSON to Object mapping
│   │   │   └── RestAssuredSpecifications.java  # REST Assured spec builders
│   │   └── validator/
│   │       └── UserValidator.java       # Response validation
│   └── resources/
│       └── application.properties
└── test/
    ├── java/com/swiggy/APISample/
    │   ├── ApiSampleApplicationTests.java # Base test class
    │   └── tests/
    │       └── UserTests.java            # Test cases
    └── resources/
        └── testData/
            ├── userData.json
            └── fetchUserData.json
```

## 🔧 Key Components

### 1. **UserClient.java** - REST Assured API Client
- Uses `RestAssured` fluent API for making HTTP requests
- Implements centralized request specifications
- Supports Headers, Path Parameters, and Request Body
- Automatic request/response logging

```java
public Response createUserData(Object createUserRequest) {
    return getRequestSpec()
            .body(createUserRequest)
            .when().post(CREATE_USERS_RESOURCE_PATH)
            .then().log().all()
            .extract().response();
}
```

### 2. **UserValidator.java** - Response Validation
- Validates HTTP Status Codes
- Deserializes JSON responses to Java objects
- Validates Content Types
- Checks Response Times
- Provides detailed assertion messages

```java
public void validateCreateUser(int expectedStatusCode, Response response) {
    Assert.assertEquals(response.getStatusCode(), expectedStatusCode);
    CreateUserResponse createUserResponse = response.getBody().as(CreateUserResponse.class);
    Assert.assertNotNull(createUserResponse.getId());
}
```

### 3. **RestAssuredSpecifications.java** - Specifications Builder
- Centralized request specification configuration
- Pre-built response specifications
- Consistent header management
- Base URI configuration

### 4. **UserTests.java** - Test Cases
- Parameterized tests using TestNG DataProvider
- Tests for user creation and fetching
- Data-driven testing with JSON test data
- Comprehensive validation

## 🚀 Dependencies Added

```xml
<!-- REST Assured -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.5.6</version>
</dependency>

<!-- JSON Path for REST Assured -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>json-path</artifactId>
    <version>5.5.6</version>
</dependency>

<!-- Jackson for JSON serialization -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.20.1</version>
</dependency>

<!-- TestNG for Test Execution -->
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>7.11.0</version>
</dependency>
```

## 📝 Configuration

### application.properties
```properties
spring.application.name=APISample
baseUrl=https://reqres.in
x.api.value=reqres-free-v1
```

## 🧪 Running Tests

### Run all tests:
```bash
mvn clean test
```

### Run specific test class:
```bash
mvn test -Dtest=UserTests
```

### Run specific test method:
```bash
mvn test -Dtest=UserTests#createUsers
```

### Run with TestNG suite:
```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testNGSuites/products.xml
```

## 📊 Example Test Case

```java
@Test(dataProvider = "userCases", description = "Create users with various test cases")
public void createUsers(UserTestData userTestData) {
    // Prepare request
    CreateUserRequest createUserData = new CreateUserRequest(
        userTestData.getName(), 
        userTestData.getJob()
    );

    // Execute API call using REST Assured
    Response response = userClient.createUserData(createUserData);

    // Validate response
    userValidator.validateCreateUser(userTestData.getExpectedStatusCode(), response);
    userValidator.validateContentType(response, "application/json");
}
```

## 🔍 REST Assured Features Used

### 1. **Request Building**
```java
RestAssured.given()
    .headers(headers)
    .contentType(ContentType.JSON)
    .body(requestData)
```

### 2. **Response Handling**
```java
.when().post(endpoint)
.then().log().all()
.extract().response();
```

### 3. **JSON Deserialization**
```java
response.getBody().as(CreateUserResponse.class)
```

### 4. **Path Parameters**
```java
.pathParam("id", userId)
.get(endpoint)
```

### 5. **Assertions**
```java
Assert.assertEquals(response.getStatusCode(), expectedCode);
Assert.assertTrue(response.getContentType().contains("application/json"));
```

## 📈 Best Practices Implemented

✅ **Separation of Concerns**: Client, Validator, and Test classes are separate
✅ **DRY Principle**: Common RequestSpec builder method in UserClient
✅ **Fluent API**: Clear and readable test code
✅ **Parameterized Testing**: DataProvider for multiple test scenarios
✅ **Centralized Configuration**: Properties file for base URL and headers
✅ **Logging**: Automatic request/response logging for debugging
✅ **Error Messages**: Descriptive assertion messages for failures
✅ **Type Safety**: Strong typing with DTO objects for requests and responses
✅ **Reusability**: Validator methods can be used across multiple tests
✅ **Spring Integration**: Using Spring Boot for dependency injection

## 🔗 REST Assured Documentation

- [Official Documentation](https://rest-assured.io/)
- [GitHub Repository](https://github.com/rest-assured/rest-assured)
- [JSONPath Guide](https://rest-assured.io/docs/getting-started/#json-body-validation)

## 📚 Common REST Assured Methods

| Method | Purpose |
|--------|---------|
| `given()` | Start building request specification |
| `when()` | Execute HTTP method |
| `then()` | Add response conditions |
| `extract()` | Extract response data |
| `log()` | Log request/response details |
| `body()` | Add request body |
| `headers()` | Add request headers |
| `pathParam()` | Add path parameters |
| `queryParam()` | Add query parameters |
| `.as(Class)` | Deserialize JSON to Java object |

## 💡 Tips & Tricks

1. **Reuse RequestSpecification**: Use the builder pattern for consistent requests
2. **Log Everything**: Use `.log().all()` during development
3. **Extract Response Parts**: Use `.extract().statusCode()`, `.extract().header()`, etc.
4. **Timeout Handling**: Add `.time(Matchers.lessThan(5000L))` for response time validation
5. **Header Management**: Centralize headers in utilities for easy maintenance
6. **Error Handling**: Always validate status codes and error messages in tests

## 🐛 Troubleshooting

### Issue: Import RestAssured not working
**Solution**: Ensure REST Assured dependency is added to pom.xml

### Issue: TestNG not running
**Solution**: Add surefire-testng dependency and configure pom.xml correctly

### Issue: JSON deserialization fails
**Solution**: Ensure DTO classes have proper Lombok annotations (@Data, @NoArgsConstructor, @AllArgsConstructor)

---

**Project is now fully configured with REST Assured for API testing! 🎉**
