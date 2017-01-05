package cz.matyapav.models.validators;

import cz.matyapav.models.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Pavel on 05.01.2017.
 */
public class UserValidatorTest {

    UserValidator userValidator;

    @Before
    public void setUp() throws Exception {
        userValidator = new UserValidator();
    }

    /**
     * Test validation positive - no values is null, username is under 40 chars long, firstname is under 80 chars long
     * lastname is under 80 chars long, password is between 8 and 60 chars long and passwords do match
     * validator should return true = validation passed
     * @throws Exception
     */
    @Test
    public void testValidatePositive() throws Exception {
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("Pavel");
        user.setLastName("Matyas");
        String password = "password";
        user.setPassword(password);
        user.setPasswordRetype(password);
        boolean result = userValidator.validate(user);
        assertTrue(result);
    }

    /**
     * Test validation negative - username is null
     * validator should return false = validation failed
     * @throws Exception
     */
    @Test
    public void testValidateNegativeUsernameNull() throws Exception{
        User user = new User();
        user.setUsername(null);
        user.setFirstName("Pavel");
        user.setLastName("Matyas");
        String password = "password";
        user.setPassword(password);
        user.setPasswordRetype(password);
        boolean result = userValidator.validate(user);
        assertFalse(result);
    }

    /**
     * Test validation negative - firstname is null
     * validator should return false = validation failed
     * @throws Exception
     */
    @Test
    public void testValidateNegativeFirstNameNull() throws Exception{
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName(null);
        user.setLastName("Matyas");
        String password = "password";
        user.setPassword(password);
        user.setPasswordRetype(password);
        boolean result = userValidator.validate(user);
        assertFalse(result);
    }

    /**
     * Test validation negative - lastname is null
     * validator should return false = validation failed
     * @throws Exception
     */
    @Test
    public void testValidateNegativeLastNameNull() throws Exception{
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("Pavel");
        user.setLastName(null);
        String password = "password";
        user.setPassword(password);
        user.setPasswordRetype(password);
        boolean result = userValidator.validate(user);
        assertFalse(result);
    }

    /**
     * Test validation negative - password is null
     * validator should return false = validation failed
     * @throws Exception
     */
    @Test
    public void testValidateNegativePasswordNull() throws Exception{
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("Pavel");
        user.setLastName("Matyas");
        user.setPassword(null);
        user.setPasswordRetype("password");
        boolean result = userValidator.validate(user);
        assertFalse(result);
    }


    /**
     * Test validation negative - password retype is null
     * validator should return false = validation failed
     * @throws Exception
     */
    @Test
    public void testValidateNegativePasswordRetypeNull() throws Exception{
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("Pavel");
        user.setLastName("Matyas");
        user.setPassword("password");
        user.setPasswordRetype(null);
        boolean result = userValidator.validate(user);
        assertFalse(result);
    }


    /**
     * Test validation negative - username has over 40 chars.
     * validator should return false = validation failed
     * @throws Exception
     */
    @Test
    public void testValidateNegativeUsernameTooLong() throws Exception{
        User user = new User();
        //over 40 characters long text
        user.setUsername("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. " +
                "Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque " +
                "penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
                "Donec quam felis, ultricies nec, pellentesque eu, pretium quis,.");
        user.setFirstName("Pavel");
        user.setLastName("Matyas");
        String password = "password";
        user.setPassword(password);
        user.setPasswordRetype(password);
        boolean result = userValidator.validate(user);
        assertFalse(result);
    }

    /**
     * Test validation negative - firstname has over 80 chars.
     * validator should return false = validation failed
     * @throws Exception
     */
    @Test
    public void testValidateNegativeFirstNameTooLong() throws Exception{
        User user = new User();
        user.setUsername("matyapav");
        //over 80 characters long text
        user.setFirstName("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. " +
                "Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque " +
                "penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
                "Donec quam felis, ultricies nec, pellentesque eu, pretium quis,.");
        user.setLastName("Matyas");
        String password = "password";
        user.setPassword(password);
        user.setPasswordRetype(password);
        boolean result = userValidator.validate(user);
        assertFalse(result);
    }

    /**
     * Test validation negative - lastname has over 80 chars.
     * validator should return false = validation failed
     * @throws Exception
     */
    @Test
    public void testValidateNegativeLastNameTooLong() throws Exception{
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("Pavel");
        //over 80 characters long text
        user.setLastName("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. " +
                "Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque " +
                "penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
                "Donec quam felis, ultricies nec, pellentesque eu, pretium quis,.");
        String password = "password";
        user.setPassword(password);
        user.setPasswordRetype(password);
        boolean result = userValidator.validate(user);
        assertFalse(result);
    }

    /**
     * Test validation negative - password has over 60 chars.
     * validator should return false = validation failed
     * @throws Exception
     */
    @Test
    public void testValidateNegativePasswordTooLong() throws Exception{
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("Pavel");
        user.setLastName("Matyas");
        //over 60 characters long text
        String password = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. " +
                "Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque " +
                "penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
                "Donec quam felis, ultricies nec, pellentesque eu, pretium quis,.";
        user.setPassword(password);
        user.setPasswordRetype(password);
        boolean result = userValidator.validate(user);
        assertFalse(result);
    }

    /**
     * Test validation negative - password has under 8 chars.
     * validator should return false = validation failed
     * @throws Exception
     */
    @Test
    public void testValidateNegativePasswordTooShort() throws Exception{
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("Pavel");
        user.setLastName("Matyas");
        //under 8 characters long text
        String password = "pass";
        user.setPassword(password);
        user.setPasswordRetype(password);
        boolean result = userValidator.validate(user);
        assertFalse(result);
    }


    /**
     * Test validation negative - passwords do not match
     * validator should return false = validation failed
     * @throws Exception
     */ @Test
    public void testValidateNegativePasswordsNotMatch() throws Exception{
        User user = new User();
        user.setUsername("matyapav");
        user.setFirstName("Pavel");
        user.setLastName("Matyas");
        //under 8 characters long text
        user.setPassword("password");
        user.setPasswordRetype("password1");
        boolean result = userValidator.validate(user);
        assertFalse(result);
    }

    /**
     * Smoke Test validation - validated user is null
     * validator should expect null argument
     * @throws Exception
     */
    @Test
    public void testValidateUserNull() throws Exception {
        User user = null;
        userValidator.validate(user);
    }


}