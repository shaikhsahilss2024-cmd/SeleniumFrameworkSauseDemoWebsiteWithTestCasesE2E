# SauceDemo Automation Framework — Login Module

A Java 17 + Selenium WebDriver + TestNG + Maven Page Object Model (POM) framework for https://www.saucedemo.com/.

## Current implementation scope

This delivery implements the framework foundation and the Login module only. The Inventory, Product Details, Cart, Checkout Information, Checkout Overview and Checkout Complete page/test classes are intentionally not implemented yet. They will be added after the Login suite has been executed and verified.

## Technology versions

- Java: 17
- Selenium Java: 4.52.0
- TestNG: 7.12.0
- Allure TestNG: 2.35.5
- Apache Log4j2: 2.26.1
- AspectJ Weaver: 1.9.24 (for Allure `@Step` annotations)
- Maven Compiler Plugin: 3.14.1
- Maven Surefire Plugin: 3.5.4

## Project structure (current delivery)

```text
SauceDemoAutomationFramework
├── src/main/java
│   ├── base/BasePage.java
│   ├── pages/LoginPage.java
│   └── utilities
│       ├── ConfigReader.java
│       └── DriverFactory.java
├── src/test/java
│   ├── base/BaseTest.java
│   ├── tests/LoginTest.java
│   ├── listeners/TestListener.java
│   └── utilities/ScreenshotUtils.java
├── src/test/resources
│   ├── config.properties
│   ├── log4j2.xml
│   └── testng.xml
├── logs
├── allure-results
├── allure-report
├── pom.xml
└── README.md
```

The final seven-module structure will also include the six remaining Page Object and test classes described in the project plan.

## Prerequisites

1. Install a 64-bit JDK 17 and set `JAVA_HOME`.
2. Install Apache Maven 3.9.x or newer and add `mvn` to `PATH`.
3. Install Google Chrome. Alternatively, change `browser=chrome` to `browser=firefox` and install Firefox.
4. Selenium Manager (included with Selenium 4.52.0) resolves the compatible browser driver automatically when the browser and network access are available.
5. Install Allure Commandline and add `allure` to `PATH` to generate/open the HTML report.

## Configuration

Edit `src/test/resources/config.properties`:

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

These are public SauceDemo demonstration credentials, not credentials for a private account. Do not put real secrets in a committed properties file.

## Run the Login suite

From the project root:

```bash
mvn clean test
```

The configured Surefire plugin reads `src/test/resources/testng.xml`, which currently runs `tests.LoginTest` only.

Run only one test method (example):

```bash
mvn -Dtest=LoginTest#testLoginWithValidCredentials test
```

The `-Dtest` selector is supported by Maven Surefire and takes precedence over the suite XML for a one-off class/method run. For an IDE run, right-click `LoginTest.java` and choose Run As > TestNG Test.

## Allure reporting

After a successful TestNG execution, raw results are written to `allure-results/`.

```bash
allure generate allure-results --clean -o allure-report
allure open allure-report
```

The custom `TestListener` is registered on `LoginTest` with TestNG's `@Listeners` annotation, so it also runs when Maven Surefire selects an individual method with `-Dtest`. The Allure TestNG adapter is supplied by `allure-testng` and uses its TestNG service-loader registration. The TestListener attaches a PNG screenshot to Allure when a test method fails. AspectJ weaving is configured in Maven Surefire so `@Step` methods in page objects become Allure steps.

## Logs and screenshots

- Log4j2 console output: terminal / IDE console.
- Log4j2 file output: `logs/automation.log`.
- Failure screenshots: `target/screenshots/`.
- Allure raw result files: `allure-results/`.
- Generated Allure HTML: `allure-report/`.

## Important implementation notes

- `ConfigReader` loads `config.properties` once via the classloader from `src/test/resources`.
- `DriverFactory` uses a `ThreadLocal<WebDriver>` so independent test threads do not share a browser instance.
- Implicit wait defaults to zero; interactions use explicit `WebDriverWait` in `BasePage`.
- `TestListener.afterInvocation` captures a failure screenshot immediately after the failed test method, before BaseTest's `@AfterMethod` calls `quit()`.
- The LoginTest methods map to SD-LOG-001 through SD-LOG-012 from the test inventory.

## Eclipse / IntelliJ setup

1. Extract the project ZIP.
2. In Eclipse: File > Import > Existing Maven Projects; select the extracted project folder. In IntelliJ: File > Open; select `pom.xml` or the project folder and choose Open as Project.
3. Set the project SDK/JDK to 17.
4. Allow Maven to import/download dependencies (`Update Project` in Eclipse or Maven Reload in IntelliJ).
5. Check `config.properties` and confirm Chrome is installed.
6. Run `mvn clean test` from the project terminal, or run `LoginTest` as a TestNG test from the IDE.

## Troubleshooting

- `mvn` not recognized: install Maven and add its `bin` directory to PATH.
- Unsupported class file / Java version errors: select JDK 17 for Maven and the IDE project.
- Browser/driver startup failure: update Chrome, check network access for Selenium Manager, and retry.
- Element timeout: confirm SauceDemo is reachable, check the locator and increase `explicit.wait.seconds` only when justified.
- Allure report empty: confirm `allure-results/` contains result JSON files after a successful `mvn clean test`.
- Log file absent: confirm Log4j2 found `src/test/resources/log4j2.xml` and the process can write to the project `logs/` directory.
