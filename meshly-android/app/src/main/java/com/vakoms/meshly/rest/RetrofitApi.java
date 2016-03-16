package com.vakoms.meshly.rest;

import com.squareup.okhttp.OkHttpClient;
import com.vakoms.meshly.rest.calls.ChatCalls;
import com.vakoms.meshly.rest.calls.EventsCalls;
import com.vakoms.meshly.rest.calls.MeshlyCalls;
import com.vakoms.meshly.rest.calls.UserCalls;
import com.vakoms.meshly.rest.calls.WallCalls;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

import static com.vakoms.meshly.constants.Constants.API_URL;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 7/11/15.
 */
public class RetrofitApi {

    private static RetrofitApi INSTANCE;

    private static RestAdapter mAdapter;

    private static RestAdapter mAdapterWithoutTokenCheck;

    private EventsCalls eventsCalls;

    private WallCalls wallCalls;

    private UserCalls userCalls;

    private ChatCalls chatCalls;
    private MeshlyCalls meshlyCalls;

    public static RetrofitApi getInstance() {
        if (INSTANCE == null) INSTANCE = new RetrofitApi();
        return INSTANCE;
    }


    private RetrofitApi() {

        mAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setClient(new OkClient(new OkHttpClient()))
                .setRequestInterceptor(new TokenInterceptor())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();


        mAdapterWithoutTokenCheck = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    public static RestAdapter getDynamicAdapter(String url) {

        return new RestAdapter.Builder()
                .setEndpoint(url)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    public UserCalls user() {
        if (userCalls == null)
            userCalls = new UserCalls(mAdapter);

        return userCalls;
    }

    public EventsCalls events() {
        if (eventsCalls == null)
            eventsCalls = new EventsCalls(mAdapter);
        return eventsCalls;
    }

    public WallCalls wall() {
        if (wallCalls == null)
            wallCalls = new WallCalls(mAdapter);
        return wallCalls;
    }


    public ChatCalls chat() {
        if (chatCalls == null)
            chatCalls = new ChatCalls(mAdapter);
        return chatCalls;
    }


    public MeshlyCalls meshly() {
        if (meshlyCalls == null)
            meshlyCalls = new MeshlyCalls(mAdapterWithoutTokenCheck);

        return meshlyCalls;
    }


}
