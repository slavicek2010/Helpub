package frontend;

import frontend.utils.Constants;
import frontend.utils.DriverHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Pavel on 06.01.2017.
 */
public class DeleteBill {

    WebDriver driver;

    @Before
    public void setup() {
        this.driver = DriverHolder.driver;
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @Test
    public void startTest() {
        driver.get(Constants.APP_URL+"/bills");

        List<WebElement> tableOfBillsRows = driver.findElement(By.id("tableOfBills")).findElements(By.id("tbodyRow"));
        for (WebElement row : tableOfBillsRows){
            if(row.findElement(By.id("billName")).getText().equals(Constants.TEST_BILL_NAME)){
                row.findElement(By.id("deleteBill")).findElement(By.tagName("a")).click();
            }
        }
        WebElement tableOfBills = driver.findElement(By.id("tableOfBills"));
        String innerHtml = tableOfBills.getAttribute("innerHTML");
        boolean successfullyDeleted = !innerHtml.contains(Constants.TEST_BILL_NAME);
        Assert.assertTrue(successfullyDeleted);
    }
}
