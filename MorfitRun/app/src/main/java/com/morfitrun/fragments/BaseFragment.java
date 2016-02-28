package com.morfitrun.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import com.morfitrun.activity.MainActivity;
import com.morfitrun.listeners.OnClickLeftToolbarListener;
import com.morfitrun.listeners.OnClickRightToolbarListener;
import com.morfitrun.widgets.MorFitToolBar;

/**
 * Created by Виталий on 12/03/2015.
 */
public abstract class BaseFragment extends Fragment {

    protected MainActivity mActivity;
    protected View mInflatedView;
    protected MorFitToolBar mToolBar;
    private boolean isNotNeedGoBack = false;

    @Override
    public void onAttach(final Activity _activity) {
        super.onAttach(_activity);
        mActivity = (MainActivity) _activity;
        mToolBar = mActivity.getToolBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        mToolBar = mActivity.getToolBar();
        initToolBar();
    }

    protected final void initToolBar() {
        mToolBar.setCustomTitle(getTitle().toString());
        mToolBar.setRightImage(getRightToolbarImage());
        mToolBar.setLeftImage(getLeftToolbarImage());
        mToolBar.setOnClickLeftListener(getLeftOnClickListener());
        mToolBar.setOnClickRightListener(getRightOnClickListener());
    }

    public boolean isNeedGoBack(){
        return isNotNeedGoBack;
    }

    public void setNeedGoBack(boolean _goBack){
        isNotNeedGoBack = _goBack;
    }

    public void onBack(){

    }

    protected void changeRightImage(final int _resource) {
        mToolBar.setRightImage(_resource);
    }

    protected abstract void findViews();

    public abstract CharSequence getTitle();

    protected abstract OnClickRightToolbarListener getRightOnClickListener();

    protected OnClickLeftToolbarListener getLeftOnClickListener() {
        return mActivity;
    }

    protected int getLeftToolbarImage() {
        return mActivity.getLeftToolbarImage();
    }

    protected int getRightToolbarImage() {
        return -1;
    }

    protected void replaceFragmentWithBackStack(final BaseFragment _fragment) {
        mActivity.replaceFragmentWithBackStack(_fragment);
    }

    protected void replaceTopNavigationFragment(final BaseFragment _fragment) {
        mActivity.replaceTopNavigationFragment(_fragment);
    }
}
