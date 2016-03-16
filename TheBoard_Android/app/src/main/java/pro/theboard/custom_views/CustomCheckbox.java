package pro.theboard.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.lapioworks.cards.R;


/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/3/15.
 */
public class CustomCheckbox extends CheckBox{

        private String mFontPath;

        public CustomCheckbox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

        public CustomCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomCheckbox);

        try {
            mFontPath = a.getString(R.styleable.CustomCheckbox_checkbox_font);
        } finally {
            a.recycle();
        }

        init();
    }

        public CustomCheckbox(Context context) {
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



