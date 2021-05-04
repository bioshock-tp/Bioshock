package org.bioshock.engine.core;

import org.bioshock.main.App;

/**
 * 
 * @author William Holbrook - Mircea Andrei Radu
 * A class that is used in managing text chat on the client side
 */
public class ChatManager {
	/**
	 * Private constructor as it's meant to be used as a static 
	 * class and there are not meant to be any instances of it
	 */
    private ChatManager() {}

    /**
     * A boolean to tell whether a user should be typing in chat or not
     */
    private static boolean inChat = false;
    /**
     * The message prompt for chat
     */
    private static final String msgPrompt = "Type: ";
    /**
     * A string builder used to construct the text that a user is typing
     */
    private static StringBuilder currentText = new StringBuilder(msgPrompt);
    /**
     * A boolean to store whether the current message should be sent
     */
    private static boolean sendMessage = false;
    /**
     * the maximum number of chars in a single message
     */
    private static int maxMessageLen = 80;
    
    /**
     * getter
     * @return 
     */
    public static boolean isSendMessage() {
        return sendMessage;
    }

    /**
     * setter
     * @param sendMessage
     */
    public static void setSendMessage(boolean sendMessage) {
        ChatManager.sendMessage = sendMessage;
    }

    /**
     * getter
     * @return if the game is in chat or not
     */
    public static boolean inChat() {
        return inChat;
    }

    public static StringBuilder getStrBuild() {
        return currentText;
    }

    /**
     * setter
     * @param inChat 
     * 		the value to set inChat to
     */
    public static void setInChat(boolean inChat) {
        ChatManager.inChat = inChat;
    }

    /**
     * Appends a message to the current text if the message is alphanumeric
     * 
     * @param message the message to append
     */
    public static void append(String message) {
        if (message.matches("[a-zA-Z0-9]")) currentText.append(message);
        currentText.setLength(msgPrompt.length() + maxMessageLen);
    }
    
    /**
     * removes one char off the end of the string
     * making sure not to remove the msgPrompt
     */
    public static void backSpace() {
        if (currentText.length() > msgPrompt.length()) {
            currentText.setLength(currentText.length() - 1);
        }
    }

    public static String popText() {
    	//Remove the msgPrompt and then get the rest of the text in currentText
        String textToSend = currentText.delete(0, msgPrompt.length()).toString();

        App.logger.debug("Text to Send: {}", textToSend);
        sendMessage = true;
        //Clear the current text string builder
        currentText.setLength(0);
        //Put the message prompt back in the string builder
        currentText.append(msgPrompt);

        return textToSend;
    }
}
