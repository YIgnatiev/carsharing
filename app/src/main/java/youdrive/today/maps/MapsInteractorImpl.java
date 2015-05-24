package youdrive.today.maps;

import com.google.gson.Gson;

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
                    if (response.getCars() != null) {
                        listener.onCars(response.getCars());
                    } else if (response.getCar() != null) {
                        listener.onCar(response.getCar());
                    }
                    listener.onStatus(Status.fromString(response.getStatus()));
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
                CarResponse response = (CarResponse) baseResponse;
                if (response.isSuccess()) {
                    if (response.getCars() != null) {
                        listener.onCars(response.getCars());
                    } else if (response.getCar() != null) {
                        listener.onCar(response.getCar());
                    }
                    listener.onStatus(Status.fromString(response.getStatus()));
                } else {
                    handlingError(new ApiError(response.getCode(),
                            response.getText()), listener);
                }
            }
        }).subscribe();
//        mApiClient.getStatusCars(lat, lon, new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Timber.e("Exception " + Log.getStackTraceString(e));
//                listener.onError();
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                Timber.d("URL " + response.request().urlString());
//                String json = response.body().string();
//                Timber.d("JSON " + json);
//                CarResponse resp = mGson.fromJson(json, CarResponse.class);
//                if (resp.isSuccess()){
//                    if (resp.getCars() != null){
//                        listener.onCars(resp.getCars());
//                    } else if (resp.getCar() != null){
//                        listener.onCar(resp.getCar());
//                    }
//
//                    if (resp.getCheck() != null){
//                        listener.onCheck(resp.getCheck());
//                    }
//
//                    if (resp.getStatus() != null) {
//                        listener.onStatus(Status.fromString(resp.getStatus()));
//                    }
//
//                } else {
//                    handlingError(resp.getCode(), listener);
//                }
//            }
//        });
    }

    private void handlingError(ApiError error, MapsActionListener listener) {
        if (error.getCode() == ApiError.FORBIDDEN){
            listener.onForbidden();
        } else if (error.getCode() == ApiError.TARIFF_NOT_FOUND){
            listener.onTariffNotFound();
        } else {
            listener.onError();
        }
    }

    public Subscription getSubscription() {
        return subscription;
    }
}
