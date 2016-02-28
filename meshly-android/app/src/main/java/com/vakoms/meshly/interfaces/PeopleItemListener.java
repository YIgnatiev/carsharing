package com.vakoms.meshly.interfaces;

import android.view.View;

import com.vakoms.meshly.models.NewUser;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/7/15.
 */
public interface PeopleItemListener {

        void onFollow(NewUser userId);

        void onChat(String userId);

        void onEvent(String eventId);

        void onProfile(View imageView ,NewUser user);



    }
