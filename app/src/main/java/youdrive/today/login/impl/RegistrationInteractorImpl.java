package youdrive.today.login.impl;

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
import youdrive.today.Region;
import youdrive.today.data.network.ApiClient;
import youdrive.today.login.RegistrationActionListener;
import youdrive.today.login.interactors.RegistrationInteractor;

/**
 * Created by psuhoterin on 16.04.15.
 */
public class RegistrationInteractorImpl implements RegistrationInteractor {

    private final ApiClient mApiClient;

    public RegistrationInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
    }



    @Override
    public void getInvite(String name, String phone, String region, final RegistrationActionListener listener) {
        mApiClient.invite(name, phone, region, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Timber.e("Exception " + Log.getStackTraceString(e));
                listener.onError();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject object = new JSONObject(json);
                    boolean success = object.getBoolean("success");
                    if (success) {
                        listener.onInvite();
                    } else {
                        listener.onError();
                    }
                } catch (JSONException e) {
                    Timber.e("Exception " + Log.getStackTraceString(e));
                    listener.onError();
                }
            }
        });
    }

    @Override
    public void getRegions(final RegistrationActionListener listener) {
        mApiClient.getRegions(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Timber.e("Exception " + Log.getStackTraceString(e));
                listener.onError();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject object = new JSONObject(json);
                    boolean success = object.getBoolean("success");
                    if (success) {
                        Type listType = new TypeToken<List<Region>>() {}.getType();
                        List<Region> regions = new Gson().fromJson(object.getString("regions"), listType);
                        listener.onRegions(regions);
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

    private void handlingError(ApiError error, RegistrationActionListener listener) {
        if (error.getCode() == ApiError.UNKNOWN_ERROR){
            listener.onUnknownError();
        } else if (error.getCode() == ApiError.USER_ALREADY_EXISTS){
            listener.onUserAlreadyExist();
        } else if (error.getCode() == ApiError.REGION_NOT_FOUND){
            listener.onRegionNotFound();
        } else {
            listener.onError();
        }
    }
}
