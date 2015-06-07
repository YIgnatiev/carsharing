package youdrive.today.maps;

import java.io.IOException;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import youdrive.today.ApiError;
import youdrive.today.App;
import youdrive.today.BaseObservable;
import youdrive.today.Status;
import youdrive.today.data.network.ApiClient;
import youdrive.today.login.RequestListener;
import youdrive.today.response.BaseResponse;
import youdrive.today.response.CarResponse;

public class MapsInteractorImpl implements MapsInteractor {

    private final ApiClient mApiClient;
    private Subscription subscription = Subscriptions.empty();

    public MapsInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
    }

    @Override
    public void getStatusCar(final MapsActionListener listener) {
        subscription = BaseObservable.ApiCall(new RequestListener() {
            @Override
            public BaseResponse onRequest() {
                try {
                    return mApiClient.getStatusCars();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }).doOnNext(new Action1<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {
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
            }
        }).subscribe();
    }

    @Override
    public void getStatusCars(final double lat, final double lon, final MapsActionListener listener) {
        subscription = BaseObservable.ApiCall(new RequestListener() {
            @Override
            public BaseResponse onRequest() {
                try {
                    return mApiClient.getStatusCars(lat, lon);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }).doOnNext(new Action1<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {
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
            }
        }).subscribe();
    }

    private void handlingError(ApiError error, MapsActionListener listener) {
        if (error.getCode() == ApiError.FORBIDDEN) {
            listener.onForbidden();
        } else if (error.getCode() == ApiError.TARIFF_NOT_FOUND) {
            listener.onTariffNotFound();
        } else {
            listener.onError();
        }
    }

    public Subscription getSubscription() {
        return subscription;
    }
}
