package youdrive.today.listeners;

import java.util.List;

import youdrive.today.models.Region;

/**
 * Created by psuhoterin on 16.04.15.
 */
public interface RegistrationActionListener {

    void onInvite();
    void onRegions(List<Region> regions);

    void onError();
    void onUnknownError(String text);
    void onRegionNotFound(String text);
    void onUserAlreadyExist(String text);

}
