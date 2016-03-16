package pro.theboard;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/27/15.
 */


public class Operations {
    private LoginActivity mContext;




    public Operations(LoginActivity context) {
        mContext = context;
    }


    public void login() {
        mContext.requestLogin();
    }
}