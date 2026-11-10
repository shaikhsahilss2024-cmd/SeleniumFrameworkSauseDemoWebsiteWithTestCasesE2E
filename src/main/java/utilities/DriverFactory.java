package utilities;

import java.time.Duration;
import java.util.Locale;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/** Creates and manages one WebDriver instance per execution thread. */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
        // Utility class; prevent object creation.
    }

    public static WebDriver createDriver() {
        if (DRIVER.get() != null) {
            quitDriver();
        }

        String browser = ConfigReader.getBrowser().toLowerCase(Locale.ROOT);
        WebDriver webDriver;

        switch (browser) {
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                options.addArguments("--disable-notifications");
                webDriver = new ChromeDriver(options);
            }
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                webDriver = new FirefoxDriver(options);
                webDriver.manage().window().maximize();
            }
            default -> throw new IllegalArgumentException(
                    "Unsupported browser configured: " + browser +
                    ". Supported values are chrome and firefox.");
        }

        webDriver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getImplicitWaitSeconds()));
        DRIVER.set(webDriver);
        return webDriver;
    }

    public static WebDriver getDriver() {
        WebDriver webDriver = DRIVER.get();
        if (webDriver == null) {
            throw new IllegalStateException(
                    "WebDriver is not initialized. Call DriverFactory.createDriver() first.");
        }
        return webDriver;
    }

    public static void quitDriver() {
        WebDriver webDriver = DRIVER.get();
        try {
            if (webDriver != null) {
                webDriver.quit();
            }
        } finally {
            DRIVER.remove();
        }
    }
}
