package com.example.homeworkanimation;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class MyScale extends Animation{
    private float centerX;
    private float centerY;

    public MyScale (float centerX , float centerY ){
        this.centerX = centerX;
        this.centerY = centerY;
    }
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        setRepeatCount(1);
        setRepeatMode(REVERSE);
        setDuration(3000);
        setFillAfter(true);
        setInterpolator(new LinearInterpolator());

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Matrix matrix = t.getMatrix();
        matrix.setScale(interpolatedTime , interpolatedTime);

        matrix.preTranslate(-centerX,-centerY);
        matrix.postTranslate(centerX,centerY);


    }
}

