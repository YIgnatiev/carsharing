package com.vakoms.meshly.fragments;

import android.animation.Animator;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.vakoms.meshly.BaseActivity;
import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.views.ResideMenu;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Oleh Makhobey on 03.06.2015.
 * tajcig@ya.ru
 */
public abstract class BaseFragment<T extends Activity> extends Fragment implements ResideMenu.OnMenuListener {




    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    protected T mActivity;
protected View mRoot;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (T)activity;
    }


    @Override
    public void onStop() {
        mSubscriptions.unsubscribe();
        super.onStop();
    }

    @Override
    public void onStart() {
        mSubscriptions = new CompositeSubscription();
        super.onStart();
    }




    protected void addMenuListener(  View root , MainActivity activity){

       activity.getResideMenu().setMenuListener(this);
       root.setAlpha(0);
        mRoot =root;

    }

    @Override
    public void endClosing() {
        if(mRoot != null)
         mRoot.animate().setDuration(300).alpha(1);
    }




    @Override
    public void startOpening() {

    }

    @Override
    public void endOpening() {

    }

    @Override
    public void startClosing() {

    }
    protected abstract void handleError(Throwable throwable);
}
