package youdrive.today.data.network;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import timber.log.Timber;
import youdrive.today.Command;
import youdrive.today.response.BaseResponse;
import youdrive.today.response.CarResponse;
import youdrive.today.response.CommandResponse;
import youdrive.today.response.LoginResponse;
import youdrive.today.response.PolygonResponse;
import youdrive.today.response.RegionsResponse;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class ApiClient {

    private final OkHttpClient mClient;

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

//    private static String HOST = "http://youdrive.today";
    private static  String HOST = "http://54.191.34.18";

    private final Gson mGson;

    public ApiClient() {
        mClient = new OkHttpClient();
        mGson = new Gson();
        setCookie();
    }

    private void setCookie(){
        mClient.interceptors().add(new AddCookiesInterceptor());
        mClient.interceptors().add(new ReceivedCookiesInterceptor());
    }

    public LoginResponse login(String email, String password) throws IOException {
        String url = HOST + "/session";
        String json = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";
        return mGson.fromJson(post(url, json), LoginResponse.class);
    }

    public BaseResponse logout() throws IOException {
        String url = HOST + "/session";
        return mGson.fromJson(delete(url), BaseResponse.class);
    }

    public CarResponse getStatusCars(double lat, double lon) throws IOException {
        String url;
        if(lat == 0 && lon == 0) url = HOST + "/status";
        else url = HOST + "/status?lat=" + lat + "&lon=" + lon;


        return mGson.fromJson(get(url), CarResponse.class);
    }

    public CarResponse getStatusCars() throws IOException {
        String url = HOST + "/status";
        return mGson.fromJson(get(url), CarResponse.class);
    }


    public PolygonResponse getPolygon()throws IOException {
        String url = HOST + "/info";

        String response = get(url);
        response.length();
        return mGson.fromJson(response,PolygonResponse.class);
    }

    public RegionsResponse getRegions() throws IOException {
        String url = HOST + "/regions";
        return mGson.fromJson(get(url), RegionsResponse.class);
    }

    public BaseResponse invite(String email, Long phone, String region, boolean readyToUse) throws IOException {
        String url = HOST + "/invite";
        String json = "{\"email\":\"" + email + "\", \"phone\":" + phone +", \"region_id\":\"" + region + "\", \"ready_to_use\": " + readyToUse + "}";
        return mGson.fromJson(post(url, json), BaseResponse.class);
    }

    public CarResponse booking(String id, double lat, double lon) throws IOException {
        String url = HOST + "/order";
        String json = "{\"car_id\":\"" + id + "\", \"lat\":" + lat +", \"lon\":" + lon + "}";
        return mGson.fromJson(post(url, json), CarResponse.class);
    }

    public CommandResponse command(Command command) throws IOException {
        String url = HOST + "/action";
        String json = "{\"command\": \"" + command + "\"}";
        return mGson.fromJson(post(url, json), CommandResponse.class);
    }

    public CommandResponse complete() throws IOException {
        String url = HOST + "/order";
        return mGson.fromJson(delete(url), CommandResponse.class);
    }

    public CommandResponse result(String token) throws IOException {
        String url = HOST + "/action/" + token;
        return mGson.fromJson(get(url), CommandResponse.class);
    }

    private String get(String url) throws IOException {
        Timber.tag("ApiClient").d("URL " + url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = mClient.newCall(request).execute();
        String result = response.body().string();
        Timber.tag("ApiClient").d("JSON " + result);
        return result;
    }

    private String post(String url, String json) throws IOException {
        Timber.tag("ApiClient").d("URL " + url + " BODY " + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = mClient.newCall(request).execute();
        String result = response.body().string();
        Timber.tag("ApiClient").d("JSON " + result);
        return result;
    }

    private String delete(String url) throws IOException {
        Timber.tag("ApiClient").d("URL " + url);
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        Response response = mClient.newCall(request).execute();
        String result = response.body().string();
        Timber.tag("ApiClient").d("JSON " + result);
        return result;
    }
}
