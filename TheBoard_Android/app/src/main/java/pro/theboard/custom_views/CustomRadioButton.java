package pro.theboard.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.lapioworks.cards.R;


/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/3/15.
 */
public class CustomRadioButton   extends RadioButton {

        private String mFontPath;

        public CustomRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

        public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomRadioButton);

        try {
            mFontPath = a.getString(R.styleable.CustomRadioButton_radio_font);
        } finally {
            a.recycle();
        }

        init();
    }

        public CustomRadioButton(Context context) {
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
