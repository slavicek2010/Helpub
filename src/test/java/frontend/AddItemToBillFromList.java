package frontend;

import frontend.utils.CSVfileReader;
import frontend.utils.Constants;
import frontend.utils.DriverHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Pavel on 06.01.2017.
 */
@RunWith(Parameterized.class)
public class AddItemToBillFromList {

    WebDriver driver;

    String itemName;
    String itemPrice;

    public AddItemToBillFromList(String itemName, String itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    @Parameterized.Parameters
    public static Collection<String[]> data1() {
        return CSVfileReader.readCSVfileToCollection(Constants.RESOURCES_PATH+"items.csv");
    }

    @Before
    public void setup() {
        this.driver = DriverHolder.driver;
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @Test
    public void startTest() {
        Select select = new Select(driver.findElement(By.id("itemSelect")));
        select.selectByVisibleText(itemName);
        driver.findElement(By.name("price")).sendKeys(itemPrice);
        driver.findElement(By.xpath("//input[@type='submit' and @value='Add']")).click();

        List<WebElement> webElementList = driver.findElements(By.className("item-info"));
        boolean successfullyAdded = false;
        for(WebElement element : webElementList){
            if(element.getText().contains(itemName)){
                successfullyAdded = true;
            }
        }
        Assert.assertTrue(successfullyAdded);
    }
}
