# REST Assured API Testing Framework

A professional **REST Assured** API testing framework using **Java** and **TestNG**, integrated with **Spring Boot** for dependency management.

---

## 📋 Project Overview

This project demonstrates a clean, maintainable API testing framework with:

- ✅ **REST Assured** for HTTP API testing
- ✅ **TestNG** for test execution
- ✅ **Jackson** for JSON serialization/deserialization
- ✅ **POJO** objects for type-safe testing
- ✅ **Plain Java** architecture (No Spring Boot dependencies in tests)
- ✅ **Data-driven testing** with JSON test data
- ✅ **Configuration management** via properties file
- ✅ **Comprehensive validation** layer

---

## 🗂️ Project Structure

```
APISample/
├── src/
│   ├── main/
│   │   ├── java/com/swiggy/APISample/
│   │   │   ├── ApiSampleApplication.java          # Spring Boot main class
│   │   │   ├── clients/
│   │   │   │   └── UserClient.java                # ✨ REST Assured API client (Plain Java)
│   │   │   ├── commons/
│   │   │   │   ├── FilePathConstatns.java         # File path constants
│   │   │   │   └── UserConstants.java             # API constants (header names)
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   ├── CreateUserRequest.java     # Request POJO
│   │   │   │   │   └── testData/
│   │   │   │   │       └── UserTestData.java      # Test data POJO
│   │   │   │   └── response/
│   │   │   │       ├── CreateUserResponse.java    # Response POJO
│   │   │   │       └── GetUserResponse.java       # Response POJO
│   │   │   ├── utils/
│   │   │   │   ├── ConfigReader.java              # Properties file reader
│   │   │   │   ├── DataMapper.java                # JSON to POJO mapper
│   │   │   │   └── RestAssuredSpecifications.java # ✨ REST Assured spec builder (Plain Java)
│   │   │   └── validator/
│   │   │       └── UserValidator.java             # ✨ Response validator
│   │   └── resources/
│   │       └── config.properties                  # Configuration file
│   ├── test/
│   │   ├── java/com/swiggy/APISample/
│   │   │   ├── tests/
│   │   │   │   └── UserTests.java                 # ✨ Test cases (Plain Java)
│   │   │   └── ApiSampleApplicationTests.java     # Base test class (if using Spring)
│   │   └── resources/
│   │       └── testData/
│   │           ├── userData.json                  # Test data for create user
│   │           └── fetchUserData.json             # Test data for fetch user
│   ├── pom.xml                                    # Maven configuration
│   ├── REST_ASSURED_GUIDE.md                      # Complete setup guide
│   ├── REST_ASSURED_MIGRATION_SUMMARY.md          # Migration details
│   └── IMPLEMENTATION_SUMMARY.md                  # Implementation details
```

---

## 🎯 Core Components

### **1. UserClient.java** - REST Assured API Client

**Location**: `src/main/java/com/swiggy/APISample/clients/UserClient.java`

Plain Java class that encapsulates all REST Assured API calls:

```java
public class UserClient {
    
    // Create User via POST
    public Response createUserData(Object createUserRequest) {
        return RestAssured.given()
            .spec(RestAssuredSpecifications.getRequestSpecification())
            .body(createUserRequest)
            .contentType(ContentType.JSON)
            .log().all()
        .when()
            .post("/api/users")
        .then()
            .log().all()
            .extract().response();
    }
    
    // Fetch User via GET
    public Response fetchUserById(String id) {
        return RestAssured.given()
            .spec(RestAssuredSpecifications.getRequestSpecification())
            .pathParam("id", id)
            .log().all()
        .when()
            .get("/api/users/{id}")
        .then()
            .log().all()
            .extract().response();
    }
}
```

**Features:**
- ✅ Centralized REST Assured specification usage
- ✅ Automatic request/response logging
- ✅ Clean fluent API
- ✅ No Spring Boot dependency

---

### **2. RestAssuredSpecifications.java** - Configuration Builder

