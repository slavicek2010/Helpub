package cz.matyapav.models.validators;

import cz.matyapav.models.Bill;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Pavel on 05.01.2017.
 */

@RunWith(Parameterized.class)
public class BillValidatorTestParametrized {

    @Parameterized.Parameter(0)
    public String billName;
    @Parameterized.Parameter(1)
    public boolean opened;
    @Parameterized.Parameter(2)
    public boolean locked;
    @Parameterized.Parameter(3)
    public String password;
    @Parameterized.Parameter(4)
    public String passwordRetype;
    @Parameterized.Parameter(5)
    public boolean expectedResult;

    BillValidator billValidator;

    @Before
    public void setUp() throws Exception {
        billValidator = new BillValidator();
    }

    @Parameterized.Parameters
    public static Collection primeNumbers() {
        String longText = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. " +
                "Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque " +
                "penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
                "Donec quam felis, ultricies nec, pellentesque eu, pretium quis,.";
        return Arrays.asList(new Object[][] {
                { "bill", true, true, "password", "password", true}, //positive test
                { null, true, true, "password", "password", false}, //bill name null
                { "bill", true, true, null, "password", false }, //password null
                {"bill", true, true, "password", null, false}, //password retype null
                { longText, true, true, "password", "password", false }, //bill name too long (over 255 chars)
                { "bill", true, true, "pa", "pa", false}, //password too short (under 4 chars)
                { "bill", true, true, longText, longText, false}, //password too long (over 60 chars)
                { "bill", true, true, "papa", "pass", false} //password dont match
        });
    }

    @Test
    public void testValidateBill() throws Exception {
        Bill bill = new Bill();
        //256 characters log text
        bill.setName(billName);
        bill.setOpened(opened);
        bill.setLocked(locked);
        bill.setPassword(password);
        bill.setPasswordRetype(passwordRetype);
        boolean result = billValidator.validate(bill);
        assertEquals(result, expectedResult);
    }

}
