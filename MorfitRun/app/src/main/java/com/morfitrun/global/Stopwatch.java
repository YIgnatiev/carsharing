package com.morfitrun.global;

import android.app.Notification;
import android.os.Handler;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.morfitrun.global.Constants.*;
/**
 * Created by vasia on 19.03.2015.
 */
public class Stopwatch {

    private long mTimeUpdateMillis;
    private String mTimeFormat = "HH:mm:ss.SSS";
    private SimpleDateFormat mDateFormat;
    private Handler mHandler;
    private TextView mTimerView;
    private long mStartTime = 0;
    private long mTime = 0;
    private boolean isTimerRunning = false;

    public Stopwatch(final TextView _textView){
        mTimerView = _textView;
        mTimeUpdateMillis = ONE_MILLIS;
        mHandler = new Handler();
        mDateFormat = new SimpleDateFormat(mTimeFormat, Locale.UK);
        mDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

    }

    public long getTime(){
        return mTime;
    }

    public String getFormatTime(){
        return mDateFormat.format(new Date(mTime));
    }

    public void setFormat(final String _timeFormat){
        mTimeFormat = _timeFormat;
        mDateFormat = new SimpleDateFormat(mTimeFormat);
    }

    public void setTimeView(TextView _tv){
        mTimerView = _tv;
    }

    public boolean isTimerRunning(){
        return isTimerRunning;
    }

    public void start(){
        if (!isTimerRunning) {
            isTimerRunning = true;
            mStartTime = System.currentTimeMillis() - mTime;
            mHandler.postDelayed(updateTimer, mTimeUpdateMillis);
        }
    }

    public void pause(){
        if (isTimerRunning){
            isTimerRunning = false;
            mHandler.removeCallbacks(updateTimer);
        }
    }

    public void stop(){
        if (isTimerRunning)
            mHandler.removeCallbacks(updateTimer);
        mTime = 0;
        mStartTime = 0;
    }

    protected void setTime(String _formatTime){

    }

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            mTime = System.currentTimeMillis()- mStartTime;
            if (mTimerView != null)
                mTimerView.setText(getFormatTime());
            setTime(getFormatTime());
            mHandler.postDelayed(this, mTimeUpdateMillis);
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }
    };
}
