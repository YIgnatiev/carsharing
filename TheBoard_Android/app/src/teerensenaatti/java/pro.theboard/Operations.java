package pro.theboard;

import pro.theboard.utils.Preferences;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/27/15.
 */


public class Operations {
    private LoginActivity mContext;
    private static Operations INSTANCE;


    public static Operations getInstace(LoginActivity context) {

        if (INSTANCE == null)
            INSTANCE = new Operations(context);

        return INSTANCE;
    }

    public Operations(LoginActivity context) {
        mContext = context;
    }


    public void login() {

        if (Preferences.getIsAdult()) {
            mContext.requestLogin();
        } else {
            mContext.startAlertFragment();
        }
    }


}