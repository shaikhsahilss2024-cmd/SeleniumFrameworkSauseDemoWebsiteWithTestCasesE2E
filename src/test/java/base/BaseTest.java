package base;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import utilities.ConfigReader;
import utilities.DriverFactory;

/** Shared TestNG setup and teardown for UI tests. */
public abstract class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method testMethod) {
        logger.info("Starting test method: {}", testMethod.getName());
        logger.info("Launching browser: {}", ConfigReader.getBrowser());

        driver = DriverFactory.createDriver();
        logger.info("Browser launched successfully.");

        String applicationUrl = ConfigReader.getApplicationUrl();
        logger.info("Navigating to application: {}", applicationUrl);
        driver.get(applicationUrl);
        logger.info("Application page loaded. Current URL: {}", driver.getCurrentUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult testResult) {
        if (testResult.getStatus() == ITestResult.FAILURE) {
            logger.error("Test failed: {}", testResult.getName(), testResult.getThrowable());
        } else if (testResult.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test passed: {}", testResult.getName());
        } else if (testResult.getStatus() == ITestResult.SKIP) {
            logger.warn("Test skipped: {}", testResult.getName());
        }

        if (driver != null) {
            logger.info("Closing browser for test: {}", testResult.getName());
            DriverFactory.quitDriver();
            driver = null;
            logger.info("Browser closed.");
        }
    }

    /** Exposes the active driver to listeners for failure diagnostics. */
    public WebDriver getDriver() {
        return driver;
    }
}
