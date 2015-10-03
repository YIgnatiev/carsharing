package youdrive.today;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by psuhoterin on 15.04.15.
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindActivity();

    }



    public abstract void bindActivity();

    protected void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


}
