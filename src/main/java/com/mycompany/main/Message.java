
package com.mycompany.main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Creates the Message class
public class Message {

    // Message variables
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status;

    // Stores all messages created
    private static ArrayList<Message> allMessages = new ArrayList<>();

    // Counts successfully sent messages
    private static int totalMessagesSent = 0;

    // ==========================================
    // PART 3: REQUIRED ARRAYS
    // ==========================================

    public static ArrayList<String> sentMessages = new ArrayList<>();
    public static ArrayList<String> disregardedMessages = new ArrayList<>();
    public static ArrayList<String> storedMessages = new ArrayList<>();
    public static ArrayList<String> messageHashes = new ArrayList<>();
    public static ArrayList<String> messageIDs = new ArrayList<>();

    // Additional arrays for stored message information
    public static ArrayList<String> storedRecipients = new ArrayList<>();
    public static ArrayList<String> storedMessageIDs = new ArrayList<>();
    public static ArrayList<String> storedHashes = new ArrayList<>();

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public Message(int messageNumber, String recipient, String messageText) {

        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;

        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();

        this.status = "";
    }

    // ==========================================
    // MESSAGE ID VALIDATION
    // ==========================================

    public boolean checkMessageID() {

        return messageID != null
                && messageID.matches("[0-9]{10}");
    }

    // ==========================================
    // RECIPIENT CELL NUMBER VALIDATION
    // ==========================================

    public String checkRecipientCell() {

        if (recipient != null
                && recipient.matches("^\\+27[0-9]{9}$")) {

            return "Cell phone number successfully captured.";

        } else {

            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // ==========================================
    // MESSAGE HASH GENERATION
    // ==========================================

    public String createMessageHash() {

        String firstTwo = messageID.substring(0, 2);

        String[] words = messageText.trim().split("\\s+");

        String firstWord = "";
        String lastWord = "";

        if (words.length > 0) {
            firstWord = words[0];
        }

        if (words.length > 1) {
            lastWord = words[words.length - 1];
        } else {
            lastWord = firstWord;
        }

        firstWord = firstWord.replaceAll("[^a-zA-Z]", "");
        lastWord = lastWord.replaceAll("[^a-zA-Z]", "");

        messageHash = (
                firstTwo + ":"
                + messageNumber + ":"
                + firstWord + lastWord
        ).toUpperCase();

        return messageHash;
    }

    // ==========================================
    // SEND, DISREGARD OR STORE MESSAGE
    // ==========================================

    public String SentMessage() {

        System.out.println("\nChoose an option:");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("Enter choice (1-3): ");

        String choice = Main.input.nextLine().trim();

        if (choice.equals("1")) {

            status = "Sent";

            totalMessagesSent++;

            allMessages.add(this);

            sentMessages.add(messageText);
            messageHashes.add(messageHash);
            messageIDs.add(messageID);

            return "Message successfully sent.";

        } else if (choice.equals("2")) {

            status = "Disregarded";

            disregardedMessages.add(messageText);

            return "Press 0 to delete the message.";

        } else if (choice.equals("3")) {

            status = "Stored";

            allMessages.add(this);

            storedMessages.add(messageText);
            storedRecipients.add(recipient);
            storedMessageIDs.add(messageID);
            storedHashes.add(messageHash);

            messageHashes.add(messageHash);
            messageIDs.add(messageID);

            storeMessage();

            return "Message successfully stored.";

        } else {

            status = "Disregarded";

            disregardedMessages.add(messageText);

            return "Invalid option – message disregarded.";
        }
    }

    // ==========================================
    // PRINT ALL SENT AND STORED MESSAGES
    // ==========================================

    public static String printMessages() {

        if (allMessages.isEmpty()) {

            return "No messages have been sent yet.";
        }

        StringBuilder result = new StringBuilder();

        for (Message m : allMessages) {

            result.append("Message ID: ")
                    .append(m.messageID).append("\n");

            result.append("Message Hash: ")
                    .append(m.messageHash).append("\n");

            result.append("Recipient: ")
                    .append(m.recipient).append("\n");

            result.append("Message: ")
                    .append(m.messageText).append("\n");

            result.append("Status: ")
                    .append(m.status).append("\n");

            result.append("------------------------\n");
        }

        return result.toString();
    }

    // ==========================================
    // RETURN TOTAL SENT MESSAGES
    // ==========================================

    public static int returnTotalMessages() {

        return totalMessagesSent;
    }

    // ==========================================
    // SAVE STORED MESSAGES TO JSON
    // ==========================================

    @SuppressWarnings("unchecked")
    public void storeMessage() {

        try {

            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < storedMessages.size(); i++) {

                JSONObject obj = new JSONObject();

                obj.put("messageID", storedMessageIDs.get(i));
                obj.put("recipient", storedRecipients.get(i));
                obj.put("message", storedMessages.get(i));
                obj.put("messageHash", storedHashes.get(i));
                obj.put("status", "Stored");

                jsonArray.add(obj);
            }

            try (FileWriter file = new FileWriter("messages.json")) {

                file.write(jsonArray.toJSONString());
            }

            System.out.println(
                    "Messages successfully stored in messages.json"
            );

        } catch (IOException e) {

            System.out.println(
                    "Error writing to JSON file: " + e.getMessage()
            );
        }
    }

    // ==========================================
    // LOAD STORED MESSAGES FROM JSON
    // ==========================================

    public static void loadStoredMessagesFromJSON() {

        try {

            JSONParser parser = new JSONParser();

            JSONArray jsonArray;

            try (FileReader reader = new FileReader("messages.json")) {

                jsonArray = (JSONArray) parser.parse(reader);
            }

            storedMessages.clear();
            storedRecipients.clear();
            storedMessageIDs.clear();
            storedHashes.clear();

            for (Object item : jsonArray) {

                JSONObject jsonObj = (JSONObject) item;

                storedMessageIDs.add(
                        (String) jsonObj.get("messageID")
                );

                storedRecipients.add(
                        (String) jsonObj.get("recipient")
                );

                storedMessages.add(
                        (String) jsonObj.get("message")
                );

                storedHashes.add(
                        (String) jsonObj.get("messageHash")
                );
            }

            System.out.println(
                    "Stored messages loaded from JSON file."
            );

        } catch (Exception e) {

            System.out.println(
                    "No stored messages file found or error reading JSON."
            );
        }
    }

    // ==========================================
    // PART 3 FEATURE A:
    // DISPLAY RECIPIENTS AND STORED MESSAGES
    // ==========================================

    public static void displayStoredSendersAndRecipients() {

        if (storedMessages.isEmpty()) {

            System.out.println("No stored messages found.");
            return;
        }

        System.out.println("\n----- Stored Messages -----");

        for (int i = 0; i < storedMessages.size(); i++) {

            System.out.println(
                    "Recipient: " + storedRecipients.get(i)
            );

            System.out.println(
                    "Message: " + storedMessages.get(i)
            );

            System.out.println("------------------------");
        }
    }

    // ==========================================
    // PART 3 FEATURE B:
    // DISPLAY LONGEST STORED MESSAGE
    // ==========================================

    public static String displayLongestStoredMessage() {

        if (storedMessages.isEmpty()) {

            return "No stored messages found.";
        }

        String longest = storedMessages.get(0);

        for (int i = 1; i < storedMessages.size(); i++) {

            if (storedMessages.get(i).length() > longest.length()) {

                longest = storedMessages.get(i);
            }
        }

        return longest;
    }

    // ==========================================
    // PART 3 FEATURE C:
    // SEARCH MESSAGE USING MESSAGE ID
    // ==========================================

    public static String searchByMessageID(String id) {

        for (int i = 0; i < storedMessageIDs.size(); i++) {

            if (storedMessageIDs.get(i).equals(id)) {

                return storedMessages.get(i);
            }
        }

        return "No message found with that ID.";
    }

    // ==========================================
    // PART 3 FEATURE D:
    // SEARCH MESSAGES USING RECIPIENT
    // ==========================================

    public static String searchByRecipient(String recipient) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < storedRecipients.size(); i++) {

            if (storedRecipients.get(i).equals(recipient)) {

                result.append(storedMessages.get(i)).append("\n");
            }
        }

