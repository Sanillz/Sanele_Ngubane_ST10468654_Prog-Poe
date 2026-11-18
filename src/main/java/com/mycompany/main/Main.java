
package com.mycompany.main;

import java.util.Scanner;

// Main class for the QuickChat application
public class Main {

    // Scanner for user input
    static Scanner input = new Scanner(System.in);

    // User registration details
    static String username;
    static String password;
    static String CellPhone;

    // Successfully registered details
    static String registeredUsername;
    static String registeredPassword;
    static String registeredCellPhone;

    // ==========================================
    // USERNAME VALIDATION
    // ==========================================

    public static boolean checkUserName(String username) {

        return username != null
                && username.length() <= 5
                && username.contains("_");
    }

    // ==========================================
    // PASSWORD VALIDATION
    // ==========================================

    public static boolean checkPasswordComplexity(String password) {

        return password != null
                && password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[^a-zA-Z0-9].*");
    }

    // ==========================================
    // CELLPHONE VALIDATION
    // ==========================================

    public static boolean checkCellPhoneNumber(String cellphone) {

        return cellphone != null
                && cellphone.matches("^\\+27[0-9]{9}$");
    }

    // ==========================================
    // USER REGISTRATION
    // ==========================================

    public static void registerUser() {

        boolean usernameCorrect = false;

        // Username validation loop
        while (!usernameCorrect) {

            System.out.print("Enter your username: ");
            username = input.nextLine().trim();

            usernameCorrect = checkUserName(username);

            if (usernameCorrect) {

                System.out.println(
                        "Username successfully captured."
                );

            } else {

                System.out.println(
                        "Username is not correctly formatted. "
                        + "Please ensure that your username contains "
                        + "an underscore and is no more than five characters long."
                );
            }
        }

        boolean passwordCorrect = false;

        // Password validation loop
        while (!passwordCorrect) {

            System.out.print("Enter your password: ");
            password = input.nextLine();

            passwordCorrect = checkPasswordComplexity(password);

            if (passwordCorrect) {

                System.out.println(
                        "Password successfully captured."
                );

            } else {

                System.out.println(
                        "Password is not correctly formatted. "
                        + "Please ensure that it contains at least "
                        + "eight characters, a capital letter, "
                        + "a lowercase letter, a number and a special character."
                );
            }
        }

        boolean phoneCorrect = false;

        // Cellphone validation loop
        while (!phoneCorrect) {

            System.out.print("Enter your cellphone number (+27): ");
            CellPhone = input.nextLine().trim();

            phoneCorrect = checkCellPhoneNumber(CellPhone);

            if (phoneCorrect) {

                System.out.println(
                        "Cell phone number successfully captured."
                );

            } else {

                System.out.println(
                        "Cell phone number incorrectly formatted. "
                        + "Please use the format +27 followed by nine digits."
                );
            }
        }

        // Assign registered details after validation
        registeredUsername = username;
        registeredPassword = password;
        registeredCellPhone = CellPhone;

        System.out.println("\nUser registered successfully.");
    }

    // ==========================================
    // USER LOGIN
    // ==========================================

    public static boolean loginUser(
            String username,
            String password) {

        return username != null
                && password != null
                && username.equals(registeredUsername)
                && password.equals(registeredPassword);
    }

    // ==========================================
    // LOGIN STATUS
    // ==========================================

    public static String returnLoginStatus(
            boolean loginSuccessful) {

        if (loginSuccessful) {

            return "Welcome " + registeredUsername
                    + ", it is great to see you again.";

        } else {

            return "Username or password incorrect, please try again.";
        }
    }

    // ==========================================
    // MAIN MENU
    // ==========================================

    public static void showMainMenu() {

        System.out.println("\nWelcome to QuickChat.");

        boolean running = true;

        while (running) {

            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");

            System.out.print("Enter choice: ");

            String choice = input.nextLine().trim();

            switch (choice) {

                case "1":

                    sendMessages();
                    break;

                case "2":

                    displaySentMessages();
                    break;

                case "3":

                    System.out.println("Goodbye!");
                    running = false;
                    break;

                case "4":

                    storedMessagesMenu();
                    break;

                default:

                    System.out.println(
                            "Invalid option. Please choose 1, 2, 3 or 4."
                    );
                    break;
            }
        }
    }

    // ==========================================
    // DISPLAY SENT MESSAGES
    // ==========================================

    public static void displaySentMessages() {

        if (Message.sentMessages.isEmpty()) {

            System.out.println("\nNo sent messages available.");

            return;
        }

        System.out.println("\n----- Recently Sent Messages -----");

        for (int i = 0; i < Message.sentMessages.size(); i++) {

            System.out.println(
                    "Message " + (i + 1) + ": "
                    + Message.sentMessages.get(i)
            );

            System.out.println("------------------------");
        }

        System.out.println(
                "Total sent messages: "
                + Message.returnTotalMessages()
        );
    }

    // ==========================================
    // PART 3: STORED MESSAGES MENU
    // ==========================================

