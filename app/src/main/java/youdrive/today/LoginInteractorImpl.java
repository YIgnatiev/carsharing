package youdrive.today;

/**
 * Created by psuhoterin on 15.04.15.
 */
public class LoginInteractorImpl implements LoginInteractor {

    @Override
    public void login(String name, String password, LoginActionListener listener) {
        listener.onSuccess();
    }
}
