package com.vakoms.meshly.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import meshly.vakoms.com.meshly.R;


/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 8/18/15.
 */
public class LabelLayout extends ViewGroup {


    int mHorizontalSpacing = 50;
    int mVerticalSpacing = 50;

    int mSpace;


    public LabelLayout(Context context) {
        super(context);
    }


    public LabelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LabelLayout, 0, 0);
        try {

            mSpace = (int) a.getDimension(R.styleable.LabelLayout_item_space, 10);

        } finally {
            a.recycle();
        }


    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int parrentWidth = getMeasuredWidth();

        int lastWidth =getPaddingLeft();
        int lastHeight = getPaddingTop();



        for (int i = 0; i < this.getChildCount(); i++) {
            View v = getChildAt(i);

            if(parrentWidth < (lastWidth +v.getMeasuredWidth())){
                lastHeight += v.getMeasuredHeight() +mSpace;
                lastWidth = getPaddingLeft();

            }

            v.layout(lastWidth ,lastHeight , lastWidth + v.getMeasuredWidth(),lastHeight +  v.getMeasuredHeight());

            lastWidth += v.getMeasuredWidth() +mSpace;


        }
    }

    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        boolean prepareNewLine = false;
        int widthSum = 0;
        int heightSum = 0;
        int parrentWidth = getMeasuredWidth();

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);

            if(heightSum == 0 )heightSum = v.getMeasuredHeight() +  mSpace ;
            if(prepareNewLine){
                heightSum += v.getMeasuredHeight() + mSpace;
                prepareNewLine = false;
            }

            widthSum += v.getMeasuredWidth();

            if(parrentWidth < (widthSum + v.getMeasuredWidth())){
                prepareNewLine = true;
                widthSum = 0;

            }

            //  heightSum = v.getMeasuredHeight();



        }

        setMeasuredDimension(
                resolveSizeAndState(getMeasuredWidth(), widthMeasureSpec, 0),
                resolveSizeAndState(heightSum + getPaddingTop() + getPaddingBottom(), heightMeasureSpec, 0));

    }
}
