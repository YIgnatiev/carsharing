package com.vakoms.meshly.views.font;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.Hashtable;

import meshly.vakoms.com.meshly.R;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 7/30/15.
 */
public class CustomTextView extends TextView {

    private String mFontPath;

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

        try {
            mFontPath = a.getString(R.styleable.CustomTextView_custom_font);
        } finally {
            a.recycle();
        }

        init();
    }

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        if (mFontPath != null) {
            Typeface tf = get(getContext(), mFontPath);
            setTypeface(tf);
        }

    }










    //typeFaces provoke out of memory error use this method to load typeface once;
    private static final String TAG = "Typefaces";

    private static final Hashtable<String, Typeface> cache = new Hashtable<>();

    public static Typeface get(Context c, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(),
                            assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + assetPath
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }


}

