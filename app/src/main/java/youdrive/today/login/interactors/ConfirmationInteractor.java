package youdrive.today.login.interactors;

import youdrive.today.login.ConfirmationActionListener;

/**
 * Created by psuhoterin on 16.04.15.
 */
public interface ConfirmationInteractor {

    void invite(String request, ConfirmationActionListener listener);

}
