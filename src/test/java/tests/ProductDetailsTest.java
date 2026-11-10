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

/** TestNG tests for the SauceDemo Product Details page. */
@Listeners(TestListener.class)
@Epic("SauceDemo E-Commerce Application")
@Feature("Product Details")
public class ProductDetailsTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(ProductDetailsTest.class);

    @BeforeMethod(alwaysRun = true)
    public void logTestSetup() {
        logger.info("Starting Product Details test. Browser session URL: {}", driver.getCurrentUrl());
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


    @Test(priority = 1, description = "SD-PDP-001: Open product details from inventory")
    @Story("Product details navigation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify selecting a product name opens its details page.")
    public void testOpenProductDetails() {
        InventoryPage inventory = loginToInventory();
        ProductDetailsPage details = inventory.openProductDetails("Sauce Labs Backpack");

        logger.info("Asserting product details page and product name.");
        Assert.assertTrue(details.isPageDisplayed());
        Assert.assertEquals(details.getProductName(), "Sauce Labs Backpack");
    }

    @Test(priority = 2, description = "SD-PDP-002: Verify product details content")
    @Story("Product details content")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify the selected product has a description, price and image alternative text.")
    public void testProductDetailsContentIsDisplayed() {
        ProductDetailsPage details = loginToInventory()
                .openProductDetails("Sauce Labs Backpack");

        Assert.assertTrue(details.isPageDisplayed());
        Assert.assertFalse(details.getProductDescription().isBlank());
        Assert.assertEquals(details.getProductPrice(), "$29.99");
        Assert.assertFalse(details.getProductImageAltText().isBlank());
        Assert.assertFalse(details.getProductImageSource().isBlank());
        logger.info("Product details content assertions passed.");
    }

    @Test(priority = 3, description = "SD-PDP-003: Add product from details page")
    @Story("Product details cart action")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Add to cart on product details changes to Remove and updates cart badge.")
    public void testAddProductToCartFromDetails() {
        ProductDetailsPage details = loginToInventory()
                .openProductDetails("Sauce Labs Backpack");

        details.addToCart();
        Assert.assertEquals(details.getCartButtonLabel(), "Remove");
        Assert.assertEquals(new InventoryPage(driver).getCartBadgeCount(), 1);

        details.removeFromCart();
        Assert.assertEquals(details.getCartButtonLabel(), "Add to cart");
        logger.info("Product details cart action assertions passed.");
    }

    @Test(priority = 4, description = "SD-PDP-004: Return to inventory")
    @Story("Product details navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Back to Products returns to the inventory page.")
    public void testBackToProductsNavigation() {
        ProductDetailsPage details = loginToInventory()
                .openProductDetails("Sauce Labs Backpack");

        InventoryPage inventory = details.backToProducts();
        Assert.assertTrue(inventory.isPageDisplayed());
        Assert.assertTrue(inventory.getCurrentUrl().endsWith("/inventory.html"));
        logger.info("Returned to inventory successfully.");
    }

}
