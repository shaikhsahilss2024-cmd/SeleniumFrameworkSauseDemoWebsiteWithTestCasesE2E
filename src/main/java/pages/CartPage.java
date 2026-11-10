package pages;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import base.BasePage;
import io.qameta.allure.Step;

/** Page Object for SauceDemo's shopping cart page. */
public class CartPage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector(".title");
    private static final By CART_ITEMS = By.cssSelector(".cart_item");
    private static final By ITEM_NAME = By.cssSelector(".inventory_item_name");
    private static final By ITEM_DESCRIPTION = By.cssSelector(".inventory_item_desc");
    private static final By ITEM_QUANTITY = By.cssSelector(".cart_quantity");
    private static final By ITEM_PRICE = By.cssSelector(".inventory_item_price");
    private static final By REMOVE_BUTTON = By.cssSelector("button.cart_button");
    private static final By CONTINUE_SHOPPING_BUTTON = By.id("continue-shopping");
    private static final By CHECKOUT_BUTTON = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @Step("Check that the cart page is displayed")
    public boolean isPageDisplayed() {
        return isDisplayed(PAGE_TITLE)
                && "Your Cart".equalsIgnoreCase(getText(PAGE_TITLE));
    }

    @Step("Get cart page heading")
    public String getPageHeading() {
        return getText(PAGE_TITLE);
    }

    @Step("Get number of products in cart")
    public int getCartItemCount() {
        waitForVisibility(PAGE_TITLE);
        return driver.findElements(CART_ITEMS).size();
    }

    @Step("Get names of products in cart")
    public List<String> getCartItemNames() {
        waitForVisibility(PAGE_TITLE);
        return driver.findElements(CART_ITEMS).stream()
                .map(item -> item.findElement(ITEM_NAME).getText().trim())
                .collect(Collectors.toList());
    }

    @Step("Get quantity for cart product: {0}")
    public String getItemQuantity(String productName) {
        return getCartItem(productName).findElement(ITEM_QUANTITY).getText().trim();
    }

    @Step("Get price for cart product: {0}")
    public String getItemPrice(String productName) {
        return getCartItem(productName).findElement(ITEM_PRICE).getText().trim();
    }

    @Step("Get description for cart product: {0}")
    public String getItemDescription(String productName) {
        return getCartItem(productName).findElement(ITEM_DESCRIPTION).getText().trim();
    }

    @Step("Remove product from cart: {0}")
    public void removeProduct(String productName) {
        WebElement removeButton = getCartItem(productName).findElement(REMOVE_BUTTON);
        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
    }

    @Step("Continue shopping and return to inventory")
    public InventoryPage continueShopping() {
        click(CONTINUE_SHOPPING_BUTTON);
        return new InventoryPage(driver);
    }

    @Step("Proceed from cart to checkout information")
    public CheckoutPage proceedToCheckout() {
        click(CHECKOUT_BUTTON);
        return new CheckoutPage(driver);
    }

    private WebElement getCartItem(String productName) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name must not be null or blank.");
        }

        waitForVisibility(PAGE_TITLE);
        for (WebElement item : driver.findElements(CART_ITEMS)) {
            String displayedName = item.findElement(ITEM_NAME).getText().trim();
            if (displayedName.equals(productName.trim())) {
                return item;
            }
        }

        throw new NoSuchElementException(
                "Product was not found in the cart: " + productName);
    }
}
