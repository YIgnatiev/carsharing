package com.vakoms.meshly.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import meshly.vakoms.com.meshly.R;

/**
 * User: specialfile:///home/taras.melko/Downloads/AndroidResideMenu-master/ResideMenu/libs/nineoldandroids-library-2.4.0.jar
 * Date: 13-12-10
 * Time: 下午10:44
 * Mail: specialcyci@gmail.com
 */
public class ResideMenu extends FrameLayout {

    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    private static final int PRESSED_MOVE_HORIZANTAL = 2;
    private static final int PRESSED_DOWN = 3;
    private static final int PRESSED_DONE = 4;
    private static final int PRESSED_MOVE_VERTICAL = 5;

    private static final float TRANSLATE_FACTOR = 0.55f;

//    private ImageView imageViewShadow;
//    private ImageView imageViewBackground;
    private LinearLayout layoutLeftMenu;

    /**
     * the activity that view attach to
     */
    private Activity activity;
    /**
     * the decorview of the activity
     */
    private ViewGroup viewDecor;
    /**
     * the viewgroup of the activity
     */
    private TouchDisableView viewActivity;
    /**
     * the flag of menu open status
     */
    private boolean isOpened;

    private float shadowAdjustScaleX;
    private float shadowAdjustScaleY;
    /**
     * the view which don't want to intercept touch event
     */
    private List<View> ignoredViews;
    private List<View> leftMenuItems;
    private List<View> rightMenuItems;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private OnMenuListener menuListener;
    private float lastRawX;
    private boolean isInIgnoredView = false;
    private int scaleDirection = DIRECTION_LEFT;
    private int pressedState = PRESSED_DOWN;
    public List<Integer> disabledSwipeDirection = new ArrayList<Integer>();

    //valid scale factor is between 0.0f and 1.0f.
    private float mScaleValue = 0.5f;

    public ResideMenu(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu, this);
//        imageViewShadow = (ImageView) findViewById(R.id.iv_shadow);
        layoutLeftMenu = (LinearLayout) findViewById(R.id.layout_left_menu);
//        imageViewBackground = (ImageView) findViewById(R.id.iv_background);
    }

    /**
     * use the method to set up the activity which residemenu need to show;
     *
     * @param activity
     */
    public void attachToActivity(Activity activity) {
        initValue(activity);
        setShadowAdjustScaleXByOrientation();
        viewDecor.addView(this, 0);
        setViewPadding();
    }

    private void initValue(Activity activity) {
        this.activity = activity;
        leftMenuItems = new ArrayList<>();
        rightMenuItems = new ArrayList<>();
        ignoredViews = new ArrayList<>();
        viewDecor = (ViewGroup) activity.getWindow().getDecorView();
        viewActivity = new TouchDisableView(this.activity);

        View mContent = viewDecor.getChildAt(0);
        viewDecor.removeViewAt(0);
        viewActivity.setContent(mContent);
        addView(viewActivity);

//        ViewGroup parent = (ViewGroup) scrollViewLeftMenu.getParent();
//        parent.removeView(scrollViewLeftMenu);
//        parent.removeView(scrollViewRightMenu);
    }

    private void setShadowAdjustScaleXByOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            shadowAdjustScaleX = 0.034f;
            shadowAdjustScaleY = 0.12f;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            shadowAdjustScaleX = 0.06f;
            shadowAdjustScaleY = 0.07f;
        }
    }

    /**
     * set the menu background picture;
     *
     * @param bitmap
     */
    public void setBackground(Bitmap bitmap) {
//        imageViewBackground.setImageBitmap(bitmap);
    }
    public void setBackground(int _ressourceId) {
//        imageViewBackground.setImageResource(_ressourceId);
    }

    /**
     * the visiblity of shadow under the activity view;
     *
     * @param isVisible
     */
    public void setShadowVisible(boolean isVisible) {
//        if (isVisible)
//            imageViewShadow.setImageResource(R.drawable.shadow);
//        else
//            imageViewShadow.setImageBitmap(null);
    }




    public View getRoot(){
        return layoutLeftMenu;
    }
    public void addMenuItem(View menuItem, int direction) {
        if (direction == DIRECTION_LEFT) {
            this.leftMenuItems.add(menuItem);
            layoutLeftMenu.addView(menuItem);
        }
    }

    /**
     * set the menu items by array list to left menu;
     *
     * @param menuItems
     */
    @Deprecated
    public void setMenuItems(List<View> menuItems) {
        this.leftMenuItems = menuItems;
        rebuildMenu();
    }
