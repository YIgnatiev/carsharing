package youdrive.today.listeners;

import youdrive.today.response.SearchCarResponse;


public interface SearchActionListener {
    void onError();

    void onSuccess(SearchCarResponse search, int type);

    void onAccessDenied(String text);

    void onCommandNotSupported(String text);

    void onSessionNotFound(String text);

    void onUnknownError(String text);
}
