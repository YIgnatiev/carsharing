package youdrive.today.interceptors;


import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import youdrive.today.App;
import youdrive.today.listeners.SearchActionListener;
import youdrive.today.network.ApiClient;
import youdrive.today.response.SearchCarResponse;

public class SearchInteractorImpl  implements SearchInteractor  {
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
                .subscribe(response -> onGetInfoSuccess(response, listener),
                        error -> handleNetworkError(error, listener));
        subscriptions.add(subscription);
    }

    public void getSearchCar( SearchActionListener listener) {
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
            listener.onSuccess(response);
            System.out.println("ssss"+response.getText());
            System.out.println("ssss"+response.toString());

        } else {

        }
    }

    private void handleNetworkError(Throwable error, SearchActionListener listener) {
    }

    private void onGetInfoSuccess(SearchCarResponse response, SearchActionListener listener) {
        if (response.isSuccess()) {
            listener.onSuccess(response);
            System.out.println("ssss"+response.getText());
            System.out.println("ssss"+response.toString());

        } else {

        }
    }


    @Override
    public void getSearchCars(SearchActionListener listener) {

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
            System.out.println("ssss"+response.getText());
            mListener.onSuccess(response);
            subscriptions.clear();

        }
    }


    public Subscription getSubscription() {
        return subscriptions;
    }
}