public View getRootViewProfile(){
    return layoutLeftMenu;
}
    /**
     * set the menu items by array list;
     *
     * @param menuItems
     * @param direction
     */
    public void setMenuItems(List<View> menuItems, int direction) {
        if (direction == DIRECTION_LEFT)
            this.leftMenuItems = menuItems;
        else
            this.rightMenuItems = menuItems;
        rebuildMenu();
    }

    private void rebuildMenu() {
        layoutLeftMenu.removeAllViews();

        for (int i = 0; i < leftMenuItems.size(); i++)
            layoutLeftMenu.addView(leftMenuItems.get(i), i);

    }



    /**
     * get the menu items;
     *
     * @return
     */
    public List<View> getMenuItems(int direction) {
        if (direction == DIRECTION_LEFT)
            return leftMenuItems;
        else
            return rightMenuItems;
    }

    /**
     * if you need to do something on the action of closing or opening
     * menu, set the listener here.
     *
     * @return
     */
    public void setMenuListener(OnMenuListener menuListener) {
        this.menuListener = menuListener;
    }


    public OnMenuListener getMenuListener() {
        return menuListener;
    }

    /**
     * we need the call the method before the menu show, because the
     * padding of activity can't get at the moment of onCreateView();
     */
    private void setViewPadding() {
        this.setPadding(viewActivity.getPaddingLeft(),
                viewActivity.getPaddingTop(),
                viewActivity.getPaddingRight(),
                viewActivity.getPaddingBottom());
    }

    /**
     * show the reside menu;
     */
    public void openMenu(int direction) {
        setScaleDirection(direction);

        isOpened = true;


        AnimatorSet scaleDown_activity = buildScaleDownAnimation(viewActivity, mScaleValue, mScaleValue);
//        AnimatorSet scaleDown_shadow = buildScaleDownAnimation(imageViewShadow,
//                mScaleValue + shadowAdjustScaleX, mScaleValue + shadowAdjustScaleY);
        AnimatorSet alpha_menu = buildMenuAnimation(layoutLeftMenu, 1.0f);
        AnimatorSet translate = buildMenuTranslate(viewActivity,getScreenWidth()* TRANSLATE_FACTOR);
//        AnimatorSet translateShadow = buildMenuTranslate(imageViewShadow,getScreenWidth()* TRANSLATE_FACTOR);
//        AnimatorSet scale_menu = buildScaleUpAnimation(layoutLeftMenu,1.0f,1.0f);

//        scaleDown_activity.playTogether(scale_menu);
        scaleDown_activity.addListener(animationListener);
//        scaleDown_activity.playTogether(scaleDown_shadow);
        scaleDown_activity.playTogether(alpha_menu);
        scaleDown_activity.playTogether(translate);
//        scaleDown_activity.playTogether(translateShadow);
        scaleDown_activity.setInterpolator(new FastOutSlowInInterpolator());

        scaleDown_activity.start();
        if(menuListener!= null)menuListener.startOpening();
    }

    /**
     * close the reslide menu;
     */
    public void closeMenu() {

        isOpened = false;
        AnimatorSet scaleUp_activity = buildScaleUpAnimation(viewActivity, 1.0f, 1.0f);
//        AnimatorSet scaleUp_shadow = buildScaleUpAnimation(imageViewShadow, 1.0f, 1.0f);
        AnimatorSet alpha_menu = buildMenuAnimation(layoutLeftMenu, 0.0f);
        //new stuff
        AnimatorSet translate = buildMenuTranslate(viewActivity, 0);
//        AnimatorSet translateShadow = buildMenuTranslate(imageViewShadow,0);
//        AnimatorSet scaleDown_menu = buildScaleUpAnimation(layoutLeftMenu,2,2);

        scaleUp_activity.addListener(animationListener);
//        scaleUp_activity.playTogether(scaleDown_menu);
//        scaleUp_activity.playTogether(scaleUp_shadow);
        scaleUp_activity.playTogether(alpha_menu);
        scaleUp_activity.playTogether(translate);
//        scaleUp_activity.playTogether(translateShadow);
        scaleUp_activity.setInterpolator(new FastOutSlowInInterpolator());
        scaleUp_activity.start();
        if(menuListener!= null)menuListener.startClosing();
    }


    public void setSwipeDirectionDisable(int direction) {
        disabledSwipeDirection.add(direction);
    }


    private boolean isInDisableDirection(int direction) {
        return disabledSwipeDirection.contains(direction);
    }


    public List<Integer> getSwipeDirections(){
        return disabledSwipeDirection;
    }


    private void setScaleDirection(int direction) {

        int screenWidth = getScreenWidth();
        float pivotX;
        float pivotY = getScreenHeight() * 0.5f;

        if (direction == DIRECTION_LEFT) {
           // scrollViewMenu = scrollViewLeftMenu;
            pivotX = screenWidth * 1.5f;
        } else {
          //  scrollViewMenu = scrollViewRightMenu;
            pivotX = screenWidth * -0.5f;

        }



        layoutLeftMenu.setPivotX(0);
        layoutLeftMenu.setPivotY(getScreenHeight() / 2);
        viewActivity.setPivotX(pivotX);
        viewActivity.setPivotY(pivotY);
//        imageViewShadow.setPivotX( pivotX);
//        imageViewShadow.setPivotY( pivotY);
        scaleDirection = direction;
    }

    /**
     * return the flag of menu status;
     *
     * @return
     */
    public boolean isOpened() {
        return isOpened;
    }

    private OnClickListener viewActivityOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isOpened()) closeMenu();
        }
    };

    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened()) {
                showScrollViewMenu();

            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // reset the view;
            if (isOpened()) {
                viewActivity.setTouchDisable(true);
                viewActivity.setOnClickListener(viewActivityOnClickListener);
            } else {
                viewActivity.setTouchDisable(false);
                viewActivity.setOnClickListener(null);
                hideScrollViewMenu();
                if (menuListener != null)
                    if(isOpened )menuListener.endOpening();
                    else menuListener.endClosing();


            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    /**
     * a helper method to build scale down animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleDownAnimation(View target, float targetScaleX, float targetScaleY) {

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        scaleDown.setInterpolator(new DecelerateInterpolator());
        scaleDown.setDuration(250);
        return scaleDown;
    }

    /**
     * a helper method to build scale up animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleUpAnimation(View target, float targetScaleX, float targetScaleY) {

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        scaleUp.setDuration(250);

        return scaleUp;
    }

    private AnimatorSet buildMenuAnimation(View target, float alpha) {

        AnimatorSet alphaAnimation = new AnimatorSet();
        alphaAnimation.playTogether(
                ObjectAnimator.ofFloat(target, "alpha", alpha)
        );

        alphaAnimation.setDuration(250);
        return alphaAnimation;
    }

private AnimatorSet buildMenuTranslate(View target, float translateX) {

        AnimatorSet translateAnimation = new AnimatorSet();
        translateAnimation.playTogether(
                ObjectAnimator.ofFloat(target, "translationX", translateX)
        );

        translateAnimation.setDuration(250);

    return translateAnimation;
    }


    public void clearIgnoredViewList() {
        ignoredViews.clear();
    }

    /**
     * if the motion evnent was relative to the view
     * which in ignored view list,return true;
     *
     * @param ev
     * @return
     */
    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : ignoredViews) {
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY()))
                return true;
        }
        return false;
    }

    private void setScaleDirectionByRawX(float currentRawX) {
        if (currentRawX < lastRawX)
            setScaleDirection(DIRECTION_RIGHT);
        else
            setScaleDirection(DIRECTION_LEFT);
    }

    private float getTargetScale(float currentRawX) {
        float scaleFloatX = ((currentRawX - lastRawX) / getScreenWidth()) * 0.75f;
        scaleFloatX = scaleDirection == DIRECTION_RIGHT ? -scaleFloatX : scaleFloatX;

        float targetScale = viewActivity.getScaleX() - scaleFloatX;
        targetScale = targetScale > 1.0f ? 1.0f : targetScale;
        targetScale = targetScale < 0.5f ? 0.5f : targetScale;
        return targetScale;
    }

    private float lastActionDownX, lastActionDownY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentActivityScaleX = viewActivity.getScaleX();
        if (currentActivityScaleX == 1.0f)
            setScaleDirectionByRawX(ev.getRawX());

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastActionDownX = ev.getX();
                lastActionDownY = ev.getY();
                isInIgnoredView = isInIgnoredView(ev) && !isOpened();
                pressedState = PRESSED_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:

                if (isInIgnoredView || isInDisableDirection(scaleDirection))
                    break;

                if (pressedState != PRESSED_DOWN &&
                        pressedState != PRESSED_MOVE_HORIZANTAL)
                    break;

                int xOffset = (int) (ev.getX() - lastActionDownX);
                int yOffset = (int) (ev.getY() - lastActionDownY);


                if (pressedState == PRESSED_DOWN) {
                    if (yOffset > 25 || yOffset < -25) {
                        pressedState = PRESSED_MOVE_VERTICAL;
                        break;
                    }
                    if (xOffset < -50 || xOffset > 50) {
                        pressedState = PRESSED_MOVE_HORIZANTAL;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }




                } else if (pressedState == PRESSED_MOVE_HORIZANTAL) {
                    if (currentActivityScaleX < 0.95)
                        showScrollViewMenu();


                    float relativeTranslate = getRelativeTranslate(xOffset);



                    viewActivity.setTranslationX(getScreenWidth() * TRANSLATE_FACTOR * relativeTranslate);
//                    imageViewShadow.setTranslationX(getScreenWidth() * TRANSLATE_FACTOR * relativeTranslate);
                    viewActivity.setScaleX(getRelativeScaleOffset(xOffset));
                    viewActivity.setScaleY(getRelativeScaleOffset(xOffset));
//                    imageViewShadow.setScaleX(getRelativeScaleOffset(xOffset) + shadowAdjustScaleX);
//                    imageViewShadow.setScaleY(getRelativeScaleOffset(xOffset) + shadowAdjustScaleY);
                    layoutLeftMenu.setAlpha(relativeTranslate);
//                    layoutLeftMenu.setScaleX(2-relativeTranslate);
//                    layoutLeftMenu.setScaleY(2-relativeTranslate);

                    lastRawX = ev.getRawX();

                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:

                if (isInIgnoredView) break;
                if (pressedState != PRESSED_MOVE_HORIZANTAL) break;

                pressedState = PRESSED_DONE;
                if (isOpened()) {
                    if (currentActivityScaleX > 0.56f)
                        closeMenu();
                    else
                        openMenu(scaleDirection);
                } else {
                    if (currentActivityScaleX < 0.94f) {
                        openMenu(scaleDirection);
                    } else {
                        closeMenu();
                    }
                }

                break;

        }
        lastRawX = ev.getRawX();
        return super.dispatchTouchEvent(ev);
    }

    public int getScreenHeight() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public int getScreenWidth() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void setScaleValue(float scaleValue) {
        this.mScaleValue = scaleValue;
    }

    private float getRelativeTranslate(int xOffset){

        if (!isOpened()){
            return   xOffset/(float)getScreenWidth();
        }else {
            return (getScreenWidth() + xOffset)/(float)getScreenWidth();
        }
    }

    private float getRelativeScaleOffset (int xOffset){
        return 1-(1-mScaleValue)*getRelativeTranslate(xOffset);

    }

    public interface OnMenuListener {

        /**
         * the method will call on the finished time of opening menu's animation.
         */
         void startOpening();
         void endOpening();

        /**
         * the method will call on the finished time of closing menu's animation  .
         */
         void startClosing();
         void endClosing();
    }

    private void showScrollViewMenu() {
        if (layoutLeftMenu!= null && layoutLeftMenu.getParent() == null) {
            addView(layoutLeftMenu);
        }
    }

    private void hideScrollViewMenu() {
        if (layoutLeftMenu != null && layoutLeftMenu.getParent() != null) {
            removeView(layoutLeftMenu);
        }
    }
}
