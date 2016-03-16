package com.morfitrun.data_models;

/**
 * Created by Виталий on 13/03/2015.
 */
public abstract class BaseResponseModel {
    private String success;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
