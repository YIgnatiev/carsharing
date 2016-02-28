package com.vakoms.meshly.models.chat;

import java.util.ArrayList;

/**
 * Created by Sviatoslav Kashchin on 22.12.14.
 *
 * This class holds counters of unread messages and last messages from
 * connected (and not) users
 */
public class Data {
    private String unreadOthers;
    private String unreadConnections;

    private ArrayList<MessageBody> others;
    private ArrayList<MessageBody> connections;

    public String getUnreadOthers() {
        return unreadOthers;
    }

    public void setUnreadOthers(String unreadOthers) {
        this.unreadOthers = unreadOthers;
    }

    public String getUnreadConnections() {
        return unreadConnections;
    }

    public void setUnreadConnections(String unreadConnections) {
        this.unreadConnections = unreadConnections;
    }

    public ArrayList<MessageBody> getOthers() {
        return others;
    }

    public void setOthers(ArrayList<MessageBody> others) {
        this.others = others;
    }

    public ArrayList<MessageBody> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<MessageBody> connections) {
        this.connections = connections;
    }
}
