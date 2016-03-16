package com.vakoms.meshly.models.chat;

import java.util.ArrayList;

/**
 * Created by Sviatoslav Kashchin on 22.12.14.
 * <p/>
 * Class that holds response with last messages from all users who has been writing to
 * current user.
 */
public class ChatResponseModel {




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








    /**
     * Check if there's any new messages in response. Method will check messages
     * from connected users and others.
     *
     * @return true if there's any new message, otherwise false
     */
    public boolean hasNewMessages(String userId) {
        int connectedCount = 0;
        int otherCount = 0;


            connectedCount = countNewMessages(getConnections() ,userId);
            otherCount = countNewMessages(getOthers(),userId);


        return connectedCount + otherCount > 0;
    }

    private int countNewMessages(ArrayList<MessageBody> messages ,String userId) {
        int count = 0;
        if (messages == null) return 0;

        for (MessageBody mb : messages) {
            if (mb.getLastMessage().isNew(userId)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Get all new message ids for users, who isn't connected
     *
     * @return array list with ids or empty array list, if no connected (others)
     */
    public ArrayList<String> getAllIDForNewOtherMsg(String userId) {

            return getAllIDForNewMessages(getOthers(),userId);



    }

    /**
     * Get all new message ids for users, who connected
     *
     * @return array list with ids or empty array list, if no connected
     */
    public ArrayList<String> getAllIDForNewConnectionsMsg(String userId) {

            return getAllIDForNewMessages(getConnections(),userId);


    }

    /**
     * Get all new message ids for selected new messages
     *
     * @param messageList messages to parse
     * @return array with ids or empty array if no ids found
     */
    private ArrayList<String> getAllIDForNewMessages(ArrayList<MessageBody> messageList,String userId) {
        if (messageList == null) return null;

        ArrayList<String> ids = new ArrayList<>();

        for (MessageBody mb : messageList) {
            if (mb.getLastMessage() != null) {
                if (mb.getLastMessage().isNew(userId)) {
                    ids.add(mb.getLastMessage().getId());
                }
            }
        }

        return ids;
    }

    /**
     * Searches chat id my given user id.
     *
     * @param id user ID with which you want to have a chat
     * @return id or empty string ""
     */
    public String findChatIdByUserId(String id) {


        String connectedId = searchForChatId(getConnections(), id);
        String otherId = searchForChatId(getOthers(), id);


        //end

        if (!connectedId.equals("")) {
            return connectedId;
        }

        if (!otherId.equals("")) {
            return otherId;
        }

        return "";
    }

    /**
     * Search for chat id by given user id with which you want to have chat
     *
     * @param list   chat list (connected, others etc) to search in
     * @param userId user id
     * @return chat id or empty string ""
     */
    private String searchForChatId(ArrayList<MessageBody> list, String userId) {
        if (list == null) {
            return "";
        }

        for (MessageBody messageBody : list) {
            if (messageBody.getUser() != null) {
                if (messageBody.getUser().getId().equals(userId)) {
                    return messageBody.getId();
                }
            }
        }

        return "";
    }
}