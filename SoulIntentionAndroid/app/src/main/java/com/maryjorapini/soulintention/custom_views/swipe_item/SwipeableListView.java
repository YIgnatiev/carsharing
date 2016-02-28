package com.maryjorapini.soulintention.custom_views.swipe_item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class SwipeableListView extends ListView {

    private GestureController mGestureController;

    public SwipeableListView(Context context) {
        this(context, null);
    }

    public SwipeableListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mGestureController = new GestureController();
        mGestureController.setListView(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureController.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }
}