        if (result.length() == 0) {

            return "No messages found for that recipient.";
        }

        return result.toString().trim();
    }

    // ==========================================
    // PART 3 FEATURE E:
    // DELETE MESSAGE USING MESSAGE HASH
    // ==========================================

    public static String deleteByHash(String hash) {

        for (int i = 0; i < storedHashes.size(); i++) {

            if (storedHashes.get(i).equals(hash)) {

                String deletedMessage = storedMessages.get(i);

                storedMessages.remove(i);
                storedRecipients.remove(i);
                storedMessageIDs.remove(i);
                storedHashes.remove(i);

                // Update JSON file after deletion
                saveStoredMessagesToJSON();

                return "Message: \"" + deletedMessage
                        + "\" successfully deleted.";
            }
        }

        return "No message found with that hash.";
    }

    // ==========================================
    // SAVE CURRENT STORED ARRAYS TO JSON
    // ==========================================

    @SuppressWarnings("unchecked")
    private static void saveStoredMessagesToJSON() {

        try {

            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < storedMessages.size(); i++) {

                JSONObject obj = new JSONObject();

                obj.put("messageID", storedMessageIDs.get(i));
                obj.put("recipient", storedRecipients.get(i));
                obj.put("message", storedMessages.get(i));
                obj.put("messageHash", storedHashes.get(i));
                obj.put("status", "Stored");

                jsonArray.add(obj);
            }

            try (FileWriter file = new FileWriter("messages.json")) {

                file.write(jsonArray.toJSONString());
            }

        } catch (IOException e) {

            System.out.println(
                    "Error updating JSON file after delete: "
                    + e.getMessage()
            );
        }
    }

    // ==========================================
    // PART 3 FEATURE F:
    // DISPLAY FULL STORED MESSAGE REPORT
    // ==========================================

    public static void displayFullReport() {

        if (storedMessages.isEmpty()) {

            System.out.println("No stored messages to display.");
            return;
        }

        System.out.println(
                "\n========== FULL STORED MESSAGES REPORT =========="
        );

        for (int i = 0; i < storedMessages.size(); i++) {

            System.out.println(
                    "Message Hash: " + storedHashes.get(i)
            );

            System.out.println(
                    "Recipient: " + storedRecipients.get(i)
            );

            System.out.println(
                    "Message: " + storedMessages.get(i)
            );

            System.out.println("------------------------");
        }

        System.out.println(
                "================================================="
        );
    }

    // ==========================================
    // GENERATE UNIQUE 10-DIGIT MESSAGE ID
    // ==========================================

    private String generateMessageID() {

        Random rnd = new Random();

        long number = 1000000000L
                + (long) (rnd.nextDouble() * 9000000000L);

        return String.valueOf(number);
    }

    // ==========================================
    // GETTERS
    // ==========================================

    public String getMessageID() {

        return messageID;
    }

    public String getMessageHash() {

        return messageHash;
    }

    public String getRecipient() {

        return recipient;
    }

    public String getMessageText() {

        return messageText;
    }

    public int getMessageNumber() {

        return messageNumber;
    }

    public String getStatus() {

        return status;
    }
}
