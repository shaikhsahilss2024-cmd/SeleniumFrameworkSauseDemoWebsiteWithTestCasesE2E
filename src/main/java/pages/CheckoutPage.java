package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.BasePage;
import io.qameta.allure.Step;

/** Page Object for checkout step one: customer information. */
public class CheckoutPage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector(".title");
    private static final By FIRST_NAME_FIELD = By.id("first-name");
    private static final By LAST_NAME_FIELD = By.id("last-name");
    private static final By POSTAL_CODE_FIELD = By.id("postal-code");
    private static final By CONTINUE_BUTTON = By.id("continue");
    private static final By CANCEL_BUTTON = By.id("cancel");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");
    private static final By ERROR_CLOSE_BUTTON = By.cssSelector("[data-test='error-button']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    @Step("Check that checkout information page is displayed")
    public boolean isPageDisplayed() {
        return isDisplayed(PAGE_TITLE)
                && "Checkout: Your Information".equalsIgnoreCase(getText(PAGE_TITLE))
                && isDisplayed(FIRST_NAME_FIELD)
                && isDisplayed(LAST_NAME_FIELD)
                && isDisplayed(POSTAL_CODE_FIELD);
    }

    @Step("Get checkout information page heading")
    public String getPageHeading() {
        return getText(PAGE_TITLE);
    }

    @Step("Enter first name")
    public void enterFirstName(String firstName) {
        type(FIRST_NAME_FIELD, firstName);
    }

    @Step("Enter last name")
    public void enterLastName(String lastName) {
        type(LAST_NAME_FIELD, lastName);
    }

    @Step("Enter postal code")
    public void enterPostalCode(String postalCode) {
        type(POSTAL_CODE_FIELD, postalCode);
    }

    @Step("Fill checkout information form")
    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
    }

    @Step("Click Continue on checkout information page")
    public void clickContinue() {
        click(CONTINUE_BUTTON);
    }

    @Step("Cancel checkout and return to cart")
    public CartPage cancelCheckout() {
        click(CANCEL_BUTTON);
        return new CartPage(driver);
    }

    @Step("Get checkout validation error message")
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    @Step("Check whether checkout validation error is displayed")
    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }

    @Step("Dismiss checkout validation error")
    public void dismissErrorMessage() {
        click(ERROR_CLOSE_BUTTON);
        waitUntilInvisible(ERROR_MESSAGE);
    }

    @Step("Read first name field value")
    public String getFirstNameValue() {
        return getAttribute(FIRST_NAME_FIELD, "value");
    }

    @Step("Read last name field value")
    public String getLastNameValue() {
        return getAttribute(LAST_NAME_FIELD, "value");
    }

    @Step("Read postal code field value")
    public String getPostalCodeValue() {
        return getAttribute(POSTAL_CODE_FIELD, "value");
    }
}
