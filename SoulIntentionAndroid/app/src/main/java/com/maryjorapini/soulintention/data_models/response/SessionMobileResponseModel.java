package com.maryjorapini.soulintention.data_models.response;

import java.io.Serializable;

/**
 * Created by Sasha on 19.11.2014.
 */
public class SessionMobileResponseModel implements Serializable{
    private String success;
    private String error;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
