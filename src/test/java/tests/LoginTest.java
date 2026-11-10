package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import listeners.TestListener;
import pages.LoginPage;
import utilities.ConfigReader;

@Listeners(TestListener.class)
@Epic("SauceDemo E-Commerce Application")
@Feature("Login")
public class LoginTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(LoginTest.class);
    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void initializeLoginPage() {
        logger.info("Starting Login test. URL: {}", driver.getCurrentUrl());
        loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be visible.");
    }

    @Test(priority = 1, description = "SD-LOG-001: Login with valid credentials")
    @Story("Successful authentication")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify standard_user can log in and reach inventory.")
    public void testLoginWithValidCredentials() {
        logger.info("Entering configured valid credentials.");
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());
        Assert.assertTrue(loginPage.getCurrentUrl().endsWith("/inventory.html"));
        Assert.assertEquals(loginPage.getPageTitle(), "Swag Labs");
        logger.info("Valid login passed.");
    }

    @Test(priority = 2, description = "SD-LOG-002: Incorrect password")
    @Story("Invalid credential validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify an incorrect password is rejected.")
    public void testLoginWithIncorrectPassword() {
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getInvalidPassword());
        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Username and password do not match any user in this service");
        logger.info("Incorrect password validation passed.");
    }

    @Test(priority = 3, description = "SD-LOG-003: Unknown username")
    @Story("Invalid credential validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify an unknown username is rejected.")
    public void testLoginWithInvalidUsername() {
        loginPage.login(ConfigReader.getInvalidUsername(), ConfigReader.getValidPassword());
        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Username and password do not match any user in this service");
        logger.info("Invalid username validation passed.");
    }

    @Test(priority = 4, description = "SD-LOG-004: Both fields empty")
    @Story("Required field validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify username-required validation when both fields are empty.")
    public void testLoginWithBothFieldsEmpty() {
        loginPage.clickLogin();
        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username is required");
    }

    @Test(priority = 5, description = "SD-LOG-005: Empty username")
    @Story("Required field validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify username-required validation.")
    public void testLoginWithEmptyUsername() {
        loginPage.enterPassword(ConfigReader.getValidPassword());
        loginPage.clickLogin();
        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username is required");
    }

    @Test(priority = 6, description = "SD-LOG-006: Empty password")
    @Story("Required field validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify password-required validation.")
    public void testLoginWithEmptyPassword() {
        loginPage.enterUsername(ConfigReader.getValidUsername());
        loginPage.clickLogin();
        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Password is required");
    }

    @Test(priority = 7, description = "SD-LOG-007: Locked-out user")
    @Story("Locked account handling")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify SauceDemo rejects locked_out_user.")
    public void testLoginWithLockedOutUser() {
        loginPage.login(ConfigReader.getLockedUsername(), ConfigReader.getValidPassword());
        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Sorry, this user has been locked out.");
    }

    @Test(priority = 8, description = "SD-LOG-008: Password masking")
    @Story("Login page UI")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify password field is configured as a password input.")
    public void testPasswordFieldIsMasked() {
        loginPage.enterPassword(ConfigReader.getValidPassword());
        Assert.assertEquals(loginPage.getPasswordInputType(), "password");
    }
}