    public static void storedMessagesMenu() {

        // Load previously stored messages from JSON
        Message.loadStoredMessagesFromJSON();

        boolean back = false;

        while (!back) {

            System.out.println("\n----- Stored Messages Menu -----");

            System.out.println(
                    "a) Display recipient and message of all stored messages"
            );

            System.out.println(
                    "b) Display the longest stored message"
            );

            System.out.println(
                    "c) Search for a message by Message ID"
            );

            System.out.println(
                    "d) Search for messages by recipient"
            );

            System.out.println(
                    "e) Delete a message using the message hash"
            );

            System.out.println(
                    "f) Display full report of all stored messages"
            );

            System.out.println(
                    "g) Back to main menu"
            );

            System.out.print("Enter choice: ");

            String choice = input.nextLine()
                    .trim()
                    .toLowerCase();

            switch (choice) {

                case "a":

                    Message.displayStoredSendersAndRecipients();
                    break;

                case "b":

                    System.out.println("\nLongest stored message:");

                    System.out.println(
                            Message.displayLongestStoredMessage()
                    );

                    break;

                case "c":

                    System.out.print(
                            "Enter Message ID to search: "
                    );

                    String id = input.nextLine().trim();

                    System.out.println(
                            Message.searchByMessageID(id)
                    );

                    break;

                case "d":

                    System.out.print(
                            "Enter recipient number: "
                    );

                    String recipient = input.nextLine().trim();

                    System.out.println(
                            Message.searchByRecipient(recipient)
                    );

                    break;

                case "e":

                    System.out.print(
                            "Enter Message Hash to delete: "
                    );

                    String hash = input.nextLine().trim();

                    System.out.println(
                            Message.deleteByHash(hash)
                    );

                    break;

                case "f":

                    Message.displayFullReport();
                    break;

                case "g":

                    back = true;
                    break;

                default:

                    System.out.println(
                            "Invalid option. Please choose a, b, c, d, e, f or g."
                    );

                    break;
            }
        }
    }

    // ==========================================
    // SEND MESSAGES
    // ==========================================

    public static void sendMessages() {

        System.out.print(
                "\nHow many messages do you wish to enter? "
        );

        int numMessages;

        try {

            numMessages = Integer.parseInt(
                    input.nextLine().trim()
            );

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid number. Returning to main menu."
            );

            return;
        }

        // Validate message count
        if (numMessages <= 0) {

            System.out.println(
                    "Please enter a number greater than zero."
            );

            return;
        }

        // Loop through the requested number of messages
        for (int i = 1; i <= numMessages; i++) {

            System.out.println(
                    "\n--- Message " + i + " of " + numMessages + " ---"
            );

            // Get recipient cellphone number
            System.out.print(
                    "Enter recipient cell number (+27): "
            );

            String recipient = input.nextLine().trim();

            // Validate recipient before continuing
            if (!checkCellPhoneNumber(recipient)) {

                System.out.println(
                        "Invalid recipient cellphone number. "
                        + "Please use +27 followed by nine digits."
                );

                System.out.println(
                        "This message will be skipped."
                );

                continue;
            }

            // Get message text
            String messageText = "";

            boolean messageOk = false;

            while (!messageOk) {

                System.out.print(
                        "Enter your message (maximum 250 characters): "
                );

                messageText = input.nextLine();

                if (messageText.length() > 250) {

                    int excess = messageText.length() - 250;

                    System.out.println(
                            "Message exceeds 250 characters by "
                            + excess + ". Please reduce its size."
                    );

                } else if (messageText.trim().isEmpty()) {

                    System.out.println(
                            "Message cannot be empty. Please try again."
                    );

                } else {

                    System.out.println(
                            "Message ready to send."
                    );

                    messageOk = true;
                }
            }

            // Create Message object
            Message msg = new Message(
                    i,
                    recipient,
                    messageText
            );

            // Display message ID
            if (msg.checkMessageID()) {

                System.out.println(
                        "Message ID generated: "
                        + msg.getMessageID()
                );

            } else {

                System.out.println(
                        "Message ID is invalid."
                );

                continue;
            }

            // Display message hash
            System.out.println(
                    "Message Hash: " + msg.getMessageHash()
            );

            // Display recipient validation result
            System.out.println(
                    msg.checkRecipientCell()
            );

            // Ask user to send, disregard or store
            String result = msg.SentMessage();

            System.out.println(result);

            // Display details if message was sent
            if (result.equals("Message successfully sent.")) {

                System.out.println("\n----- Message Details -----");

                System.out.println(
                        "Message ID: " + msg.getMessageID()
                );

                System.out.println(
                        "Message Hash: " + msg.getMessageHash()
                );

                System.out.println(
                        "Recipient: " + msg.getRecipient()
                );

                System.out.println(
                        "Message: " + msg.getMessageText()
                );

                System.out.println(
                        "Status: " + msg.getStatus()
                );
            }

            // Display details if message was stored
            if (result.equals("Message successfully stored.")) {

                System.out.println(
                        "Message ID: " + msg.getMessageID()
                );

                System.out.println(
                        "Message successfully added to stored messages."
                );
            }
        }

        // Display total messages sent
        System.out.println(
                "\nTotal number of messages sent: "
                + Message.returnTotalMessages()
        );

        // Display messages
        System.out.println(
                "\nAll messages so far:\n"
                + Message.printMessages()
        );
    }

    // ==========================================
    // MAIN METHOD
    // ==========================================

    public static void main(String[] args) {

        // Register user
        registerUser();

        // Request login details
        System.out.println("\n----- Login -----");

        System.out.print("Enter username to login: ");

        String loginUsername = input.nextLine().trim();

        System.out.print("Enter password to login: ");

        String loginPassword = input.nextLine();

        // Verify login
        boolean loginSuccessful = loginUser(
                loginUsername,
                loginPassword
        );

        // Display login status
        System.out.println(
                returnLoginStatus(loginSuccessful)
        );

        // Open menu only after successful login
        if (loginSuccessful) {

            showMainMenu();

        } else {

            System.out.println(
                    "Login failed. Application will close."
            );
        }
    }
}
