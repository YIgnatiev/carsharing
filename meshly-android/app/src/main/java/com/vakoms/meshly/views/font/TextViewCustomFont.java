package com.vakoms.meshly.views.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.Hashtable;

/**
 * Created by Oleh Makhobey on 06.04.2015.
 * tajcig@ya.ru
 */
public class TextViewCustomFont extends TextView {

    public TextViewCustomFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewCustomFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewCustomFont(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = get(getContext(), "fonts/fontawesome-webfont.ttf");
        setTypeface(tf);

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
