package com.vakoms.meshly.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by taras.melko on 11/11/14.
 */
public class Privacy  {


    public String location;
    public String lastSeen;
    public long hidden;
    public String chat;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public long getHidden() {
        return hidden;
    }

    public void setHidden(long hidden) {
        this.hidden = hidden;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
