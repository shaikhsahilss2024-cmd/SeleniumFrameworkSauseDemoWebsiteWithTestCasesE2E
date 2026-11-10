package tests;

import java.math.BigDecimal;
import java.util.List;

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
import pages.InventoryPage;
import pages.LoginPage;
import pages.ProductDetailsPage;
import utilities.ConfigReader;

@Listeners(TestListener.class)
@Epic("SauceDemo E-Commerce Application")
@Feature("Inventory")
public class InventoryTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(InventoryTest.class);
    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BIKE_LIGHT = "Sauce Labs Bike Light";
    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void openInventory() {
        logger.info("Preparing Inventory test. URL: {}", driver.getCurrentUrl());
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginFormDisplayed());
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());
        inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isPageDisplayed());
    }

    @Test(priority = 1, description = "SD-INV-001: Inventory page loads")
    @Story("Inventory page navigation")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify Products heading, browser title and inventory URL.")
    public void testInventoryPageLoads() {
        Assert.assertEquals(inventoryPage.getPageHeading(), "Products");
        Assert.assertEquals(inventoryPage.getPageTitle(), "Swag Labs");
        Assert.assertTrue(inventoryPage.getCurrentUrl().endsWith("/inventory.html"));
        logger.info("Inventory page load passed.");
    }

    @Test(priority = 2, description = "SD-INV-002: Six products and prices")
    @Story("Inventory catalogue")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the six standard-user products and their displayed prices.")
    public void testSixProductsAreDisplayed() {
        Assert.assertEquals(inventoryPage.getProductCount(), 6);
        Assert.assertEquals(inventoryPage.getProductPrice(BACKPACK), "$29.99");
        Assert.assertEquals(inventoryPage.getProductPrice(BIKE_LIGHT), "$9.99");
        logger.info("Inventory product count and prices passed.");
    }

    @Test(priority = 3, description = "SD-INV-003: Default alphabetical sort")
    @Story("Inventory sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify the default inventory sort is Name A to Z.")
    public void testDefaultSortIsNameAscending() {
        Assert.assertEquals(inventoryPage.getSelectedSortOptionValue(), "az");
        List<String> names = inventoryPage.getProductNames();
        List<String> sorted = names.stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();
        Assert.assertEquals(names, sorted);
    }

    @Test(priority = 4, description = "SD-INV-004: Sort by name descending")
    @Story("Inventory sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Name Z to A sorting.")
    public void testSortNameDescending() {
        inventoryPage.sortProducts("za");
        List<String> names = inventoryPage.getProductNames();
        List<String> sorted = names.stream()
                .sorted(String.CASE_INSENSITIVE_ORDER.reversed()).toList();
        Assert.assertEquals(names, sorted);
    }

    @Test(priority = 5, description = "SD-INV-005: Sort price low to high")
    @Story("Inventory sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify price ascending sorting.")
    public void testSortPriceAscending() {
        inventoryPage.sortProducts("lohi");
        List<BigDecimal> prices = inventoryPage.getProductPrices().stream()
                .map(p -> new BigDecimal(p.replace("$", ""))).toList();
        for (int i = 1; i < prices.size(); i++) {
            Assert.assertTrue(prices.get(i - 1).compareTo(prices.get(i)) <= 0);
        }
    }

    @Test(priority = 6, description = "SD-INV-006: Add one product")
    @Story("Inventory cart actions")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding a product updates its button and cart badge.")
    public void testAddOneProductToCart() {

        inventoryPage.addProductToCart(BACKPACK);

        Assert.assertEquals(
                inventoryPage.getCartBadgeCount(), 1,
                "Cart badge should display 1."
        );

        Assert.assertEquals(
                inventoryPage.getProductCartButtonLabel(BACKPACK),
                "Remove",
                "Product button should change to Remove."
        );
    }

    @Test(priority = 7, description = "SD-INV-007: Add multiple products")
    @Story("Inventory cart actions")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding two products updates cart badge count.")
    public void testAddMultipleProductsToCart() {
        inventoryPage.addProductToCart(BACKPACK);
        inventoryPage.addProductToCart(BIKE_LIGHT);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2);
    }

    @Test(priority = 8, description = "SD-INV-008: Remove product")
    @Story("Inventory cart actions")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify removing an added product resets its button and badge.")
    public void testRemoveProductFromInventory() {
        inventoryPage.addProductToCart(BACKPACK);
        inventoryPage.removeProductFromCart(BACKPACK);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 0);
        Assert.assertEquals(inventoryPage.getProductCartButtonLabel(BACKPACK), "Add to cart");
    }

    @Test(priority = 9, description = "SD-INV-009: Open product details")
    @Story("Inventory navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify selecting product name opens matching details.")
    public void testOpenProductDetails() {
        ProductDetailsPage details = inventoryPage.openProductDetails(BACKPACK);
        Assert.assertTrue(details.isPageDisplayed());
        Assert.assertEquals(details.getProductName(), BACKPACK);
    }

    @Test(priority = 10, description = "SD-INV-010: Open and close menu")
    @Story("Inventory navigation menu")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify hamburger menu opens and closes.")
    public void testOpenAndCloseMenu() throws InterruptedException {

        Thread.sleep(3000);
        inventoryPage.openMenu();
        Assert.assertTrue(inventoryPage.isMenuOpen());
        Assert.assertEquals(inventoryPage.getNavigationMenuItems(),
                List.of("All Items", "About", "Logout", "Reset App State"));
        Thread.sleep(3000);
        inventoryPage.closeMenu();
        Assert.assertFalse(inventoryPage.isMenuOpen());
    }

    @Test(priority = 11, description = "SD-INV-011: Logout")
    @Story("Inventory session management")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify logout returns to login form.")
    public void testLogout() {
        LoginPage loginPage = inventoryPage.logout();
        Assert.assertTrue(loginPage.isLoginFormDisplayed());
    }
}
