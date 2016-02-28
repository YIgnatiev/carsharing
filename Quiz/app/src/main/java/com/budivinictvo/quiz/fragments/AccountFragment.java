package com.budivinictvo.quiz.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.budivinictvo.quiz.R;
import com.budivinictvo.quiz.activity.QuizActivity;
import com.budivinictvo.quiz.core.ConstantsApp;
import com.budivinictvo.quiz.core.QuizApi;
import com.budivinictvo.quiz.core.ResponseCallback;
import com.budivinictvo.quiz.model.*;
import com.budivinictvo.quiz.utils.Network;

/**
 * Created by Администратор on 04.01.2015.
 */

public class AccountFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "com.budivnictvo.quiz.AccountFragment";
    private TextView textViewName;
    private TextView textViewSurname;
    private TextView textViewEmail;
    private TextView textViewSubmit;
    private QuizActivity mActivity;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.account_layout, container, false);
        mActivity = (QuizActivity) getActivity();
        mActivity.showMenu();
        mActivity.isQuetionFragmentActive = false;
        findViews(rootView);
        setListeners();
        setActionBar();
        setUserInfo();

        return rootView;
    }

    private void setUserInfo() {
        if (mActivity.mUser.getUser_firstname() == null) {
            if (isConnected()) {
                getStatistics();
            }
        } else {
            setFields();
        }
    }

    private void findViews(View _rootView) {
        textViewName = (TextView) _rootView.findViewById(R.id.textView_name_AccountFragment);
        textViewSurname = (TextView) _rootView.findViewById(R.id.textView_surname_AccountFragment);
        textViewEmail = (TextView) _rootView.findViewById(R.id.textView_email_AccountFragment);
        textViewSubmit = (TextView) _rootView.findViewById(R.id.textView_submit_AccountFragment);
    }

    private void setListeners() {
        textViewSubmit.setOnClickListener(this);
    }

    private User getUser() {
        return (User) getActivity().getIntent().getSerializableExtra(ConstantsApp.USER_INTENT);
    }

    private void setActionBar() {
        mActivity.setActionBarTitle(getString(R.string.options));
    }

    private void showDialog(String _message , int error) {

        if (error ==9 || error  == 12){
            mActivity.logOut();
            return;
        }


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

    private boolean isConnected() {
        boolean connected = false;
        if (Network.isInternetConnectionAvailable(mActivity)) {
            connected = true;
        } else {
            showAlertDialog("Отсутсвует интернет соединение. Пожалуйста, включите и попробуйте снова", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setUserInfo();

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

    private void getStatistics() {
        mActivity.showProgressDialog();
        QuizApi.postGetUserInfo(getUser(), new ResponseCallback() {

            @Override
            public void onFailure(Object object) {
                mActivity.turnOffProgressDialog();
                if(object != null) {
                    try {
                        com.budivinictvo.quiz.model.Error error = (com.budivinictvo.quiz.model.Error) object;
                        String[] errors = getResources().getStringArray(R.array.errors);
                        showDialog(errors[error.getCode() - 1] , error.getCode());
                    } catch (ClassCastException e) {
                        showDialog((String) object , 0);
                    }
                }else {
                    Toast.makeText(getActivity(),"Ошибка подключения",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(Object object) {
                User user = (User) object;
                mActivity.mUser.setUser_firstname(user.getUser_firstname());
                mActivity.mUser.setUser_lastname(user.getUser_lastname());
                setFields();
                mActivity.turnOffProgressDialog();
            }
        });
    }

    private void setFields() {
        textViewName.setText(mActivity.mUser.getUser_firstname());
        textViewSurname.setText(mActivity.mUser.getUser_lastname());
        textViewEmail.setText(mActivity.mUser.getUser_email());
    }

    @Override
    public void onClick(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.account_home)));
        startActivity(browserIntent);
    }
}


