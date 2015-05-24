package youdrive.today.data.network;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;

import timber.log.Timber;
import youdrive.today.App;
import youdrive.today.Command;
import youdrive.today.response.BaseResponse;
import youdrive.today.response.CarResponse;
import youdrive.today.response.LoginResponse;
import youdrive.today.response.RegionsResponse;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class ApiClient {

    private final OkHttpClient mClient;

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static String HOST = "http://54.191.34.18";
    private final Gson mGson;

    public ApiClient() {
        mClient = new OkHttpClient();
        mGson = new Gson();
        setCookie();
    }

    private void setCookie(){
        mClient.setCookieHandler(new CookieManager(
                new PersistentCookieStore(App.getInstance()),
                CookiePolicy.ACCEPT_ALL));
    }

    public LoginResponse login(String email, String password) throws IOException {
        String url = HOST + "/session";
        String json = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";
        return mGson.fromJson(post(url, json), LoginResponse.class);
    }

    public void logout(Callback callback) {
        String url = HOST + "/session";
        delete(url, callback);
    }

    public CarResponse getStatusCars(double lat, double lon) throws IOException {
        String url = HOST + "/status?lat=" + lat + "&lon=" + lon;
        return mGson.fromJson(get(url), CarResponse.class);
    }

    public CarResponse getStatusCars() throws IOException {
        String url = HOST + "/status";
        return mGson.fromJson(get(url), CarResponse.class);
    }

    public RegionsResponse getRegions() throws IOException {
        String url = HOST + "/regions";
        return mGson.fromJson(get(url), RegionsResponse.class);
    }

    public BaseResponse invite(String email, String phone, String region) throws IOException {
        String url = HOST + "/invite";
        String json = "{\"email\":\"" + email + "\", \"phone\":\"" + phone +"\", \"region_id\":\"" + region + "\", \"ready_to_use\": true}";
        return mGson.fromJson(post(url, json), BaseResponse.class);
    }

    public String getRequest(String email, String phone, String region){
        return "{\"email\":\"" + email + "\", \"phone\":\"" + phone +"\", \"region_id\":\"" + region + "\", \"ready_to_use\": true}";
    }

    public void order(String id, double lat, double lon, Callback callback){
        String url = HOST + "/order";
        String json = "{\"car_id\":\"" + id + "\", \"lat\":" + lat +", \"lon\":" + lon + "}";
        post(url, json, callback);
    }

    public void command(Command command, Callback callback) {
        String url = HOST + "/action";
        String json = "{\"command\": \"" + command + "\"}";
        post(url, json, callback);
    }

    public void complete(Callback callback){
        String url = HOST + "/order";
        delete(url, callback);
    }

    public void result(String token, Callback callback) {
        String url = HOST + "/action/" + token;
        get(url, callback);
    }

    private void get(String url, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .build();
        mClient.newCall(request).enqueue(callback);
    }

    private String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

    private String post(String url, String json) throws IOException {
        Timber.d("URL " + url + " BODY " + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
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
