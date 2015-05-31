package youdrive.today.login.interactors;

import youdrive.today.login.RegistrationActionListener;

/**
 * Created by psuhoterin on 16.04.15.
 */
public interface RegistrationInteractor {

    void getInvite(String name, Long phone, String region, boolean readyToUse, RegistrationActionListener listener);
    void getRegions(RegistrationActionListener listener);

}
