package youdrive.today;

/**
 * Created by psuhoterin on 16.04.15.
 */
public interface RegistrationInteractor {

    void getRequest(String name, String phone, String region, RegistrationActionListener listener);

}
