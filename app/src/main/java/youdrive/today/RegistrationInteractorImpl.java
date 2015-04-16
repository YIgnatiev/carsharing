package youdrive.today;

/**
 * Created by psuhoterin on 16.04.15.
 */
public class RegistrationInteractorImpl implements RegistrationInteractor {

    @Override
    public void getRequest(String name, String phone, String region, RegistrationActionListener listener) {
        listener.onRequest("Hello");
    }
}
