package com.vakoms.meshly;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Version;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.constants.Constants;
import com.vakoms.meshly.utils.P;
import com.vakoms.meshly.utils.TokenValidator;

import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SplashActivity extends Activity{

private Subscription subscription;



    private void showActivity(){
        boolean hasAccessToken = TokenValidator.hasAccessTokens();

        Intent intent;
        if (hasAccessToken) {

            intent = new Intent(SplashActivity.this, MainActivity.class);
            if(getIntent().getData() !=null){
                String action = getIntent().getData().toString();
               if(action.contains("http://")){
                   int index = action.lastIndexOf("http://");
                   action = action.substring(index);
               }
                intent.putExtra(Constants.DEEPLINKING_ACTION,action);
            }


        } else {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(subscription!= null)subscription.unsubscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkVersion();
        if(P.isFirstTime()){
            P.clearData();
            P.saveIsFirstTime(false);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }



    private void showObsoleteVersionDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.message))
                .setMessage(getString(R.string.obsolete_app_version))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    final String appPackageName = SplashActivity.this.getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    finish();
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      showActivity();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showDeprecatedVersionDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.message))
                .setMessage(getString(R.string.deprecated_app_version))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    final String appPackageName = SplashActivity.this.getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    finish();
                }).setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private PackageInfo getPackageInfo(){
        PackageInfo info = null;
        try{
            info =  SplashActivity
                    .this
                    .getPackageManager()
                    .getPackageInfo(getPackageName(), 0);
        }catch(PackageManager.NameNotFoundException e ){
            e.printStackTrace();
        }
       return info;
    }



    public void checkVersion() {
        subscription = RetrofitApi
                .getInstance()
                .meshly()
                .getCurrentStoreVersion()
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::handleVersion, this::handleError);


    }


    private void handleVersion(Version _version){


        String latestVersion =_version.getAndroid().getVersion();
        String minimumSupportedVersion = _version.getAndroid().getSupportedVersion();

        PackageInfo pinfo =getPackageInfo();
        String currentVersion = pinfo.versionName;

        if (currentVersion.compareTo(latestVersion) >= 0) {
            showActivity();
        } else if (currentVersion.compareTo(minimumSupportedVersion) < 0) {
            showDeprecatedVersionDialog();
        } else {
            showObsoleteVersionDialog();
        }
    }





    public void handleError(Throwable t) {
           showActivity();
    }
}