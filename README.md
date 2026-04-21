# RestAssured-TestNG-API-Test

Java-based, scalable REST API automation framework using **Maven**, **Rest Assured**, and **TestNG**.

Automates the **Notes API** at [practice.expandtesting.com](https://practice.expandtesting.com/notes/api/api-docs/).

---

## 🏗️ Project Structure

```
src/
├── main/java/com/expandtesting/
│   ├── config/
│   │   ├── ConfigManager.java          # Singleton – loads config.json (fallback-safe)
│   │   └── EndpointConstants.java      # Endpoint paths loaded from endpoints.json
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
├── main/resources/
│   └── endpoints.json                  # API endpoint paths + auth header key
│
└── test/resources/
    └── config.json                     # Runtime environment config

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

## ⚙️ Configuration Files

### `src/test/resources/config.json`
Controls runtime settings consumed by `ConfigManager`:

```json
{
  "base.url": "https://practice.expandtesting.com",
  "request.timeout": 30000,
  "log.all.requests": true,
  "log.all.responses": true
}
```

You can still override any key at runtime using JVM properties (for example `-Dbase.url=...`).

### `src/main/resources/endpoints.json`
Centralized API paths consumed by `EndpointConstants`:

- `HEALTH_CHECK`
- `USERS_REGISTER`, `USERS_LOGIN`, `USERS_PROFILE`, `USERS_CHANGE_PASSWORD`, etc.
- `NOTES`, `NOTES_BY_ID`
- `AUTH_TOKEN_HEADER`

---

## 🚀 Running Tests

### Prerequisites
- Java 11+
- Maven Wrapper (`mvnw.cmd`/`mvnw`) is included

### Windows (PowerShell)

#### Run all tests
```powershell
Set-Location "D:\IdeaProjects"
.\mvnw.cmd clean test
```

#### Run smoke tests only
```powershell
Set-Location "D:\IdeaProjects"
./mvnw.cmd test -Dgroups=smoke
```

#### Run regression tests only
```powershell
Set-Location "D:\IdeaProjects"
.\mvnw.cmd clean test -Dgroups=regression
```

#### Override base URL
```powershell
Set-Location "D:\IdeaProjects"
.\mvnw.cmd clean test -Dbase.url=https://practice.expandtesting.com
```

### macOS/Linux
```bash
./mvnw clean test
./mvnw test -Dgroups=smoke
./mvnw clean test -Dgroups=regression
./mvnw clean test -Dbase.url=https://practice.expandtesting.com
```


---

## 📊 Reports

After each run, an **ExtentReports HTML** file is generated in the `reports/` directory.

---

## 🧰 Tech Stack

| Tool | Purpose |
|------|---------|
| Java 11+ (JDK 25 tested) | Language/runtime |
| Maven | Build & dependency management |
| Rest Assured 5.x | HTTP client & assertions |
| TestNG 7.x | Test framework |
| Jackson | JSON serialization |
| JavaFaker | Random test data |
| ExtentReports 5.x | HTML test reporting |
| SLF4J | Logging |
