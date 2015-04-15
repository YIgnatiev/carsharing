package youdrive.today;

/**
 * Created by psuhoterin on 15.04.15.
 */
public interface LoginInteractor {

    void login(String name, String password, LoginActionListener listener);

}
