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
import youdrive.today.Status;
import youdrive.today.data.network.ApiClient;

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
    public void getStatusCars(final MapsActionListener listener) {
        mApiClient.getStatusCars(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Timber.e("Exception " + Log.getStackTraceString(e));
                listener.onError();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String json = response.body().string();
                Timber.d("JSON " + json);
                try {
                    JSONObject object = new JSONObject(json);
                    boolean success = object.getBoolean("success");
                    if (success){
                        if (object.has("cars")){
                            Type listType = new TypeToken<List<Car>>(){}.getType();
                            List<Car> cars = mGson.fromJson(object.getString("cars"), listType);
                            listener.onCars(cars);
                        } else if (object.has("car")){
                            Car car = mGson.fromJson(object.getString("car"), Car.class);
                            if (object.has("status")){
                                car.setStatus(Status.fromString(object.getString("status")));
                            }
                            listener.onCar(car);
                        }

                    } else {
                        handlingError(new Gson().fromJson(json, ApiError.class), listener);
                    }
                } catch (JSONException e) {
                    Timber.e("Exception " + Log.getStackTraceString(e));
                    listener.onError();
                }
            }
        });
    }

    @Override
    public void order(String id, double lat, double lon, final MapsActionListener listener) {
        mApiClient.order(id, lat, lon, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Timber.e("Exception " + Log.getStackTraceString(e));
                listener.onError();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String json = response.body().string();
                Timber.d("JSON " + json);
                try {
                    JSONObject object = new JSONObject(json);
                    boolean success = object.getBoolean("success");
                    if (success) {
                        Car car = mGson.fromJson(object.getString("car"), Car.class);
                        listener.onOrder(car);
                    } else {
                        handlingError(new Gson().fromJson(json, ApiError.class), listener);
                    }
                } catch (JSONException e) {
                    Timber.e("Exception " + Log.getStackTraceString(e));
                    listener.onError();
                }
            }
        });
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
}
