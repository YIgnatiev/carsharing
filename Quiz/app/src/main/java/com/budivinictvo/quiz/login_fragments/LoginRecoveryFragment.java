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
public class LoginRecoveryFragment extends Fragment implements View.OnClickListener {
    private FormEditText mFormEditTextEmail;
    private TextView mTextViewSubmit;
    private LoginActivity mActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.forgot_password_fragment_layout, container, false);
        findViews(rootView);
        setListeners();
        setActionBar();
        return rootView;
    }
    private void findViews(View _rootView){
        mFormEditTextEmail = (FormEditText)_rootView.findViewById(R.id.editText_email_ForgotPasswordFragment);
        mTextViewSubmit = (TextView)_rootView.findViewById(R.id.textView_submit_ForgotPasswordFragment);
        mActivity = (LoginActivity)getActivity();
    }
    private void setListeners(){
        mTextViewSubmit.setOnClickListener(this);
    }

    private boolean checkFields(){
        if (!mFormEditTextEmail.testValidity()){
            mFormEditTextEmail.requestFocus();
        }
        return mFormEditTextEmail.testValidity();
    }

    private void sendRequestForRecovery(String _email){
        mActivity.showProgressDialog();
        QuizApi.postRecoveryLogin(_email, new ResponseCallback() {
            @Override
            public void onSuccess(Object object) {
                mActivity.turnOffProgressDialog();
                createDialog(getString(R.string.dialog_success_message));
            }

            @Override
            public void onFailure(Object object) {
                mActivity.turnOffProgressDialog();
                if (object != null) {

                    String[] errors = getResources().getStringArray(R.array.errors);

                    createDialog(errors[((Error) object).getCode() - 1]);
                }else {
                    Toast.makeText(getActivity(), "Ошибка подключения", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createDialog(String _message){

           AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(_message)
                    .setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().onBackPressed();
                }
            }).create()
              .show();

    }

    private void setActionBar(){
        mActivity.setActionBarTitle(getString(R.string.action_bar_forgot_password));
        mActivity.setBackButtonVisible(true);
        mActivity.showBar();
    }

    private void createErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.dialog_error_message)).setPositiveButton(getString(R.string.dialog_error_repeat), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                recoverPassword();
            }
        }).setNegativeButton(getString(R.string.dialog_error_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().onBackPressed();
            }
        });
        builder.create().show();

            }

    private boolean isConnected(){
        boolean connected = false;
        if (Network.isInternetConnectionAvailable(mActivity)){
            connected = true;
        } else {
            showAlertDialog("Отсутсвует интернет соединение. Пожалуйста, включите и попробуйте снова", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    recoverPassword();

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
    private void recoverPassword(){
            if (isConnected() && checkFields()){
                String email = mFormEditTextEmail.getText().toString();
                sendRequestForRecovery(email);
            }
        }

    @Override
    public void onClick(View v) {
        recoverPassword();
    }
}
