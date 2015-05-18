package youdrive.today.maps;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import timber.log.Timber;
import youdrive.today.ApiError;
import youdrive.today.App;
import youdrive.today.Car;
import youdrive.today.Result;
import youdrive.today.Status;
import youdrive.today.data.network.ApiClient;
import youdrive.today.response.CarsResponse;

/**
 * Created by psuhoterin on 22.04.15.
 */
public class MapsInteractorImpl implements MapsInteractor {

    private final ApiClient mApiClient;
    private final Gson mGson;

    public MapsInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
        mGson = App.getInstance().getGson();
    }

    @Override
    public void getStatusCar(final MapsActionListener listener) {
        mApiClient.getStatusCars(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Timber.e("Exception " + Log.getStackTraceString(e));
                listener.onError();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Timber.d("URL " + response.request().urlString());
                String json = response.body().string();
                Timber.d("JSON " + json);
                CarsResponse resp = mGson.fromJson(json, CarsResponse.class);
                if (resp.isSuccess()){
                    if (resp.getCars() != null){
                        listener.onCars(resp.getCars());
                    } else if (resp.getCar() != null){
                        listener.onCar(resp.getCar());
                    }
                    listener.onStatus(Status.fromString(resp.getStatus()));
                } else {
                    handlingError(resp.getCode(), listener);
                }
            }
        });
    }

    @Override
    public void getStatusCars(double lat, double lon, final MapsActionListener listener) {
        mApiClient.getStatusCars(lat, lon, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Timber.e("Exception " + Log.getStackTraceString(e));
                listener.onError();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Timber.d("URL " + response.request().urlString());
                String json = response.body().string();
                Timber.d("JSON " + json);
                CarsResponse resp = mGson.fromJson(json, CarsResponse.class);
                if (resp.isSuccess()){
                    if (resp.getCars() != null){
                        listener.onCars(resp.getCars());
                    } else if (resp.getCar() != null){
                        listener.onCar(resp.getCar());
                    }

                    if (resp.getCheck() != null){
                        listener.onCheck(resp.getCheck());
                    }

                    if (resp.getStatus() != null){
                        listener.onStatus(Status.fromString(resp.getStatus()));
                    }

                } else {
                    handlingError(resp.getCode(), listener);
                }
            }
        });
    }

    private void handlingError(int code, MapsActionListener listener) {
        if (code == ApiError.FORBIDDEN){
            listener.onForbidden();
        } else if (code == ApiError.TARIFF_NOT_FOUND){
            listener.onTariffNotFound();
        } else {
            listener.onError();
        }
    }
}
