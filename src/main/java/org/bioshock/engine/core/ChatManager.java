package org.bioshock.engine.core;

import org.bioshock.main.App;

public class ChatManager {

    private static boolean inChat = false;
    private static StringBuilder currentText = new StringBuilder("Type: ");
    private static boolean sendMessage = false;
    private static String textToSend;
    
    public static boolean isSendMessage() {
        return sendMessage;
    }
    
    public static void setSendMessage(boolean sendMessage) {
        ChatManager.sendMessage = sendMessage;
    }
    
    public static String getTextToSend() {
        return textToSend;
    }
    
    public static boolean isInChat() {
        return inChat;
    }
    
    public static void setInChat(boolean inChat) {
        ChatManager.inChat = inChat;
    }
    
    public static StringBuilder getStrBuild() {
        return currentText;
    }
    
    public static String popText() {
        textToSend = currentText.delete(0, 6).toString();
        App.logger.debug("Text to Send: " + textToSend);
        sendMessage = true;
        currentText.setLength(0);
        currentText.append("Type: ");

        return textToSend;
    }
    
}
