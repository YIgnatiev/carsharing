package com.morfitrun.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.morfitrun.api.services.*;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Виталий on 16/03/2015.
 */
public class RestClient {

    public static final String BASE_URL = "http://104.236.246.207:8877/";
    private SignUpService mSignUpService;
    private SignInService mSignInService;
    private RaceService mRaceService;
    private GetRunnersService mGetRunnersService;
    private AddRunnerService mAddRunnerService;
    private GetUserService mGetUserService;
    private SingOutService mSingOutService;

    private static RestClient mInstance;

    private RestClient() {
        Gson gson = new GsonBuilder().create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        mSignUpService = restAdapter.create(SignUpService.class);
        mSignInService = restAdapter.create(SignInService.class);
        mRaceService = restAdapter.create(RaceService.class);
        mGetRunnersService = restAdapter.create(GetRunnersService.class);
        mAddRunnerService = restAdapter.create(AddRunnerService.class);
        mGetUserService = restAdapter.create(GetUserService.class);
        mSingOutService = restAdapter.create(SingOutService.class);
    }

    public static final RestClient getInstance() {
        if (mInstance == null) {
            mInstance = new RestClient();
        }
        return mInstance;
    }

    public final SignUpService getSignUpService() {
        return mSignUpService;
    }

    public final SignInService getSignInService() {
        return mSignInService;
    }

    public final RaceService getRaceService(){
        return mRaceService;
    }

    public final GetRunnersService getRunnersService(){
        return mGetRunnersService;
    }

    public final AddRunnerService getAddRunnerSevice(){
        return mAddRunnerService;
    }

    public final GetUserService getUserService (){return mGetUserService;}

    public final SingOutService getSingOutService(){
        return mSingOutService;
    }
}
