package com.maryjorapini.soulintention.custom_views.swipe_item;

import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ListView;

public class GestureController {

    private final static int SCROLL_NONE = 0;
    private final static int SCROLL_VERTICAL = 0;
    private final static int SCROLL_HORIZONTAL = 1;

    private int mScrollDirection = SCROLL_NONE;

    private ListView mListView;
    private final static int MAX_THRESHOLD = 25;
    private float mTouchDownXPos;
    private float mTouchDownYPos;


    public void setListView(final ListView _listView) {
        if (mListView == null) {
            mListView = _listView;
            mListView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        }
    }

    public void onTouchEvent(final MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownXPos = event.getX();
                mTouchDownYPos = event.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mTouchDownXPos = 0;
                mTouchDownYPos = 0;
                mScrollDirection = SCROLL_NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mScrollDirection == SCROLL_NONE) {
                    if (Math.abs(mTouchDownXPos - event.getX()) > MAX_THRESHOLD)
                        mScrollDirection = SCROLL_HORIZONTAL;
                    if (Math.abs(mTouchDownYPos - event.getY()) > MAX_THRESHOLD)
                        mScrollDirection = SCROLL_VERTICAL;
                }
        }
        if (mScrollDirection == SCROLL_HORIZONTAL)
            event.setLocation(event.getX(), mTouchDownYPos);
    }

    private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            mListView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    };
}
