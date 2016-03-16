package com.vakoms.meshly.fragment_transition.core;

import android.os.Bundle;

/**
 * Created by takam on 2015/05/16.
 */
public class TransitionData {
    public static final String EXTRA_IMAGE_LEFT = ".left";
    public static final String EXTRA_IMAGE_TOP = ".top";
    public static final String EXTRA_IMAGE_WIDTH = ".width";
    public static final String EXTRA_IMAGE_HEIGHT = ".height";
    public static final String EXTRA_IMAGE_URI = ".imageUri";

    public final int thumbnailTop;
    public final int thumbnailLeft;
    public final int thumbnailWidth;
    public final int thumbnailHeight;
    public final String imageUri;
    private String appId = "meshly";

    public TransitionData( int thumbnailLeft, int thumbnailTop, int thumbnailWidth, int thumbnailHeight,String imageUri) {
        this.thumbnailLeft = thumbnailLeft;
        this.thumbnailTop = thumbnailTop;
        this.thumbnailWidth = thumbnailWidth;
        this.thumbnailHeight = thumbnailHeight;
        this.imageUri = imageUri;
    }

    public TransitionData( Bundle bundle) {

        thumbnailTop = bundle.getInt(appId + EXTRA_IMAGE_TOP);
        thumbnailLeft = bundle.getInt(appId + EXTRA_IMAGE_LEFT);
        thumbnailWidth = bundle.getInt(appId + EXTRA_IMAGE_WIDTH);
        thumbnailHeight = bundle.getInt(appId + EXTRA_IMAGE_HEIGHT);
        imageUri = bundle.getString(appId + EXTRA_IMAGE_URI);

    }




    public Bundle getBundle() {
        final Bundle bundle = new Bundle();

        bundle.putString(appId + EXTRA_IMAGE_URI, imageUri);
        bundle.putInt(appId + EXTRA_IMAGE_LEFT, thumbnailLeft);
        bundle.putInt(appId + EXTRA_IMAGE_TOP, thumbnailTop);
        bundle.putInt(appId + EXTRA_IMAGE_WIDTH, thumbnailWidth);
        bundle.putInt(appId + EXTRA_IMAGE_HEIGHT, thumbnailHeight);
        return bundle;

    }
}
