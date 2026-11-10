# SauceDemo Selenium Automation Framework – End-to-End Testing

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.52.0-green)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.12.0-red)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-Build-blue)](https://maven.apache.org/)
[![Allure](https://img.shields.io/badge/Reporting-Allure-purple)](https://allurereport.org/)
## 👨‍💻 Author

**Sahil Shaikh**

🎓 **YouTube Channel:** Test Automation Academy 2.0

GitHub: [@shaikhsahilss2024-cmd](https://github.com/shaikhsahilss2024-cmd)

**Content & Learning:** Selenium Automation, Java, TestNG, Framework Development, and End-to-End Testing.
## 📌 Project Overview

This project is an End-to-End (E2E) test automation framework developed using **Java, Selenium WebDriver, TestNG, and Maven** to automate functional testing of the [SauceDemo web application](https://www.saucedemo.com/).

The framework follows the **Page Object Model (POM)** design pattern to maintain reusable, scalable, and maintainable automation code.

It aims to validate important e-commerce workflows, including user authentication, inventory interactions, shopping cart operations, and checkout functionality as the framework evolves.

## 🎯 Project Objectives

- Automate functional test scenarios for SauceDemo.
- Implement a maintainable Selenium automation framework.
- Follow the Page Object Model design pattern.
- Execute test cases using TestNG.
- Generate detailed execution reports using Allure.
- Capture screenshots automatically when tests fail.
- Maintain execution logs using Log4j2.
- Improve test reusability and scalability.

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language / development runtime |
| Selenium WebDriver 4.52.0 | Browser automation |
| TestNG 7.12.0 | Test execution and assertions |
| Maven | Build and dependency management |
| Allure TestNG 2.35.5 | Test reporting |
| Log4j2 2.26.1 | Logging |
| AspectJ Weaver 1.9.24 | Allure step annotations |
| Chrome | Browser execution |
| IntelliJ IDEA | Development environment |
| Git & GitHub | Version control |

## 🏗️ Framework Architecture

The framework follows the Page Object Model (POM) architecture.

Each web page has a dedicated Page Object class containing its locators and reusable page interaction methods. Test classes contain test scenarios and validation logic.

### Project Structure

```text
SeleniumFrameworkSauseDemoWebsiteWithTestCasesE2E
│
├── src
│   ├── main
│   │   └── java
│   │       ├── base
│   │       │   └── BasePage.java
│   │       │
│   │       ├── pages
│   │       │   ├── LoginPage.java
│   │       │   └── InventoryPage.java
│   │       │
│   │       └── utilities
│   │           ├── ConfigReader.java
│   │           └── DriverFactory.java
│   │
│   └── test
│       ├── java
│       │   ├── base
│       │   │   └── BaseTest.java
│       │   │
│       │   ├── tests
│       │   │   ├── LoginTest.java
│       │   │   └── InventoryTest.java
│       │   │
│       │   ├── listeners
│       │   │   └── TestListener.java
│       │   │
│       │   └── utilities
│       │       └── ScreenshotUtils.java
│       │
│       └── resources
│           ├── config.properties
│           ├── log4j2.xml
│           └── testng.xml
│
├── logs
├── allure-results
├── allure-report
├── target
│
├── pom.xml
└── README.md
```

## 🧪 Test Coverage

The framework is organized around individual application modules.

### 1. Login Module

Validates user authentication functionality.

Test scenarios include:

- Login with valid credentials.
- Login with invalid username.
- Login with invalid password.
- Login with empty username.
- Login with empty password.
- Login with locked-out user.
- Validation of login error messages.
- Successful navigation to the inventory page.

### 2. Inventory Module

Validates inventory page functionality.

Test scenarios include:

- Verify inventory page navigation.
- Verify inventory page elements.
- Open the navigation menu.
- Close the navigation menu.
- Verify menu navigation options.
- Validate inventory page interactions.

### 3. Future E2E Modules

The framework can be extended with:

- Product Details
- Shopping Cart
- Checkout Information
- Checkout Overview
- Checkout Complete

These modules can be implemented incrementally as part of the E2E automation suite.

## ⚙️ Framework Features

### Page Object Model (POM)

- Separate page classes for individual application pages.
- Centralized element locators.
- Reusable page interaction methods.
- Separation of test logic and application logic.

### WebDriver Management

- Centralized WebDriver initialization.
- Browser configuration through properties.
- ThreadLocal WebDriver management.
- Browser cleanup after test execution.
- Selenium Manager support for driver resolution.

### TestNG Integration

- Test execution using TestNG.
- Test annotations and assertions.
- Centralized test setup and teardown.
- Test listener integration.

### Allure Reporting

The framework integrates Allure to generate detailed test execution reports.

Features include:

- Test execution status.
- Test execution details.
- Allure step annotations.
- Failure screenshots.
- Execution diagnostics.

### Automatic Failure Screenshots

When a test fails, the custom `TestListener` captures a screenshot and attaches it to the Allure report.

Screenshots are stored under:

```text
target/screenshots/
```

### Logging with Log4j2

Log4j2 is integrated for execution monitoring and debugging.

Logs provide information about:

- Browser initialization.
- Test execution.
- Test failures.
- Screenshot capture.
- Framework execution events.

Log file:

```text
logs/automation.log
```

## 💻 Prerequisites

Make sure the following software is installed:

1. Java JDK 17 or newer (Java 21 is used in the current development environment).
2. Apache Maven 3.9.x or newer.
3. Google Chrome browser.
4. IntelliJ IDEA or another Java IDE.
5. Git.
6. Allure Commandline for generating HTML reports.

Verify Java installation:

```bash
java -version
```

Verify Maven installation:

```bash
mvn -version
```

Verify Git installation:

```bash
git --version
```

## 🚀 Getting Started

### Step 1: Clone the Repository

```bash
git clone https://github.com/shaikhsahilss2024-cmd/SeleniumFrameworkSauseDemoWebsiteWithTestCasesE2E.git
```

### Step 2: Navigate to Project Directory

```bash
cd SeleniumFrameworkSauseDemoWebsiteWithTestCasesE2E
```

### Step 3: Open the Project

Open the project in IntelliJ IDEA.

Import it as a Maven project and allow Maven to download all required dependencies.

### Step 4: Configure the Application

Open:

```text
src/test/resources/config.properties
```

Example configuration:

```properties
application.url=https://www.saucedemo.com/
browser=chrome
valid.username=standard_user
valid.password=secret_sauce
invalid.username=invalid_user
invalid.password=wrong_password
locked.username=locked_out_user
explicit.wait.seconds=10
implicit.wait.seconds=0
```

These are public SauceDemo demonstration credentials.

## ▶️ Running Test Cases

### Execute All Test Cases

From the project root:

```bash
mvn clean test
```

### Execute Login Tests

```bash
mvn -Dtest=LoginTest test
```

### Execute Inventory Tests

```bash
mvn -Dtest=InventoryTest test
```

### Execute a Specific Test Method

```bash
mvn -Dtest=InventoryTest#testOpenAndCloseMenu test
```

You can also execute individual TestNG classes or methods directly from IntelliJ IDEA.

## 📊 Allure Test Reporting

After test execution, Allure results are generated in:

```text
allure-results/
```

### Generate Allure HTML Report

```bash
allure generate allure-results --clean -o allure-report
```

### Open Allure Report

```bash
allure open allure-report
```

Alternatively, run:

```bash
allure serve allure-results
```

The Allure report provides a visual overview of test execution, including passed tests, failed tests, execution details, and attached screenshots.

## 📸 Screenshots and Logs

| Artifact | Location |
|---|---|
| Failure Screenshots | `target/screenshots/` |
| Allure Results | `allure-results/` |
| Allure HTML Report | `allure-report/` |
| Application Logs | `logs/automation.log` |
| Maven Test Reports | `target/surefire-reports/` |

## 🔍 Troubleshooting

**1. WebDriver initialization failure**

- Verify Chrome is installed.
- Verify network connectivity for Selenium Manager.
- Check browser compatibility.

**2. Element not found or TimeoutException**

- Verify element locators.
- Check whether the page has loaded completely.
- Review explicit wait conditions.
- Check the failure screenshot.

**3. Allure report is empty**

- Verify that test execution generated files in `allure-results/`.
- Check the Allure Commandline installation.
- Execute the test suite again.

**4. Maven build failure**

```bash
mvn clean install
```

Verify Java and Maven configurations if the issue persists.

## 👨‍💻 Author

**Sahil Shaikh**

GitHub: [@shaikhsahilss2024-cmd](https://github.com/shaikhsahilss2024-cmd)

## 🔗 Project Repository

[Selenium SauceDemo Automation Framework – GitHub](https://github.com/shaikhsahilss2024-cmd/SeleniumFrameworkSauseDemoWebsiteWithTestCasesE2E)

## ⭐ Support

If you find this project useful, consider giving the repository a ⭐ on GitHub.

---
## 👨‍💻 Author

**Sahil Shaikh**

🎓 **YouTube Channel:** Test Automation Academy 2.0

GitHub: [@shaikhsahilss2024-cmd](https://github.com/shaikhsahilss2024-cmd)

**Content & Learning:** Selenium Automation, Java, TestNG, Framework Development, and End-to-End Testing.
**Developed with Java, Selenium WebDriver, TestNG, Maven, and Allure Reporting.**
