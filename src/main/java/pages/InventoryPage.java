
package pages;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import base.BasePage;
import io.qameta.allure.Step;

/**
 * Page Object for SauceDemo Inventory Page.
 */
public class InventoryPage extends BasePage {
    private static final Logger logger =
            LogManager.getLogger(InventoryPage.class);
    // ---------------- LOCATORS ----------------

    private static final By PAGE_TITLE =
            By.cssSelector("[data-test='title']");

    private static final By INVENTORY_LIST =
            By.cssSelector("[data-test='inventory-list']");

    private static final By PRODUCT_CARDS =
            By.cssSelector("[data-test='inventory-item']");

    private static final By PRODUCT_NAME =
            By.cssSelector("[data-test='inventory-item-name']");

    private static final By PRODUCT_DESCRIPTION =
            By.cssSelector("[data-test='inventory-item-desc']");

    private static final By PRODUCT_PRICE =
            By.cssSelector("[data-test='inventory-item-price']");

    private static final By PRODUCT_IMAGE_LINK =
            By.cssSelector(".inventory_item_img a");

    private static final By PRODUCT_BUTTON =
            By.cssSelector("#add-to-cart-sauce-labs-backpack");

    private static final By SORT_DROPDOWN =
            By.cssSelector("[data-test='product-sort-container']");

    private static final By CART_LINK =
            By.cssSelector("[data-test='shopping-cart-link']");

    private static final By CART_BADGE =
            By.cssSelector("[data-test='shopping-cart-badge']");

    // Hamburger menu locators

    private static final By MENU_BUTTON =
            By.cssSelector("#react-burger-menu-btn");

    private static final By MENU_CLOSE_BUTTON =
            By.cssSelector("#react-burger-cross-btn");

    private static final By ALL_ITEMS_LINK =
            By.id("inventory_sidebar_link");

    private static final By ABOUT_LINK =
            By.id("about_sidebar_link");

    private static final By LOGOUT_LINK =
            By.id("logout_sidebar_link");

    private static final By RESET_APP_STATE_LINK =
            By.id("reset_sidebar_link");

    private static final List<By> MENU_LINKS = List.of(
            ALL_ITEMS_LINK,
            ABOUT_LINK,
            LOGOUT_LINK,
            RESET_APP_STATE_LINK
    );

    // ---------------- CONSTRUCTOR ----------------

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    // ---------------- PAGE VALIDATION ----------------

    @Step("Verify Inventory Page is displayed")
    public boolean isPageDisplayed() {

        return isDisplayed(PAGE_TITLE)
                && "Products".equalsIgnoreCase(getText(PAGE_TITLE))
                && isDisplayed(INVENTORY_LIST);
    }

    @Step("Get Inventory Page heading")
    public String getPageHeading() {

        return getText(PAGE_TITLE);
    }

    @Step("Get total number of products")
    public int getProductCount() {

        waitForVisibility(INVENTORY_LIST);

        return driver.findElements(PRODUCT_CARDS).size();
    }

    // ---------------- PRODUCT DETAILS ----------------

    @Step("Get all product names")
    public List<String> getProductNames() {

        waitForVisibility(INVENTORY_LIST);

        return driver.findElements(PRODUCT_CARDS)
                .stream()
                .map(card -> card.findElement(PRODUCT_NAME)
                        .getText().trim())
                .collect(Collectors.toList());
    }

    @Step("Get product price for {0}")
    public String getProductPrice(String productName) {

        return getProductCard(productName)
                .findElement(PRODUCT_PRICE)
                .getText()
                .trim();
    }

    @Step("Get product description for {0}")
    public String getProductDescription(String productName) {

        return getProductCard(productName)
                .findElement(PRODUCT_DESCRIPTION)
                .getText()
                .trim();
    }

    // ---------------- CART BUTTON OPERATIONS ----------------


