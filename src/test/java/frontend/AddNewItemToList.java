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
import java.util.concurrent.TimeUnit;

/**
 * Created by Pavel on 06.01.2017.
 */
@RunWith(Parameterized.class)
public class AddNewItemToList {

    WebDriver driver;

    String itemName;
    String itemType;

    public AddNewItemToList(String itemName, String itemType) {
        this.itemName = itemName;
        this.itemType = itemType;
    }

    @Parameterized.Parameters
    public static Collection<String[]> data1() {
        return CSVfileReader.readCSVfileToCollection(Constants.RESOURCES_PATH+"itemsToAdd.csv");
    }

    @Before
    public void setup() {
        this.driver = DriverHolder.driver;
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @Test
    public void startTest() {
        driver.findElement(By.id("addNewItem")).click();

        driver.findElement(By.id("name")).sendKeys(itemName);
        Select select = new Select(driver.findElement(By.id("type")));
        select.selectByVisibleText(itemType);
        driver.findElement(By.xpath("//input[@type='submit']")).click();
        Select itemSelect = new Select(driver.findElement(By.id("itemSelect")));
        boolean cointainsNewItem = false;
        for(WebElement webElement : itemSelect.getOptions()){
            if(webElement.getText().equals(itemName)){
                cointainsNewItem = true;
            }
        }
        Assert.assertTrue(cointainsNewItem);
    }
}
