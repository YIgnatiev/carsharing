package com.vakoms.meshly.models.chat;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.utils.TimeCounterUtil;

import java.util.ArrayList;

/**
 * Created by Sviatoslav Kashchin on 22.12.14.
 *
 * This class holds message object with all needed info.
 */
public class MessageBody {
    private String id;
    private String updatedAt;
    private LastMessage lastMessage;
    private ArrayList<String> users;
    private NewUser user;
    private String createdAt;
    private String connected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LastMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public NewUser getUser() {
        return user;
    }

    public void setUser(NewUser user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getConnected() {
        return connected;
    }

    public void setConnected(String connected) {
        this.connected = connected;
    }




    @BindingAdapter("bind:setTime")
    public static void setTime(TextView textView,MessageBody body) {

        if(body.getLastMessage().getTimestamp()!= null) {
            long curTime = Long.parseLong(body.getLastMessage().getTimestamp().replace(".", "").substring(0, 10));
            textView.setText( TimeCounterUtil.getChatTime(curTime, textView.getContext() ));
        }else textView.setText("Unknown");

    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(SimpleDraweeView imageView, String v) {
        if(v!= null)
            imageView.setImageURI(Uri.parse(v));
    }

    @BindingAdapter("bind:setIsNew")
    public static void setIsNew(View dotView, MessageBody body) {
        String id = ((MainActivity)dotView.getContext()).mUser.getId();
        if(body.lastMessage.isNew(id)) dotView.setVisibility(View.VISIBLE);
        else dotView.setVisibility(View.GONE);

    }

}