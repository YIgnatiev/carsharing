package com.vakoms.meshly.fragment_transition;

import android.app.Fragment;
import android.view.ViewGroup;

import com.transitionseverywhere.Transition;

public class TransitionHelperBuilder {
    private ViewGroup oldView;
    private ViewGroup newView;
    private ViewGroup mainContainer;
    private Transition transition;
    private Fragment fragment;

    public TransitionHelperBuilder setOldView(ViewGroup oldView) {
        this.oldView = oldView;
        return this;
    }

    public TransitionHelperBuilder setNewView(ViewGroup newView) {
        this.newView = newView;
        return this;
    }

    public TransitionHelperBuilder setMainContainer(ViewGroup mainContainer) {
        this.mainContainer = mainContainer;
        return this;
    }

    public TransitionHelperBuilder setTransition(Transition transition) {
        this.transition = transition;
        return this;
    }

    public TransitionHelperBuilder setFragment(Fragment fragment) {
        this.fragment = fragment;
        return this;
    }



    public FragmentTransactionHelper createFragmentTransactionHelper() {
        return new FragmentTransactionHelper(oldView, newView, mainContainer, transition, fragment );
    }
}