package com.vakoms.meshly.fragment_transition.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.vakoms.meshly.fragment_transition.core.TransitionBundleFactory;

/**
 * Created by takam on 2015/03/26.
 */
public class FragmentTransitionLauncher {
    private static final String TAG = "TransitionLauncher";

    private final Context context;
    private View fromView;
    private View rippleView;
    private View movingView;
    private String uri;

    private FragmentTransitionLauncher(Context context) {
        this.context = context;
    }

    public static FragmentTransitionLauncher with(Context context) {
        return new FragmentTransitionLauncher(context);
    }

    public FragmentTransitionLauncher from(View fromView) {
        this.fromView = fromView;
        return this;
    }


    public FragmentTransitionLauncher uri( String uriPath) {
        this.uri = uriPath;
        return this;
    }


    public FragmentTransitionLauncher rippleView(View rippleView) {
        this.rippleView =rippleView;
        return this;
    }

    public FragmentTransitionLauncher movingView(View movingView) {
        this.movingView = movingView;
        return this;
    }



    public void prepare(Fragment toFragment) {
        final Bundle transitionBundle = TransitionBundleFactory.createTransitionBundle( fromView, uri);
        toFragment.setArguments(transitionBundle);
    }



}
