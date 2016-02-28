package com.vakoms.meshly.interfaces;

/**
 * Created by taras.melko on 10/9/14.
 */
public interface AddMessageListener {
     void addMessage(String user, String timestamp, String username, String message, String chatId, String messageId);
}
