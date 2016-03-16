package com.budivnictvo.rssnews.app.utils;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SaxAsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Администратор on 11.01.2015.
 */
public class RssApi {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getRss(String _link, final ResponseCallback callback) {
        client.addHeader("Content-Type", "text/xml");
        client.addHeader("Content-Type", "text/html");
        client.addHeader("Content-Type", "application/xhtml+xml");
        client.setConnectTimeout(5000);

        client.get(_link,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String charset ="UTF-8";
                root: for (Header header :headers){
                    if(header.getName().equals("Content-Type")){
                       for(HeaderElement element :header.getElements()){
                            for(NameValuePair pair :element.getParameters()){
                                if  (pair.getName().equals("charset")){
                                    charset = pair.getValue();
                                    break root;
                                }
                            }
                        }
                    }

                }
                String response ="";
                try {
                    response = new String(responseBody, charset);
                } catch (Exception e) {
                    callback.onFailure("Ошибка запроса");

                }
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response ="";
                try {
                    response = new String(responseBody, "windows-1251");
                } catch (Exception e) {
                    callback.onFailure("Ошибка запроса");
                }
                callback.onFailure(response);
            }
        } );
    }

    public static void getHtml(String _link, final ResponseCallback callback) {

        client.addHeader("Content-Type", "text/html");

        client.setConnectTimeout(5000);

        client.get(_link, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
               String charset = RssApi.getCharset(responseBody);
                String response = null;
                try{
                  response =   new String(responseBody, charset);
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                callback.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public static String getCharset(byte [] responseBytes){
            ByteArrayInputStream bis = new ByteArrayInputStream(responseBytes);
            byte[] buf = new byte[4096];



            UniversalDetector detector = new UniversalDetector(null);


            int nread;
        try {
            while ((nread = bis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
            detector.dataEnd();


            String encoding = detector.getDetectedCharset();

            if (encoding != null) {
                detector.reset();
              return encoding;
            } else {
                detector.reset();
              return  null;
            }



        }



}