    @Step("Get cart button label for {0}")
    public String getProductCartButtonLabel(String productName) {

        // Find the product card using its product name.
        WebElement productCard = getProductCard(productName);

        // Each inventory product card has one action button.
        // Its ID changes between Add to cart and Remove.
        WebElement actionButton = productCard.findElement(
                By.cssSelector("button")
        );

        // Return the current button text.
        return actionButton.getText().trim();
    }


    @Step("Add product to cart: {0}")
    public void addProductToCart(String productName) {
        clickProductButton(productName, "Remove");
    }

    @Step("Remove product from cart: {0}")
    public void removeProductFromCart(String productName) {
        clickProductButton(productName, "Add to cart");
    }

    @Step("Get cart badge count")
    public int getCartBadgeCount() {

        List<WebElement> badges =
                driver.findElements(CART_BADGE);

        if (badges.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(
                badges.get(0).getText().trim()
        );
    }

    // ---------------- PRODUCT NAVIGATION ----------------

    @Step("Open product details for {0}")
    public ProductDetailsPage openProductDetails(String productName) {

        WebElement productLink =
                getProductCard(productName)
                        .findElement(PRODUCT_NAME);

        wait.until(
                ExpectedConditions.elementToBeClickable(productLink)
        ).click();

        return new ProductDetailsPage(driver);
    }

    @Step("Open product details using product image")
    public ProductDetailsPage openProductDetailsByImage(
            String productName) {

        WebElement productImageLink =
                getProductCard(productName)
                        .findElement(PRODUCT_IMAGE_LINK);

        wait.until(
                ExpectedConditions.elementToBeClickable(productImageLink)
        ).click();

        return new ProductDetailsPage(driver);
    }

    // ---------------- PRODUCT SORTING ----------------

    @Step("Sort products using option: {0}")
    public void sortProducts(String optionValue) {

        if (!List.of("az", "za", "lohi", "hilo")
                .contains(optionValue)) {

            throw new IllegalArgumentException(
                    "Unsupported sort option: " + optionValue
            );
        }

        new Select(waitForVisibility(SORT_DROPDOWN))
                .selectByValue(optionValue);
    }

    @Step("Get selected product sorting option")
    public String getSelectedSortOptionValue() {

        return new Select(
                waitForVisibility(SORT_DROPDOWN)
        ).getFirstSelectedOption()
                .getAttribute("value");
    }

    @Step("Get product prices in current display order")
    public List<String> getProductPrices() {

        waitForVisibility(INVENTORY_LIST);

        return driver.findElements(PRODUCT_CARDS)
                .stream()
                .map(card -> card.findElement(PRODUCT_PRICE)
                        .getText().trim())
                .collect(Collectors.toList());
    }

    // ---------------- CART NAVIGATION ----------------

    @Step("Open shopping cart")
    public CartPage openCart() {

        click(CART_LINK);

        return new CartPage(driver);
    }

    // ---------------- HAMBURGER MENU ----------------

    @Step("Open hamburger navigation menu")
    public void openMenu() {

        logger.info("Opening hamburger navigation menu.");

        if (!isMenuOpen()) {

            WebElement menuButton = wait.until(
                    ExpectedConditions.elementToBeClickable(MENU_BUTTON)
            );

            menuButton.click();

            logger.info("Hamburger menu button clicked.");
        }

        // Wait until the All Items link becomes visible.
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        ALL_ITEMS_LINK
                )
        );

