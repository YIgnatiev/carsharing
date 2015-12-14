package youdrive.today;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import youdrive.today.helpers.FontsOverride;
import youdrive.today.listeners.Function;

/**
 * Created by psuhoterin on 15.04.15.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private final int LOCATION_PERMISSION = 10;
    private final int WRITE_PERMISSION = 9;
    private final int PHONE = 8;
    private Function mLocationFunction;
    private Function mWritePermission;
    private Function mPhonePemission;

    private static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontsOverride.setDefaultFont(this, "MONOSPACE", "HelveticaRegular.ttf");
        bindActivity();

    }


    public abstract void bindActivity();

    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        return (activeInfo != null && activeInfo.isConnected());
    }


    public void getWriteExternalPermission(Function function) {

        mWritePermission = function;


        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, WRITE_PERMISSION);

        } else {
            mWritePermission.apply();
        }
    }


    public void getLocationPermission(Function function) {
        mLocationFunction = function;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION);

        } else {
            mLocationFunction.apply();
        }
    }


    public void checkPhonePermission(Function phonePemission){
        mPhonePemission = phonePemission;
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                "android.permission.CALL_PHONE");
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.CALL_PHONE")) {

            } else {

                ActivityCompat.requestPermissions(this, new String[]{"android.permission.CALL_PHONE"}, PHONE);

            }
        }else phonePemission.apply();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mLocationFunction != null) mLocationFunction.apply();
                } else {

                    Toast.makeText(this, "You did not allow to access your current location", Toast.LENGTH_LONG).show();
                }
            }
            break;
            case WRITE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mWritePermission != null) mWritePermission.apply();
                } else {

                    Toast.makeText(this, "You did not allow to choose picture`", Toast.LENGTH_LONG).show();
                }

            break;

            case PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mPhonePemission != null) mPhonePemission.apply();
                } else {
                    Toast.makeText(this, "You did not allow to use phone`", Toast.LENGTH_LONG).show();
                }

        }
    }


}
