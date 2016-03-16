package com.vakoms.meshly.models.chat;

import com.vakoms.meshly.utils.P;

/**
 * Created by taras.melko on 8/18/14.
 * <p/>
 * This class holds last message. When users taps on it - app opens full dialog
 */
public class LastMessage {
    private String id;
    private String updatedAt;
    private String timestamp;
    private String message;
    private String chat_id;
    @com.google.gson.annotations.SerializedName("new")
    private boolean isNew;
    private String createdAt;
    private String user_id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatId() {
        return chat_id;
    }

    public void setChatId(String chatId) {
        this.chat_id = chatId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Check if this message is new. This method returns status from server. But it will
     * return false, if last message is from current user (e.g. response to received message)
     *
     * @return true if message is new and not from current user.
     */
    public boolean isNew(String userId) {
        return !getUserId().equals(userId) && isNew;

    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
}