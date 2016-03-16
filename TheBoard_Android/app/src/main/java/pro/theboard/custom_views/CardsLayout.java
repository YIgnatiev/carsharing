package pro.theboard.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.lapioworks.cards.R;

import pro.theboard.fragments.cards_fragments.BaseFragment;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 27/03/15.
 */

public class CardsLayout extends ViewGroup {

    private int mVerticalSpacing;

    private AccelerateInterpolator mAccelerateInterpolator = new AccelerateInterpolator();

    public CardsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardsLayout);

        try {
            mVerticalSpacing = a.getDimensionPixelSize(R.styleable.CardsLayout_top_offset, getResources().getDimensionPixelSize(R.dimen.vertical_spacing));
        } finally {
            a.recycle();
        }
    }

    @Override
    public void removeView(View view) {
        isUpdated = true;
        super.removeView(view);
    }

    @Override
    public void addView(View child) {
        isUpdated = true;
        super.addView(child, 0);
    }

    private void initFrontChild(View _view) {
        _view.setScaleX(0.9f);
        _view.setScaleY(0.9f);
        _view.setTranslationY(-_view.getMeasuredWidth() * .1f);
        _view.animate()
                .scaleX(1)
                .scaleY(1)
                .alpha(1)
                .translationX(0)
                .translationY(0)
                .setDuration(200)
                .setInterpolator(mAccelerateInterpolator);
        BaseFragment listener = (BaseFragment)_view.getTag();
        _view.setOnTouchListener(listener);

    }

    private boolean isUpdated; // animate only if new view added
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        if(isUpdated) {
            isUpdated = false;
            final int count = getChildCount();
            int topOffset;
            int leftOffset;
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (i == count - 1) {

                    initFrontChild(child);

                } else {
                    child.setScaleX(0.9f);
                    child.setScaleY(0.9f);
                    child.setAlpha(0);
                    child.setTranslationY(-child.getMeasuredWidth() * .1f);
                    child.animate().alpha(0.5f);

                }
                leftOffset = (getMeasuredWidth() - child.getMeasuredWidth()) / 2;
                topOffset = (getMeasuredHeight() - child.getMeasuredHeight()) / 2;
                child.layout(leftOffset,
                        topOffset + mVerticalSpacing,
                        child.getMeasuredWidth() + leftOffset,
                        child.getMeasuredHeight() + topOffset + mVerticalSpacing);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = mVerticalSpacing;


        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            if (i == 0) {
                height += heightMeasureSpec;
                width = widthMeasureSpec;
            } else {
                height += 10;
            }
        }
        setMeasuredDimension(width, height);
    }
}
