package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.BasePage;
import io.qameta.allure.Step;

/** Page Object for SauceDemo's completed checkout confirmation page. */
public class CheckoutCompletePage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector(".title");
    private static final By CONFIRMATION_HEADER = By.cssSelector(".complete-header");
    private static final By CONFIRMATION_MESSAGE = By.cssSelector(".complete-text");
    private static final By CONFIRMATION_IMAGE = By.cssSelector(".pony_express");
    private static final By BACK_HOME_BUTTON = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    @Step("Check that checkout complete page is displayed")
    public boolean isPageDisplayed() {
        return isDisplayed(PAGE_TITLE)
                && "Checkout: Complete!".equalsIgnoreCase(getText(PAGE_TITLE))
                && isDisplayed(CONFIRMATION_HEADER);
    }

    @Step("Get checkout complete page heading")
    public String getPageHeading() {
        return getText(PAGE_TITLE);
    }

    @Step("Get order confirmation heading")
    public String getConfirmationHeader() {
        return getText(CONFIRMATION_HEADER);
    }

    @Step("Get order confirmation message")
    public String getConfirmationMessage() {
        return getText(CONFIRMATION_MESSAGE);
    }

    @Step("Check whether the order confirmation image is displayed")
    public boolean isConfirmationImageDisplayed() {
        return isDisplayed(CONFIRMATION_IMAGE);
    }

    @Step("Return to Products inventory from checkout confirmation")
    public InventoryPage backHome() {
        click(BACK_HOME_BUTTON);
        return new InventoryPage(driver);
    }
}
