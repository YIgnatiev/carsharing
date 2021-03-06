package youdrive.today.listeners;

import youdrive.today.models.User;

/**
 * Created by psuhoterin on 15.04.15.
 */
public interface LoginActionListener {

    void onSuccess(User user);

    void onError();

    void onErrorUserNotFound(String message);

    void onErrorFieldEmpty(String message);

    void onUnknownError(String text);
}
