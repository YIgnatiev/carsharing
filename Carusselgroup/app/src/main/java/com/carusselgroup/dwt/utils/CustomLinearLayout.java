package com.carusselgroup.dwt.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.carusselgroup.dwt.ui.fragment.CarDetailFragment;

public class CustomLinearLayout extends LinearLayout {
    public void setmTouchListener(CarDetailFragment.OnTouchDetection mTouchListener) {
        this.mTouchListener = mTouchListener;
    }

    CarDetailFragment.OnTouchDetection mTouchListener;
    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
              //  Log.d("cs_c", " MotionEvent.ACTION_UP:");
                if (mTouchListener != null)
                    mTouchListener.OnTouchUp();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
