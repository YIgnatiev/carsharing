package com.morfitrun.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.NumberPicker;
import com.morfitrun.R;
import com.morfitrun.global.Constants;
import com.morfitrun.listeners.OnNumberCompletedListener;

/**
 * Created by vasia on 15.03.2015.
 */
public class NumberDialog implements DialogInterface.OnClickListener{

    private int mMaxValue;
    private String mTitle;
    private Context mContext;
    private OnNumberCompletedListener mNumberListener;
    private NumberPicker mNumberPicker;
    private int mRequestCode;

    public NumberDialog(
            final Context _context,
            final String _title,
            final int _requestCode,
            final int _maxValue,
            final OnNumberCompletedListener numberListener) {

        mMaxValue = _maxValue;
        mRequestCode = _requestCode;
        mTitle = _title;
        mContext = _context;
        mNumberListener = numberListener;
    }

    public void show(){
        mNumberPicker = new NumberPicker(mContext);
        mNumberPicker.setMaxValue(mMaxValue);
        mNumberPicker.setMinValue(Constants.MIN_RACE_PEOPLE);

        final AlertDialog.Builder numberDialog = new AlertDialog.Builder(mContext);
        numberDialog
                .setTitle(mTitle)
                .setNegativeButton(mContext.getResources().getString(R.string.cancel), null)
                .setPositiveButton(mContext.getResources().getString(R.string.ok), this)
                .setView(mNumberPicker)
                .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mNumberListener.onNumberCompleted(mRequestCode, mNumberPicker.getValue());
    }
}
