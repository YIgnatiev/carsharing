package youdrive.today.data.network;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import timber.log.Timber;
import youdrive.today.Car;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class ApiClient {

    private final OkHttpClient mClient;

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static String HOST = "http://54.191.34.18";

    public ApiClient() {
        mClient = new OkHttpClient();
        setCookie();
    }

    private void setCookie(){
        mClient.interceptors().add(new AddCookiesInterceptor());
        mClient.interceptors().add(new ReceivedCookiesInterceptor());
    }

    public void login(String email, String password, Callback callback) throws UnsupportedEncodingException {
        String url = HOST + "/session";
        String json = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";
        post(url, json, callback);
    }

    public void logout(Callback callback) {
        String url = HOST + "/session";
        delete(url, callback);
    }

    public void getStatusCars(Callback callback){
        String url = HOST + "/status";
        get(url, callback);
    }

    public void getRegions(Callback callback){
        String url = HOST + "/regions";
        get(url, callback);
    }

    public void invite(String email, String phone, String region, Callback callback) {
        String url = HOST + "/invite";
        String json = "{\"email\":\"" + email + "\", \"phone\":\"" + phone +"\", \"region_id\":\"" + region + "\", \"ready_to_use\": true}";
        post(url, json, callback);
    }

    public void order(String id, double lat, double lon, Callback callback){
        String url = HOST + "/order";
        String json = "{\"car_id\":\"" + id + "\", \"lat\":" + lat +", \"lon\":" + lon + "}";
        post(url, json, callback);
    }

    public void open(Callback callback) {
        String url = HOST + "/action";
        String json = "{\"command\": \"open\"}";
        post(url, json, callback);
    }

    private void get(String url, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .build();
        mClient.newCall(request).enqueue(callback);
    }

    private void post(String url, String json, Callback callback){
        Timber.d("URL " + url + " BODY " + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        mClient.newCall(request).enqueue(callback);
    }

    public void getResult(String token, Callback callback) {
        String url = HOST + "/action/" + token;
        get(url, callback);
    }

    private void delete(String url, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        mClient.newCall(request).enqueue(callback);
    }

    private String getEncode(String params) throws UnsupportedEncodingException {
        return URLEncoder.encode(params, "utf-8");
    }
}
