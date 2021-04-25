package org.bioshock.engine.core;

import org.bioshock.main.App;

public class ChatManager {
    private ChatManager() {}

    private static boolean inChat = false;
    private static StringBuilder currentText = new StringBuilder("Type: ");
    private static boolean sendMessage = false;

    public static boolean isSendMessage() {
        return sendMessage;
    }

    public static void setSendMessage(boolean sendMessage) {
        ChatManager.sendMessage = sendMessage;
    }

    public static boolean inChat() {
        return inChat;
    }

    public static void setInChat(boolean inChat) {
        ChatManager.inChat = inChat;
    }

    public static void append(String message) {
        if (message.matches("[a-zA-Z0-9]")) currentText.append(message);
        currentText.setLength(86);
    }

    public static void backSpace() {
        if (currentText.length() > 6) {
            currentText.setLength(currentText.length() - 1);
        }
    }

    public static String popText() {
        String textToSend = currentText.delete(0, 6).toString();
        App.logger.debug("Text to Send: {}", textToSend);
        sendMessage = true;
        currentText.setLength(0);
        currentText.append("Type: ");

        return textToSend;
    }
}
