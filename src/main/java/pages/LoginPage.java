package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.BasePage;
import io.qameta.allure.Step;

/** Page Object representing the SauceDemo login page. */
public class LoginPage extends BasePage {

    private static final By USERNAME_FIELD = By.cssSelector("[data-test='username']");
    private static final By PASSWORD_FIELD = By.cssSelector("[data-test='password']");
    private static final By LOGIN_BUTTON = By.cssSelector("[data-test='login-button']");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");
    private static final By ERROR_CLOSE_BUTTON = By.cssSelector("[data-test='error-button']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Enter username: {0}")
    public void enterUsername(String username) {
        type(USERNAME_FIELD, username);
    }

    @Step("Enter password")
    public void enterPassword(String password) {
        type(PASSWORD_FIELD, password);
    }

    @Step("Click Login")
    public void clickLogin() {
        click(LOGIN_BUTTON);
    }

    @Step("Submit login credentials for user: {0}")
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    @Step("Read login validation message")
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    @Step("Check whether login error is displayed")
    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }

    @Step("Dismiss login validation message")
    public void dismissErrorMessage() {
        click(ERROR_CLOSE_BUTTON);
        waitUntilInvisible(ERROR_MESSAGE);
    }

    @Step("Read password field input type")
    public String getPasswordInputType() {
        return getAttribute(PASSWORD_FIELD, "type");
    }

    @Step("Check login form visibility")
    public boolean isLoginFormDisplayed() {
        return isDisplayed(USERNAME_FIELD)
                && isDisplayed(PASSWORD_FIELD)
                && isDisplayed(LOGIN_BUTTON);
    }
}
