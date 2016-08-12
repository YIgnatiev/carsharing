package youdrive.today.interceptors;


import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import youdrive.today.App;
import youdrive.today.listeners.SearchActionListener;
import youdrive.today.models.ApiError;
import youdrive.today.network.ApiClient;
import youdrive.today.response.SearchCarResponse;

public class SearchInteractorImpl implements SearchInteractor {
    private final ApiClient mApiClient;
    private SearchActionListener mListener;
    CompositeSubscription subscriptions = new CompositeSubscription();

    public SearchInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
    }

    @Override
    public void postSearchCars(double lat, double lon, int radius, SearchActionListener listener) {
        mListener = listener;
        Subscription subscription = mApiClient
                .postSearchCar(lat, lon, radius)
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onPostSearchSuccess(response, listener),
                        error -> handleNetworkError(error, listener));
        subscriptions.add(subscription);
    }

    private void onPostSearchSuccess(SearchCarResponse response, SearchActionListener listener) {
        if (response.isSuccess()) {
            listener.onSuccess(response, 1);
        } else {
            handlingError(new ApiError(response.getCode(), response.getText()), mListener);

        }
    }

    public void getSearchCar(SearchActionListener listener) {
        mListener = listener;
        Subscription subscription = mApiClient
                .getSearchCar()
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onGetSearchSuccess(response, listener),
                        error -> handleNetworkError(error, listener));
        subscriptions.add(subscription);
    }

    private void onGetSearchSuccess(SearchCarResponse response, SearchActionListener listener) {
        if (response.isSuccess()) {
            listener.onSuccess(response, 2);

        } else {
            handlingError(new ApiError(response.getCode(), response.getText()), mListener);

        }
    }


    @Override
    public void deleteSearchCars(SearchActionListener listener) {
        mListener = listener;
        Subscription subscription = mApiClient
                .deleteSearchCar()
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onDeleteSearchSuccess(response, listener),
                        error -> handleNetworkError(error, listener));
        subscriptions.add(subscription);

    }

    private void onDeleteSearchSuccess(SearchCarResponse response, SearchActionListener listener) {
        if (response.isSuccess()) {
            listener.onSuccess(response, 3);
        } else {
            handlingError(new ApiError(response.getCode(), response.getText()), mListener);

        }
    }

    private void handlingError(ApiError error, SearchActionListener listener) {
        if (error.getCode() == ApiError.ACCESS_DENIED) {
            listener.onAccessDenied(error.getText());
        } else if (error.getCode() == ApiError.COMMAND_NOT_SUPPORTED) {
            listener.onCommandNotSupported(error.getText());
        } else if (error.getCode() == ApiError.SESSION_NOT_FOUND) {
            listener.onSessionNotFound(error.getText());
        } else if (error.getText() != null) {
            listener.onUnknownError(error.getText());
        } else {
            listener.onError();
        }
    }

    private void handleNetworkError(Throwable error, SearchActionListener listener) {
        listener.onError();

    }


    public Subscription getSubscription() {
        return subscriptions;
    }
}
