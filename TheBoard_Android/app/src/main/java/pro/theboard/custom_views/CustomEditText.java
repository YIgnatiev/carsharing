package pro.theboard.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.lapioworks.cards.R;


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
            mFontPath = a.getString(R.styleable.CustomEditText_edit_text_font);
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
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), mFontPath);
            setTypeface(tf);
        }

    }

}