        logger.info("Navigation menu opened successfully.");
    }

    @Step("Get navigation menu items")
    public List<String> getNavigationMenuItems() {

        waitForVisibility(ALL_ITEMS_LINK);

        List<String> menuItems = new ArrayList<>();

        for (By locator : MENU_LINKS) {

            menuItems.add(
                    waitForVisibility(locator)
                            .getText()
                            .trim()
            );
        }

        return menuItems;
    }

    @Step("Check whether navigation menu is open")
    public boolean isMenuOpen() {

        try {

            List<WebElement> menuLinks =
                    driver.findElements(ALL_ITEMS_LINK);

            return !menuLinks.isEmpty()
                    && menuLinks.get(0).isDisplayed();

        } catch (StaleElementReferenceException exception) {

            return false;
        }
    }

    @Step("Close hamburger navigation menu")
    public void closeMenu() {

        logger.info("Closing hamburger navigation menu.");

        if (isMenuOpen()) {

            WebElement closeButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            MENU_CLOSE_BUTTON
                    )
            );

            closeButton.click();

            wait.until(
                    ExpectedConditions.invisibilityOfElementLocated(
                            ALL_ITEMS_LINK
                    )
            );

            logger.info("Navigation menu closed successfully.");

        } else {

            logger.info("Navigation menu is already closed.");
        }
    }

    @Step("Navigate to All Items")
    public void navigateToAllItems() {

        openMenu();

        click(ALL_ITEMS_LINK);

        waitForVisibility(INVENTORY_LIST);
    }

    @Step("Navigate to About page")
    public void clickAbout() {

        openMenu();

        click(ABOUT_LINK);
    }

    @Step("Logout from SauceDemo")
    public LoginPage logout() {

        openMenu();

        click(LOGOUT_LINK);

        return new LoginPage(driver);
    }

    @Step("Reset application state")
    public void resetAppState() {

        openMenu();

        click(RESET_APP_STATE_LINK);

        waitUntilInvisible(CART_BADGE);
    }

    // ---------------- PRIVATE REUSABLE METHODS ----------------





    private void clickProductButton(
            String productName,
            String expectedButtonLabel) {

        By buttonLocator = getProductButtonLocator(productName);

        logger.info("Adding/removing product: {}", productName);

        // Find button and wait until clickable.
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(buttonLocator)
        );

        // Execute click.
        button.click();

        logger.info("Button clicked for product: {}", productName);

        try {

            // Directly locate the updated button.
            // Do not call getProductCard() inside this wait.
            wait.until(webDriver -> {

                List<WebElement> buttons =
                        webDriver.findElements(buttonLocator);

                return !buttons.isEmpty()
                        && buttons.get(0).getText()
                        .trim()
                        .equals(expectedButtonLabel);
            });

            logger.info(
                    "Product button successfully changed to: {}",
                    expectedButtonLabel
            );

        } catch (org.openqa.selenium.TimeoutException exception) {

            logger.error("Product button state did not update.");
            logger.error("Current URL: {}", driver.getCurrentUrl());
            logger.error("Browser title: {}", driver.getTitle());

            logger.error(
                    "Inventory list count: {}",
                    driver.findElements(INVENTORY_LIST).size()
            );

            logger.error(
                    "Inventory product card count: {}",
                    driver.findElements(PRODUCT_CARDS).size()
            );

            logger.error(
                    "Cart badge count: {}",
                    getCartBadgeCount()
            );

            throw exception;
        }
    }



    private By getProductButtonLocator(String productName) {

        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException(
                    "Product name cannot be empty."
            );
        }

        String productNameLiteral = toXPathLiteral(productName.trim());

        return By.xpath(
                "//div[@data-test='inventory-item']" +
                        "[.//*[@data-test='inventory-item-name' and " +
                        "normalize-space(.)=" + productNameLiteral + "]]" +
                        "//button"
        );
    }

    private String toXPathLiteral(String value) {

        if (!value.contains("'")) {
            return "'" + value + "'";
        }

        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }

        String[] parts = value.split("'", -1);

        return "concat('" +
                String.join("', \"'\", '", parts) +
                "')";
    }



    private WebElement getProductCard(String productName) {

        if (productName == null || productName.isBlank()) {

            throw new IllegalArgumentException(
                    "Product name must not be null or blank."
            );
        }

        waitForVisibility(INVENTORY_LIST);

        for (WebElement card :
                driver.findElements(PRODUCT_CARDS)) {

            String displayedName =
                    card.findElement(PRODUCT_NAME)
                            .getText().trim();

            if (displayedName.equals(productName.trim())) {

                return card;
            }
        }

        throw new NoSuchElementException(
                "Product was not found on inventory page: "
                        + productName
        );
    }
}
