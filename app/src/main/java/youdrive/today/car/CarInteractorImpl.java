package youdrive.today.car;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import timber.log.Timber;
import youdrive.today.ApiError;
import youdrive.today.App;
import youdrive.today.Car;
import youdrive.today.Command;
import youdrive.today.Result;
import youdrive.today.data.network.ApiClient;

/**
 * Created by psuhoterin on 26.04.15.
 */
public class CarInteractorImpl implements CarInteractor {

    private final ApiClient mApiClient;
    private final Gson mGson;

    public CarInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
        mGson = App.getInstance().getGson();
    }

    @Override
    public void order(String id, double lat, double lon, final CarActionListener listener) {
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

    @Override
    public void command(final Command command, final CarActionListener listener) {
        mApiClient.command(command, new Callback() {
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
                        String token = object.getString("result_token");
                        listener.onToken(command, token);
                        result(command, token, listener);
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
    public void complete(final Command command, final CarActionListener listener) {
        mApiClient.complete(new Callback() {
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
                        String token = object.getString("result_token");
                        listener.onToken(command, token);
                        result(command, token, listener);
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
    public void result(final Command command, String token, final CarActionListener listener) {
        mApiClient.getResult(token, new Callback() {
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
                        String result = object.getString("status");
                        if (Result.fromString(result).equals(Result.NEW)
                                || Result.fromString(result).equals(Result.PROCESSING)) {
                            listener.onPleaseWait();
                        } else if (Result.fromString(result).equals(Result.ERROR)) {
                            listener.onErrorOpen();
                        } else {
                            if (command.equals(Command.OPEN)) {
                                listener.onOpen();
                            } else if (command.equals(Command.CLOSE)){
                                listener.onClose();
                            } else {
                                listener.onComplete();
                            }
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

    private void handlingError(ApiError error, CarActionListener listener) {
        if (error.getCode() == ApiError.CAR_NOT_FOUND){
            listener.onCarNotFound();
        } else if (error.getCode() == ApiError.NOT_INFO){
            listener.onNotInfo();
        } else if(error.getCode() == ApiError.NOT_ORDER){
            listener.onNotOrder();
        } else if(error.getCode() == ApiError.ACCESS_DENIED){
            listener.onAccessDenied();
        } else if(error.getCode() == ApiError.COMMAND_NOT_SUPPORTED){
            listener.onCommandNotSupported();
        } else if(error.getCode() == ApiError.TOKEN_NOT_FOUND){
            listener.onTokenNotFound();
        }  else if(error.getCode() == ApiError.INTERNAL_ERROR){
            listener.onInternalError();
        }{
            listener.onError();
        }
    }

}
