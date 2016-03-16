package com.vakoms.meshly.fragment_transition.core;

import android.animation.TimeInterpolator;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by takam on 2015/05/17.
 */
public class TransitionAnimation {
    private static final String TAG = "Transition";



    public static MoveData startAnimation(final View toView, Bundle transitionBundle, Bundle savedInstanceState, final int duration, final TimeInterpolator interpolator) {
        final TransitionData transitionData = new TransitionData(transitionBundle);




        if (transitionData.imageUri != null) {
            setImageUri(transitionData.imageUri, toView);
        }


        final MoveData moveData = new MoveData();
        moveData.toView = toView;
        moveData.duration = duration;
        if (savedInstanceState == null) {

            ViewTreeObserver observer = toView.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    toView.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] screenLocation = new int[2];
                    toView.getLocationOnScreen(screenLocation);
                    moveData.leftDelta = transitionData.thumbnailLeft - screenLocation[0];
                    moveData.topDelta = transitionData.thumbnailTop - screenLocation[1];

                    moveData.widthScale = (float) transitionData.thumbnailWidth / toView.getWidth();
                    moveData.heightScale = (float) transitionData.thumbnailHeight / toView.getHeight();

                    runEnterAnimation(moveData, interpolator);

                    return true;
                }
            });
        }
        return moveData;
    }


    private static void setImageUri(String uri, View view) {

        ((ImageView) view).setImageURI(Uri.parse(uri));
    }

    private static void runEnterAnimation(MoveData moveData, TimeInterpolator interpolator) {
        final View toView = moveData.toView;
        toView.setPivotX(0);
        toView.setPivotY(0);
        toView.setScaleX(moveData.widthScale);
        toView.setScaleY(moveData.heightScale);
        toView.setTranslationX(moveData.leftDelta);
        toView.setTranslationY(moveData.topDelta);

        toView.animate()
                .setDuration(moveData.duration)
                .scaleX(1)
                .scaleY(1)
                .translationX(0)
                .translationY(0)
                .setInterpolator(interpolator);
    }



    public static void startExitAnimation(MoveData moveData, TimeInterpolator interpolator, final Runnable endAction) {
        View view = moveData.toView;
        int duration = moveData.duration;
        int leftDelta = moveData.leftDelta;
        int topDelta = moveData.topDelta;
        float widthScale = moveData.widthScale;
        float heightScale = moveData.heightScale;





        view.animate()
                .setDuration(duration)
                .scaleX(widthScale)
                .scaleY(heightScale)
                .setInterpolator(interpolator).
                translationX(leftDelta)
                .translationY(topDelta);
        view.postDelayed(endAction, duration);
    }
}
