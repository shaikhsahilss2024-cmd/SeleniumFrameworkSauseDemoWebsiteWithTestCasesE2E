package listeners;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import base.BaseTest;
import io.qameta.allure.Allure;
import utilities.ScreenshotUtils;

/** Logs TestNG outcomes and attaches a failure screenshot before @AfterMethod teardown. */
public class TestListener implements ITestListener, IInvokedMethodListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("TestNG context started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("TestNG context finished: {}", context.getName());
        logger.info("Passed: {}, Failed: {}, Skipped: {}",
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test execution started: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test execution passed: {}", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test execution failed: {}", result.getName(), result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test execution skipped: {}", result.getName());
    }

    /**
     * TestNG invokes this immediately after the test method. The @AfterMethod teardown
     * configuration runs afterwards, so the browser is still available for capture.
     */
    @Override
    public void afterInvocation(IInvokedMethod invokedMethod, ITestResult result) {
        if (!invokedMethod.isTestMethod() || result.getStatus() != ITestResult.FAILURE) {
            return;
        }

        Object testInstance = result.getInstance();
        if (!(testInstance instanceof BaseTest baseTest)) {
            logger.warn("Cannot capture screenshot: test class does not extend BaseTest.");
            return;
        }

        WebDriver activeDriver = baseTest.getDriver();
        if (activeDriver == null) {
            logger.warn("Cannot capture screenshot: WebDriver is already unavailable.");
            return;
        }

        try {
            Path screenshotPath = ScreenshotUtils.captureScreenshot(activeDriver, result.getName());
            try (InputStream screenshotStream = Files.newInputStream(screenshotPath)) {
                Allure.addAttachment(
                        "Failure screenshot",
                        "image/png",
                        screenshotStream,
                        ".png");
            }
            logger.info("Failure screenshot saved and attached to Allure: {}", screenshotPath);
        } catch (IOException | RuntimeException exception) {
            logger.error("Unable to capture or attach failure screenshot for test: {}",
                    result.getName(), exception);
        }
    }
}
