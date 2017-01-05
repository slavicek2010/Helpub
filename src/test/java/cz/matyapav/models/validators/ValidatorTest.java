package cz.matyapav.models.validators;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Pavel on 05.01.2017.
 */
public class ValidatorTest {

    Validator validator;

    @Before
    public void setUp() throws Exception {
        validator = new UserValidator(); //some implementation of validator abstract class
    }

    /**
     * Test add error message - add valid error message
     * message should be added into error messages list
     * @throws Exception
     */
    @Test
    public void testAddErrorMessage() throws Exception {
        validator.addErrorMessage("Error message");
        assertEquals(validator.getErrorMessages().size(), 1);
        assertEquals(validator.getErrorMessages().get(0), "Error message");
    }

    /**
     * Test add error message - add null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testAddErrorMessageArgumentNull() throws Exception {
        validator.addErrorMessage(null);
    }

}