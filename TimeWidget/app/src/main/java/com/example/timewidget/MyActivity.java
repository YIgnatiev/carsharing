package com.example.timewidget;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;


public class MyActivity extends Activity {
    private Button btn_start;
    private Button btn_stop;
    private RadioButton rbtn_up;
    private RadioButton rbtn_down;
    private RadioButton rbtn_left;
    private RadioButton rbtn_center;
    private RadioButton rbtn_right;
    private CheckBox checkBox;
    private ImageView imageView;
    Intent positionIntent = null;

    public static String VERTICAL_POSITION = "com.example.VERTICAL_POSITION";

    public static String HORIZONTAL_POSITION = "com.example.HORIZONTAL_POSITION";

    public static String AUTORUN_ENABLE = "com.example.AUTORUN_ENABLE";

    public static String PREFERENCES_NAME = "widget_preferences";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        positionIntent = new Intent(MyActivity.this, TimeService.class);
        checkBox  = (CheckBox)findViewById(R.id.checkBox);
        checkBox.setChecked(isAutorunEnable());
        checkBox.setOnClickListener(new OnClickCheckboxListener() );
        initComponents();
        setNonActive();

    }

    private boolean isAutorunEnable(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        boolean status = sharedPreferences.getBoolean(AUTORUN_ENABLE, false);
        return status;
    }

    private void setAutorunEnable(boolean status){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AUTORUN_ENABLE, status);
        editor.commit();

    }





    private void savePreferences(Intent intent){
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int verticalPosition = intent.getIntExtra(VERTICAL_POSITION,0);
        int horizontalPosition = intent.getIntExtra(HORIZONTAL_POSITION,0);
        editor.putInt(VERTICAL_POSITION, verticalPosition);
        editor.putInt(HORIZONTAL_POSITION, horizontalPosition);
        editor.commit();
    }

    private Intent readPreferences(Intent intent){

       SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
       intent.putExtra(VERTICAL_POSITION, preferences.getInt(VERTICAL_POSITION, 0));
       intent.putExtra(HORIZONTAL_POSITION, preferences.getInt(HORIZONTAL_POSITION, 0));
       return intent;
    }

    public void setActive () {
        imageView.setImageResource(R.drawable.ic_green_circle);
        rbtn_right.setEnabled(true);
        rbtn_center.setEnabled(true);
        rbtn_left.setEnabled(true);
        rbtn_up.setEnabled(true);
        rbtn_down.setEnabled(true);

    }
    public void setNonActive(){
        imageView.setImageResource(R.drawable.ic_red_circle);
        rbtn_right.setEnabled(false);
        rbtn_center.setEnabled(false);
        rbtn_left.setEnabled(false);
        rbtn_up.setEnabled(false);
        rbtn_down.setEnabled(false);

    }

    private void initComponents(){
        btn_start = (Button) findViewById(R.id.btn_start_service);
        btn_stop = (Button) findViewById(R.id.btn_stop_service);
        btn_start.setOnClickListener(new OnClickButtonListener());
        btn_stop.setOnClickListener(new OnClickButtonListener());

        rbtn_up = (RadioButton)findViewById(R.id.radio_up);
        rbtn_down = (RadioButton)findViewById(R.id.radio_down);
        rbtn_left = (RadioButton)findViewById(R.id.radio_left);
        rbtn_center = (RadioButton)findViewById(R.id.radio_center);
        rbtn_right = (RadioButton)findViewById(R.id.radio_right);


        rbtn_up.setOnClickListener(new OnClickRadioButtonListener());
        rbtn_down.setOnClickListener(new OnClickRadioButtonListener());
        rbtn_left.setOnClickListener(new OnClickRadioButtonListener());
        rbtn_center.setOnClickListener(new OnClickRadioButtonListener());
        rbtn_right.setOnClickListener(new OnClickRadioButtonListener());

        imageView = (ImageView)findViewById(R.id.image_view);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }

        }
        return false;
    }

    private class OnClickButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_start_service:
                    if (!isServiceRunning(TimeService.class)) {
                        startService(new Intent(MyActivity.this, TimeService.class));
                        setActive();
                    }
                    break;
                case R.id.btn_stop_service:

                        stopService(new Intent(MyActivity.this, TimeService.class));
                        setNonActive();
                    break;
                default:
                    break;
            }
        }
    }

    private class OnClickRadioButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.radio_up :
                    positionIntent.putExtra(VERTICAL_POSITION, TimeService.UP_POSITION);
                    break;
                case R.id.radio_down:
                    positionIntent.putExtra(VERTICAL_POSITION, TimeService.DOWN_POSITION);
                    break;
                case R.id.radio_left:
                    positionIntent.putExtra(HORIZONTAL_POSITION, TimeService.LEFT_POSITION);
                    break;
                case R.id.radio_center:
                    positionIntent.putExtra(HORIZONTAL_POSITION, TimeService.CENTER_POSITION);
                    break;
                case R.id.radio_right:
                    positionIntent.putExtra(HORIZONTAL_POSITION, TimeService.RIGHT_POSITION);
                    break;
            }

           savePreferences(positionIntent);


            stopService(readPreferences(positionIntent));
            startService(readPreferences(positionIntent));


        }
    }

    private class OnClickCheckboxListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            setAutorunEnable(checkBox.isChecked());
        }
    }

}
