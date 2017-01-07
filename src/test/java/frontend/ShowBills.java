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
public class ShowBills {


    WebDriver driver;

    @Before
    public void setup() {
        this.driver = DriverHolder.driver;
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @Test
    public void startTest() {
        driver.findElement(By.id("showBills")).click();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.urlToBe(Constants.APP_URL+"/bills"));
        Assert.assertEquals(Constants.APP_URL+"/bills", driver.getCurrentUrl());
    }
}
