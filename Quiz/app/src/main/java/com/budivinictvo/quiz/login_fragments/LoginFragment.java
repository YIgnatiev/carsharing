package com.budivinictvo.quiz.login_fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.budivinictvo.quiz.activity.LoginActivity;
import com.budivinictvo.quiz.core.QuizApi;
import com.budivinictvo.quiz.R;
import com.budivinictvo.quiz.core.ResponseCallback;
import com.budivinictvo.quiz.model.*;
import com.budivinictvo.quiz.model.Error;
import com.budivinictvo.quiz.utils.Network;

/**
 * Created by Администратор on 31.12.2014.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private FormEditText mFormEditTextEmail;
    private FormEditText mFormEditTextPassword;
    private TextView mTextViewSubmit;
    private TextView mTextViewForgotPassword;
    private LoginActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment, container, false);
        findViews(rootView);
        setListeners();
        setActionBar();
        return rootView;
    }

    private void findViews(View _rootView) {
        mTextViewSubmit = (TextView) _rootView.findViewById(R.id.textView_submit_login_LoginFragment);
        mTextViewForgotPassword = (TextView) _rootView.findViewById(R.id.textView_forgot_password_LoginFragment);
        mFormEditTextEmail = (FormEditText) _rootView.findViewById(R.id.editText_email_LoginFragment);
        mFormEditTextPassword = (FormEditText) _rootView.findViewById(R.id.editText_password_LoginFragment);
        mActivity = (LoginActivity) getActivity();
    }
    private void setActionBar(){
        mActivity.setActionBarTitle(getString(R.string.action_bar_autorisation));
        mActivity.setBackButtonVisible(true);
        mActivity.showBar();
    }
    private void setListeners() {
        mTextViewSubmit.setOnClickListener(this);
        mTextViewForgotPassword.setOnClickListener(this);
    }

    private void startPasswordRecoveryFragment() {
        mActivity.replaceFragmentWithBackStack(new LoginRecoveryFragment());
    }



    private void startQuizActivity(User user) {
       mActivity.startQuizActivity(user);
    }

    private void saveUser(User user){
        mActivity.writePreferences(user);
    }

    private boolean checkFields() {
        boolean allValid = true;
        FormEditText[] allFields = { mFormEditTextPassword, mFormEditTextEmail,};
        for (FormEditText field : allFields) {
            allValid = field.testValidity() && allValid;
            if (!field.testValidity()){
                field.requestFocus();
            }
        }
        return allValid;
    }

    private void getUserInfo() {
        if (isConnected() && checkFields() ) {
            String email = mFormEditTextEmail.getText().toString();
            String password = mFormEditTextPassword.getText().toString();
            requestLogin(email, password);
        }
    }

    private void requestLogin(String _email, String _password) {
        mActivity.showProgressDialog();
        QuizApi.postLogin(_email, _password, new ResponseCallback() {
            @Override
            public void onFailure(Object object) {
                mActivity.turnOffProgressDialog();
                if (object != null){
                    try {
                        Error error = (Error) object;
                        String[] errors = getResources().getStringArray(R.array.errors);
                        showDialog(errors[error.getCode() - 1]);
                    } catch (ClassCastException e) {
                        showDialog((String) object);
                    }
            }else{
                    Toast.makeText(getActivity(), "Ошибка подключения", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(Object object){
                mActivity.turnOffProgressDialog();
                User user = (User)object;
                saveUser(user);
                startQuizActivity(user);
            }
        });
    }
    private void showDialog(String _message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(_message)
                .setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create()
                .show();

    }
    private boolean isConnected(){
        boolean connected = false;
        if (Network.isInternetConnectionAvailable(mActivity)){
            connected = true;
        } else {
            showAlertDialog("Отсутсвует интернет соединение. Пожалуйста, включите и попробуйте снова", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getUserInfo();

                }
            });
        }
        return connected;
    }

    public final void showAlertDialog(final String _message, final DialogInterface.OnClickListener _tryAgainListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        builder.setMessage(_message)
                .setCancelable(false)
                .setPositiveButton("Попробовать еще раз", _tryAgainListener)
                .setNegativeButton("Выход", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mActivity.finish();
                    }
                });
        builder.create().show();

    }
    @Override
    public void onClick(View _view) {
        switch (_view.getId()) {
            case R.id.textView_submit_login_LoginFragment:
                getUserInfo();
                break;

            case R.id.textView_forgot_password_LoginFragment:
                startPasswordRecoveryFragment();
                break;
        }
    }
}
