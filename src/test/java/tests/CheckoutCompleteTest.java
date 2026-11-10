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

/** TestNG tests for the SauceDemo Checkout Complete page. */
@Listeners(TestListener.class)
@Epic("SauceDemo E-Commerce Application")
@Feature("Checkout Complete")
public class CheckoutCompleteTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(CheckoutCompleteTest.class);

    @BeforeMethod(alwaysRun = true)
    public void logTestSetup() {
        logger.info("Starting Checkout Complete test. Browser session URL: {}", driver.getCurrentUrl());
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


    @Test(priority = 1, description = "SD-COMP-001: Verify successful order confirmation")
    @Story("Order completion confirmation")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify finishing checkout displays the order-complete confirmation.")
    public void testOrderCompletionConfirmation() {
        CheckoutCompletePage complete = completeOneProductOrder();

        Assert.assertTrue(complete.isPageDisplayed());
        Assert.assertEquals(complete.getPageHeading(), "Checkout: Complete!");
        Assert.assertEquals(complete.getConfirmationHeader(), "Thank you for your order!");
        Assert.assertFalse(complete.getConfirmationMessage().isBlank());
        logger.info("Order confirmation content verified.");
    }

    @Test(priority = 2, description = "SD-COMP-002: Verify confirmation image")
    @Story("Order completion UI")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify the confirmation illustration is displayed on the completion page.")
    public void testConfirmationImageDisplayed() {
        CheckoutCompletePage complete = completeOneProductOrder();

        Assert.assertTrue(complete.isConfirmationImageDisplayed());
        logger.info("Confirmation image is displayed.");
    }

    @Test(priority = 3, description = "SD-COMP-003: Back Home navigation")
    @Story("Order completion navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Back Home returns to the Products inventory after checkout.")
    public void testBackHomeReturnsToInventory() {
        CheckoutCompletePage complete = completeOneProductOrder();
        InventoryPage inventory = complete.backHome();

        Assert.assertTrue(inventory.isPageDisplayed());
        Assert.assertTrue(inventory.getCurrentUrl().endsWith("/inventory.html"));
        logger.info("Back Home navigation verified.");
    }

    private CheckoutCompletePage completeOneProductOrder() {
        CheckoutPage checkout = openCartWithBackpack().proceedToCheckout();
        checkout.fillCheckoutInformation("Sahil", "Tester", "411001");
        checkout.clickContinue();

        CheckoutOverviewPage overview = new CheckoutOverviewPage(driver);
        Assert.assertTrue(overview.isPageDisplayed());
        logger.info("Finishing demo checkout order.");
        return overview.finishOrder();
    }

}
