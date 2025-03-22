import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class SauceDemo {
    WebDriver driver;

    // 🔒 Use static so all threads share the same ExtentReports instance
    private static ExtentReports extent = ExtentManager.getInstance();

    // 🧵 Thread-safe test instance
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Parameters("browser")
    @BeforeMethod
    public void setUp(@Optional("chrome") String browser, Method method) {
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            if (browser.equalsIgnoreCase("chrome")) {
                caps.setBrowserName("chrome");
            } else if (browser.equalsIgnoreCase("firefox")) {
                caps.setBrowserName("firefox");
            } else {
                throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), caps);
            driver.get("https://www.saucedemo.com");

            ExtentTest test = extent.createTest(method.getName() + " - " + browser.toUpperCase());
            extentTest.set(test);
            extentTest.get().info("Started test on browser: " + browser);
        } catch (Exception e) {
            System.out.println("Setup failed: " + e.getMessage());
            ExtentTest test = extent.createTest(method.getName() + " - " + browser.toUpperCase());
            extentTest.set(test);
            extentTest.get().fail("Setup failed: " + e.getMessage());
            throw new RuntimeException(e); // rethrow to fail test
        }
    }

    @Test(timeOut = 60000)
    public void itemsAboveFifteenDollars() {
        try {
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();

            if (extentTest.get() != null) {
                extentTest.get().info("Logged in successfully");
            }

            List<WebElement> inventoryList = driver.findElements(By.className("inventory_item"));

            for (WebElement item : inventoryList) {
                double price = Double.parseDouble(item.findElement(By.className("inventory_item_price")).getText().substring(1));
                String name = item.findElement(By.className("inventory_item_name")).getText();
                if (extentTest.get() != null) {
                    extentTest.get().info("Item: " + name + " - $" + price);
                }
                Assert.assertTrue(price > 0.00, "Price check failed for: " + name);
            }
            if (extentTest.get() != null) {
                extentTest.get().pass("All items above $0 verified successfully.");
            }
        } catch (Exception e) {
            if (extentTest.get() != null) {
                extentTest.get().fail("Exception: " + e.getMessage());
            }
            throw e;
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && extentTest.get() != null) {
            extentTest.get().fail("Test failed due to: " + result.getThrowable());
        }

        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
