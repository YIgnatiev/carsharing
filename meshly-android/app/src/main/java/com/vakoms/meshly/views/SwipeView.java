package com.vakoms.meshly.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import meshly.vakoms.com.meshly.R;


/**
 * Created by Oleh Makhobey on 22.04.2015.
 * tajcig@ya.ru
 */
public class SwipeView extends RelativeLayout implements View.OnClickListener {


    public static final String BUTTON =  "SwipeView_button";
    public static final String PICTURE = "SwipeView_picture";
    public static final String ITEM =    "SwipeView_item";

    private View movableView;
    private View buttonView;
    SwipeOnItemClickListener mListener;
    private boolean isOpened;
    private int mPosition;
    private int mOffset = 10;
    private int buttonResource;
    private String buttonDescription;


    public SwipeView(Context context) {
        super(context);
    }


    public SwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (movableView == null) {
            movableView = getChildAt(0);
            movableView.setOnTouchListener(new AnimationListener());
            movableView.setOnClickListener(this);
            movableView.findViewById(R.id.ivUserAvatar_ItemWall).setOnClickListener(this);
        }
        if (buttonView == null) {
            if(movableView != null) {
                buttonView = getChildAt(1);
                buttonView.setOnClickListener(this);
                buttonView.setTranslationX(getWidth());
                ImageView imageView =(ImageView)buttonView.findViewById(R.id.ivFist_ItemWall);
                TextView textView = (TextView)buttonView.findViewById(R.id.tvButtonDescription_ItemWall);
                textView.setText(buttonDescription);
                imageView.setImageResource(buttonResource);
            }
        }
    }

    public void setButtonImage(int _resource){
       buttonResource = _resource;
    }

    public void setButtonDescription (String _buttonDescription){
        buttonDescription = _buttonDescription;
    }
    public void setPosition(int _position){
        mPosition = _position;
    }

    public void setSwipeOnItemClickListener(SwipeOnItemClickListener _listener){
        mListener = _listener;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlMovable_ItemWall:
                mListener.onItemClick(ITEM,mPosition);
                break;
            case R.id.rlButton_ItemWall:
                mListener.onItemClick(BUTTON,mPosition);
                break;
            case R.id.ivUserAvatar_ItemWall:
                mListener.onItemClick(PICTURE,mPosition);
                break;
        }
    }

    private float newX;
    private float newY;
    private float startMoveX;
    private float startMoveY;

    private class AnimationListener implements View.OnTouchListener {


        public boolean onTouch(View view, MotionEvent event) {

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:

                    startMoveX = event.getX();
                    startMoveY = event.getY();
                   break;

                case MotionEvent.ACTION_MOVE:
                    newX = view.getX() + (event.getX() - startMoveX );
                    newY = view.getY() + (event.getY() - startMoveY );
                    if(newX > 0 )return true;
                    if(newX < -buttonView.getWidth())return true;

                    view.setTranslationX(newX);
                    buttonView.setTranslationX(getWidth()+ newX);


                    break;

                case MotionEvent.ACTION_CANCEL:


                case MotionEvent.ACTION_UP:

                        if(Math.abs(startMoveX - event.getX())< 5 && Math.abs(startMoveY - event.getY()) < 5){
                            mListener.onItemClick(ITEM, mPosition);
                            return true;
                        }


                    float half = buttonView.getWidth()/8;
                    if (isOpened) {
                        if(buttonView.getWidth() - newX > half ){
                            closeAnimation(view);
                        }else{
                            openAnimation(view, buttonView);

                        }

                    } else {
                        if(-newX > half ){
                            openAnimation(view, buttonView);
                        }else{
                            closeAnimation(view);
                        }

                    }


                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;

            }

            gd.onTouchEvent(event);
            view.invalidate();

            return true;
        }

    }



    public void openAnimation(View _movableView, View _buttonView) {



        _movableView
                .animate()
                .translationX(-_buttonView.getWidth())
                .setDuration(200)
                .setInterpolator(new OvershootInterpolator())
                .start();

        _buttonView
                .animate()
                .translationX(getWidth() -_buttonView.getWidth())
                .setDuration(200)
                .setInterpolator(new OvershootInterpolator())
                .start();




        isOpened = true;
    }
    public void open() {
        movableView.setTranslationX(-buttonView.getWidth());
        isOpened = true;
    }




    public void closeAnimation(View _view) {



        _view.animate()
                .translationX(0)
                .setDuration(200)
                .setInterpolator(new OvershootInterpolator())
                .start();

        buttonView.animate()
                .translationX(getWidth())
                .setDuration(200)
                .setInterpolator(new OvershootInterpolator())
                .start();


        isOpened = false;

    }
    public void close() {
        if(movableView !=null){
            movableView.setTranslationX(0);
            isOpened = false;
            buttonView.setTranslationX(getWidth());

        }
    }


    private final GestureDetector.SimpleOnGestureListener mGestureListener
            = new GestureDetector.SimpleOnGestureListener() {


        @Override
        public boolean onSingleTapUp(MotionEvent e) {
                if(!isOpened){
                    closeAnimation(movableView);
                    return true;
                }else{
                   // mListener.onItemClick(e.getmPosition);
                    return true;
                }


        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {

            if( isOpened && velocityX > 0 ) {
                closeAnimation(movableView);
            }else if(!isOpened && velocityX < 0) {

                openAnimation(movableView, buttonView);
            }
            return true;
        }


    };
    final GestureDetector gd = new GestureDetector(getContext(), mGestureListener);


public interface SwipeOnItemClickListener{
         void onItemClick(String _tag, int position);
    }

}