**Location**: `src/main/java/com/swiggy/APISample/utils/RestAssuredSpecifications.java`

Plain Java utility class that builds RequestSpecification:

```java
public class RestAssuredSpecifications {
    
    private static final String baseUrl = ConfigReader.get("baseUrl");
    private static final String apiKey = ConfigReader.get("x.api.value");
    
    public static RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
            .setBaseUri(baseUrl)
            .addHeader("X-API-Key", apiKey)
            .setContentType(ContentType.JSON)
            .build();
    }
}
```

**Features:**
- ✅ Centralized configuration
- ✅ ConfigReader for property file loading
- ✅ Reusable RequestSpecification
- ✅ Header and content type management

---

### **3. UserValidator.java** - Response Validation

**Location**: `src/main/java/com/swiggy/APISample/validator/UserValidator.java`

Provides comprehensive validation methods:

```java
public class UserValidator {
    
    public void validateCreateUser(int expectedStatusCode, Response response) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode);
        CreateUserResponse createUserResponse = response.getBody()
            .as(CreateUserResponse.class);
        Assert.assertNotNull(createUserResponse);
    }
    
    public void validateContentType(Response response, String expectedType) {
        String actualType = response.getContentType();
        Assert.assertTrue(actualType.contains(expectedType));
    }
}
```

**Available Methods:**
- `validateCreateUser()` - Validate create user response
- `validateFetchUser()` - Validate fetch user response
- `validateStatusCode()` - HTTP status validation
- `validateContentType()` - Content type validation
- `validateResponseTime()` - Performance validation

---

### **4. UserTests.java** - Test Cases

**Location**: `src/test/java/com/swiggy/APISample/tests/UserTests.java`

Plain Java test class with two test cases:

```java
public class UserTests {
    
    private final UserClient userClient = new UserClient();
    private final UserValidator userValidator = new UserValidator();
    private final DataMapper dataMapper = new DataMapper();
    
    /**
     * Test 1: Create User
     */
    @Test(description = "Create user using JSON test data")
    public void createUser() throws IOException {
        // Load test data from userData.json
        List<UserTestData> testDataList = 
            dataMapper.userList(FilePathConstatns.userFilePath, UserTestData.class);
        UserTestData testData = testDataList.get(0);
        
        // Create request using Lombok builder
        CreateUserRequest request = CreateUserRequest.builder()
            .name(testData.getName())
            .job(testData.getJob())
            .build();
        
        // Execute API
        Response response = userClient.createUserData(request);
        
        // Validate
        userValidator.validateCreateUser(testData.getExpectedStatusCode(), response);
        userValidator.validateContentType(response, "application/json");
    }
    
    /**
     * Test 2: Fetch User
     */
    @Test(description = "Fetch user using JSON test data")
    public void fetchUser() throws IOException {
        // Load test data
        List<UserTestData> testDataList = 
            dataMapper.userList(FilePathConstatns.fetchUserFilePath, UserTestData.class);
        UserTestData testData = testDataList.get(0);
        
        // Execute API
        Response response = userClient.fetchUserById(testData.getId());
        
        // Validate
        userValidator.validateFetchUser(testData.getExpectedStatusCode(), response);
        userValidator.validateContentType(response, "application/json");
    }
}
```

**Test Design:**
- ✅ Plain Java (no Spring Boot in tests)
- ✅ Data-driven from JSON files
- ✅ POJO-based test data
- ✅ Comprehensive validation
- ✅ Clean, readable test code

---

## 📊 Test Data Files

### **userData.json** - Create User Test Data

**Location**: `src/test/resources/testData/userData.json`

```json
[
    {
        "name": "John Doe",
        "job": "Software Engineer",
        "expectedStatusCode": 201
    },
    {
        "name": "Jane Smith",
        "job": "QA Engineer",
        "expectedStatusCode": 201
    }
]
```

### **fetchUserData.json** - Fetch User Test Data

**Location**: `src/test/resources/testData/fetchUserData.json`

```json
[
    {
        "id": "1",
        "expectedStatusCode": 200
    }
]
```

