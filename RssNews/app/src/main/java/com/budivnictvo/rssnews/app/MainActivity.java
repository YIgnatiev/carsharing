package com.budivnictvo.rssnews.app;

import android.app.ActionBar;
import android.app.Activity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budivnictvo.rssnews.app.fragments.CountryChooseFragment;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private ImageView mIvActionBarBack;
    private ImageView mIvActionBarAdd;
    private TextView mTvActionBarText;
    private FrameLayout mFrameLayout;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initParsecom();
       // saveToParse();
        getFromParse();



        findUI();
        ActionBar mActionBar = getActionBar();
        initActionBar(mActionBar);
        findActionBarUI(mActionBar);
        setActionBarListenrs(mActionBar);

        replaceFragment( new CountryChooseFragment());
    }

    private void findUI(){
        mFrameLayout = (FrameLayout)findViewById(R.id.flMain);
    }

    private void initParsecom(){
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "JQCz5W34Tlv8KCRcuWTDPOsUQeIebasenUNdSj0w", "73sXqI9tKwdp4BtKSxKEQy8bWPhLH3wMsFo8oe2x");


    }

    private void saveToParse(){
        ParseObject testObject = new ParseObject("MyObject");
        testObject.put("name", "oleh");

        String id =  testObject.getObjectId();
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(MainActivity.this, "Done" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFromParse(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("RssCategory");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null){
                    for (ParseObject pObject : parseObjects){
                           String name = (String) pObject.get("name");
                        name.length();
                    }
                }
            }
        });
    }

    private void replaceFragment(Fragment _fragment){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.flMain, _fragment)
                .commitAllowingStateLoss();
    }
    public void replaceFragmentWithBackstack(Fragment _fragment){
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_right_in, R.animator.slide_right_out,
                R.animator.slide_left_in,R.animator.slide_left_out)
                .replace(R.id.flMain, _fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }
    public void replaceFragmentWithRotation(Fragment _fragment){
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.flMain, _fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }



    private void initActionBar(ActionBar _actionBar) {
        View cView = getLayoutInflater().inflate(R.layout.action_bar, null);

            _actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            _actionBar.setDisplayShowHomeEnabled(false);
            _actionBar.setDisplayShowTitleEnabled(false);
            _actionBar.setDisplayHomeAsUpEnabled(false);
            _actionBar.setDisplayShowCustomEnabled(true);
            _actionBar.setCustomView(cView);
            _actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    private void findActionBarUI(ActionBar _actionBar){
        mTvActionBarText = (TextView)_actionBar.getCustomView().findViewById(R.id.tvActionBarText);
        mIvActionBarBack =  (ImageView)_actionBar.getCustomView().findViewById(R.id.ivActionBarBack);
        mIvActionBarAdd = (ImageView) _actionBar.getCustomView().findViewById(R.id.ivActionBarAdd);
    }

    private void setActionBarListenrs(ActionBar _actionBar){
        mIvActionBarBack.setOnClickListener(this);
        mIvActionBarAdd.setOnClickListener(this);
    }

    public void setBackButtonVisible(boolean isVisible){
        if (isVisible){
           mIvActionBarBack.setVisibility(View.VISIBLE);
        }else{
            mIvActionBarBack.setVisibility(View.GONE);
        }
    }

    public void setActionBarTitle (String _text){
        mTvActionBarText.setText(_text);
    }

    public float getXFraction() {
        return mFrameLayout.getX() /mFrameLayout.getWidth();

    }

    public void setXFraction(float xFraction) {
        // TODO: cache width
        final int width = mFrameLayout.getWidth();
        mFrameLayout.setX((width > 0) ? (xFraction * width) : -9999);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            this.mProgressDialog = new ProgressDialog(this);
            this.mProgressDialog.setCancelable(false);
        }
        this.mProgressDialog.setMessage(getResources().getString(R.string.loading));

        if (!this.mProgressDialog.isShowing()) {
            this.mProgressDialog.show();
        }


    }

    public void turnOffProgressDialog() {
        this.mProgressDialog.dismiss();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivActionBarAdd:
                break;
            case R.id.ivActionBarBack:
                onBackPressed();
                break;
        }
    }



}

