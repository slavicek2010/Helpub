package frontend;

import frontend.utils.DriverHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.transaction.Transactional;


@RunWith(Suite.class)
@Transactional
@Suite.SuiteClasses({
        Register.class,
        Login.class,
        ShowBills.class,
        AddBill.class,
        AddNewItemToList.class,
        AddItemToBillFromList.class,
        ChangePrice.class,
        ChangeQuantity.class,
        DeleteBill.class,
        Logout.class,
})
public class TestRun {

    @BeforeClass
    public static void setUp() {
        System.out.println("setting up");
        // should be here or in a separate test for initialization ?

        System.setProperty("webdriver.chrome.driver", DriverHolder.CHROME_DRIVER_PATH);
        DriverHolder.driver = new ChromeDriver();
    }


    @AfterClass
    public static void tearDown() {
        System.out.println("tearing down");
        DriverHolder.driver.quit();

    }
}