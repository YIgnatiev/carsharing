package com.vakoms.meshly.views;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import meshly.vakoms.com.meshly.R;

/**
 * Created by Oleh Makhobey on 17.05.2015.
 * tajcig@ya.ru
 */
public class CustomLoading extends RelativeLayout implements View.OnClickListener{

    private ImageView ivLoadingLogo;
    private View vwBackground;
    private View vwCircle;
    private boolean isProgress;
    private boolean isCanceable;
    public CustomLoading (Context _context){
        super(_context);
        initView();
    }


    public CustomLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.item_loading, this);
        ivLoadingLogo =  (ImageView)findViewById(R.id.ivLoadingLogo_ItmeLoading);
        vwBackground = findViewById(R.id.vwBackground_ItemLoading);
        vwCircle = findViewById(R.id.vwLoadingCircle_ItemLoading);
        vwBackground.setOnClickListener(this);
        setCustomLoadingVisibility(GONE);


    }
    public void startProgress(){
        isProgress = true;
        setCustomLoadingVisibility(VISIBLE);
       this.setAlpha(1);
        vwCircle.setScaleY(1);
        vwCircle.setScaleX(1);
        vwCircle.setRotation(0);


        accelerateCircle();

    }

    public void endProgress(){
        isProgress = false;
    }

    public void switchProgress(){
        if(isProgress){
            endProgress();
        }else {
            startProgress();
        }
    }

    public void setCanceable (boolean isCanceable){
        this.isCanceable = isCanceable;
    }
    private void setCustomLoadingVisibility(int _visibility){
        vwBackground.setVisibility(_visibility);
        vwCircle.setVisibility(_visibility);
        ivLoadingLogo.setVisibility(_visibility);
    }



    private void accelerateCircle(){




        vwCircle.animate()
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(500)
                .rotationBy(+90)
                .setListener(new AnimationListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rotateCircle();
                    }

                }).start();
    }






    private void rotateCircle(){

        vwCircle.animate()
                .rotationBy(-180)
                .setInterpolator(new LinearInterpolator()).setDuration(250).setListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                if (isProgress) {
                    rotateCircle();
                } else {
                    deccelerateCircle();
                }
            }
        }).start();
    }


    private void deccelerateCircle(){


setInvisible();

//
//        vwCircle.animate()
//                .setInterpolator(new DecelerateInterpolator())
//                .scaleY(0)
//                .scaleX(0)
//                .setDuration(250)
//                .rotationBy(-180)
//                .setListener(new AnimationListenerAdapter() {
//
//                })
//                .start();
//
//
//                vwBackground.animate().alpha(0).setDuration(250).setInterpolator(new DecelerateInterpolator()).start();

    }


    private void setInvisible(){
        this.animate()
                .alpha(0)
                .setDuration(250)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimationListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setCustomLoadingVisibility(GONE);
                    }
                }).start();
    }


    private void setCustomViewAlpha(float _alpha){
        vwBackground.setAlpha(_alpha);
        vwCircle.setAlpha(_alpha);
        ivLoadingLogo.setAlpha(_alpha);
    }


    private void animateLogoSmall(){
        ivLoadingLogo.animate()
                .scaleX(0.85f)
                .scaleY(0.85f)

                .setInterpolator(new DecelerateInterpolator()).setDuration(1000).setListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animateLogoBig();
            }
        }).start();
    }

    private void animateLogoBig(){
        ivLoadingLogo.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)

                .setInterpolator(new AccelerateInterpolator()).setDuration(1000).setListener(new AnimationListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                animateLogoSmall();
            }

        }).start();
    }

    @Override
    public void onClick(View v) {
        if(isCanceable){
            endProgress();
        }
    }


    private class AnimationListenerAdapter implements Animator.AnimatorListener{
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
