package com.example.homeworkanimation;


import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class MyRotation extends Animation{
    private float centerX;
    private float centerY;

    public MyRotation (float centerX , float centerY ){
        this.centerX = centerX;
        this.centerY = centerY;
    }
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        setRepeatCount(INFINITE);
        setRepeatMode(RESTART);
        setDuration(5000);
        setFillAfter(true);
        setInterpolator(new LinearInterpolator());

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Matrix matrix = t.getMatrix();

        matrix.setRotate(interpolatedTime * 360);
        matrix.preTranslate(-centerX,-centerY);
        matrix.postTranslate(centerX,centerY);


    }
}
