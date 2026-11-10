package utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/** Captures and stores browser screenshots for failed tests. */
public final class ScreenshotUtils {

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {
        // Utility class; prevent object creation.
    }

    public static Path captureScreenshot(WebDriver driver, String testName) throws IOException {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver must not be null when taking a screenshot.");
        }
        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            throw new IllegalStateException("The configured WebDriver does not support screenshots.");
        }

        Path screenshotDirectory = Paths.get("target", "screenshots");
        Files.createDirectories(screenshotDirectory);

        String safeTestName = testName == null
                ? "unknown_test"
                : testName.replaceAll("[^a-zA-Z0-9._-]", "_");
        String fileName = safeTestName + "_"
                + LocalDateTime.now().format(TIMESTAMP_FORMAT) + ".png";
        Path screenshotPath = screenshotDirectory.resolve(fileName);

        byte[] screenshotBytes = screenshotDriver.getScreenshotAs(OutputType.BYTES);
        Files.write(screenshotPath, screenshotBytes);
        return screenshotPath;
    }
}
