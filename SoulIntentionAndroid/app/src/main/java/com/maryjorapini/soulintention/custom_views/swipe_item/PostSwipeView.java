package com.maryjorapini.soulintention.custom_views.swipe_item;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.maryjorapini.soulintention.R;
import com.maryjorapini.soulintention.data_models.response.Post.PostResponseModel;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Sasha on 24.11.2014.
 */
public class PostSwipeView extends RelativeLayout {
    private static int WIDTH_PREV_VIEW;
    private static int HEIGHT_ITEM;
    private SwipeListener mSwipeListener;
    private PostResponseModel postResponseModel;
    //    private StateDocument stateDocument = StateDocument.IDLE;
    private LayoutInflater mInflater;
    public View showView, leftPreView, rightPreView, currentBottomView;
    private StateItem stateItem = StateItem.IDLE;
    private final int OFFSET = 5;
    public PostSwipeView(Context context) {
        super(context);
    }


    public PostSwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WIDTH_PREV_VIEW = (int) getResources().getDimension(R.dimen.preview_width);
        HEIGHT_ITEM = (int) getResources().getDimension(R.dimen.height_item);
        initializeLayout();
        setListeners();
    }


    private void initializeLayout() {
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setRightPreview(mInflater.inflate(R.layout.item_social, this, false));
        setLeftPreView(mInflater.inflate(R.layout.item_like, this, false));
        setShowedView(mInflater.inflate(R.layout.item_post_showed, this, false));
    }

    private void setListeners() {
        showView.setOnTouchListener(showTouchListener);
    }

    private void setShowedView(final View _showView) {
        showView = _showView;
        addView(showView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setLeftPreView(final View _leftPreview) {
        leftPreView = _leftPreview;
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = WIDTH_PREV_VIEW;
        params.addRule(ALIGN_PARENT_LEFT);
        addView(leftPreView, params);
    }


    private void setBottomView(final View _leftFullView) {
        removeView(currentBottomView);
        currentBottomView = _leftFullView;
        addView(currentBottomView, 0);
    }

    private void setRightPreview(final View _rightPreview) {
        rightPreView = _rightPreview;
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = WIDTH_PREV_VIEW;
        params.addRule(ALIGN_PARENT_RIGHT);
        addView(rightPreView, params);
    }

    private final OnTouchListener showTouchListener = new OnTouchListener() {
        private float startPosX;
        private float startPosY;
        private boolean isDown = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    if (stateItem.equals(StateItem.IDLE_LIKE) || stateItem.equals(StateItem.IDLE_SOCIAL)) {
                        translateToValue(0, WIDTH_PREV_VIEW, StateItem.IDLE);
                        return true;
                    }
                    startPosX = event.getRawX();
                    startPosY = event.getRawY();
                    isDown = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (showView != null) {

                        final float leftOffset = event.getRawX() - startPosX;
                        if (Math.abs(leftOffset) <= WIDTH_PREV_VIEW)
                            ViewHelper.setTranslationX(showView, leftOffset);
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    //if (event.getRawX() == startPosX && event.getRawY() == startPosY){
                    if(event.getRawX() > startPosX - OFFSET && event.getRawX() < startPosX + OFFSET){
                       mSwipeListener.onItemClick();
                        return true;
                    }
                    isDown = false;
                    animToEnd();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    isDown = false;
                    animToEnd();
                    break;
            }
            return true;
        }
    };

    private void animToEnd() {
        final float left = showView.getTranslationX();
        if (left < 0 && Math.abs(left) > WIDTH_PREV_VIEW / 2)
            translateToValue(-WIDTH_PREV_VIEW, WIDTH_PREV_VIEW - Math.abs(left), StateItem.IDLE_SOCIAL);
        else if (left < 0 && Math.abs(left) < WIDTH_PREV_VIEW / 2)
            translateToValue(0, Math.abs(left), StateItem.IDLE);
        else if (left > 0 && Math.abs(left) < WIDTH_PREV_VIEW / 2)
            translateToValue(0, Math.abs(left), StateItem.IDLE);
        else if (left > 0 && Math.abs(left) > WIDTH_PREV_VIEW / 2)
            translateToValue(WIDTH_PREV_VIEW, WIDTH_PREV_VIEW - Math.abs(left), StateItem.IDLE_LIKE);
    }

    private void translateToValue(final float endX, final float duration, final StateItem state) {
        final ObjectAnimator mSlideAnimator =
                ObjectAnimator.ofFloat(showView, "translationX", showView.getTranslationX(), endX);
        mSlideAnimator.setInterpolator(new LinearInterpolator());
        mSlideAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                showView.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showView.setEnabled(true);
                stateItem = state;
                postResponseModel.stateItem = stateItem;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mSlideAnimator.setDuration((long) ((duration / WIDTH_PREV_VIEW) * 300));
        mSlideAnimator.start();
    }

    public void setOnItemClickListener(SwipeListener _swipeListener){
        mSwipeListener =_swipeListener;
    }

    public void setStateCurrentItem(final PostResponseModel _postResponseModel) {
        this.postResponseModel = _postResponseModel;
        this.stateItem = _postResponseModel.stateItem;
        switch (stateItem) {
            case IDLE:
                showView.setTranslationX(0);
                break;
            case IDLE_LIKE:
                showView.setTranslationX(WIDTH_PREV_VIEW);
                break;
            case IDLE_SOCIAL:
                showView.setTranslationX(-WIDTH_PREV_VIEW);
                break;

        }

    }


}
