package com.budivinictvo.quiz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Администратор on 31.12.2014.
 */
public class User implements Serializable {
    private int user_id;
    private String token;
    private String expires;
    private int statistics;
    private String user_firstname;
    private String user_lastname;
    private String user_email;
    private String user_date;

    public void setUser_firstname(String user_firstname) {
        this.user_firstname = user_firstname;
    }

    public String getUser_firstname() {
        return user_firstname;
    }

    public void setUser_lastname(String user_lastname) {
        this.user_lastname = user_lastname;
    }

    public String getUser_lastname() {
        return user_lastname;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public int getStatistics() {
        return statistics;
    }

    public void setStatistics(int statistics) {
        this.statistics = statistics;
    }




    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpires() {
        return expires;
    }






    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getUser_date() {
        return user_date;
    }


}