---

## 🔧 Configuration

### **config.properties** - Application Configuration

**Location**: `src/main/resources/config.properties`

```properties
spring.application.name=APISample
baseUrl=https://reqres.in
x.api.value=reqres-free-v1
```

**Properties Used:**
- `baseUrl` - Base URL for API calls
- `x.api.value` - API key header value

---

## 📝 POJO Objects

### **UserTestData.java** - Test Data POJO

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTestData {
    public String name;
    public String job;
    public int expectedStatusCode;
    public String id;
}
```

### **CreateUserRequest.java** - Request POJO

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreateUserRequest {
    private String name;
    private String job;
}
```

### **CreateUserResponse.java** - Response POJO

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreateUserResponse {
    private String name;
    private String job;
    private String id;
    private String createdAt;
}
```

---

## 🚀 Running the Tests

### **Prerequisites**
- Java 17+
- Maven 3.8+

### **Run All Tests**
```bash
cd /Users/macbook/Downloads/APISample
./mvnw clean test
```

### **Run Specific Test Class**
```bash
./mvnw test -Dtest=UserTests
```

### **Run Specific Test Method**
```bash
./mvnw test -Dtest=UserTests#createUser
./mvnw test -Dtest=UserTests#fetchUser
```

### **Compile Without Running Tests**
```bash
./mvnw clean compile -DskipTests
```

---

## 📦 Dependencies

All dependencies are configured in `pom.xml`:

```xml
<!-- REST Assured -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.5.6</version>
</dependency>

<!-- TestNG -->
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>7.11.0</version>
</dependency>

<!-- Jackson for JSON serialization -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.20.1</version>
</dependency>

<!-- Lombok for POJO generation -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.42</version>
    <scope>provided</scope>
</dependency>
```

---

## 🏗️ Architecture

### **Test Flow Diagram**

```
┌─────────────────────────────────────────────┐
│           UserTests (Test Cases)            │
│  - Plain Java class (no Spring Boot)        │
│  - Reads JSON test data using DataMapper    │
│  - Creates POJO objects (UserTestData)      │
└────────────────────┬────────────────────────┘
                     │
                     ├─────────────────────┐
                     │                     │
        ┌────────────▼──────────┐   ┌──────▼──────────┐
        │    UserClient         │   │ RestAssured     │
        │  (Plain Java)         │   │ Specifications  │
        │                       │   │  (Plain Java)   │
        │ ✓ createUserData()    │   │                 │
        │ ✓ fetchUserById()     │   │ ✓ Config        │
        │                       │   │ ✓ Headers       │
        └────────────┬──────────┘   │ ✓ Base URL      │
                     │              └──────┬──────────┘
                     │                     │
        ┌────────────▼─────────────────────▼──────┐
        │      REST Assured Fluent API            │
        │  given().spec(...).body(...).when()...  │
        └────────────┬──────────────────────────────┘
                     │
        ┌────────────▼──────────────────┐
        │     HTTP Request/Response     │
        │   (REST API Endpoint)         │
        └────────────┬──────────────────┘
                     │
        ┌────────────▼──────────────────┐
        │    UserValidator              │
        │  (Plain Java)                 │
        │  ✓ validateCreateUser()       │
        │  ✓ validateFetchUser()        │
        │  ✓ validateContentType()      │
        │  ✓ validateStatusCode()       │
        └────────────────────────────────┘
```

---

## ✨ Key Features

| Feature | Implementation |
|---------|-----------------|
| **API Testing Framework** | REST Assured 5.5.6 |
| **Test Framework** | TestNG 7.11.0 |
| **JSON Serialization** | Jackson 2.20.1 |
| **POJO Generation** | Lombok 1.18.42 |
| **Configuration** | ConfigReader + Properties |
| **Test Data** | JSON files + DataMapper |
| **Plain Java** | No Spring Boot in tests |
| **Fluent API** | Clean, readable code |
| **Logging** | Automatic request/response |
| **Validation** | Comprehensive assertions |

---

## 🔍 REST Assured Usage Patterns

### **Pattern 1: Given-When-Then**
```java
RestAssured.given()              // Setup request
    .spec(specification)
    .body(data)
