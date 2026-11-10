package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.BasePage;
import io.qameta.allure.Step;

/** Page Object for an individual SauceDemo product details page. */
public class ProductDetailsPage extends BasePage {

    private static final By PRODUCT_NAME = By.cssSelector(".inventory_details_name");
    private static final By PRODUCT_DESCRIPTION = By.cssSelector(".inventory_details_desc");
    private static final By PRODUCT_PRICE = By.cssSelector(".inventory_details_price");
    private static final By PRODUCT_IMAGE = By.cssSelector(".inventory_details_img");
    private static final By PRODUCT_BUTTON = By.cssSelector("button.btn_inventory");
    private static final By BACK_TO_PRODUCTS_BUTTON = By.id("back-to-products");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Check that the product details page is displayed")
    public boolean isPageDisplayed() {
        return isDisplayed(PRODUCT_NAME)
                && isDisplayed(PRODUCT_DESCRIPTION)
                && isDisplayed(PRODUCT_PRICE);
    }

    @Step("Get product name from details page")
    public String getProductName() {
        return getText(PRODUCT_NAME);
    }

    @Step("Get product description from details page")
    public String getProductDescription() {
        return getText(PRODUCT_DESCRIPTION);
    }

    @Step("Get product price from details page")
    public String getProductPrice() {
        return getText(PRODUCT_PRICE);
    }

    @Step("Get product image alternative text")
    public String getProductImageAltText() {
        return getAttribute(PRODUCT_IMAGE, "alt");
    }

    @Step("Get product image source")
    public String getProductImageSource() {
        return getAttribute(PRODUCT_IMAGE, "src");
    }

    @Step("Add product to cart from details page")
    public void addToCart() {
        click(PRODUCT_BUTTON);
    }

    @Step("Remove product from cart from details page")
    public void removeFromCart() {
        click(PRODUCT_BUTTON);
    }

    @Step("Get product cart button label")
    public String getCartButtonLabel() {
        return getText(PRODUCT_BUTTON);
    }

    @Step("Navigate back to Products inventory")
    public InventoryPage backToProducts() {
        click(BACK_TO_PRODUCTS_BUTTON);
        return new InventoryPage(driver);
    }
}
