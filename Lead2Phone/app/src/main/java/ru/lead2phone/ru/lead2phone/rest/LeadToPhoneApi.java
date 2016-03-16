package ru.lead2phone.ru.lead2phone.rest;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import ru.lead2phone.ru.lead2phone.models.Caller;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 7/11/15.
 */
public class LeadToPhoneApi {

    private static LeadToPhoneApi INSTANCE;

    private static LeadToPhoneLoginService mLoginService;
    private static LeadToPhoneService mService;
    private Context mContext;

    public static LeadToPhoneApi getInstance(Context _context) {
        if (INSTANCE == null) INSTANCE = new LeadToPhoneApi(_context);
        return INSTANCE;
    }

    public LeadToPhoneApi(Context _context) {
        this.mContext = _context;

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();


       mService = new RestAdapter.Builder()
                .setEndpoint(Constants.BASE_URL)
                .setRequestInterceptor(new RequestHeaders())
                .setClient(new OkClient(new OkHttpClient()))
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build().create(LeadToPhoneService.class);



        mLoginService  = new RestAdapter.Builder()
                .setEndpoint(Constants.BASE_URL)
                .setRequestInterceptor(new RequestHeaders())
                .setConverter(new StringConverter())
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(LeadToPhoneLoginService.class);


    }

    public void login(String login,String password, final Callback<String> callback) {
        mLoginService.login(login,password,callback);

    }

     public void getList(String login,String password, final Callback<List<Caller>> callback) {
        mService.getList(login, password, 0, callback);
    }

    public void getLaterList(String login,String password, final Callback<List<Caller>> callback) {
        mService.getList(login, password, 2,callback);
    }

    public void deleteUser(String login, String password, String callerId,final Callback<String> callback){
        mLoginService.deleteUser(login, password, callerId, callback);

    }
    public void callLater(String login, String password, String callerId,final Callback<String> callback) {
        mLoginService.callLater(login, password, callerId, callback);
    }
    public void about(final Callback<String> callback) {
        mLoginService.about( callback);

    }
}
