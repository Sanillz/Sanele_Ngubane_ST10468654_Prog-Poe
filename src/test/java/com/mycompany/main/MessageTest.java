package com.mycompany.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MessageTest {

    // Reset test data before every test
    @BeforeEach
    public void setUp() {
        Message.sentMessages.clear();
        Message.disregardedMessages.clear();
        Message.storedMessages.clear();
        Message.messageHashes.clear();
        Message.messageIDs.clear();

        Message.storedRecipients.clear();
        Message.storedMessageIDs.clear();
        Message.storedHashes.clear();
    }

    // ==========================================
    // PART 2: MESSAGE VALIDATION TESTS
    // ==========================================

    @Test
    public void testMessageIDGeneration() {
        Message msg = new Message(
                1,
                "+27718693002",
                "Test message"
        );

        assertNotNull(msg.getMessageID());
        assertTrue(msg.getMessageID().matches("[0-9]{10}"));
        assertTrue(msg.checkMessageID());
    }

    @Test
    public void testRecipientCellSuccess() {
        Message msg = new Message(
                1,
                "+27718693002",
                "Hello Mike"
        );

        assertEquals(
                "Cell phone number successfully captured.",
                msg.checkRecipientCell()
        );
    }

    @Test
    public void testRecipientCellFailure() {
        Message msg = new Message(
                2,
                "08575975889",
                "Hello Mike"
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                msg.checkRecipientCell()
        );
    }

    @Test
    public void testMessageHashGeneration() {
        Message msg = new Message(
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight"
        );

        String hash = msg.getMessageHash();

        assertNotNull(hash);
        assertTrue(hash.matches("[0-9]{2}:0:HITONIGHT"));
    }

    @Test
    public void testMessageHashIsUppercase() {
        Message msg = new Message(
                1,
                "+27718693002",
                "hello there everyone"
        );

        assertEquals(
                msg.getMessageHash(),
                msg.getMessageHash().toUpperCase()
        );
    }

    @Test
    public void testMessageConstructor() {
        Message msg = new Message(
                5,
                "+27718693002",
                "QuickChat test message"
        );

        assertEquals(5, msg.getMessageNumber());
        assertEquals("+27718693002", msg.getRecipient());
        assertEquals("QuickChat test message", msg.getMessageText());
        assertNotNull(msg.getMessageID());
        assertNotNull(msg.getMessageHash());
    }

    // ==========================================
    // PART 3: MESSAGE ARRAY TESTS
    // ==========================================

    @Test
    public void testSentMessagesArray() {
        Message.sentMessages.add("Did you get the cake?");
        Message.sentMessages.add("It is dinner time !");

        assertEquals(2, Message.sentMessages.size());

        assertTrue(
                Message.sentMessages.contains("Did you get the cake?")
        );

        assertTrue(
                Message.sentMessages.contains("It is dinner time !")
        );
    }

    @Test
    public void testDisregardedMessagesArray() {
        Message.disregardedMessages.add(
                "Yohoooo, I am at your gate."
        );

        assertEquals(1, Message.disregardedMessages.size());

        assertEquals(
                "Yohoooo, I am at your gate.",
                Message.disregardedMessages.get(0)
        );
    }

    @Test
    public void testStoredMessagesArray() {
        Message.storedMessages.add("First stored message");
        Message.storedMessages.add("Second stored message");

        assertEquals(2, Message.storedMessages.size());
        assertTrue(
                Message.storedMessages.contains("First stored message")
        );
    }

    // ==========================================
    // PART 3: LONGEST STORED MESSAGE
    // ==========================================

    @Test
    public void testLongestStoredMessage() {
        Message.storedMessages.add(
                "Where are you? You are late! I have asked you to be on time."
        );

        Message.storedMessages.add(
                "Ok, I am leaving without you."
        );

        String longest = Message.displayLongestStoredMessage();

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                longest
        );
    }

    @Test
    public void testLongestStoredMessageWhenEmpty() {
        assertEquals(
                "No stored messages found.",
                Message.displayLongestStoredMessage()
        );
    }

    // ==========================================
    // PART 3: SEARCH BY MESSAGE ID
    // ==========================================

    @Test
    public void testSearchByMessageID() {
        Message.storedMessageIDs.add("1234567890");
        Message.storedMessages.add("It is dinner time !");
        Message.storedRecipients.add("+27838884567");

        String result = Message.searchByMessageID("1234567890");

        assertEquals("It is dinner time !", result);
    }

    @Test
    public void testSearchByMessageIDNotFound() {
        String result = Message.searchByMessageID("9999999999");

        assertEquals(
                "No message found with that ID.",
                result
        );
    }

    // ==========================================
    // PART 3: SEARCH BY RECIPIENT
    // ==========================================

    @Test
    public void testSearchByRecipient() {
        Message.storedRecipients.add("+27838884567");

        Message.storedMessages.add(
                "Where are you? You are late! I have asked you to be on time."
        );

        Message.storedRecipients.add("+27838884567");

        Message.storedMessages.add(
                "Ok, I am leaving without you."
        );

        String result = Message.searchByRecipient("+27838884567");

        assertTrue(
                result.contains("Where are you? You are late!")
        );

        assertTrue(
                result.contains("Ok, I am leaving without you.")
        );
    }

    @Test
    public void testSearchByRecipientNotFound() {
        String result = Message.searchByRecipient("+27839999999");

        assertEquals(
                "No messages found for that recipient.",
                result
        );
    }

    // ==========================================
    // PART 3: DELETE MESSAGE BY HASH
    // ==========================================

    @Test
    public void testDeleteByHash() {
        Message.storedMessages.add("Test stored message");
        Message.storedRecipients.add("+27838884567");
        Message.storedMessageIDs.add("1234567890");
        Message.storedHashes.add("12:2:TESTMESSAGE");

        String result = Message.deleteByHash("12:2:TESTMESSAGE");

        assertTrue(result.contains("successfully deleted"));

        assertEquals(0, Message.storedMessages.size());
        assertEquals(0, Message.storedRecipients.size());
        assertEquals(0, Message.storedMessageIDs.size());
        assertEquals(0, Message.storedHashes.size());
    }

    @Test
    public void testDeleteByHashNotFound() {
        String result = Message.deleteByHash("INVALIDHASH");

        assertEquals(
                "No message found with that hash.",
                result
        );
    }

    // ==========================================
    // PART 3: DISPLAY STORED MESSAGES
    // ==========================================

    @Test
    public void testDisplayFullReport() {

        Message.storedMessages.add("Did you get the cake?");
        Message.storedRecipients.add("+27834557896");
        Message.storedHashes.add("00:1:DIDCAKE");
        Message.storedMessageIDs.add("0012345678");

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output));

            Message.displayFullReport();

        } finally {
            System.setOut(originalOut);
        }

        String report = output.toString();

        assertTrue(report.contains("FULL STORED MESSAGES REPORT"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("00:1:DIDCAKE"));
    }

    @Test
    public void testDisplayFullReportWhenEmpty() {

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output));

            Message.displayFullReport();

        } finally {
            System.setOut(originalOut);
        }

        assertTrue(
                output.toString().contains(
                        "No stored messages to display."
                )
        );
    }

    // ==========================================
    // PART 3: DISPLAY RECIPIENTS AND MESSAGES
    // ==========================================

    @Test
    public void testDisplayStoredSendersAndRecipients() {

        Message.storedMessages.add("Hello, how are you?");
        Message.storedRecipients.add("+27838884567");

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output));

            Message.displayStoredSendersAndRecipients();

        } finally {
            System.setOut(originalOut);
        }

        String result = output.toString();

        assertTrue(result.contains("Hello, how are you?"));
        assertTrue(result.contains("+27838884567"));
    }

    @Test
    public void testDisplayStoredMessagesWhenEmpty() {

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output));

            Message.displayStoredSendersAndRecipients();

        } finally {
            System.setOut(originalOut);
        }

        assertTrue(
                output.toString().contains("No stored messages found.")
        );
    }

}
