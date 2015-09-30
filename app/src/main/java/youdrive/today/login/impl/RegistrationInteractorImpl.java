package youdrive.today.login.impl;

import java.io.IOException;

import rx.Subscription;
import rx.subscriptions.Subscriptions;
import youdrive.today.ApiError;
import youdrive.today.App;
import youdrive.today.BaseObservable;
import youdrive.today.data.network.ApiClient;
import youdrive.today.login.RegistrationActionListener;
import youdrive.today.login.interactors.RegistrationInteractor;
import youdrive.today.response.BaseResponse;
import youdrive.today.response.RegionsResponse;

/**
 * Created by psuhoterin on 16.04.15.
 */
public class RegistrationInteractorImpl implements RegistrationInteractor {

    private final ApiClient mApiClient;
    private Subscription subscription = Subscriptions.empty();

    public RegistrationInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
    }

    @Override
    public void getInvite(final String email, final Long phone, final String region, final boolean readyToUse, final RegistrationActionListener listener) {
        subscription = BaseObservable.ApiCall(() -> {
            try {
                return mApiClient.invite(email, phone, region, readyToUse);
            } catch (IOException e) {
                e.printStackTrace();
                return new BaseResponse();
            }
        }).doOnNext(baseResponse -> {
            if (baseResponse.isSuccess()){
                listener.onInvite();
            } else {
                handlingError(new ApiError(baseResponse.getCode(),
                        baseResponse.getText()), listener);
            }
        }).subscribe();
    }

    @Override
    public void getRegions(final RegistrationActionListener listener) {
        subscription = BaseObservable.ApiCall(() -> {
            try {
                return mApiClient.getRegions();
            } catch (IOException e) {
                e.printStackTrace();
                return new RegionsResponse();
            }
        }).doOnNext(baseResponse -> {
            RegionsResponse response = (RegionsResponse) baseResponse;
            if (response.isSuccess()){
                listener.onRegions(response.getRegions());
            } else {
                handlingError(new ApiError(response.getCode(),
                        response.getText()), listener);
            }
        }).subscribe();
    }

    private void handlingError(ApiError error, RegistrationActionListener listener) {
        if (error.getCode() == ApiError.USER_ALREADY_EXISTS){
            listener.onUserAlreadyExist(error.getText());
        } else if (error.getCode() == ApiError.REGION_NOT_FOUND){
            listener.onRegionNotFound(error.getText());
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
