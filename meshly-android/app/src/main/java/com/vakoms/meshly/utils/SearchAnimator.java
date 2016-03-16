package com.vakoms.meshly.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 8/26/15.
 */
public class SearchAnimator extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {

    ValueAnimator mAnimator;
    private View mView;
    private boolean mIsOpen;


    private List<View> viewToShow;
    private List<View> viewToHide;
    public SearchAnimator(View _animatedView) {
        mView = _animatedView;
    }



    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

        mView.getLayoutParams().width = (int)animation.getAnimatedValue();
        mView.requestLayout();
    }

    public void open(int width){
            if(mAnimator!= null && mAnimator.isRunning())return;
                if(!mIsOpen)animateWidth(0, width, true);

    }

    public void close(int width){
        if(mAnimator!= null && mAnimator.isRunning() )return;

        if(mIsOpen)animateWidth(width,0,false);
    }

    public void setViewToShow(View... views) {
        viewToShow = new ArrayList<>();
        Collections.addAll(viewToShow,views);
    }

    public void setViewToHide(View... views) {
        viewToHide = new ArrayList<>();
        Collections.addAll(viewToHide, views);

    }

    public boolean isOpen(){
        return mIsOpen;
    }

    private void animateWidth(int begin ,int width,boolean isOpen){
        mIsOpen = isOpen;
        mAnimator = ValueAnimator.ofInt(begin, width);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.setDuration(300);
        mAnimator.addUpdateListener(this);
        mAnimator.addListener(this);
        mAnimator.start();
    }



    @Override
    public void onAnimationStart(Animator animation) {
        for(View view :viewToShow)
            view.animate().alpha(mIsOpen?1:0).setDuration(300).start();

        for(View view: viewToHide)
            view.animate().alpha(mIsOpen?0:1).setDuration(300).start();
    }

}
