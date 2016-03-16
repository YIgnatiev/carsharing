package com.budivnictvo.rssnews.app.animations;

import android.graphics.Matrix;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;


public class MyTranslate extends Animation{
    private int direction;

    public static int LEFT = -1;
    public static int RIGHT = 1;
    public MyTranslate (int direction){

        this.direction = direction;

    }
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        setRepeatCount(1);
        setRepeatMode(REVERSE);
        setDuration(2000);
        setFillAfter(true);
        setInterpolator(new AccelerateInterpolator());
        setFillAfter(true);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        Matrix matrix = t.getMatrix();
        float deltaX = interpolatedTime * 500* direction;
        matrix.setTranslate(deltaX, 0);

    }
}
