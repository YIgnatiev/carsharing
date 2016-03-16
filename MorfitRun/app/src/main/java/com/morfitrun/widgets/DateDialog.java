package com.morfitrun.widgets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;
import com.morfitrun.R;
import com.morfitrun.listeners.OnDateCompletedListener;
import java.util.Calendar;

/**
 * Created by vasia on 13.03.2015.
 */
public class DateDialog {

    private Calendar mStartDate, mEndDate, mCurrentDate;
    private Context mContext;
    private String mTitle;
    private OnDateCompletedListener mOnDateCompletedListener;
    private int mRequestCode;
    private String mMassage = "incorrect date";
    private DatePickerDialog mPicker;

    public DateDialog(final Context _context,
                      final String _title,
                      final int _requestCode,
                      final OnDateCompletedListener _onDateCompletedListener,
                      final Calendar _startDate,
                      final Calendar _endDate,
                      final Calendar _currentDate
                      ){
        mContext = _context;
        mOnDateCompletedListener = _onDateCompletedListener;
        mRequestCode = _requestCode;
        mTitle = _title;
        mStartDate = _startDate;
        mEndDate = _endDate;
        mCurrentDate = _currentDate;
    }
    public void setMassage(String _massage){
        mMassage = _massage;
    }

    public void show(){
        mPicker = new DatePickerDialog(
                mContext,
                null,
                mCurrentDate.get(Calendar.YEAR),
                mCurrentDate.get(Calendar.MONTH),
                mCurrentDate.get(Calendar.DAY_OF_MONTH));

        mPicker.setTitle(mTitle);
        mPicker.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getResources().getString(R.string.ok), onClick);
        mPicker.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getResources().getString(R.string.cancel), onClick);
        mPicker.show();
        mPicker.getButton(DialogInterface.BUTTON_POSITIVE)
              .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compareDate();
            }
        });
    }

    DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };

    private void compareDate(){
        if (dateAfterStartDate() && mEndDate == null){
            setDate(getNewDate());

        } else if (dateAfterStartDate() && dateBeforeEndDate()){
            setDate(getNewDate());
        }
        else Toast.makeText(mContext, mMassage, Toast.LENGTH_SHORT).show();
    }

    private boolean dateAfterStartDate(){
        return !getNewDate().before(mStartDate);
    }

    private boolean dateBeforeEndDate(){
        return getNewDate().before(mEndDate);
    }

    private Calendar getNewDate(){
        Calendar newDate = Calendar.getInstance();
        newDate.set(mPicker.getDatePicker().getYear(), mPicker.getDatePicker().getMonth(), mPicker.getDatePicker().getDayOfMonth());
        return newDate;
    }

    private void setDate(Calendar _newDate){
        mOnDateCompletedListener.onDateCompleted(
                mRequestCode,
                _newDate.get(Calendar.YEAR),
                _newDate.get(Calendar.MONTH) + 1,
                _newDate.get(Calendar.DAY_OF_MONTH));
        mPicker.dismiss();
    }

}
