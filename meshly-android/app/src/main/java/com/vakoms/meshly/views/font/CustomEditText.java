package com.vakoms.meshly.views.font;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import java.util.Hashtable;

import meshly.vakoms.com.meshly.R;


/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 8/27/15.
 */
public class CustomEditText extends EditText{

    private String mFontPath;

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);

        try {
            mFontPath = a.getString(R.styleable.CustomEditText_custom_edit_text_font);
        } finally {
            a.recycle();
        }

        init();
    }

    public CustomEditText(Context context) {
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
