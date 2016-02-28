package com.vakoms.meshly.views.font;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import meshly.vakoms.com.meshly.R;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/5/15.
 */
public class MovingTextView extends TextView {



    private String mFontPath;

        public MovingTextView(Context context, AttributeSet attrs) {
            super(context, attrs);


            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MovingTextView);

            try {
                mFontPath = a.getString(R.styleable.MovingTextView_moving_font);
            } finally {
                a.recycle();
            }

            init();


            setEllipsize(TextUtils.TruncateAt.MARQUEE);
            setMarqueeRepeatLimit(-1);
            setSingleLine(true);
        }


    public void init() {
        if (mFontPath != null) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), mFontPath);
            setTypeface(tf);
        }
    }

        @Override
        protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
            if(focused) {
                super.onFocusChanged(focused, direction, previouslyFocusedRect);
            }
        }

        @Override
        public void onWindowFocusChanged(boolean focused) {
            if(focused) {
                super.onWindowFocusChanged(focused);
            }
        }

        @Override
        public boolean isFocused() {
            return true;
        }
    }







