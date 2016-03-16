package com.budivnictvo.rssnews.app.utils;

/**
 * Created by Администратор on 11.01.2015.
 */
public interface ResponseCallback {
    public void onSuccess(String response);
    public void onFailure(Object object);
}
