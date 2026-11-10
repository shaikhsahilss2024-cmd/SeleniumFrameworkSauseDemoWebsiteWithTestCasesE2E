
package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads framework configuration once from the test classpath.
 * Values are accessed through type-safe convenience methods.
 */
public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    // Static initialization: configuration loads once when the class initializes.
    static {
        try (InputStream inputStream =
                     ConfigReader.class.getClassLoader()
                             .getResourceAsStream("config.properties")) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "config.properties was not found on the classpath. " +
                                "Place it under src/test/resources."
                );
            }

            PROPERTIES.load(inputStream);

        } catch (IOException exception) {
            throw new ExceptionInInitializerError(
                    "Unable to load config.properties: "
                            + exception.getMessage()
            );
        }
    }

    // Private constructor prevents object creation.
    private ConfigReader() {
    }

    // Common method to retrieve configuration values.
    public static String getProperty(String key) {

        String value = PROPERTIES.getProperty(key);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Missing or blank configuration property: " + key
            );
        }

        return value.trim();
    }

    public static String getApplicationUrl() {
        return getProperty("application.url");
    }

    public static String getBrowser() {
        return getProperty("browser").toLowerCase();
    }

    public static String getValidUsername() {
        return getProperty("valid.username");
    }

    public static String getValidPassword() {
        return getProperty("valid.password");
    }

    public static String getInvalidUsername() {
        return getProperty("invalid.username");
    }

    public static String getInvalidPassword() {
        return getProperty("invalid.password");
    }

    public static String getLockedUsername() {
        return getProperty("locked.username");
    }

    public static int getExplicitWaitSeconds() {
        return getPositiveOrZeroIntProperty("explicit.wait.seconds");
    }

    public static int getImplicitWaitSeconds() {
        return getPositiveOrZeroIntProperty("implicit.wait.seconds");
    }

    // Validates numerical configuration values.
    private static int getPositiveOrZeroIntProperty(String key) {

        try {
            int value = Integer.parseInt(getProperty(key));

            if (value < 0) {
                throw new IllegalArgumentException(
                        "Configuration property must be zero or greater: " + key
                );
            }

            return value;

        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Configuration property must be an integer: " + key,
                    exception
            );
        }
    }
}
