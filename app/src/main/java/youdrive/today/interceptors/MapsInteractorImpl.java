package youdrive.today.interceptors;

import java.io.IOException;

import rx.Subscription;
import rx.subscriptions.Subscriptions;
import youdrive.today.App;
import youdrive.today.BaseObservable;
import youdrive.today.listeners.MapsActionListener;
import youdrive.today.listeners.PolygonListener;
import youdrive.today.models.ApiError;
import youdrive.today.models.Status;
import youdrive.today.newtwork.ApiClient;
import youdrive.today.response.CarResponse;
import youdrive.today.response.PolygonResponse;

public class MapsInteractorImpl implements MapsInteractor {

    private final ApiClient mApiClient;
    private Subscription subscription = Subscriptions.empty();

    public MapsInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
    }




    @Override
    public void getStatusCar(final MapsActionListener listener) {
        subscription = BaseObservable.ApiCall(() -> {
            try {
                return mApiClient.getStatusCars();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).doOnNext(baseResponse -> {
            CarResponse response = (CarResponse) baseResponse;
            if (response.isSuccess()) {
//                    getStatusCarsInterval(listener);

                if (response.getCars() != null) {
                    listener.onCars(response.getCars());
                } else if (response.getCar() != null) {
                    listener.onCar(response.getCar());
                }

                listener.onStatus(Status.fromString(response.getStatus()));

                if (response.getCheck() != null) {
                    listener.onCheck(response.getCheck());
                }

            } else {
                handlingError(new ApiError(response.getCode(),
                        response.getText()), listener);
            }
        }).subscribe();
    }

    @Override
    public void getInfo(final PolygonListener listener) {
        subscription = BaseObservable.ApiCall(() -> {
            try {
                return mApiClient.getPolygon();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).doOnNext(baseResponse -> {
            PolygonResponse response = (PolygonResponse) baseResponse;
            if (response.isSuccess()) {
//                    getStatusCarsInterval(listener);
                listener.onPolygonSuccess(response.getArea());

            } else {
               listener.onPolygonFailed();
            }
        }).subscribe();
    }

    @Override
    public void getStatusCars(final double lat, final double lon, final MapsActionListener listener) {
        subscription = BaseObservable.ApiCall(() -> {
            try {
                return mApiClient.getStatusCars(lat, lon);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).doOnNext(baseResponse -> {
            if (baseResponse != null) {
                CarResponse response = (CarResponse) baseResponse;
                if (response.isSuccess()) {

                    if (response.getCars() != null) {
                        listener.onCars(response.getCars());
                    } else if (response.getCar() != null) {
                        listener.onCar(response.getCar());
                    }

                    listener.onStatus(Status.fromString(response.getStatus()));

                    if (response.getCheck() != null) {
                        listener.onCheck(response.getCheck());
                    }

                } else {
                    handlingError(new ApiError(response.getCode(),
                            response.getText()), listener);
                }
            }
        }).subscribe();
    }

    private void handlingError(ApiError error, MapsActionListener listener) {
        if (error.getCode() == ApiError.FORBIDDEN) {
            listener.onForbidden();
        } else if (error.getCode() == ApiError.TARIFF_NOT_FOUND) {
            listener.onTariffNotFound();
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
