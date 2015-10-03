package youdrive.today.interceptors;

import java.io.IOException;

import rx.Subscription;
import rx.subscriptions.Subscriptions;
import youdrive.today.models.ApiError;
import youdrive.today.App;
import youdrive.today.BaseObservable;
import youdrive.today.models.User;
import youdrive.today.newtwork.ApiClient;
import youdrive.today.listeners.LoginActionListener;
import youdrive.today.response.LoginResponse;

public class LoginInteractorImpl implements LoginInteractor {

    private final ApiClient mApiClient;
    private Subscription subscription = Subscriptions.empty();

    public LoginInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
    }

    @Override
    public void login(final String email, final String password, final LoginActionListener listener) {
        subscription = BaseObservable.ApiCall(() -> {
            try {
                return mApiClient.login(email, password);
            } catch (IOException e) {
                e.printStackTrace();
                return new LoginResponse();
            }
        }).doOnNext(baseResponse -> {
            LoginResponse response = (LoginResponse) baseResponse;
            if (response.isSuccess()) {
                listener.onSuccess(new User(
                        response.getSessionId(),
                        response.getName(),
                        response.getAvatar()));
            } else {
                handlingError(new ApiError(response.getCode(),
                        response.getText()), listener);
            }

        }).subscribe();
    }

    private void handlingError(ApiError error, LoginActionListener listener) {
        if (error.getCode() == ApiError.FIELD_IS_EMPTY) {
            listener.onErrorFieldEmpty(error.getText());
        } else if (error.getCode() == ApiError.USER_NOT_FOUND) {
            listener.onErrorUserNotFound(error.getText());
        } else if (error.getText() != null){
            listener.onUnknownError(error.getText());
        } else {
            listener.onError();
        }
    }

    public Subscription getSubscription() {
        return subscription;
    }
}