.when()                           // Execute
    .post(endpoint)
.then()                           // Verify
    .log().all()
    .extract().response();
```

### **Pattern 2: Request Specification**
```java
RequestSpecification spec = new RequestSpecBuilder()
    .setBaseUri(baseUrl)
    .addHeader("X-API-Key", apiKey)
    .setContentType(ContentType.JSON)
    .build();
```

### **Pattern 3: Response Deserialization**
```java
CreateUserResponse response = 
    httpResponse.getBody().as(CreateUserResponse.class);
```

### **Pattern 4: Path Parameters**
```java
RestAssured.given()
    .pathParam("id", userId)
    .get("/api/users/{id}")
```

---

## 📚 File Locations Quick Reference

| Component | Location |
|-----------|----------|
| Test Cases | `src/test/java/.../tests/UserTests.java` |
| API Client | `src/main/java/.../clients/UserClient.java` |
| Validator | `src/main/java/.../validator/UserValidator.java` |
| Specifications | `src/main/java/.../utils/RestAssuredSpecifications.java` |
| Test Data | `src/test/resources/testData/*.json` |
| Config | `src/main/resources/config.properties` |
| POJOs | `src/main/java/.../dto/**/*.java` |

---

## 🧪 Test Cases Summary

### **Test 1: Create User**
- **File**: `UserTests.java::createUser()`
- **Data Source**: `userData.json`
- **HTTP Method**: POST `/api/users`
- **Validation**: Status code 201, JSON content type, response body

### **Test 2: Fetch User**
- **File**: `UserTests.java::fetchUser()`
- **Data Source**: `fetchUserData.json`
- **HTTP Method**: GET `/api/users/{id}`
- **Validation**: Status code 200, JSON content type, response structure

---

## ✅ Best Practices Implemented

✅ **Plain Java Architecture** - No Spring Boot in test code
✅ **Separation of Concerns** - Client, Validator, Tests separate
✅ **POJO Usage** - Type-safe test data and responses
✅ **Configuration Management** - Externalized properties
✅ **Reusable Specifications** - Centralized REST Assured config
✅ **Data-Driven Testing** - JSON-based test data
✅ **Comprehensive Validation** - Multiple assertion types
✅ **Automatic Logging** - Built-in request/response logging
✅ **Clean Code** - Readable, maintainable test code
✅ **Fluent API** - Expressive REST Assured usage

---

## 📖 Additional Documentation

This project includes comprehensive documentation:

1. **REST_ASSURED_GUIDE.md** - Complete setup and usage guide
2. **REST_ASSURED_MIGRATION_SUMMARY.md** - Migration details
3. **IMPLEMENTATION_SUMMARY.md** - Implementation overview

---

## 🎓 Learning Resources

- **REST Assured Official Docs**: https://rest-assured.io/
- **TestNG Documentation**: https://testng.org/
- **Jackson Serialization**: https://www.baeldung.com/jackson
- **GitHub REST Assured**: https://github.com/rest-assured/rest-assured

---

## ✅ Build Status

Latest build: **SUCCESS** ✅

```
Building APISample 0.0.1-SNAPSHOT
Compiling source files
Total time: 2.5s
BUILD SUCCESS
```

---

## 🚀 Quick Start

1. **Clone/Navigate to project**
   ```bash
   cd /Users/macbook/Downloads/APISample
   ```

2. **Build project**
   ```bash
   ./mvnw clean compile -DskipTests
   ```

3. **Run tests**
   ```bash
   ./mvnw clean test
   ```

4. **View test results** in console output

---

## 📞 Support

For questions or issues:
1. Check the documentation files
2. Review test implementation in `UserTests.java`
3. Check configuration in `config.properties`
4. Verify test data in JSON files

---

**Ready to test APIs with REST Assured! 🚀**
