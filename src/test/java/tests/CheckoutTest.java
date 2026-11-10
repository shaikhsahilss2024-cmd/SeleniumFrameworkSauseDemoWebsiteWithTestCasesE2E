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
import pages.*;
import utilities.ConfigReader;

/** TestNG tests for the SauceDemo Checkout Information page. */
@Listeners(TestListener.class)
@Epic("SauceDemo E-Commerce Application")
@Feature("Checkout Information")
public class CheckoutTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(CheckoutTest.class);

    @BeforeMethod(alwaysRun = true)
    public void logTestSetup() {
        logger.info("Starting Checkout Information test. Browser session URL: {}", driver.getCurrentUrl());
    }

    private InventoryPage loginToInventory() {
        logger.info("Logging in with configured standard-user credentials.");
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed.");
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());
        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isPageDisplayed(), "Products inventory should be displayed.");
        return inventoryPage;
    }

    private CartPage openCartWithBackpack() {
        InventoryPage inventoryPage = loginToInventory();
        logger.info("Adding Sauce Labs Backpack to cart.");
        inventoryPage.addProductToCart("Sauce Labs Backpack");
        return inventoryPage.openCart();
    }


    @Test(priority = 1, description = "SD-CHK-001: Verify checkout information page")
    @Story("Checkout information UI")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify checkout information heading and all three required fields.")
    public void testCheckoutInformationPageDisplayed() {
        CheckoutPage checkout = openCartWithBackpack().proceedToCheckout();

        Assert.assertTrue(checkout.isPageDisplayed());
        Assert.assertEquals(checkout.getPageHeading(), "Checkout: Your Information");
        logger.info("Checkout information page verified.");
    }

    @Test(priority = 2, description = "SD-CHK-002: Continue with valid customer information")
    @Story("Checkout information submission")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify valid first name, last name and postal code advance to order overview.")
    public void testContinueWithValidInformation() {
        CheckoutPage checkout = openCartWithBackpack().proceedToCheckout();
        checkout.fillCheckoutInformation("Sahil", "Tester", "411001");
        checkout.clickContinue();

        CheckoutOverviewPage overview = new CheckoutOverviewPage(driver);
        Assert.assertTrue(overview.isPageDisplayed());
        Assert.assertEquals(overview.getPageHeading(), "Checkout: Overview");
        logger.info("Valid checkout information advanced to overview.");
    }

    @Test(priority = 3, description = "SD-CHK-003: Submit with all fields empty")
    @Story("Checkout required-field validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify SauceDemo displays the first-name-required error when all fields are empty.")
    public void testCheckoutWithAllFieldsEmpty() {
        CheckoutPage checkout = openCartWithBackpack().proceedToCheckout();
        checkout.clickContinue();

        Assert.assertTrue(checkout.isErrorDisplayed());
        Assert.assertEquals(checkout.getErrorMessage(), "Error: First Name is required");
        logger.info("Empty checkout form validation verified.");
    }

    @Test(priority = 4, description = "SD-CHK-004: Submit without last name")
    @Story("Checkout required-field validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the last-name-required validation when first name and postal code are supplied.")
    public void testCheckoutWithoutLastName() {
        CheckoutPage checkout = openCartWithBackpack().proceedToCheckout();
        checkout.fillCheckoutInformation("Sahil", "", "411001");
        checkout.clickContinue();

        Assert.assertTrue(checkout.isErrorDisplayed());
        Assert.assertEquals(checkout.getErrorMessage(), "Error: Last Name is required");
        logger.info("Missing last name validation verified.");
    }

    @Test(priority = 5, description = "SD-CHK-005: Submit without postal code")
    @Story("Checkout required-field validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the postal-code-required validation when name fields are supplied.")
    public void testCheckoutWithoutPostalCode() {
        CheckoutPage checkout = openCartWithBackpack().proceedToCheckout();
        checkout.fillCheckoutInformation("Sahil", "Tester", "");
        checkout.clickContinue();

        Assert.assertTrue(checkout.isErrorDisplayed());
        Assert.assertEquals(checkout.getErrorMessage(), "Error: Postal Code is required");
        logger.info("Missing postal code validation verified.");
    }

    @Test(priority = 6, description = "SD-CHK-006: Cancel checkout")
    @Story("Checkout navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Cancel returns the user to the cart.")
    public void testCancelCheckoutReturnsToCart() {
        CartPage cart = openCartWithBackpack().proceedToCheckout().cancelCheckout();

        Assert.assertTrue(cart.isPageDisplayed());
        Assert.assertEquals(cart.getPageHeading(), "Your Cart");
        logger.info("Checkout cancellation navigation verified.");
    }

    @Test(priority = 7, description = "SD-CHK-007: Dismiss checkout validation error")
    @Story("Checkout validation interaction")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify the checkout validation message can be dismissed.")
    public void testDismissCheckoutError() {
        CheckoutPage checkout = openCartWithBackpack().proceedToCheckout();
        checkout.clickContinue();
        Assert.assertTrue(checkout.isErrorDisplayed());

        checkout.dismissErrorMessage();
        Assert.assertFalse(checkout.isErrorDisplayed());
        logger.info("Checkout error dismissal verified.");
    }

}
