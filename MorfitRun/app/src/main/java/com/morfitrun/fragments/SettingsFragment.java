package com.morfitrun.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.andreabaccega.widget.FormEditText;
import com.morfitrun.R;
import com.morfitrun.global.Constants;
import com.morfitrun.listeners.OnClickRightToolbarListener;
import com.morfitrun.listeners.OnDateCompletedListener;
import com.morfitrun.widgets.DateDialog;
import com.morfitrun.widgets.validators.ConfirmValidator;
import com.morfitrun.widgets.validators.PasswordValidator;
import java.util.Calendar;

/**
 * Created by vasia on 16.03.2015.
 */
public class SettingsFragment extends BaseFragment implements View.OnClickListener, OnDateCompletedListener {

    private FormEditText etName, etEmail, etPassword, etConfirm;
    private TextView tvUserName, tvBirthday, tvAddBirthday;
    private ImageView ivUserPhoto, ivGenderMale, ivGenderFeamle;
    private Button btnSave;
    private SwitchCompat switchGender;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.fragment_settings, container, false);
        findViews();
        setListener();
        initPasswordFields();
//        readDataFromPreferences();

        return mInflatedView;
    }

    @Override
    protected void findViews() {
        etName = (FormEditText) mInflatedView.findViewById(R.id.etName_FS);
        etEmail = (FormEditText) mInflatedView.findViewById(R.id.etEmail_FS);
        etPassword = (FormEditText) mInflatedView.findViewById(R.id.etPassword_FS);
        etConfirm = (FormEditText) mInflatedView.findViewById(R.id.etConfirm_FS);

        tvBirthday = (TextView) mInflatedView.findViewById(R.id.tvBirthdayData_FS);
        tvAddBirthday = (TextView) mInflatedView.findViewById(R.id.tvAddBirthday_FS);
        tvUserName = (TextView) mInflatedView.findViewById(R.id.tvUserName_FS);
        switchGender = (SwitchCompat) mInflatedView.findViewById(R.id.switchGender_FS);

        ivUserPhoto = (ImageView) mInflatedView.findViewById(R.id.ivUserPhoto_FS);
        ivGenderFeamle = (ImageView) mInflatedView.findViewById(R.id.ivGenderFeamle_FS);
        ivGenderMale = (ImageView) mInflatedView.findViewById(R.id.ivGenderMale_FS);
        btnSave = (Button) mInflatedView.findViewById(R.id.btnSave_FS);

    }

    private void setListener(){
        ivUserPhoto.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        tvAddBirthday.setOnClickListener(this);
        ivGenderMale.setOnClickListener(this);
        ivGenderFeamle.setOnClickListener(this);
    }

    private void initPasswordFields(){
        etPassword.addValidator(new PasswordValidator(getActivity()));
        etConfirm.addValidator(new ConfirmValidator(getActivity(), etPassword));

    }

    @Override
    public CharSequence getTitle() {
        return getResources().getString(R.string.settings);
    }

    @Override
    protected OnClickRightToolbarListener getRightOnClickListener() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ivGenderMale_FS:
                switchGender.setChecked(false);
                break;
            case R.id.ivGenderFeamle_FS:
                switchGender.setChecked(true);
                break;
            case R.id.ivUserPhoto_FS:
                getUserPhoto();
                break;
            case R.id.btnSave_FS:
                onSaveSettings();
                break;
            case R.id.tvAddBirthday_FS:
                showDateDialog();
                break;
        }
    }
    private void onSaveSettings(){
        if (checkFields()){
//            writeDataToPreferences();
        }
    }

    private boolean checkFields() {
        boolean allValid = true;
        FormEditText[] allFields = {etEmail, etPassword, etConfirm};
        for (FormEditText field : allFields) {
            allValid = field.testValidity() && allValid;
            if (!field.testValidity()) {
                field.requestFocus();
            }
        }
        return allValid;
    }

    private void showDateDialog(){
        Calendar startDate = Calendar.getInstance();
        int currentYear = startDate.get(Calendar.YEAR);
        startDate.set(Calendar.YEAR, currentYear - 50);
        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.YEAR, currentYear - 12);

        DateDialog dialog = new DateDialog(getActivity(), getResources().getString(R.string.your_age), 0, this, startDate, endDate, Calendar.getInstance());
        dialog.setMassage(getResources().getString(R.string.incorrect_age));
        dialog.show();
    }

    private void getUserPhoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onDateCompleted(int requestCode, int _year, int _monthOfYear, int _dayOfMonth) {
        tvBirthday.setText(String.format("%d/%d/%d", _year, _monthOfYear, _dayOfMonth));
    }

//    public void readDataFromPreferences() {
//        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
//        final String userName = preferences.getString(Constants.USER_NAME, null);
//        tvUserName.setText(userName);
//        etName.setText(userName);
//        final String password = preferences.getString(Constants.USER_PASSWORD, null);
//        etPassword.setText(password);
//        etConfirm.setText(password);
//        etEmail.setText(preferences.getString(Constants.USER_EMAIL, null));
//        tvBirthday.setText(preferences.getString(Constants.USER_BIRTHDAY, "_/_/_"));
//        if (preferences.getString(Constants.USER_GENDER, null) == Constants.WOMAN)
//            switchGender.setChecked(true);
//    }
//
//    public void writeDataToPreferences() {
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(Constants.USER_NAME, etName.getText().toString());
//        editor.putString(Constants.USER_PASSWORD, etPassword.getText().toString());
//        editor.putString(Constants.USER_EMAIL, etEmail.getText().toString());
//        editor.putString(Constants.USER_BIRTHDAY, tvBirthday.getText().toString());
//
//        if (switchGender.isChecked())
//            editor.putString(Constants.USER_GENDER, Constants.WOMAN);
//        else editor.putString(Constants.USER_GENDER, Constants.MAN);
//
//        editor.apply();
//    }
}
