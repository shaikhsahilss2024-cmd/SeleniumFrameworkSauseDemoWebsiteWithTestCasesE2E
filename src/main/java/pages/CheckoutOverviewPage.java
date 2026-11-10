package pages;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.BasePage;
import io.qameta.allure.Step;

/** Page Object for checkout step two: order overview. */
public class CheckoutOverviewPage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector(".title");
    private static final By CART_ITEMS = By.cssSelector(".cart_item");
    private static final By ITEM_NAME = By.cssSelector(".inventory_item_name");
    private static final By ITEM_PRICE = By.cssSelector(".inventory_item_price");
    private static final By ITEM_QUANTITY = By.cssSelector(".cart_quantity");
    private static final By SUMMARY_VALUES = By.cssSelector(".summary_value_label");
    private static final By SUBTOTAL_LABEL = By.cssSelector(".summary_subtotal_label");
    private static final By TAX_LABEL = By.cssSelector(".summary_tax_label");
    private static final By TOTAL_LABEL = By.cssSelector(".summary_total_label");
    private static final By CANCEL_BUTTON = By.id("cancel");
    private static final By FINISH_BUTTON = By.id("finish");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Check that checkout overview page is displayed")
    public boolean isPageDisplayed() {
        return isDisplayed(PAGE_TITLE)
                && "Checkout: Overview".equalsIgnoreCase(getText(PAGE_TITLE));
    }

    @Step("Get checkout overview page heading")
    public String getPageHeading() {
        return getText(PAGE_TITLE);
    }

    @Step("Get number of products on checkout overview")
    public int getItemCount() {
        waitForVisibility(PAGE_TITLE);
        return driver.findElements(CART_ITEMS).size();
    }

    @Step("Get product names from checkout overview")
    public List<String> getItemNames() {
        waitForVisibility(PAGE_TITLE);
        return driver.findElements(CART_ITEMS).stream()
                .map(item -> item.findElement(ITEM_NAME).getText().trim())
                .collect(Collectors.toList());
    }

    @Step("Get quantity for overview product: {0}")
    public String getItemQuantity(String productName) {
        return getOverviewItem(productName).findElement(ITEM_QUANTITY).getText().trim();
    }

    @Step("Get price for overview product: {0}")
    public String getItemPrice(String productName) {
        return getOverviewItem(productName).findElement(ITEM_PRICE).getText().trim();
    }

    @Step("Get payment information")
    public String getPaymentInformation() {
        List<WebElement> values = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions
                        .visibilityOfAllElementsLocatedBy(SUMMARY_VALUES));
        if (values.isEmpty()) {
            throw new IllegalStateException("Payment information was not displayed.");
        }
        return values.get(0).getText().trim();
    }

    @Step("Get shipping information")
    public String getShippingInformation() {
        List<WebElement> values = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions
                        .visibilityOfAllElementsLocatedBy(SUMMARY_VALUES));
        if (values.size() < 2) {
            throw new IllegalStateException("Shipping information was not displayed.");
        }
        return values.get(1).getText().trim();
    }

    @Step("Get displayed item subtotal text")
    public String getSubtotalText() {
        return getText(SUBTOTAL_LABEL);
    }

    @Step("Get displayed tax text")
    public String getTaxText() {
        return getText(TAX_LABEL);
    }

    @Step("Get displayed final total text")
    public String getTotalText() {
        return getText(TOTAL_LABEL);
    }

    @Step("Get numeric item subtotal")
    public BigDecimal getSubtotalAmount() {
        return extractAmount(getSubtotalText());
    }

    @Step("Get numeric tax amount")
    public BigDecimal getTaxAmount() {
        return extractAmount(getTaxText());
    }

    @Step("Get numeric final total")
    public BigDecimal getTotalAmount() {
        return extractAmount(getTotalText());
    }

    @Step("Cancel order review and return to Products inventory")
    public InventoryPage cancelOrder() {
        click(CANCEL_BUTTON);
        return new InventoryPage(driver);
    }

    @Step("Finish checkout and place the demo order")
    public CheckoutCompletePage finishOrder() {
        click(FINISH_BUTTON);
        return new CheckoutCompletePage(driver);
    }

    private WebElement getOverviewItem(String productName) {
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

        throw new java.util.NoSuchElementException(
                "Product was not found on checkout overview: " + productName);
    }

    private BigDecimal extractAmount(String labelText) {
        int currencySymbolIndex = labelText.lastIndexOf('$');
        if (currencySymbolIndex < 0 || currencySymbolIndex == labelText.length() - 1) {
            throw new IllegalStateException(
                    "Could not extract a dollar amount from checkout summary text: "
                            + labelText);
        }

        try {
            return new BigDecimal(labelText.substring(currencySymbolIndex + 1).trim());
        } catch (NumberFormatException exception) {
            throw new IllegalStateException(
                    "Invalid amount displayed in checkout summary: " + labelText,
                    exception);
        }
    }
}
