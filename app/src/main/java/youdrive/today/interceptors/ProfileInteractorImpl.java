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
import youdrive.today.listeners.ProfileActionListener;
import youdrive.today.models.ApiError;
import youdrive.today.network.ApiClient;
import youdrive.today.response.BaseResponse;

public class ProfileInteractorImpl implements ProfileInteractor, Observer<BaseResponse> {

    private final ApiClient mApiClient;
    private ProfileActionListener mListener;
    private Subscription subscription = Subscriptions.empty();

    public ProfileInteractorImpl() {
        mApiClient = new ApiClient();
    }

    @Override
    public void logout(final ProfileActionListener listener) {
        mListener = listener;
        subscription = mApiClient
                .logout()
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);

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
            BaseResponse response = new Gson().fromJson(message, BaseResponse.class);
            handlingError(new ApiError(response.getCode(),
                    response.getText()), mListener);
        } catch (Exception ex) {
            mListener.onError();
        }
    }

    @Override
    public void onNext(BaseResponse response) {
        if (response.isSuccess()) {
            mListener.onLogout();
        } else {
            handlingError(new ApiError(response.getCode(), response.getText()),
                    mListener);
        }
    }

    private void handlingError(ApiError error, ProfileActionListener listener) {
        if (error.getCode() == ApiError.SESSION_NOT_FOUND) {
            listener.onSessionNotFound();
        } else if (error.getCode() == ApiError.INVALID_REQUEST) {
            listener.onInvalidRequest();
        } else if (error.getCode()== 101) {
            listener.onLogout();
        } else {
            listener.onError();
        }
    }


    public Subscription getSubscription() {
        return subscription;
    }
}
