package youdrive.today.interceptors;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import youdrive.today.App;
import youdrive.today.listeners.MapsActionListener;
import youdrive.today.listeners.PolygonListener;
import youdrive.today.listeners.ValueFunction;
import youdrive.today.models.ApiError;
import youdrive.today.models.ReferralRules;
import youdrive.today.models.Status;
import youdrive.today.network.ApiClient;
import youdrive.today.response.CarResponse;
import youdrive.today.response.PayoffResponse;
import youdrive.today.response.PolygonResponse;
import youdrive.today.response.UserProfileResponse;

public class MapsInteractorImpl implements MapsInteractor {

    private final ApiClient mApiClient;
    CompositeSubscription subscriptions = new CompositeSubscription();

    public MapsInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
    }

    @Override
    public void getStatusCar(final MapsActionListener listener) {


        Subscription subscription = mApiClient
                .getStatusCars()
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onStatusCarSuccess(response, listener),
                        error -> handleNetworkError(error, listener));
        subscriptions.add(subscription);


    }

    private void onStatusCarSuccess(CarResponse response, MapsActionListener listener) {

        if (response.isSuccess()) {

            listener.onStatus(Status.fromString(response.getStatus()));

            if (response.getCars() != null) listener.onCars(response.getCars());
            else if (response.getCar() != null) listener.onCar(response.getCar());


            if (response.getCheck() != null) listener.onCheck(response.getCheck());

        } else {
            handlingError(new ApiError(response.getCode(),
                    response.getText()), listener);
        }
    }

    @Override
    public void getInfo(final PolygonListener listener) {
        Subscription subscription = mApiClient
                .getPolygon()
                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onGetInfoSuccess(response, listener),
                        error -> handleNetworkError(error, listener));
        subscriptions.add(subscription);

    }

    private void onGetInfoSuccess(PolygonResponse response, PolygonListener listener) {
        if (response.isSuccess()) {
            listener.onPolygonSuccess(response);
        } else {
            listener.onPolygonFailed();
        }
    }

    @Override
    public void getUserProfile(final MapsActionListener listener) {
        subscriptions.add(mApiClient
                .getUserProfile()
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onGetUserProfile(response, listener),
                        error -> handleNetworkError(error, listener)));
    }

    private void onGetUserProfile(UserProfileResponse response, MapsActionListener listener) {
        if (response.isSuccess()) {
            listener.onUserProfileSuccess(response);
        } else {
            handlingError(new ApiError(response.getCode(), response.getText()), listener);
        }
    }

    @Override
    public void payoff(final MapsActionListener listener) {
        subscriptions.add(mApiClient
                .payoff()
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onPayoff(response, listener),
                        error -> handleNetworkError(error, listener)));
    }

    private void onPayoff(PayoffResponse response, MapsActionListener listener) {
        if (response.isSuccess()) {
            listener.onPayoffSuccess(response);
        } else {
            handlingError(new ApiError(response.getCode(), response.getText()), listener);
        }
    }


    @Override
    public void getStatusCars(final double lat, final double lon, final MapsActionListener listener) {
        Subscription subscription = mApiClient
                .getStatusCars(lat, lon)
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onCarResponseSuccess(response, listener),
                        error -> handleNetworkError(error, listener));
        subscriptions.add(subscription);

    }

    public void getInfo(ValueFunction<PolygonResponse> successFunc, ValueFunction<Throwable> errorFunc, CompositeSubscription subscriptions) {
        Subscription subscription = mApiClient
                .getPolygon()
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(successFunc::apply, errorFunc::apply);
        subscriptions.add(subscription);

    }

    public void getReferralData(ValueFunction<ReferralRules> successFunc, ValueFunction<Throwable> errorFunc) {
        Subscription subscription = mApiClient.getReferralRules()
                .subscribe(successFunc::apply, errorFunc::apply);
        subscriptions.add(subscription);

    }

    private void onCarResponseSuccess(CarResponse response, MapsActionListener listener) {
        if (response.isSuccess()) {

            if (response.getCars() != null) listener.onCars(response.getCars());
            else if (response.getCar() != null) listener.onCar(response.getCar());

            listener.onStatus(Status.fromString(response.getStatus()));

            if (response.getCheck() != null) listener.onCheck(response.getCheck());

        } else {
            handlingError(new ApiError(response.getCode(), response.getText()), listener);
        }

    }

    private void handleNetworkError(Throwable error, PolygonListener listener) {
        listener.onPolygonFailed();
    }

    private void handleNetworkError(Throwable error, MapsActionListener listener) {
        listener.onError();
    }

    private void handlingError(ApiError error, MapsActionListener listener) {
        if (error.getCode() == ApiError.FORBIDDEN) {
            listener.onForbidden();
        } else if (error.getCode() == ApiError.TARIFF_NOT_FOUND) {
            listener.onTariffNotFound();
        } else if (error.getText() != null) {
            listener.onUnknownError(error.getText());
        } else {
            listener.onError();
        }
    }

    public Subscription getSubscription() {
        return subscriptions;
    }

    public void clearSubscriptions() {
        subscriptions.unsubscribe();
        subscriptions = new CompositeSubscription();
    }
}
