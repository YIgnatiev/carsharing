package com.vakoms.meshly.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sviatoslav Kashchin on 14.01.15.
 */
@SuppressWarnings("unused")
public class LinkedInToken  {
    public String id;
    public String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

  }