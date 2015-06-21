package youdrive.today;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

/**
 * Created by psuhoterin on 15.04.15.
 */
public abstract class BaseActivity extends ActionBarActivity {

    private Toolbar toolbar;

    protected void setActionBarIcon(int iconRes) {
        toolbar.setNavigationIcon(iconRes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    protected abstract int getLayoutResource();

    public void showMessage(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    };
}
