package frontend;

import frontend.utils.Constants;
import frontend.utils.DriverHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

/**
 * Created by Pavel on 06.01.2017.
 */
public class Logout {

    private WebDriver driver;

    @Before
    public void setup() {

        this.driver = DriverHolder.driver;
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @Test
    public void startTest() {
        driver.findElement(By.id("logout")).click();
        Assert.assertEquals(driver.getCurrentUrl(), Constants.APP_URL+"/login?logout");
    }

}
