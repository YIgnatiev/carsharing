package com.budivinictvo.quiz.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


import com.budivinictvo.quiz.model.*;
import com.budivinictvo.quiz.model.Error;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Администратор on 30.12.2014.
 */
public class QuizApi {

    private static String client_id = "11115456471111111111111245454545";
    private static String client_time = String.valueOf(new Date().getTime());
    private static String secret = "zxcvlAzcxzj20g2e7.x8s3sm5dLbL53sdf$F^&H";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void postRegister( String _firstName, String _lastName , final String _email, String _password , final ResponseCallback callback){
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.setConnectTimeout(5000);
        RequestParams params = new RequestParams();
        params.put("client_id" , client_id );
        params.put("client_hash", toMd5(client_id + client_time + secret) );
        params.put("client_time" , client_time);
        params.put("user_email", _email);
        params.put("user_password", _password);
        params.put("user_firstname" ,_firstName);
        params.put("user_lastname", _lastName);

        client.post(ConstantsApp.BASE_URL + ConstantsApp.REGISTER_URL , params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = null;
                Error error = null;
                Type typeUser = new TypeToken<User>(){}.getType();
                Type typeError = new TypeToken<Error>(){}.getType();
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("status");
                    switch (status){
                        case "1":
                            user = new Gson().fromJson(response.getJSONObject("data").toString(), typeUser);
                            user.setUser_email(_email);
                            callback.onSuccess(user);
                            break;
                        case "2":
                            error = new Gson().fromJson(response.getJSONObject("error").toString(),typeError);
                            callback.onFailure(error);
                    }
                }catch (JSONException e){
                    callback.onFailure("Error in Json");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callback.onFailure(responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onFailure(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onFailure(errorResponse);
            }
        });

    }

    public static void postLogin(final String _email, String _password , final ResponseCallback callback){
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.setConnectTimeout(5000);
        RequestParams params = new RequestParams();
        params.put("client_id" , client_id);
        params.put("client_hash" , toMd5(client_id + client_time + secret));
        params.put("client_time" , client_time);
        params.put("user_email" , _email);
        params.put("user_password" , _password);

        client.post(ConstantsApp.BASE_URL + ConstantsApp.LOGIN_URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = null;
                Error error = null;
                Type typeUser = new TypeToken<User>(){}.getType();
                Type typeError = new TypeToken<Error>(){}.getType();
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("status");
                    switch (status){
                        case "1":
                            user = new Gson().fromJson(response.getJSONObject("data").toString(), typeUser);
                            user.setUser_email(_email);
                            callback.onSuccess(user);
                            break;
                        case "2":
                            error = new Gson().fromJson(response.getJSONObject("error").toString(),typeError);
                            callback.onFailure(error);


                    }


                }catch (JSONException e){
                    callback.onFailure("Error in Json");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callback.onFailure(responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onFailure(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onFailure(errorResponse);
            }
        });



    }

    public static void postRecoveryLogin(String _email, final ResponseCallback callback){
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.setConnectTimeout(5000);

        RequestParams params = new RequestParams();
        params.put("client_id" , client_id);
        params.put("client_hash" , toMd5(client_id + client_time + secret));
        params.put("client_time" , client_time);
        params.put("user_email" , _email);

        client.post(ConstantsApp.BASE_URL + ConstantsApp.LOGIN_FORGOT ,params ,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = null;
                Error error = null;
                Type typeUser = new TypeToken<User>(){}.getType();
                Type typeError = new TypeToken<Error>(){}.getType();
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("status");
                    switch (status){
                        case "1":
                            user = new Gson().fromJson(response.getJSONObject("data").toString(), typeUser);
                            callback.onSuccess(user);
                            break;
                        case "2":
                            error = new Gson().fromJson(response.getJSONObject("error").toString(),typeError);
                            callback.onFailure(error);
                    }
                }catch (JSONException e){
                    callback.onFailure("Error in Json");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callback.onFailure(responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onFailure(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onFailure(errorResponse);
            }
        });
    }

    public static void postGetTests(User user , final ResponseCallback callback){
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.setConnectTimeout(5000);

        RequestParams params = new RequestParams();
        params.put("token", user.getToken());
        params.put("user_id" , user.getUser_id());
        params.put("secret", secret);
        params.put("hash" , toMd5(user.getToken() + user.getUser_id()+ secret));
        params.put("action", "get");

        client.post(ConstantsApp.BASE_URL  ,params ,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Error error = null;

                List<Question> questionList = null;
                Type typeQuestion = new TypeToken<ArrayList<Question>>(){}.getType();
                Type typeError = new TypeToken<Error>(){}.getType();
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("status");
                    switch (status){
                        case "1":
                            questionList = new Gson().fromJson(response.getJSONObject("data").getJSONArray("questions").toString(), typeQuestion);
                            callback.onSuccess(questionList);
                            break;
                        case "2":
                            error = new Gson().fromJson(response.getJSONObject("error").toString(),typeError);
                            callback.onFailure(error);
                    }

                }catch(JsonSyntaxException  | JSONException e2) {
                    callback.onFailure("Error in Json");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callback.onFailure(responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onFailure(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onFailure(errorResponse);
            }
        });
    }

