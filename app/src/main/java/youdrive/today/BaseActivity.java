package youdrive.today;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import youdrive.today.helpers.FontsOverride;

/**
 * Created by psuhoterin on 15.04.15.
 */
public abstract class BaseActivity extends AppCompatActivity {


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

    protected boolean isNetworkConnected() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        return (activeInfo != null && activeInfo.isConnected());
    }





}
