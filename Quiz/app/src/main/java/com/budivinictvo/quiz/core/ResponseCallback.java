package com.budivinictvo.quiz.core;

import java.util.Objects;

/**
 * Created by Администратор on 30.12.2014.
 */
public interface ResponseCallback {
    public void onFailure(Object object);
    public void onSuccess(Object object);
}
