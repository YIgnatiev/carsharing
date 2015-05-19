package youdrive.today.login.interactors;

import youdrive.today.login.RegistrationActionListener;

/**
 * Created by psuhoterin on 16.04.15.
 */
public interface RegistrationInteractor {

    void getInvite(String name, String phone, String region, RegistrationActionListener listener);
    void getRegions(RegistrationActionListener listener);

}
