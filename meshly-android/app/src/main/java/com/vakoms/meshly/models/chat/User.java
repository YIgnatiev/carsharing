package com.vakoms.meshly.models.chat;

/**
 * Created by Sviatoslav Kashchin on 22.12.14.
 *
 * This class holds user information
 */
public class User {
    private String id;
    private String username;

    @com.google.gson.annotations.SerializedName("picture-url")
    private String picture_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }
}
