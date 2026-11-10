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

/** TestNG tests for the SauceDemo Checkout Overview page. */
@Listeners(TestListener.class)
@Epic("SauceDemo E-Commerce Application")
@Feature("Checkout Overview")
public class CheckoutOverviewTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(CheckoutOverviewTest.class);

    @BeforeMethod(alwaysRun = true)
    public void logTestSetup() {
        logger.info("Starting Checkout Overview test. Browser session URL: {}", driver.getCurrentUrl());
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


    @Test(priority = 1, description = "SD-OVR-001: Verify overview page and cart item")
    @Story("Order overview contents")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify overview heading, selected item, quantity and price.")
    public void testOverviewDisplaysSelectedProduct() {
        CheckoutPage checkout = openCartWithBackpack().proceedToCheckout();
        CheckoutOverviewPage overview = continueToOverview(checkout);

        Assert.assertTrue(overview.isPageDisplayed());
        Assert.assertEquals(overview.getPageHeading(), "Checkout: Overview");
        Assert.assertEquals(overview.getItemCount(), 1);
        Assert.assertTrue(overview.getItemNames().contains("Sauce Labs Backpack"));
        Assert.assertEquals(overview.getItemQuantity("Sauce Labs Backpack"), "1");
        Assert.assertEquals(overview.getItemPrice("Sauce Labs Backpack"), "$29.99");
        logger.info("Overview product details verified.");
    }

    @Test(priority = 2, description = "SD-OVR-002: Verify payment and shipping information")
    @Story("Order overview summary")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify payment and shipping summary fields are populated.")
    public void testPaymentAndShippingInformationDisplayed() {
        CheckoutPage checkout = openCartWithBackpack().proceedToCheckout();
        CheckoutOverviewPage overview = continueToOverview(checkout);

        Assert.assertFalse(overview.getPaymentInformation().isBlank());
        Assert.assertFalse(overview.getShippingInformation().isBlank());
        logger.info("Payment and shipping summary verified.");
    }

    @Test(priority = 3, description = "SD-OVR-003: Verify subtotal, tax and total calculation")
    @Story("Order price calculation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify displayed total equals item subtotal plus tax.")
    public void testSubtotalTaxAndTotalCalculation() {
        CheckoutPage checkout = openCartWithBackpack().proceedToCheckout();
        CheckoutOverviewPage overview = continueToOverview(checkout);

        Assert.assertEquals(overview.getSubtotalAmount().toPlainString(), "29.99");
        Assert.assertEquals(overview.getTotalAmount(),
                overview.getSubtotalAmount().add(overview.getTaxAmount()));
        logger.info("Subtotal, tax and total calculation verified.");
    }

    @Test(priority = 4, description = "SD-OVR-004: Cancel order review")
    @Story("Order overview navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Cancel returns from overview to inventory.")
    public void testCancelOrderReturnsToInventory() {
        CheckoutPage checkout = openCartWithBackpack().proceedToCheckout();
        InventoryPage inventory = continueToOverview(checkout).cancelOrder();

        Assert.assertTrue(inventory.isPageDisplayed());
        Assert.assertTrue(inventory.getCurrentUrl().endsWith("/inventory.html"));
        logger.info("Cancel order navigation verified.");
    }

    private CheckoutOverviewPage continueToOverview(CheckoutPage checkout) {
        checkout.fillCheckoutInformation("Sahil", "Tester", "411001");
        checkout.clickContinue();
        return new CheckoutOverviewPage(driver);
    }

}
