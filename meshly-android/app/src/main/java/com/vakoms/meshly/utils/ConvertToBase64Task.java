package com.vakoms.meshly.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import com.vakoms.meshly.interfaces.PhotoUpdateListener;

import java.io.ByteArrayOutputStream;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/24/15.
 */
public class ConvertToBase64Task  extends AsyncTask<Bitmap, String, String> {

        PhotoUpdateListener mListner;
        public ConvertToBase64Task(PhotoUpdateListener _listener){
            mListner =_listener;
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmaps[0].compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }

        @Override
        protected void onPostExecute(String stringImage) {
            mListner.onPhotoUpdate(stringImage);
        }
    }

