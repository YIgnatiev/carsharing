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
    private Function mLocationFunction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontsOverride.setDefaultFont(this, "MONOSPACE", "HelveticaRegular.ttf");
        bindActivity();

    }



    public abstract void bindActivity();

    protected void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public  boolean isNetworkConnected() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        return (activeInfo != null && activeInfo.isConnected());
    }



    public  void getLocationPermission(Function function) {
        mLocationFunction = function;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION);

        }else {
            mLocationFunction.apply();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mLocationFunction != null)mLocationFunction.apply();
                } else {

                    Toast.makeText(this, "You did not allow to access your current location", Toast.LENGTH_LONG).show();
                }
            }

        }
    }


}
