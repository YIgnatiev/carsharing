package com.carusselgroup.dwt.rest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.carusselgroup.dwt.global.ConstantsApp;
import com.carusselgroup.dwt.model.Car;
import com.carusselgroup.dwt.model.ImageCar;
import com.carusselgroup.dwt.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CarsNewApi {
    private static HttpClient client = new DefaultHttpClient();
    private static AsyncHttpClient asyncHttpClient= new AsyncHttpClient();
    private static String RESULT_SUCCESS = "RESULT_SUCCESS";
    private static String RESULT_FAILURE = "RESULT_FAILURE";

//    public static void postLogin(String user_name, String user_password, final IResponse callback) {
//        asyncHttpClient.addHeader("Content-Type", "application/x-www-form-urlencoded");
//// client.addHeader("Content-Type", "application/json");
//        asyncHttpClient.setConnectTimeout(5000);
//        RequestParams params = new RequestParams();
//        params.put("j_username", user_name);
//        params.put("j_password", user_password);
//        params.put("submit", "Login");
//        asyncHttpClient.post(ConstantsApp.BASE_URL + ConstantsApp.LOGIN_URL,
//                params,
//                new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                        String str ="";
//                        try {
//                            str = new String(bytes, "UTF-8");
//                        } catch (Exception e) {
//                            Log.d("cs_c", "Exception ");
//                            callback.onFailure("Exception");
//                            return;
//                        }
//                        Log.d("cs_c", "onSuccess " + str);
//                        callback.onSuccess(str);
//                    }
//                    @Override
//                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                        String str = "";
//                        try {
//                            str = new String(bytes, "UTF-8");
//                        } catch (Exception e) {
//                            Log.d("cs_c", "Exception ");
//                        }
//                        if (i==404 || i == 0){
//                            callback.onSuccess(str);
//                            return;
//                        }
//                        callback.onFailure(str);
//                        Log.d("cs_c", "onFailure " + str);
//                    }
//                });
//    }


    public static void postLogin(final String user_name, final String user_password, final IResponse callback) {
        new AsyncTask<Void, Void, List<Object>>() {
            @Override
            protected List<Object> doInBackground(Void... params) {
                List<Object> result = new ArrayList<Object>();
                try {
                    HttpPost httppost = new HttpPost(ConstantsApp.BASE_URL + ConstantsApp.LOGIN_URL);
                    httppost.setHeader("Content-Type",
                            "application/x-www-form-urlencoded;charset=UTF-8");
                    // Add your data
                    List<NameValuePair> paramsLogin = new ArrayList<NameValuePair>();
                    paramsLogin.add(new BasicNameValuePair("j_username", user_name));
                    paramsLogin.add(new BasicNameValuePair("j_password", user_password));
                    paramsLogin.add(new BasicNameValuePair("submit", "Login"));
                    httppost.setEntity(new UrlEncodedFormEntity(paramsLogin, "UTF-8"));
                    HttpResponse responseLogin = client.execute(httppost);
                    String responseBody = getBody(responseLogin);
                    Log.d("cs_c", responseBody);
                    if (responseLogin.getStatusLine().getStatusCode() == 200) {
                        User user = new Gson().fromJson(responseBody, User.class);
                        result.add(RESULT_SUCCESS);
                        result.add(user);
                    } else {
                        result.add(RESULT_FAILURE);
                        result.add(responseBody);
                    }
                } catch (Exception e) {
                    Log.d("cs_c", "Exception  Login");
                    result.add(RESULT_FAILURE);
                    if (e == null)
                        result.add(e.getMessage());
                    else
                        result.add("Exception Login ");
                }

                return result;
            }

            @Override
            protected void onPostExecute(List<Object> result) {
                if (result != null && result.size() > 0) {
                    if (result.get(0).equals(RESULT_SUCCESS)) {
                        callback.onSuccess(result.get(1));
                        return;
                    }

                }
                callback.onFailure(result.get(1));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public static void uploadPhoto(final int carId, int photoId, final int imgIdx, final boolean defaultImage, final String filePath, final IResponse callback) throws FileNotFoundException {

        new AsyncTask<Void, Void, List<Object>>() {
            @Override
            protected List<Object> doInBackground(Void... params) {
                List<Object> result = new ArrayList<Object>();
                try {
                    HttpPost post = new HttpPost(ConstantsApp.BASE_URL + ConstantsApp.CAR_UPLOAD_PHOTO_URL_POST);
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                    FileBody fb = new FileBody(new File(filePath));

                    builder.addPart("file", fb);
                    builder.addTextBody("vehicleId", carId + "");
                    builder.addTextBody("imgIdx", imgIdx + "");
                    builder.addTextBody("defaultImage", defaultImage ? "true" : "false");
                    HttpEntity yourEntity = builder.build();
                    post.setEntity(yourEntity);
                    HttpResponse response = client.execute(post);
                    String responseBody = getBody(response);
                    Log.d("cs_c", responseBody);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        ImageCar images = new Gson().fromJson(responseBody, ImageCar.class);
                        result.add(RESULT_SUCCESS);
                        result.add(images);
                    } else {
                        result.add(RESULT_FAILURE);
                        result.add(responseBody);
                    }
                } catch (Exception e) {
                    Log.d("cs_c", "Exception  Upload Photo");
                    result.add(RESULT_FAILURE);
                    if (e == null)
                        result.add(e.getMessage());
                    else
                        result.add("Exception Upload Photo");
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Object> result) {
                if (result != null && result.size() > 0) {
                    if (result.get(0).equals(RESULT_SUCCESS))
                        callback.onSuccess(result.get(1));
                    return;
                }
                callback.onFailure(result.get(1));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public static void getCarsList(final int roindex, final int pageSize, final String paramsUrl, final IResponse callback) {

        new AsyncTask<Void, Void, List<Object>>() {
            @Override
            protected List<Object> doInBackground(Void... params) {
                List<Object> result = new ArrayList<Object>();
                try {
                    String url = ConstantsApp.BASE_URL + ConstantsApp.CARS_LIST_URL
                            + "rowIndex=" + roindex + "&pageSize=" + pageSize
                            + paramsUrl;
                    HttpGet get = new HttpGet(url);
                    get.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(get);
                    String responseBody = getBody(response);
                    Log.d("cs_c", responseBody);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        List<Car> cars = new ArrayList<Car>();
                        Type listType = new TypeToken<ArrayList<Car>>() {
                        }.getType();

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.setLongSerializationPolicy( LongSerializationPolicy.STRING );
                        Gson gson = gsonBuilder.create();
                        cars = gson.fromJson(responseBody, listType);
                        result.add(RESULT_SUCCESS);
                        result.add(cars);
                    } else {
                        result.add(RESULT_FAILURE);
                        result.add(responseBody);
                    }
                } catch (Exception e) {
                    Log.d("cs_c", "Exception get Cars List");
                    result.add(RESULT_FAILURE);
                    if (e == null)
                        result.add(e.getMessage());
                    else
                        result.add("Exception Upload Photo");
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Object> result) {
                if (result != null && result.size() > 0) {
                    if (result.get(0).equals(RESULT_SUCCESS)) {
                        callback.onSuccess(result.get(1));
                        return;
                    }
                   callback.onFailure(result.get(1));
                }
                else
                    callback.onFailure("Unexpected error");

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void getCarDetail(final int car_id, final IResponse callback) {
        new AsyncTask<Void, Void, List<Object>>() {
            @Override
            protected List<Object> doInBackground(Void... params) {
                List<Object> result = new ArrayList<Object>();
                try {
                    String url = ConstantsApp.BASE_URL + ConstantsApp.CAR_DETAIL_URL + car_id;
                    HttpGet get = new HttpGet(url);
                    get.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(get);
                    String responseBody = getBody(response);
                    Log.d("cs_c", responseBody);
                    if (response.getStatusLine().getStatusCode() == 200) {


                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.setLongSerializationPolicy( LongSerializationPolicy.STRING );
                        Gson gson = gsonBuilder.create();

                        Car detailCar = gson.fromJson(responseBody, Car.class);
                        result.add(RESULT_SUCCESS);
                        result.add(detailCar);
                    } else {
                        result.add(RESULT_FAILURE);
                        result.add(responseBody);
                    }
                } catch (Exception e) {
                    Log.d("cs_c", "Exception get Detail Car");
                    result.add(RESULT_FAILURE);
                    if (e == null)
                        result.add(e.getMessage());
                    else
                        result.add("Exception get Detail Car");
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Object> result) {
                if (result != null && result.size() > 0) {
                    if (result.get(0).equals(RESULT_SUCCESS))
                        callback.onSuccess(result.get(1));
                    return;
                }
                callback.onFailure(result.get(1));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void deleteCarImage(final int car_id, final int imageId, final IResponse callback) {
        new AsyncTask<Void, Void, List<Object>>() {
            @Override
            protected List<Object> doInBackground(Void... params) {
                List<Object> result = new ArrayList<Object>();
                try {
                    String url = ConstantsApp.BASE_URL + ConstantsApp.CAR_DELETE_PHOTO_URL_POST
                            + "?vehicleId=" + car_id
                            + "&imageId=" + imageId;
                    HttpDelete delete = new HttpDelete(url);
                    delete.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(delete);
                    String responseBody = getBody(response);
                    Log.d("cs_c", responseBody);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        result.add(RESULT_SUCCESS);
                        result.add(responseBody);
                    } else {
                        result.add(RESULT_FAILURE);
                        result.add(responseBody);
                    }
                } catch (Exception e) {
                    Log.d("cs_c", "Exception delete image Car");
                    result.add(RESULT_FAILURE);
                    if (e == null)
                        result.add(e.getMessage());
                    else
                        result.add("Exception delete image Car");
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Object> result) {
                if (result != null && result.size() > 0) {
                    if (result.get(0).equals(RESULT_SUCCESS))
                        callback.onSuccess(result.get(1));
                    return;
                }
                callback.onFailure(result.get(1));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void reOrderCar(final int car_id, final int imageId, final int newIdx, final IResponse callback) {
        new AsyncTask<Void, Void, List<Object>>() {
            @Override
            protected List<Object> doInBackground(Void... params) {
                List<Object> result = new ArrayList<Object>();
                try {
                    Log.d("cs_c", "car_id: " + car_id + " imageId : " + imageId + " newIdx : " + newIdx);
                    String url = ConstantsApp.BASE_URL + ConstantsApp.CAR_REORDER_PHOTO_URL_GET
                            + "?vehicleId=" + car_id
                            + "&imageId=" + imageId
                            + "&newIdx=" + newIdx;
                    HttpGet get = new HttpGet(url);
                    get.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(get);
                    String responseBody = getBody(response);
                    Log.d("cs_c", responseBody);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        result.add(RESULT_SUCCESS);
                        result.add(responseBody);
                    } else {
                        result.add(RESULT_FAILURE);
                        result.add(responseBody);
                    }
                } catch (Exception e) {
                    Log.d("cs_c", "Exception reOrder Car");
                    result.add(RESULT_FAILURE);
                    if (e == null)
                        result.add(e.getMessage());
                    else
                        result.add("Exception reOrder Car");
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Object> result) {
                if (result != null && result.size() > 0) {
                    if (result.get(0).equals(RESULT_SUCCESS))
                        callback.onSuccess(result.get(1));
                    return;
                }
                callback.onFailure(result.get(1));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static String getBody(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String body = "";
        String content = "";

        while ((body = rd.readLine()) != null) {
            content += body + "\n";
        }
        return content;
    }














//    public static void getImage (String url ,  final IResponse callback){
//
//        asyncHttpClient.setTimeout(120000);
//        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//
//                Bitmap bitmap =decodeBitmap(responseBody,300,300);
//                callback.onSuccess(bitmap);
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                callback.onFailure(error.toString());
//            }
//        });
//
//
//    }
//
//
//
//    public static int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) > reqHeight
//                    && (halfWidth / inSampleSize) > reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }
//
//    public static Bitmap decodeBitmap(byte[] responseBody, int reqWidth, int reqHeight) {
//
//        ByteArrayInputStream stream = new ByteArrayInputStream(responseBody);
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        Bitmap first =  BitmapFactory.decodeStream(stream,null, options);
//
//        // Calculate inSampleSize
//        stream = new ByteArrayInputStream(responseBody);
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        Bitmap second = BitmapFactory.decodeStream(stream, null, options);
//
//        return second;
//    }

}





