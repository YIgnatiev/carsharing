package youdrive.today;

/**
 * Created by psuhoterin on 16.04.15.
 */
public interface RegistrationInteractor {

    void invite(String name, String phone, String region, RegistrationActionListener listener);

}
