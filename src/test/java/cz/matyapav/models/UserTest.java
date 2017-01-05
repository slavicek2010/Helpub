package cz.matyapav.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Pavel on 05.01.2017.
 */
public class UserTest {

    User user;

    @Before
    public void setUp() throws Exception {
        user = new User();
    }

    /**
     * Test add bill positive - bill is not null
     * Bill should be added to user bills
     * @throws Exception
     */
    @Test
    public void testAddBill() throws Exception {
        Bill bill = new Bill();
        user.addBill(bill);
        assertTrue(user.getBills().contains(bill));
    }

    /**
     * Test add bill negative - bill is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testAddBillBillNull() throws Exception {
        Bill bill = null;
        user.addBill(bill);
    }

    /**
     * Test remove bill positive - bill is not null and is in user bills
     * Bill should be removed from user bills
     * @throws Exception
     */
    @Test
    public void testRemoveBill() throws Exception {
        Bill bill = new Bill();
        user.addBill(bill);
        user.removeBill(bill);
        assertFalse(user.getBills().contains(bill));
    }

    /**
     * Test remove bill negative - bill is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testRemoveBillBillNull() throws Exception {
        Bill bill = null;
        user.removeBill(bill);
    }

    /**
     * Test remove bill negative - bill is not in user bilss
     * Nothing shoul be removed
     * @throws Exception
     */
    @Test
    public void testRemoveBillBillNotFound() throws Exception {
        int size = user.getBills().size();
        Bill bill = new Bill();
        user.removeBill(bill);
        assertEquals(size, user.getBills().size()); //nothing is removed
    }

    /**
     * Test add role positive - role is not null
     * Role should be added to user roles
     * @throws Exception
     */
    @Test
    public void testAddRole() throws Exception {
        UserRole role = new UserRole();
        user.addRole(role);
        assertTrue(user.getRoles().contains(role));
    }

    /**
     * Test add role negative - role is null
     * Function should expect null value
     * @throws Exception
     */
    @Test
    public void testAddRoleRoleNull() throws Exception {
        UserRole role = null;
        user.addRole(role);
    }

    /**
     * Test is in bill positive - bill is in user bills and is not null
     * Should return true because bill is in user bills
     * @throws Exception
     */
    @Test
    public void testIsInBill() throws Exception {
        Bill bill = new Bill();
        user.addBill(bill);
        assertTrue(user.isInBill(bill));
    }

    /**
     * Test is in bill negative - bill is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testIsInBillBillNull() throws Exception {
        Bill bill = null;
        user.isInBill(bill);
    }

    /**
     * Test is in bill negative - bill is not in user bills
     * Should return false because bill is not in user bills.
     * @throws Exception
     */
    @Test
    public void testIsInBillIsNotInBill() throws Exception {
        Bill bill = new Bill();
        assertFalse(user.isInBill(bill));
    }
}