    public static void postAnswers(User _user ,PostAnswer[] _answers,final ResponseCallback callback){
        if(_answers == null){
            postGetTests(_user,callback);
            return;
        }
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.setConnectTimeout(5000);
        RequestParams params = new RequestParams();
        params.put("token", _user.getToken());
        params.put("hash" ,toMd5(_user.getToken() + _user.getUser_id() + secret));
        params.put("user_id" , _user.getUser_id());
        params.put("action" , "set");

        params.put("data", new Gson().toJson(_answers));

        params.put("info" , new Gson().toJson( new String[]{ Locale.getDefault().getLanguage()  , android.os.Build.MODEL , android.os.Build.VERSION.RELEASE}));

        client.post(ConstantsApp.BASE_URL  ,params ,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Error error = null;
                List<Question> questionList = new ArrayList<>();
                Type typeQuestion = new TypeToken<ArrayList<Question>>(){}.getType();
                Type typeError = new TypeToken<Error>(){}.getType();
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("status");
                    switch (status){
                        case "1":
                            questionList = new Gson().fromJson(response.getJSONObject("data").getJSONArray("questions").toString(), typeQuestion);
                            callback.onSuccess(questionList);
                            break;
                        case "2":
                            error = new Gson().fromJson(response.getJSONObject("error").toString(),typeError);
                            callback.onFailure(error);
                    }
                }catch (JSONException e){
                    callback.onFailure("Error in Json");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callback.onFailure(responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onFailure(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onFailure(errorResponse);
            }
        });
    }

    public static void postGetStatistics(User _user ,final ResponseCallback callback) {
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.setConnectTimeout(5000);
        RequestParams params = new RequestParams();
        params.put("token", _user.getToken());
        params.put("hash", toMd5(_user.getToken() + _user.getUser_id() + secret));
        params.put("user_id", _user.getUser_id());
        params.put("action", "statistics");


        client.post(ConstantsApp.BASE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = null;
                Error error = null;
                Type typeUser = new TypeToken<User>() {
                }.getType();
                Type typeError = new TypeToken<Error>() {
                }.getType();
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("status");
                    switch (status) {
                        case "1":
                            user = new Gson().fromJson(response.getJSONObject("data").toString(), typeUser);
                            callback.onSuccess(user);
                            break;
                        case "2":
                            error = new Gson().fromJson(response.getJSONObject("error").toString(), typeError);
                            callback.onFailure(error);


                    }


                } catch (JSONException e) {
                    callback.onFailure("Error in Json");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callback.onFailure(responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onFailure(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onFailure(errorResponse);
            }
        });
    }

    public static void postGetUserInfo(User _user ,final ResponseCallback callback) {
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.setConnectTimeout(5000);
        RequestParams params = new RequestParams();
        params.put("token", _user.getToken());
        params.put("hash", toMd5(_user.getToken() + _user.getUser_id() + secret));
        params.put("user_id", _user.getUser_id());
        params.put("action", "account");


        client.post(ConstantsApp.BASE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = null;
                Error error = null;
                Type typeUser = new TypeToken<User>() {
                }.getType();
                Type typeError = new TypeToken<Error>() {
                }.getType();
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("status");
                    switch (status) {
                        case "1":
                            user = new Gson().fromJson(response.getJSONObject("data").toString(), typeUser);
                            callback.onSuccess(user);
                            break;
                        case "2":
                            error = new Gson().fromJson(response.getJSONObject("error").toString(), typeError);
                            callback.onFailure(error);


                    }


                } catch (JSONException e) {
                    callback.onFailure("Error in Json");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callback.onFailure(responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onFailure(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callback.onFailure(errorResponse);
            }
        });
    }

    public static void getImage (String url ,  final ImageLoadCallback callback){

        client.setTimeout(120000);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Bitmap bitmap =decodeBitmap(responseBody,300,300);
                callback.onLoaded(bitmap);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }

    public static String toMd5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }
        return hex.toString();
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeBitmap(byte[] responseBody, int reqWidth, int reqHeight) {

        ByteArrayInputStream stream = new ByteArrayInputStream(responseBody);

        // First decode with inJustDecodeBounds=true to check dimensions
         BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap first =  BitmapFactory.decodeStream(stream,null, options);

        // Calculate inSampleSize
        stream = new ByteArrayInputStream(responseBody);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap second = BitmapFactory.decodeStream(stream, null, options);

        return second;
    }

}
