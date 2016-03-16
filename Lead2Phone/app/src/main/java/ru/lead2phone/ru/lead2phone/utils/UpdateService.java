package ru.lead2phone.ru.lead2phone.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.lead2phone.ru.lead2phone.R;
import ru.lead2phone.ru.lead2phone.models.Caller;
import ru.lead2phone.ru.lead2phone.rest.LeadToPhoneApi;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/2/15.
 */
public class UpdateService extends Service implements Callback<List<Caller>> {
    Caller mCurrentCaller;
    public static boolean isDestroyed;
    MediaPlayer mPlayer;
    Vibrator mVibrator;
    long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};

    @Bind(R.id.tvNumber_DialogCaller)
    TextView tvNumber;


    View mView;
    private boolean isRequesting;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initUi();
        initMediaPlayer();
        sendRequest();
        isDestroyed = false;
        return 0;
    }

    private void sendRequest() {
        if (!isDestroyed)
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (!isRequesting) {
                        LeadToPhoneApi.getInstance(getApplicationContext()).getList(Preferences.getLogin(), Preferences.getPassword(), UpdateService.this);
                        isRequesting = true;
                        sendRequest();
                    }
                }
            }, 10000);
    }


    @Override
    public void onDestroy() {


        if (mView != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView);
            isDestroyed = true;
            stopRinging();
        }

        super.onDestroy();
    }


    private void initUi() {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);


        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;
        params.alpha = 1f;


        mView = inflater.inflate(R.layout.dialog_caller, null);
        ButterKnife.bind(this, mView);
        mView.setVisibility(View.INVISIBLE);
        windowManager.addView(mView, params);

    }

    private void initMediaPlayer() {

        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }


    @OnClick({R.id.ivCancel_DialogCaller, R.id.tvCallLater_DialogCaller})
    public void onCancelClick() {

        callLater(String.valueOf(mCurrentCaller.getId()));
        hideDialog();

    }

    @OnClick(R.id.tvDontCall_DialogCaller)
    public void onDontCallPressed() {
        deleteOnServer(String.valueOf(mCurrentCaller.getId()));
        hideDialog();
    }

    private void startRinging() {
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ringtone);
        mPlayer.start();
        mVibrator.vibrate(pattern, -1);
    }

    private void stopRinging() {
        if(mPlayer != null) mPlayer.pause();
        if(mVibrator != null)mVibrator.cancel();
    }

    @OnClick(R.id.tvCall_DialogCaller)
    public void onCallPressed() {
        hideDialog();
        deleteOnServer(String.valueOf(mCurrentCaller.getId()));
        String number = tvNumber.getText().toString();
        call(number);

    }

    private boolean isCalling() {
        return CustomPhoneStateListener.isCalling;
    }

    public void deleteOnServer(String id) {
        LeadToPhoneApi
                .getInstance(getApplicationContext())
                .deleteUser(Preferences.getLogin(), Preferences.getPassword(), String.valueOf(id), new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }

    public void callLater(String id) {
        LeadToPhoneApi
                .getInstance(getApplicationContext())
                .callLater(Preferences.getLogin(), Preferences.getPassword(), String.valueOf(id), new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }

    public void call(String _number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + _number));
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);

    }

    private void showDialog() {
        tvNumber.setText(mCurrentCaller.getNumber());
        mView.setVisibility(View.VISIBLE);
        startRinging();
    }

    private void hideDialog() {
        stopRinging();
        mView.setVisibility(View.GONE);
    }

    @Override
    public void success(List<Caller> callers, Response response) {
        isRequesting = false;


        if (callers != null && !callers.isEmpty() && mView.getVisibility() != View.VISIBLE && !isCalling() && !isDestroyed) {
            mCurrentCaller = callers.get(0);
            showDialog();

        }
    }

    @Override
    public void failure(RetrofitError error) {
        isRequesting = false;
    }
}
