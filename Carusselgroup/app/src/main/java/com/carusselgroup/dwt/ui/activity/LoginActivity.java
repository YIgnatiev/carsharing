package com.carusselgroup.dwt.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.carusselgroup.dwt.BuildConfig;
import com.carusselgroup.dwt.R;
import com.carusselgroup.dwt.model.User;
import com.carusselgroup.dwt.rest.CarsNewApi;
import com.carusselgroup.dwt.rest.IResponse;
import com.carusselgroup.dwt.utils.FontsOverride;
import com.carusselgroup.dwt.utils.TSharedPrefferenses;

import java.util.List;


public class LoginActivity extends Activity implements OnClickListener, OnEditorActionListener {

    private EditText usernameField, passwordField;
    private Button loginBtn, resetBtn;
    private LinearLayout rememberCheckBox;
    private ProgressDialog mProgressDialog;
    private ImageView checkBoxImageView;
    private boolean isAutoLogin = true;



    private IResponse mLoginCallBack = new IResponse() {

        @Override
        public void onSuccess(Object response) {
            turnOffProgressDialog();
            if (checkBoxImageView.isActivated() ) {
                saveUser();
            }else{
                TSharedPrefferenses.logout(LoginActivity.this);
            }
            try {
                User user = (User) response;
            } catch (Exception e) {
                unblockButtons();
                Toast.makeText(LoginActivity.this, "Wrong login or password", Toast.LENGTH_SHORT).show();
                return;
            }
            openMainActivity();
        }

        @Override
        public void onFailure(Object response) {
            turnOffProgressDialog();
            if (BuildConfig.DEBUG)
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            unblockButtons();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Roboto-Bold.ttf");
        setContentView(R.layout.activity_login);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> currUser = TSharedPrefferenses.getUserData(this);
        if (currUser.size() > 0) {
            usernameField.setText(currUser.get(0));
            passwordField.setText(currUser.get(1));
            checkBoxImageView.setImageResource(R.drawable.checkbox_small);
            checkBoxImageView.setActivated(true);
          //  loginRequest(currUser.get(0), currUser.get(1));
        }
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

    private void initViews() {
        usernameField = (EditText) findViewById(R.id.edittext_UserName_LoginActivity);
        passwordField = (EditText) findViewById(R.id.edittext_Password_LoginActivity);
        passwordField.setOnEditorActionListener(this);
        rememberCheckBox = (LinearLayout) findViewById(R.id.checkbox_RememberMe_LoginActivity);
        rememberCheckBox.setOnClickListener(this);
        loginBtn = (Button) findViewById(R.id.button_LoginButton_LoginActivity);
        loginBtn.setOnClickListener(this);
        checkBoxImageView= (ImageView)findViewById(R.id.login_imageview_checkbox);
        resetBtn = (Button) findViewById(R.id.button_ResetButton_LoginActivity);
        resetBtn.setOnClickListener(this);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            hideKeyboard();
            passwordField.clearFocus();

        }
        return false;
    }



    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_LoginButton_LoginActivity:
                login();
                break;
            case R.id.button_ResetButton_LoginActivity:
                resetLoginFields();
                break;
            case R.id.checkbox_RememberMe_LoginActivity:
                switchIcons(checkBoxImageView);
                break;
        }
    }

    private void login() {
        String userName = usernameField.getText().toString();
        String userPassword = passwordField.getText().toString();

        if (userName.isEmpty()) {
            Toast.makeText(this, R.string.login_username_empty_field, Toast.LENGTH_SHORT).show();
        } else if (userPassword.isEmpty()) {
            Toast.makeText(this, R.string.login_password_empty_field, Toast.LENGTH_SHORT).show();
        } else {
            isAutoLogin = false;
            loginRequest(userName, userPassword);
        }
    }

    private void loginRequest(String userName, String userPassword) {
        blockButtons();
        showProgressDialog();
        CarsNewApi.postLogin(userName, userPassword, mLoginCallBack);
    }

    private void saveUser() {
        TSharedPrefferenses.saveUser(this, usernameField.getText().toString(), passwordField.getText().toString());
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void blockButtons() {
        loginBtn.setEnabled(false);
        resetBtn.setEnabled(false);
    }

    private void unblockButtons() {
        loginBtn.setEnabled(true);
        resetBtn.setEnabled(true);
    }

    private void switchIcons(ImageView _imageView){
        if (_imageView.isActivated()){
            _imageView.setActivated(false);
            _imageView.setImageResource(R.drawable.checkbox_small_unchecked);
        }else {
            _imageView.setImageResource(R.drawable.checkbox_small);
            _imageView.setActivated(true);
        }
    }


    private void resetLoginFields() {
        usernameField.setText("");
        usernameField.requestFocus();
        passwordField.setText("");
        checkBoxImageView.setActivated(true);
        switchIcons(checkBoxImageView);

    }

}
