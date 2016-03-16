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
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private FormEditText mFormEditTextName;
    private FormEditText mFormEditTextSurname;
    private FormEditText mFormEditTextEmail;
    private FormEditText mFormEditTextPassword;
    private TextView mTextViewSubmit;
    private TextView mTextViewError;
    private LoginActivity mActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.registration_fragment_layout, container, false);
        findViews(rootView);
        setListeners();
        setActionBar();
        return rootView;
    }

    private void findViews(View _rootView){
        mFormEditTextName = (FormEditText)_rootView.findViewById(R.id.editText_name_RegistrationFragment);
        mFormEditTextSurname = (FormEditText)_rootView.findViewById(R.id.editText_surname_RegistrationFragment);
        mFormEditTextEmail = (FormEditText)_rootView.findViewById(R.id.editText_email_RegistrationFragment);
        mFormEditTextPassword = (FormEditText)_rootView.findViewById(R.id.editText_password_RegistrationFragment);
        mTextViewSubmit = (TextView)_rootView.findViewById(R.id.textView_submit_RegistrationFragment);
        mTextViewError = (TextView)_rootView.findViewById(R.id.textView_error_RegistrationFragment);
        mActivity = (LoginActivity) getActivity();
    }

    private void setActionBar(){
        mActivity.setActionBarTitle(getString(R.string.action_bar_registration));
        mActivity.setBackButtonVisible(true);
        mActivity.showBar();
    }

    private void setListeners(){
        mTextViewSubmit.setOnClickListener(this);
    }

    private boolean checkFields(){
        boolean allValid = true;
        FormEditText[] allFields = {mFormEditTextPassword, mFormEditTextEmail, mFormEditTextSurname,mFormEditTextName};
        for (FormEditText field : allFields) {
            allValid = field.testValidity() && allValid;
            if (!field.testValidity()){
                field.requestFocus();
            }
        }
        return allValid;
    }

    private void registerNewUser(){
        if(isConnected() && checkFields()){
            String name = mFormEditTextName.getText().toString();
            String surname = mFormEditTextSurname.getText().toString();
            String email = mFormEditTextEmail.getText().toString();
            String password = mFormEditTextPassword.getText().toString();
            sendRequestForNewUser(name,surname,email,password);
        }
    }

    private void startQuizActivity(User user) {
        mActivity.startQuizActivity(user);
    }

    private void sendRequestForNewUser(String _name, String _surname, String _email, String _password){
        mActivity.showProgressDialog();
        QuizApi.postRegister(_name,_surname,_email,_password , new ResponseCallback() {
            @Override
            public void onFailure(Object object) {
                mActivity.turnOffProgressDialog();
                if(object != null) {
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
            public void onSuccess(Object object) {
                mActivity.turnOffProgressDialog();
                User user = (User)object;
                mActivity.writePreferences(user);
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
                   registerNewUser();

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
    public void onClick(View v) {
        registerNewUser();
    }
}
