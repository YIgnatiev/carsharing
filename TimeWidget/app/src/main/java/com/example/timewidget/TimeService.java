package com.example.timewidget;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.print.PrintAttributes;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeService extends Service{

    public static final int  UP_POSITION = 101;
    public static final int  DOWN_POSITION = 102;
    public static final int  LEFT_POSITION = 103;
    public static final int  CENTER_POSITION = 104;
    public static final int  RIGHT_POSITION = 105;


    private View mView;
    private Button btnClose;
    private TextView txtLondon;
    private TextView txtKiev;
    private TextView txtWashington;
    private Handler handler;
    public static boolean isServiceActive = true;
    public TimeService(){
        super();
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.service_layout , null);
        btnClose = (Button)mView.findViewById(R.id.btn_sevice_close);
        btnClose.setOnClickListener(new ServiceOnClickListener());

        handler = new Handler();

        txtKiev = (TextView)mView.findViewById(R.id.txt_kiev_time);
        txtLondon = (TextView)mView.findViewById(R.id.txt_london_time);
        txtWashington = (TextView)mView.findViewById(R.id.txt_washington_time);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);


        setWindowPosition(intent, params);
        params.alpha= 0.7f;
        windowManager.addView(mView,params);
        isServiceActive = true;

        startHanler();
        return super.onStartCommand(intent, flags, startId);

    }

    public void setWindowPosition(Intent intent , WindowManager.LayoutParams params){
        int verticalPosition = intent.getIntExtra(MyActivity.VERTICAL_POSITION, 0);
        int horizontalPosition =  intent.getIntExtra(MyActivity.HORIZONTAL_POSITION, 0);

        int vertical = Gravity.CENTER_VERTICAL;
        int horizontal = Gravity.CENTER_HORIZONTAL;
        switch (verticalPosition){
            case UP_POSITION:
                vertical = Gravity.TOP;
                break;
            case DOWN_POSITION:
                vertical = Gravity.BOTTOM;
                break;
            default:
                break;
        }
        switch (horizontalPosition){
            case LEFT_POSITION:
               horizontal = Gravity.LEFT;
                break;
            case CENTER_POSITION:
                horizontal = Gravity.CENTER_HORIZONTAL;
                break;
            case RIGHT_POSITION:
                horizontal = Gravity.RIGHT;
                break;
            default:
                break;
        }
        params.gravity = vertical | horizontal;
    }

    @Override
    public void onDestroy() {
        if (mView != null){
            ((WindowManager)getSystemService(WINDOW_SERVICE)).removeView(mView);
        }

        isServiceActive= false;
        super.onDestroy();
    }

    private void setTime(Calendar calendar){

        txtKiev.setText(getKievTime(calendar));
        txtWashington.setText(getWashingtonTime(calendar));
        txtLondon.setText(getLondonTime(calendar));
    }

    private CharSequence getWashingtonTime(Calendar calendar){
        calendar.setTimeInMillis(calendar.getTimeInMillis() - 5*60 *60 *1000);
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/London"));

        CharSequence washingtonDate = DateFormat.format("a hh:mm:ss",calendar);
        return washingtonDate;

    }

    private CharSequence getLondonTime (Calendar calendar){
        calendar.setTimeInMillis(calendar.getTimeInMillis() - 2*60 *60 *1000);
        CharSequence londonDate = DateFormat.format("zzz a hh:mm:ss",calendar);
        return londonDate;
    }

    private CharSequence getKievTime(Calendar calendar){
        calendar.setTimeInMillis(calendar.getTimeInMillis() + 2*60 *60 *1000);
      //  calendar.setTimeZone(TimeZone.getTimeZone("\"Europe/Kiev\""));
        CharSequence kievDate = DateFormat.format("kk:mm:ss",calendar);
        return kievDate;
    }

    private void startHanler(){

        handler.postDelayed(new TimeUpdater(this), 1000);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ServiceOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            TimeService.this.stopSelf();
        }
    }

    private class TimeUpdater implements Runnable{
        Context context;
        public TimeUpdater(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            setTime(Calendar.getInstance());
            if(isServiceActive)
            startHanler();
        }
    }

}
