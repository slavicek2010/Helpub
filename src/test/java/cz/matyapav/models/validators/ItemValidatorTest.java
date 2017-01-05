package cz.matyapav.models.validators;

import cz.matyapav.models.Item;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Pavel on 05.01.2017.
 */
public class ItemValidatorTest {

    ItemValidator itemValidator;

    @Before
    public void setUp() throws Exception {
        itemValidator = new ItemValidator();
    }

    /**
     * Test positive validation - item name is not null and have no more than 255 characters
     * validatior should return true = validation passed
     * @throws Exception
     */
    @Test
    public void testValidatePositive() throws Exception {
        Item item = new Item();
        item.setName("item");
        boolean result = itemValidator.validate(item);
        assertTrue(result);
    }

    /**
     * Test negative validation - item name is null
     * validatior should return false = validation failed
     * @throws Exception
     */
    @Test
    public void testValidateNegativeItemNameNull() throws Exception {
        Item item = new Item();
        boolean result = itemValidator.validate(item);
        assertFalse(result);
    }

    /**
     * Test negative validation - item name has over 255 characters
     * validatior should return false = validation failed
     * @throws Exception
     */
    @Test
    public void testValidateNegativeItemNameTooLong() throws Exception {
        Item item = new Item();
        //256 characters long
        item.setName("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. " +
                "Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque " +
                "penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
                "Donec quam felis, ultricies nec, pellentesque eu, pretium quis,.");
        boolean result = itemValidator.validate(item);
        assertFalse(result);
    }

    /**
     * Smoke Test validation - validated item is null
     * validator should expect null argument
     * @throws Exception
     */
    @Test
    public void testValidateItemNull() throws Exception {
        Item item = null;
        itemValidator.validate(item);
    }

}