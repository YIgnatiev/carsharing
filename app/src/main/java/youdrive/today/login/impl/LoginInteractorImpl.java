package youdrive.today.login.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import timber.log.Timber;
import youdrive.today.ApiError;
import youdrive.today.App;
import youdrive.today.User;
import youdrive.today.data.network.ApiClient;
import youdrive.today.login.LoginActionListener;
import youdrive.today.login.interactors.LoginInteractor;

/**
 * Created by psuhoterin on 15.04.15.
 */
public class LoginInteractorImpl implements LoginInteractor {

    private final ApiClient mApiClient;

    public LoginInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
    }

    @Override
    public void login(String email, String password, final LoginActionListener listener) {
        try {
            mApiClient.login(email, password, new Callback() {

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
                        boolean success = new JSONObject(json).getBoolean("success");
                        if (success) {
                            listener.onSuccess(new Gson().fromJson(json, User.class));
                        } else {
                            handlingError(new Gson().fromJson(json, ApiError.class), listener);
                        }
                    } catch (JSONException e) {
                        Timber.e("Exception " + Log.getStackTraceString(e));
                        listener.onError();
                    }

                }
            });
        } catch (UnsupportedEncodingException e) {
            Timber.e("Exception " + Log.getStackTraceString(e));
            listener.onError();
        }
    }

    private void handlingError(ApiError error, LoginActionListener listener) {
        if (error.getCode() == ApiError.FIELD_IS_EMPTY) {
            listener.onErrorFieldEmpty(error.getText());
        } else if (error.getCode() == ApiError.USER_NOT_FOUND) {
            listener.onErrorUserNotFound(error.getText());
        } else {
            listener.onError();
        }
    }
}
