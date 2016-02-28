package com.vakoms.meshly.utils;

import android.content.Context;

/**
 * Created by Sviatoslav Kashchin on 09.02.15.
 */
public class DensityConvertor {
    public static float dpFromPx(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
