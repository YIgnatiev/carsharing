package youdrive.today.interceptors;

import java.io.IOException;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import youdrive.today.models.ApiError;
import youdrive.today.BaseObservable;
import youdrive.today.listeners.ProfileActionListener;
import youdrive.today.newtwork.ApiClient;
import youdrive.today.listeners.RequestListener;
import youdrive.today.response.BaseResponse;

public class ProfileInteractorImpl implements ProfileInteractor {

    private final ApiClient mApiClient;
    private Subscription subscription = Subscriptions.empty();

    public ProfileInteractorImpl() {
        mApiClient = new ApiClient();
    }

    @Override
    public void logout(final ProfileActionListener listener) {
        subscription = BaseObservable.ApiCall(new RequestListener() {
            @Override
            public BaseResponse onRequest() {
                try {
                    return mApiClient.logout();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }).doOnNext(new Action1<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {
                if (baseResponse.isSuccess()) {
                    listener.onLogout();
                } else {
                    handlingError(new ApiError(baseResponse.getCode(), baseResponse.getText()),
                            listener);
                }
            }
        }).subscribe();
    }

    private void handlingError(ApiError error, ProfileActionListener listener) {
        if (error.getCode() == ApiError.SESSION_NOT_FOUND){
            listener.onSessionNotFound();
        } else if (error.getCode() == ApiError.INVALID_REQUEST){
            listener.onInvalidRequest();
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
