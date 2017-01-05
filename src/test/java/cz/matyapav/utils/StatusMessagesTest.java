package cz.matyapav.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * This class tests StatusMessages class and its functions
 * Created by Pavel on 05.01.2017.
 */
public class StatusMessagesTest {

    StatusMessages statusMessages;

    @Before
    public void setUp() throws Exception {
        statusMessages = new StatusMessages();
    }

    /**
     * Tests positive addition of error
     * error is added to error messages
     * @throws Exception
     */
    @Test
    public void testAddError() throws Exception {
        String error = "error";
        statusMessages.addError(error);
        assertTrue(statusMessages.getErrors().contains(error));
    }

    /**
     * Tests negative addition of error - error is null
     * function should expect null argument
     * @throws Exception
     */
    @Test
    public void testAddErrorNull() throws Exception {
        String error = null;
        statusMessages.addError(error);
    }

    /**
     * Tests positive addition of multiple errors
     * all errors are added to error messages
     * @throws Exception
     */
    @Test
    public void testAddMultipleErrors() throws Exception {
        ArrayList<String> errors = new ArrayList<>();
        String error1 = "error 1";
        String error2 = "error 2";
        errors.add(error1);
        errors.add(error2);
        statusMessages.addMultipleErrors(errors);
        assertTrue("contains error 1", statusMessages.getErrors().contains(error1));
        assertTrue("contains error 2", statusMessages.getErrors().contains(error2));
    }

    /**
     * Tests negative addition of multiple errors - errors list is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testAddMultipleErrorsNull() throws Exception {
        ArrayList<String> errors = null;
        statusMessages.addMultipleErrors(errors);
    }

    /**
     * Tests positive has errors
     * errors are not empty should return true
     * @throws Exception
     */
    @Test
    public void testHasErrorsPositive() throws Exception {
        String error = "error";
        statusMessages.addError(error);
        assertTrue(statusMessages.hasErrors());
    }

    /**
     * Tests negative has errors
     * errors are empty should return false
     * @throws Exception
     */
    @Test
    public void testHasErrorsNegative() throws Exception {
        assertFalse(statusMessages.hasErrors());
    }

    /**
     * Tests positive has messages
     * messages are not empty should return true
     * @throws Exception
     */
    @Test
    public void testHasMessagesPositive() throws Exception {
        String message = "msg";
        statusMessages.addMessage(message);
        assertTrue(statusMessages.hasMessages());
    }

    /**
     * Tests negative has messages
     * messages are empty should return false
     * @throws Exception
     */
    @Test
    public void testHasMessagesNegative() throws Exception {
        assertFalse(statusMessages.hasMessages());
    }

    /**
     * Tests positive addition of messages
     * message is added to messages
     * @throws Exception
     */
    @Test
    public void testAddMessage() throws Exception {
        String message = "msg";
        statusMessages.addMessage(message);
        assertTrue(statusMessages.getMessages().contains(message));
    }

    /**
     * Tests positive addition of multiple messages
     * all messages are added to messages
     * @throws Exception
     */
    @Test
    public void testAddMultipleMessages() throws Exception {
        ArrayList<String> messages = new ArrayList<>();
        String message1 = "msg 1";
        String message2 = "msg 2";
        messages.add(message1);
        messages.add(message2);
        statusMessages.addMultipleMessages(messages);
        assertTrue("contains message 1", statusMessages.getMessages().contains(message1));
        assertTrue("contains message 2", statusMessages.getMessages().contains(message2));
    }

    /**
     * Tests negative addition of multiple messages - messages list is null
     * Function should expect null argument
     * @throws Exception
     */
    @Test
    public void testAddMultipleMessagesNull() throws Exception {
        ArrayList<String> messages = null;
        statusMessages.addMultipleMessages(messages);
    }
}