package com.carusselgroup.dwt.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.carusselgroup.dwt.R;
import com.carusselgroup.dwt.model.Car;
import com.carusselgroup.dwt.ui.fragment.CarDetailFragment;
import com.carusselgroup.dwt.ui.fragment.CarsListFragment;
import com.carusselgroup.dwt.ui.fragment.SearchFragment;
import com.carusselgroup.dwt.ui.fragment.SearchFragment.OnSearchListener;
import com.carusselgroup.dwt.utils.TSharedPrefferenses;

import java.util.HashMap;

public class MainActivity extends Activity implements OnSearchListener, CarsListFragment.OnCarSelectedListener {
    private LinearLayout layoutProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFragment(SearchFragment.newInstance());
        layoutProgressDialog = (LinearLayout)findViewById(R.id.progress_LinearLayout_MainActivity);
    }


    public HashMap<String, String> getmSearchParams() {
        if (mSearchParams == null){
            mSearchParams = new HashMap<>();
        }
        return mSearchParams;
    }

    public void setmSearchParams(HashMap<String, String> mSearchParams) {
        this.mSearchParams = mSearchParams;
    }

    private HashMap<String, String> mSearchParams = new HashMap<String, String>();

    private void setFragment(Fragment _fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_Container_MainActivity, _fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private View.OnClickListener mLogOutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TSharedPrefferenses.logout(MainActivity.this);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    };

    public View.OnClickListener getLogOutListener(){
        return mLogOutListener;
    }

    public void progressDialogShow(){
        layoutProgressDialog.setVisibility(View.VISIBLE);
    }

    public boolean isProgressDialogShow(){
        return layoutProgressDialog.getVisibility() == View.VISIBLE;
    }

    public void progressDialogHide(){
        layoutProgressDialog.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

    @Override
    public void onSearchListener(String searchParams) {
        HashMap<String, String> params = new HashMap<>();
        params.put("url", searchParams);
        setmSearchParams(params);

        setFragment(CarsListFragment.newInstance());
    }

    @Override
    public void onCarSelected(Car _car) {
        setFragment(CarDetailFragment.newInstance(_car));
    }
}
