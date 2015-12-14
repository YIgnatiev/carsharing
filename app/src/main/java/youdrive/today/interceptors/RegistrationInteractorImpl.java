package youdrive.today.interceptors;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import youdrive.today.App;
import youdrive.today.listeners.RegistrationActionListener;
import youdrive.today.models.ApiError;
import youdrive.today.network.ApiClient;
import youdrive.today.response.BaseResponse;
import youdrive.today.response.RegionsResponse;

/**
 * Created by psuhoterin on 16.04.15.
 */
public class RegistrationInteractorImpl implements RegistrationInteractor {

    private final ApiClient mApiClient;
    private RegistrationActionListener mListener;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    public RegistrationInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
    }

    @Override
    public void getInvite(final String email, final Long phone, final String region, final boolean readyToUse, final RegistrationActionListener listener) {


        mListener = listener;
        Subscription subscription = mApiClient
                .invite(email,phone,region,readyToUse)
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessInvite,this::onFailureInvite);

        subscriptions.add(subscription);
    }

    private void onSuccessInvite(BaseResponse response){
        mListener.onInvite();
    }

    private void onFailureInvite(Throwable throwable){
        try {
            RetrofitError error = (RetrofitError) throwable;
            TypedByteArray byteArray = (TypedByteArray) error.getResponse().getBody();
            String message = new String(byteArray.getBytes());
            BaseResponse response = new Gson().fromJson(message, BaseResponse.class);
            handlingError(new ApiError(response.getCode(),
                    response.getText()), mListener);
        }catch (Exception ex){
            mListener.onError();
        }
    }

    @Override
    public void getRegions(final RegistrationActionListener listener) {
        mListener = listener;
        Subscription subscription = mApiClient
                .getRegions()
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onRegionsRespnseSuccess,this::onRegionsFailure);
        subscriptions.add(subscription);

    }

    public void onRegionsRespnseSuccess(RegionsResponse response) {

        if (response.isSuccess()){
            mListener.onRegions(response.getRegions());
        } else {
            handlingError(new ApiError(response.getCode(),
                    response.getText()), mListener);
        }
    }

    public void onRegionsFailure(Throwable e) {
        try {
            RetrofitError error = (RetrofitError) e;
            TypedByteArray byteArray = (TypedByteArray) error.getResponse().getBody();
            String message = new String(byteArray.getBytes());
            RegionsResponse response = new Gson().fromJson(message, RegionsResponse.class);
            handlingError(new ApiError(response.getCode(),
                    response.getText()), mListener);
        }catch (Exception ex){
            mListener.onError();
        }
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
        return subscriptions;
    }







}
