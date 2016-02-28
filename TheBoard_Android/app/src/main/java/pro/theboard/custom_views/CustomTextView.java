package pro.theboard.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lapioworks.cards.R;


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
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), mFontPath);
            setTypeface(tf);
        }

    }

}

