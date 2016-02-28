package pro.theboard.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import pro.theboard.listeners.Function;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 11/30/15.
 */
public class CustomScrollViev extends ScrollView{
    private boolean mScrollable;
    private Function<Boolean> mCallback;
    public CustomScrollViev(Context context) {
        super(context);
    }

    public CustomScrollViev(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setIsScrollable(boolean scrollable){
        this.mScrollable = scrollable;
    }

    public boolean isScrollable(){
        return mScrollable;
    }

    public void setCallback(Function<Boolean> _callback){
        this.mCallback = _callback;
    }
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if(mCallback!= null) mCallback.apply(clampedY);
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }
}
