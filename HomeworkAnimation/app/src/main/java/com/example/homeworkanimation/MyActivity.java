package com.example.homeworkanimation;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

public class MyActivity extends Activity {


    private ViewGroup container;
    private ViewGroup wholeLayout;
    private ImageView imageLeft;
    private ImageView imageCenter;
    private ImageView imageRight;
    private Button btn3D;
    private int image = 1;
    private int[] imageResourceArray = new int[]{R.drawable.ic_blue_circle,
            R.drawable.ic_red_circle};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        getActionBar().hide();

        btn3D = (Button) findViewById(R.id.btn_turn3d);
        container = (ViewGroup) findViewById(R.id.container);
        imageLeft = (ImageView) findViewById(R.id.left_image);
        imageCenter = (ImageView) findViewById(R.id.center_image);
        imageRight = (ImageView) findViewById(R.id.right_image);

        btn3D.setOnClickListener(new MyOnClickListener());

        wholeLayout = (ViewGroup) findViewById(R.id.whole_layout);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        rotateAnimation();
    }

    private void rotateAnimation() {

        float centerX = container.getWidth() / 2.0f;
        float centerY = container.getHeight() / 2.0f;
        MyRotation rotation = new MyRotation(centerX, centerY);
        container.startAnimation(rotation);
    }

    private void fadeOutBall() {
        MyAlfa alfa = new MyAlfa();
        alfa.setAnimationListener(new AnimListener());
        imageCenter.startAnimation(alfa);

    }

    private void turnEverything() {
        float centerX = container.getWidth() / 2.0f;
        float centerY = container.getHeight() / 2.0f;
        Rotation3D rotation3D = new Rotation3D(0f, 360f, centerX, centerY, 500f, true);
        wholeLayout.startAnimation(rotation3D);
        rotation3D.setDuration(5000);
        rotation3D.setFillAfter(true);
        rotation3D.setRepeatMode(Animation.REVERSE);
        rotation3D.setRepeatCount(1);
        rotation3D.setInterpolator(new DecelerateInterpolator());
    }

    private void moveBalls() {
        MyTranslate translateLeft = new MyTranslate(MyTranslate.LEFT);
        MyTranslate translateRight = new MyTranslate(MyTranslate.RIGHT);
        imageLeft.startAnimation(translateLeft);
        imageRight.startAnimation(translateRight);

    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_turn3d:
                    turnEverything();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                moveBalls();
                fadeOutBall();
                return true;
            case MotionEvent.ACTION_MOVE:

                return true;
            case MotionEvent.ACTION_UP:
                return true;
            default:
                return false;
        }


    }

    private class AnimListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            imageCenter.setImageResource(imageResourceArray[image]);
            if (image == 1) {
                image = 0;
            } else {
                image = 1;
            }
        }
    }
}
