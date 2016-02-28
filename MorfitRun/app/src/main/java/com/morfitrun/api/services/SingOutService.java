package com.morfitrun.api.services;

import com.morfitrun.data_models.SignOutCallback;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by vasia on 26.03.2015.
 */
public interface SingOutService {
    @GET("/signOut")
    public void signOut(Callback<SignOutCallback> _callback);
}
