package com.budivinictvo.quiz.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * Created by Admin on 19.01.2015.
 */
public interface ImageLoadCallback {
    public void onLoaded(Bitmap image);
    public void onFailure(String error);

}
