package com.vakoms.meshly.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import meshly.vakoms.com.meshly.R;


/**
 * Created by Oleh Makhobey on 15.04.2015.
 * tajcig@ya.ru
 */


public class CustomSwitch extends View implements ValueAnimator.AnimatorUpdateListener {
    private int mFrontColor;
    private int mBackColor;
    private int mBackPressedColor;


    private Paint mFrontPaint;
    private Paint mBackPaint;
    private Paint mPaintStroke;
    private float xValue = 0;


    private int radius;
    private int roundRadius;
    private int innerPadding;

    private boolean isChecked;
    private RectF mRect;
    private RectF mRectStroke;

    private GestureDetector mGestureDetector;

    private OnCheckedChangeListener mListener;

    public CustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomSwitch, 0, 0);
        try {
            mBackColor = a.getColor(R.styleable.CustomSwitch_back_color, R.color.switch_background_pressed);
            mFrontColor = a.getColor(R.styleable.CustomSwitch_thumb_color, android.R.color.holo_red_light);
            mBackPressedColor = a.getColor(R.styleable.CustomSwitch_back_color_pressed, android.R.color.holo_green_light);
            innerPadding = (int) a.getDimension(R.styleable.CustomSwitch_inner_padding, 5);
            roundRadius = (int) a.getDimension(R.styleable.CustomSwitch_round_radius, 25);

        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {

        mFrontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFrontPaint.setColor(mFrontColor);
        mFrontPaint.setStyle(Paint.Style.FILL);
        mFrontPaint.setAntiAlias(true);

        mBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackPaint.setColor(mBackColor);
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setAntiAlias(true);


        mPaintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintStroke.setColor(getContext().getResources().getColor(R.color.switch_stroke));
        mPaintStroke.setStrokeWidth(1.5f);
        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintStroke.setAntiAlias(true);


        mRect = new RectF(0, 0, 100, 50);
        mRectStroke = new RectF(0, 0, 100, 50);

        mGestureDetector = new GestureDetector(this.getContext(), new CustomGestureDetector());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minWidth = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();

        int width = resolveSizeAndState(minWidth, widthMeasureSpec, 1);

        int minHeight = getPaddingTop() + getPaddingBottom() + getSuggestedMinimumHeight();

        int height = resolveSizeAndState(minHeight, heightMeasureSpec, 0);

        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackground(canvas);
        drawStroke(canvas);
        drawCircle(canvas);
    }


    private void drawBackground(Canvas canvas) {
        mRect.set(getPaddingLeft(),
                getPaddingTop(),
                getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());


        canvas.drawRoundRect(mRect, mRect.height()/2, mRect.height()/2, mBackPaint);
    }

    private void drawStroke(Canvas _canvas) {

        mRectStroke.set(getPaddingLeft(),
                getPaddingTop(),
                getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());


        _canvas.drawRoundRect(mRectStroke, mRectStroke.height()/2, mRectStroke.height()/2, mPaintStroke);
    }

    private void drawCircle(Canvas _canvas) {
        radius = getHeight() / 2 - (getPaddingTop() + innerPadding);
        _canvas.drawCircle(countXValue(), getHeight() / 2, radius, mFrontPaint);
        _canvas.drawCircle(countXValue(), getHeight() / 2, radius, mPaintStroke);
    }


    private float countXValue() {
        return getThumbPath() + radius + getPaddingLeft() + innerPadding;
    }

    private float getThumbPath() {
        return (mRect.width() - 2 * radius - innerPadding * 2) * xValue;
    }

    public boolean isChecked() {
        return isChecked;
    }

    private void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        if (isChecked) {
            xValue = 0;
        } else {
            xValue = 1;
        }
        invalidate();
    }

    private void check() {
        final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);

        animator.setDuration(200);
        animator.addUpdateListener(this);
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();
    }

    private void unCheck() {
        final ValueAnimator animator = ValueAnimator.ofFloat(1, 0);

        animator.setDuration(200);
        animator.addUpdateListener(this);
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        xValue = (float) animation.getAnimatedValue();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }


    private void switchStates() {
        if (isChecked) {
            isChecked = false;
            unCheck();
            mBackPaint.setColor(mBackColor);

        } else {
            isChecked = true;
            check();
            mBackPaint.setColor(mBackPressedColor);
        }

        if (mListener != null) mListener.onStateChanged(this, isChecked);
    }


    public void setOnCheckedChangeListener(OnCheckedChangeListener _listener) {
        mListener = _listener;

    }

    public static interface OnCheckedChangeListener {
        public void onStateChanged(View view, boolean isChecked);
    }

    private class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            switchStates();
            return true;


        }

    }


}


