package youdrive.today.profile;

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
import youdrive.today.data.network.ApiClient;

public class ProfileInteractorImpl implements ProfileInteractor {

    private final ApiClient mApiClient;

    public ProfileInteractorImpl() {
        mApiClient = new ApiClient();
    }

    @Override
    public void logout(final ProfileActionListener listener) {
         mApiClient.logout(new Callback() {
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
                     if (success){
                         listener.onLogout();
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

    private void handlingError(ApiError error, ProfileActionListener listener) {
        if (error.getCode() == ApiError.SESSION_NOT_FOUND){
            listener.onSessionNotFound();
        } else if (error.getCode() == ApiError.INVALID_REQUEST){
            listener.onInvalidRequest();
        } else {
            listener.onError();
        }
    }
}
