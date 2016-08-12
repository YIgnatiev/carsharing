package youdrive.today.interceptors;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import youdrive.today.App;
import youdrive.today.listeners.LoginActionListener;
import youdrive.today.models.ApiError;
import youdrive.today.models.User;
import youdrive.today.network.ApiClient;
import youdrive.today.response.LoginResponse;

public class LoginInteractorImpl implements LoginInteractor, Observer<LoginResponse> {

    private final ApiClient mApiClient;
    private LoginActionListener mListener;
    private Subscription subscription = Subscriptions.empty();

    public LoginInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
    }

    @Override
    public void login(final String email, final String password, final LoginActionListener listener) {
        mListener = listener;
        subscription = mApiClient
                .login(email, password)
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }


    public Subscription getSubscription() {
        return subscription;
    }

    @Override
    public void onCompleted() {
        subscription.unsubscribe();
    }

    @Override
    public void onError(Throwable e) {
        try {
            RetrofitError error = (RetrofitError) e;
            TypedByteArray byteArray = (TypedByteArray) error.getResponse().getBody();
            String message = new String(byteArray.getBytes());
            LoginResponse response = new Gson().fromJson(message, LoginResponse.class);
            handlingError(new ApiError(response.getCode(),
                    response.getText()), mListener);
        } catch (Exception ex) {
            mListener.onError();
        }
    }

    @Override
    public void onNext(LoginResponse response) {

        if (response.isSuccess()) {
            mListener.onSuccess(new User(
                    response.getSessionId(),
                    response.getName(),
                    response.getAvatar()));
        } else {
            handlingError(new ApiError(response.getCode(),
                    response.getText()), mListener);
        }
    }


    private void handlingError(ApiError error, LoginActionListener listener) {

        switch (error.getCode()) {
            case ApiError.FIELD_IS_EMPTY:
                listener.onErrorFieldEmpty(error.getText());
                break;
            case ApiError.USER_NOT_FOUND:
                listener.onErrorUserNotFound(error.getText());
                break;
            default:

                if (error.getText() != null) {
                    listener.onUnknownError(error.getText());
                } else {
                    listener.onError();
                }
        }

    }


}
