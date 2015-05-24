package youdrive.today.login.impl;

import java.io.IOException;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import youdrive.today.ApiError;
import youdrive.today.App;
import youdrive.today.User;
import youdrive.today.data.network.ApiClient;
import youdrive.today.BaseObservable;
import youdrive.today.login.LoginActionListener;
import youdrive.today.login.RequestListener;
import youdrive.today.login.interactors.LoginInteractor;
import youdrive.today.response.BaseResponse;
import youdrive.today.response.LoginResponse;

public class LoginInteractorImpl implements LoginInteractor {

    private final ApiClient mApiClient;
    private Subscription subscription = Subscriptions.empty();

    public LoginInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
    }

    @Override
    public void login(final String email, final String password, final LoginActionListener listener) {
        subscription = BaseObservable.ApiCall(new RequestListener() {
            @Override
            public BaseResponse onRequest() {
                try {
                    return mApiClient.login(email, password);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }).doOnNext(new Action1<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {
                LoginResponse response = (LoginResponse) baseResponse;
                if (response.isSuccess()) {
                    listener.onSuccess(new User(response.getSessionId(),
                            response.getName(),
                            response.getAvatar()));
                } else {
                    handlingError(new ApiError(response.getCode(),
                            response.getText()), listener);
                }

            }
        }).subscribe();
    }

    private void handlingError(ApiError error, LoginActionListener listener) {
        if (error.getCode() == ApiError.FIELD_IS_EMPTY) {
            listener.onErrorFieldEmpty(error.getText());
        } else if (error.getCode() == ApiError.USER_NOT_FOUND) {
            listener.onErrorUserNotFound(error.getText());
        } else {
            listener.onError();
        }
    }

    public Subscription getSubscription() {
        return subscription;
    }
}
