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

/** TestNG tests for the SauceDemo Cart page. */
@Listeners(TestListener.class)
@Epic("SauceDemo E-Commerce Application")
@Feature("Cart")
public class CartTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(CartTest.class);

    @BeforeMethod(alwaysRun = true)
    public void logTestSetup() {
        logger.info("Starting Cart test. Browser session URL: {}", driver.getCurrentUrl());
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


    @Test(priority = 1, description = "SD-CART-001: Verify cart page and selected product")
    @Story("Cart contents")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify cart heading, product name, quantity and price.")
    public void testCartDisplaysSelectedProduct() {
        CartPage cart = openCartWithBackpack();

        Assert.assertTrue(cart.isPageDisplayed());
        Assert.assertEquals(cart.getPageHeading(), "Your Cart");
        Assert.assertEquals(cart.getCartItemCount(), 1);
        Assert.assertTrue(cart.getCartItemNames().contains("Sauce Labs Backpack"));
        Assert.assertEquals(cart.getItemQuantity("Sauce Labs Backpack"), "1");
        Assert.assertEquals(cart.getItemPrice("Sauce Labs Backpack"), "$29.99");
        logger.info("Cart contents verified.");
    }

    @Test(priority = 2, description = "SD-CART-002: Remove product from cart")
    @Story("Cart item management")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify removing a product removes it from the cart.")
    public void testRemoveProductFromCart() {
        CartPage cart = openCartWithBackpack();
        cart.removeProduct("Sauce Labs Backpack");

        Assert.assertEquals(cart.getCartItemCount(), 0);
        Assert.assertFalse(cart.getCartItemNames().contains("Sauce Labs Backpack"));
        logger.info("Product removed from cart successfully.");
    }

    @Test(priority = 3, description = "SD-CART-003: Continue shopping")
    @Story("Cart navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Continue Shopping returns to inventory and retains cart state.")
    public void testContinueShoppingNavigation() {
        CartPage cart = openCartWithBackpack();
        InventoryPage inventory = cart.continueShopping();

        Assert.assertTrue(inventory.isPageDisplayed());
        Assert.assertTrue(inventory.getCurrentUrl().endsWith("/inventory.html"));
        Assert.assertEquals(inventory.getCartBadgeCount(), 1);
        logger.info("Continue Shopping navigation verified.");
    }

    @Test(priority = 4, description = "SD-CART-004: Proceed to checkout")
    @Story("Cart to checkout navigation")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify Checkout opens the checkout information page.")
    public void testProceedToCheckout() {
        CheckoutPage checkout = openCartWithBackpack().proceedToCheckout();

        Assert.assertTrue(checkout.isPageDisplayed());
        Assert.assertEquals(checkout.getPageHeading(), "Checkout: Your Information");
        logger.info("Checkout information page opened from cart.");
    }

}
