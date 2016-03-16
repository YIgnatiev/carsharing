package com.morfitrun.global;

import com.morfitrun.data_models.User;

/**
 * Created by Виталий on 16/03/2015.
 */
public class GlobalDataStorage {

    private static User mCurrentUser;

    public static User getCurrentUser() {
        return mCurrentUser;
    }

    public static void setCurrentUser(User mCurrentUser) {
        GlobalDataStorage.mCurrentUser = mCurrentUser;
    }

    public static void setCurrentUserId(final String _uId) {
        if (mCurrentUser != null) {
            mCurrentUser.setuId(_uId);
        }
    }

}
