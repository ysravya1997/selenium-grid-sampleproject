import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class SauceDemo
{
    WebDriver driver;

    @Parameters("browser")
    @BeforeMethod
    public void setUp(@Optional("chrome") String browser) throws MalformedURLException {
        DesiredCapabilities caps=new DesiredCapabilities();

        if (browser.equalsIgnoreCase("chrome")) {
            caps.setBrowserName("chrome");
        } else if (browser.equalsIgnoreCase("firefox")) {
            caps.setBrowserName("firefox");
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), caps);
        driver.get("https://www.saucedemo.com");
    }

    @Test
    public void itemsAboveFifteenDollars()  {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        System.out.println("Driver is "+driver);


        List<WebElement> inventoryList = driver.findElements(By.className("inventory_item"));

        for(WebElement inventoryItem : inventoryList){
            Double itemPrice = Double.parseDouble(inventoryItem.findElement(By.className("inventory_item_price")).getText().substring(1));

            if(itemPrice > 15.00){
                System.out.println("Item name is "+inventoryItem.findElement(By.className("inventory_item_name")).getText()+", Price = "+itemPrice);
            }
        }
    }


    @AfterMethod
    public void tearDown() {
        if(driver!=null) {
            driver.quit();
        }
    }
}
