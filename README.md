# RestAssured-TestNG-API-Test

Java-based, scalable REST API automation framework using **Maven**, **Rest Assured**, and **TestNG**.

Automates the **Notes API** at [practice.expandtesting.com](https://practice.expandtesting.com/notes/api/api-docs/).

---

## 🏗️ Project Structure

```
src/
├── main/java/com/expandtesting/
│   ├── config/
│   │   ├── ConfigManager.java          # Singleton – loads config.properties
│   │   └── EndpointConstants.java      # All endpoint path constants
│   ├── clients/
│   │   ├── BaseApiClient.java          # Shared RequestSpecification
│   │   ├── HealthClient.java
│   │   ├── UserClient.java
│   │   └── NoteClient.java
│   ├── models/
│   │   ├── request/                    # POJOs for request bodies
│   │   └── response/                   # POJOs for response deserialization
│   └── utils/
│       ├── DataGenerator.java          # Faker-based random test data
│       ├── AuthHelper.java             # Register + Login helper
│       └── ResponseValidator.java      # Reusable assertion helpers
│
└── test/java/com/expandtesting/
    ├── base/BaseTest.java              # @BeforeSuite RestAssured global config
    ├── listeners/ExtentReportListener.java  # HTML report via ExtentReports
    └── tests/
        ├── health/HealthCheckTest.java
        ├── users/
        │   ├── RegisterUserTest.java
        │   ├── LoginUserTest.java
        │   ├── UserProfileTest.java
        │   └── ChangePasswordTest.java
        └── notes/
            ├── CreateNoteTest.java
            ├── GetNotesTest.java
            ├── UpdateNoteTest.java
            └── DeleteNoteTest.java
```

---

## ✅ Tests Covered

| Area | Scenarios |
|------|-----------|
| **Health** | API health check |
| **Register** | Valid registration, duplicate email (409), invalid email/short password (400) |
| **Login** | Valid login, wrong password (401), missing fields (400) |
| **Profile** | Get profile, update profile, unauthorized access (401) |
| **Change Password** | Success, wrong current password, logout + token invalidation |
| **Create Note** | All 3 categories, missing fields (400), invalid category (400), no auth (401) |
| **Get Notes** | List all, get by ID, non-existent ID (404), no auth (401) |
| **Update Note** | Full PUT update, PATCH toggle completed, no auth (401) |
| **Delete Note** | Success + verify 404, non-existent ID, no auth (401) |

---

## 🚀 Running Tests

### Prerequisites
- Java 11+
- Maven 3.6+

### Run all tests
```bash
mvn clean test
```

### Run smoke tests only
```bash
mvn clean test -Dgroups=smoke
```

### Run regression tests only
```bash
mvn clean test -Dgroups=regression
```

### Override base URL
```bash
mvn clean test -Dbase.url=https://practice.expandtesting.com
```

---

## 📊 Reports

After each run, an **ExtentReports HTML** file is generated in the `reports/` directory.

---

## 🧰 Tech Stack

| Tool | Purpose |
|------|---------|
| Java 11 | Language |
| Maven | Build & dependency management |
| Rest Assured 5.x | HTTP client & assertions |
| TestNG 7.x | Test framework |
| Jackson | JSON serialization |
| JavaFaker | Random test data |
| ExtentReports 5.x | HTML test reporting |
| SLF4J | Logging |
