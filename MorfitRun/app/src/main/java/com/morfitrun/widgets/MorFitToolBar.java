package com.morfitrun.widgets;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.morfitrun.R;
import com.morfitrun.listeners.OnClickLeftToolbarListener;
import com.morfitrun.listeners.OnClickRightToolbarListener;

/**
 * Created by Виталий on 12/03/2015.
 */
public class MorFitToolBar extends Toolbar {

    private OnClickLeftToolbarListener mLeftClick;
    private OnClickRightToolbarListener mRightClick;

    private ImageView ivActionIconLeft_T, ivActionIconRight_T;
    private TextView tvToolbarTitle_T;

    public MorFitToolBar(Context context) {
        super(context);
        init();
    }

    public MorFitToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MorFitToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private final void init() {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toolbar, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        addView(view);
        initViews(view);
        setListeners();
    }

    private final void initViews(final View _view) {
        ivActionIconLeft_T = (ImageView) _view.findViewById(R.id.btnActionIconLeft_T);
        ivActionIconRight_T = (ImageView) _view.findViewById(R.id.ivActionIconRight_T);
        tvToolbarTitle_T = (TextView) _view.findViewById(R.id.tvToolbarTitle_T);
    }

    private final void setListeners() {
        ivActionIconLeft_T.setOnClickListener(onLeftClickListener);
        ivActionIconRight_T.setOnClickListener(onRightClickListener);
    }

    private final OnClickListener onLeftClickListener = new OnClickListener() {

        @Override
        public final void onClick(final View _view) {
            if (mLeftClick != null && ivActionIconLeft_T.getVisibility() == VISIBLE)
                mLeftClick.onLeftClick();
        }
    };

    private final OnClickListener onRightClickListener = new OnClickListener() {

        @Override
        public final void onClick(final View _view) {
            if (mRightClick != null && ivActionIconRight_T.getVisibility() == VISIBLE)
                mRightClick.onRightClick();
        }
    };

    public final void setOnClickLeftListener(final OnClickLeftToolbarListener _listener) {
        mLeftClick = _listener;
    }

    public final void setOnClickRightListener(final OnClickRightToolbarListener _listener) {
        mRightClick = _listener;
    }

    public final void setLeftImage(final int _leftImage) {
        if (_leftImage != -1) {
            ivActionIconLeft_T.setImageResource(_leftImage);
            ivActionIconLeft_T.setVisibility(VISIBLE);
        } else
            ivActionIconLeft_T.setVisibility(INVISIBLE);
    }

    public final void setRightImage(final int _rightImage) {
        if (_rightImage != -1) {
            ivActionIconRight_T.setImageResource(_rightImage);
            ivActionIconRight_T.setVisibility(VISIBLE);
        } else
            ivActionIconRight_T.setVisibility(INVISIBLE);
    }

    public final void setCustomTitle(final String _title) {
        if (!TextUtils.isEmpty(_title)) {
            tvToolbarTitle_T.setText(_title);
        }
    }
}
