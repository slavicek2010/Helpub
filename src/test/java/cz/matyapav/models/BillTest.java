package cz.matyapav.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class tests Bill model and its functions
 * Created by Pavel on 05.01.2017.
 */
public class BillTest {

    Bill bill;

    @Before
    public void setUp() throws Exception {
        bill = new Bill();
    }

    /**
     * Test add user positive - everything goes well
     * User is added
     * @throws Exception
     */
    @Test
    public void testAddUserPositive() throws Exception {
        User user = new User();
        user.setUsername("matyapav");
        bill.addUser(user);
        assertTrue(bill.getUsers().contains(user));
    }

    /**
     * Smoke Test add user - try to add null value
     * Function should expect null value and should not fail
     * @throws Exception
     */
    @Test
    public void testAddUserUserNull() throws Exception {
        User user = null;
        bill.addUser(user);
    }

    /**
     * Test remove user positive - users with username exists in set and can be removed
     * User should be removed
     * @throws Exception
     */
    @Test
    public void testRemoveUserByUserNamePositive() throws Exception {
        User user = new User();
        user.setUsername("matyapav");
        bill.addUser(user);
        bill.removeUserByUserName("matyapav");
        assertFalse(bill.getUsers().contains(user));
    }

    /**
     * Test remove user negative - user with username is not in set
     * User is not found hence nothing should be removed
     * @throws Exception
     */
    @Test
    public void testRemoveUserByUserNameNotFound() throws Exception {
        int size = bill.getUsers().size();
        bill.removeUserByUserName("matyapav");
        assertEquals(size, bill.getUsers().size()); //nothing was deleted
    }

    /**
     * Smoke test remove user - user is null
     * Function should expect null value and should not fail
     * @throws Exception
     */
    @Test
    public void testRemoveUserByUserNameUsernameNull() throws Exception {
        bill.removeUserByUserName(null);
    }

    /**
     * Test contains user with username positive - user with searched username is in set
     * Function should return true because user is found
     * @throws Exception
     */
    @Test
    public void testContainsUserWithUsernamePositive() throws Exception {
        User user = new User();
        user.setUsername("matyapav");
        bill.addUser(user);
        assertTrue(bill.containsUserWithUsername("matyapav"));
    }

    /**
     * Test contains user with username negative - user with searched username is NOT in set
     * Function should return false because user is not found
     * @throws Exception
     */
    @Test
    public void testContainsUserWithUsernameUserNotFound() throws Exception {
        assertFalse(bill.containsUserWithUsername("matyapav"));
    }

    /**
     * Test contains user with username negative - username is null
     * Function should expect null value and return false
     * @throws Exception
     */
    @Test
    public void testContainsUserWithUsernameUsernameNull() throws Exception {
        assertFalse(bill.containsUserWithUsername(null));
    }
}