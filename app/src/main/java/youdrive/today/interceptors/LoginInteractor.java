package youdrive.today.interceptors;

import youdrive.today.listeners.LoginActionListener;

/**
 * Created by psuhoterin on 15.04.15.
 */
public interface LoginInteractor {

    void login(String name, String password, LoginActionListener listener);
//    Subscription reLogin(String email, String password, LoginActionListener listener);

}
