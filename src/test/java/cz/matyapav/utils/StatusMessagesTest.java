package cz.matyapav.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Pavel on 05.01.2017.
 */
public class StatusMessagesTest {

    StatusMessages statusMessages;

    @Before
    public void setUp() throws Exception {
        statusMessages = new StatusMessages();
    }

    @Test
    public void testAddError() throws Exception {
        String error = "error";
        statusMessages.addError(error);
        assertTrue(statusMessages.getErrors().contains(error));
    }

    @Test
    public void testAddErrorNull() throws Exception {
        String error = null;
        statusMessages.addError(error);
    }

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

    @Test
    public void testAddMultipleErrorsNull() throws Exception {
        ArrayList<String> errors = null;
        statusMessages.addMultipleErrors(errors);
    }

    @Test
    public void testHasErrorsPositive() throws Exception {
        String error = "error";
        statusMessages.addError(error);
        assertTrue(statusMessages.hasErrors());
    }

    @Test
    public void testHasErrorsNegative() throws Exception {
        assertFalse(statusMessages.hasErrors());
    }

    @Test
    public void testHasMessagesPositive() throws Exception {
        String message = "msg";
        statusMessages.addMessage(message);
        assertTrue(statusMessages.hasMessages());
    }

    @Test
    public void testHasMessagesNegative() throws Exception {
        assertFalse(statusMessages.hasMessages());
    }

    @Test
    public void testAddMessage() throws Exception {
        String message = "msg";
        statusMessages.addMessage(message);
        assertTrue(statusMessages.getMessages().contains(message));
    }

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

    @Test
    public void testAddMultipleMessagesNull() throws Exception {
        ArrayList<String> messages = null;
        statusMessages.addMultipleMessages(messages);
    }
}