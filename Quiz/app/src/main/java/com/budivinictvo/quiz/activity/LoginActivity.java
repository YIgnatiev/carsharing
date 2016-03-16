package com.budivinictvo.quiz.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.budivinictvo.quiz.R;
import com.budivinictvo.quiz.core.ConstantsApp;
import com.budivinictvo.quiz.login_fragments.StarterFragment;
import com.budivinictvo.quiz.model.User;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LoginActivity extends Activity implements View.OnClickListener{
private ProgressDialog mProgressDialog;
    LinearLayout imageViewBackButton;
    TextView textViewActionBarTitle;
    ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.login_activity_layout);

    mActionBar = this.getActionBar();


    initActionBar(mActionBar);
    findActionBarUI(mActionBar);
    setActionBarListenrs(mActionBar);
    checkForAuth();


    }

    public void hideBar(){
       View customActionBar =  mActionBar.getCustomView();
        RelativeLayout layout = (RelativeLayout)customActionBar.findViewById(R.id.action_bar_layout);
        layout.setBackgroundColor(getResources().getColor(R.color.login_activity_background));


        setActionBarTitle("");

    }

    public void showBar (){
        View customActionBar =  mActionBar.getCustomView();
        RelativeLayout layout = (RelativeLayout)customActionBar.findViewById(R.id.action_bar_layout);
        layout.setBackground(getResources().getDrawable(R.drawable.login_action_bar_background));

    }

    private void initActionBar(ActionBar _actionBar) {
        View cView = getLayoutInflater().inflate(R.layout.action_bar_login, new RelativeLayout(this));
        _actionBar.setDisplayShowHomeEnabled(false);
        _actionBar.setDisplayShowTitleEnabled(false);
        _actionBar.setDisplayHomeAsUpEnabled(false);
        _actionBar.setDisplayShowCustomEnabled(true);
        _actionBar.setCustomView(cView);
        _actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

    }

    private void findActionBarUI(ActionBar _actionBar){
        textViewActionBarTitle = (TextView)_actionBar.getCustomView().findViewById(R.id.tvActionBarText);
        imageViewBackButton =  (LinearLayout)_actionBar.getCustomView().findViewById(R.id.ivActionBarBack);

    }

    public void setActionBarTitle(String _title){
       textViewActionBarTitle.setText(_title);
    }

    private void setActionBarListenrs(ActionBar _actionBar){
        imageViewBackButton.setOnClickListener(this);

    }

    public void setBackButtonVisible(boolean isVisible){
        if (isVisible){
            imageViewBackButton.setVisibility(View.VISIBLE);
        }else{
            imageViewBackButton.setVisibility(View.GONE);
        }
    }

    private void checkForAuth(){
       User user =  readPreferences();
        if(isTokenValid(user)){
            startQuizActivity(user);
        }else {
            startLoginFragment();
        }
    }

    private boolean isTokenValid(User user ){
        Date currentDate = new Date();
        if (user.getExpires() == null)
            return false;
        Date date = null;
        SimpleDateFormat format = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            date = format.parse(user.getExpires());
            return date.getTime() > currentDate.getTime();
        }catch (ParseException e){
            e.printStackTrace();
            return false;
        }

    }

    public User readPreferences(){
        SharedPreferences preferences =  getSharedPreferences(ConstantsApp.USER_DATA, Context.MODE_PRIVATE);
        User user = new User();
        user.setUser_id(preferences.getInt(ConstantsApp.USER_ID, -1));
        user.setExpires(preferences.getString(ConstantsApp.USER_EXPIRES, null));
        user.setToken(preferences.getString(ConstantsApp.USER_TOKEN, null));
        user.setUser_email(preferences.getString(ConstantsApp.USER_EMAIL,null));
        return user;
    }

    public void writePreferences(User user){
        SharedPreferences sharedPreferences =  getSharedPreferences(ConstantsApp.USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ConstantsApp.USER_ID, user.getUser_id());
        editor.putString(ConstantsApp.USER_EXPIRES, user.getExpires());
        editor.putString(ConstantsApp.USER_TOKEN, user.getToken());
        editor.putString(ConstantsApp.USER_EMAIL, user.getUser_email());
        editor.apply();
    }

    public void startQuizActivity(User user){

        Intent intent = new Intent(this,QuizActivity.class);
        intent.putExtra(ConstantsApp.USER_INTENT, user);
        startActivity(intent);
        finish();
    }

    private void startLoginFragment(){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_login, new StarterFragment())
                .commitAllowingStateLoss();
    }

    public void replaceFragmentWithBackStack(Fragment fragment){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_login, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            this.mProgressDialog = new ProgressDialog(this);
            this.mProgressDialog.setCancelable(false);
        }
        this.mProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_loading));
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
            case R.id.ivActionBarBack:
                onBackPressed();
                break;
        }
    }

}
