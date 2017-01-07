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

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Pavel on 06.01.2017.
 */
@RunWith(Parameterized.class)
public class ChangeQuantity {

    WebDriver driver;

    String itemName;
    String increment;

    public ChangeQuantity(String itemName, String increment) {
        this.itemName = itemName;
        this.increment = increment;
    }

    @Parameterized.Parameters
    public static Collection<String[]> data1() {
        return CSVfileReader.readCSVfileToCollection(Constants.RESOURCES_PATH+"itemsChangeQuantity.csv");
    }

    @Before
    public void setup() {
        this.driver = DriverHolder.driver;
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @Test
    public void startTest() {
        List<WebElement> forms = driver.findElements(By.className("increase-quantity-form"));
        WebElement correspondingForm = null;
        for(WebElement form : forms){
            if(form.findElement(By.name("item")).getAttribute("value").equals(itemName)){
                correspondingForm = form;
            }
        }
        if(correspondingForm != null){
            correspondingForm.findElement(By.name("howMuch")).clear();
            correspondingForm.findElement(By.name("howMuch")).sendKeys(increment);
            correspondingForm.submit();
        }
        List<WebElement> webElementList = driver.findElements(By.className("item-info-wrapper"));
        boolean successfullyChanged = false;
        for(WebElement element : webElementList){
            if(element.findElement(By.className("item-info")).getText().contains(itemName)){
                if(element.findElement(By.className("item-quantity")).getText().equals((Integer.parseInt(increment)+1)+" x")){
                    successfullyChanged = true;
                }
            }
        }
        Assert.assertTrue(successfullyChanged);
    }
}
