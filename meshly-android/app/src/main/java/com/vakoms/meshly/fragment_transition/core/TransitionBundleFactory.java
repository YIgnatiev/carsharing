package com.vakoms.meshly.fragment_transition.core;

import android.os.Bundle;
import android.view.View;

/**
 * Created by takam on 2015/05/17.
 */
public class TransitionBundleFactory {
    public static final String TEMP_IMAGE_FILE_NAME = "activity_transition_image.png";
    private static final String TAG = "Transition";

    public static Bundle createTransitionBundle(View fromView,String uriPath) {

        int[] screenLocation = new int[2];
        fromView.getLocationOnScreen(screenLocation);
        final TransitionData transitionData = new TransitionData(screenLocation[0],
                                                                screenLocation[1],
                                                                fromView.getMeasuredWidth(),
                                                                fromView.getMeasuredHeight(),
                                                                uriPath);
        return transitionData.getBundle();
    }



}
