package frontend;

import frontend.utils.Constants;
import frontend.utils.DriverHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * Created by Pavel on 06.01.2017.
 */
public class AddBill {


    WebDriver driver;

    @Before
    public void setup() {
        this.driver = DriverHolder.driver;
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @Test
    public void startTest() {
        driver.findElement(By.id("addBill")).click();
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.urlToBe(Constants.APP_URL+"/bills/create"));
        Assert.assertEquals(Constants.APP_URL+"/bills/create", driver.getCurrentUrl());
        driver.findElement(By.id("name")).sendKeys(Constants.TEST_BILL_NAME);
        driver.findElement(By.xpath("//input[@type='submit']")).click();
        Assert.assertTrue(driver.getCurrentUrl().startsWith(Constants.APP_URL+"/bills/show"));
    }

}
