package youdrive.today.listeners;

import youdrive.today.response.SearchCarResponse;


public interface SearchActionListener {
    void onError() ;
    void onSuccess(SearchCarResponse search);

    void onAccessDenied(String text);
    void onResut(String text);

    void onCommandNotSupported(String text);

}
