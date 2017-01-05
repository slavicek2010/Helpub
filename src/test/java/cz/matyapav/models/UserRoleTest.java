package cz.matyapav.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Pavel on 05.01.2017.
 */
public class UserRoleTest {

    UserRole role;

    @Before
    public void setUp() throws Exception {
        role = new UserRole();
        role.setRole("ROLE_USER");
    }

    /**
     * Test add user to role positive - user is not null;
     * User should be added
     * @throws Exception
     */
    @Test
    public void testAddUserPositive() throws Exception {
        User user = new User();
        role.addUser(user);
        assertTrue(role.getUsers().contains(user));
    }

    /**
     * Test add user to role negative - user is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testAddUserUserNull() throws Exception {
        User user = null;
        role.addUser(user);
    }

}