package youdrive.today.login;

import java.util.List;

import youdrive.today.Region;

/**
 * Created by psuhoterin on 16.04.15.
 */
public interface RegistrationActionListener {

    void onInvite();
    void onRegions(List<Region> regions);

    void onError();
    void onUnknownError();
    void onRegionNotFound();
    void onUserAlreadyExist();

}
