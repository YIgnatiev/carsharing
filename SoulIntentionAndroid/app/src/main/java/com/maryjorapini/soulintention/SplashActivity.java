package com.maryjorapini.soulintention;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.maryjorapini.soulintention.core.rest.RetrofitAdapter;
import com.maryjorapini.soulintention.data_models.response.SessionMobileResponseModel;
import com.maryjorapini.soulintention.gcm.GcmHelper;
import com.maryjorapini.soulintention.global.Constants;
import com.maryjorapini.soulintention.global.DeviceManager;
import com.maryjorapini.soulintention.global.Network;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SplashActivity extends Activity{
    private Callback<SessionMobileResponseModel> mStartSessionCallback, mSendTokenCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        implementStartSessionCallBack();
        implementSendTokenCallBack();

        GcmHelper gcmHelper = new GcmHelper(this);
        gcmHelper.registerDevice();
        makeStartSessionRequest();
        printHashKey();

    }

    private void implementSendTokenCallBack() {
        mSendTokenCallback = new Callback<SessionMobileResponseModel>() {
            @Override
            public void success(SessionMobileResponseModel sessionMobileResponseModel, Response response) {
                Log.d("Send token", sessionMobileResponseModel.toString());

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Send token", error.getBody().toString());

            }
        };
    }


    public final void showAlertDialog(final String _message, final DialogInterface.OnClickListener _tryAgainListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(_message)
                .setCancelable(false)
                .setPositiveButton("Try again", _tryAgainListener)
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        builder.create().show();
    }

    private void implementStartSessionCallBack() {

        mStartSessionCallback = new Callback<SessionMobileResponseModel>() {
            @Override
            public void failure(RetrofitError error) {

                showAlertDialog("Server problem.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        makeStartSessionRequest();
                    }
                });
                Log.e("RETROFIT", error.toString());
            }

            @Override
            public void success(SessionMobileResponseModel responseModel, Response response) {
                Log.d("RETROFIT", responseModel.toString());
                makeSendTokenRequest();
                startMainActivity();
            }
        };
    }

    private void makeStartSessionRequest(){
        if (isConnected()) {
            RetrofitAdapter.getInterface().startMobile(DeviceManager.loadDeviceId(this), mStartSessionCallback);
        }
    }

    private void makeSendTokenRequest(){
       String token = com.maryjorapini.soulintention.gcm.DeviceManager.loadRegistrationId(this);
        if (isConnected() && !token.isEmpty()) {
            RetrofitAdapter.getInterface().deviceToken(token, Constants.PARAM_ANDROID, mSendTokenCallback);
        }
    }
    private void startMainActivity() {
        final Intent intent = new Intent(this, MainFragmentActivity.class);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    private boolean isConnected(){
        boolean connected = false;
        if (Network.isInternetConnectionAvailable(this)){
            connected = true;
        } else {
            showAlertDialog("No internet connection. Please, turn on and try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    makeStartSessionRequest();
                }
            });
        }
        return connected;
    }





    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.maryjorapini.soulintention",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.v("HASH KEY:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

}
