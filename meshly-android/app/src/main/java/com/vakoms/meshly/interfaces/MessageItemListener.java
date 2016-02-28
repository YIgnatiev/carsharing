package com.vakoms.meshly.interfaces;

import com.vakoms.meshly.models.chat.MessageBody;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/12/15.
 */
public interface MessageItemListener {
    void onUserClick(String userId);

    void onMessageClick(MessageBody message);
}
