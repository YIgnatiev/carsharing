package com.morfitrun.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.morfitrun.R;
import com.morfitrun.api.ErrorProccessor;
import com.morfitrun.api.RestClient;
import com.morfitrun.data_models.RunnerUser;
import com.morfitrun.data_models.User;
import com.morfitrun.global.BitmapDecoder;
import com.morfitrun.global.CalleryMediaGet;
import com.morfitrun.listeners.OnClickRightToolbarListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.morfitrun.global.Constants.GALLERY_IMAGE_RESULT;
import static com.morfitrun.global.Constants.GALLERY_KITKAT_INTENT_CALLED;
import static com.morfitrun.global.Constants.GENDER_FEMALE;
import static com.morfitrun.global.Constants.GENDER_MALE;
import static com.morfitrun.global.Constants.MALE;
import static com.morfitrun.global.Constants.USER;


/**
 * Created by Admin on 19.03.2015.
 */
public class ViewProfileFragment extends BaseFragment implements OnClickRightToolbarListener
        ,View.OnClickListener, DatePickerDialog.OnDateSetListener , Callback<RunnerUser> {



    private FormEditText etNameEditable;
    private FormEditText etEmailEditable;

    private TextView tvBirthDateEditable;
    private TextView tvBirthDate;
    private TextView tvAddEditable;
    private TextView tvSaveEdiatable;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvBirthDay;


    private ImageView ivGender;
    private ImageView ivUserAvatar;

    private SwitchCompat swGenderEditable;
    private ProgressBar pbLoading;

    private LinearLayout llProfileEditable;
    private RelativeLayout rlProfile;

    private RunnerUser mUser;

    private boolean isEditableMode;


    public static ViewProfileFragment getInstance(RunnerUser _user){
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(USER, _user);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.fragment_view_profile, container,false);
        findViews();
        setListeners();
        mUser = getArguments().getParcelable(USER);
        requestUser();
        return mInflatedView;
    }

    @Override
    protected void findViews() {
        tvName              = (TextView)mInflatedView.findViewById(R.id.tvName_FP);
        tvEmail             = (TextView)mInflatedView.findViewById(R.id.tvEmail_FP);
        tvBirthDay          = (TextView)mInflatedView.findViewById(R.id.tvBirthDate_FP);
        tvBirthDateEditable = (TextView)mInflatedView.findViewById(R.id.tvBirthDateEditable_FP);
        tvBirthDate         = (TextView)mInflatedView.findViewById(R.id.tvBirthdayEditable_FP);

        ivGender            = (ImageView)mInflatedView.findViewById(R.id.ivGender_FP);
        ivUserAvatar        = (ImageView)mInflatedView.findViewById(R.id.ivUserAvatar_FP);

        etNameEditable      = (FormEditText)mInflatedView.findViewById(R.id.etNameEditable_FP);
        etEmailEditable     = (FormEditText)mInflatedView.findViewById(R.id.etEmailEditable_FP);
        tvSaveEdiatable     = (TextView)mInflatedView.findViewById(R.id.tvSaveEditable_FP);
        tvAddEditable       = (TextView)mInflatedView.findViewById(R.id.tvAddEditable_FP);
        swGenderEditable    = (SwitchCompat)mInflatedView.findViewById(R.id.swGenderEditable_FP);
        pbLoading           = (ProgressBar)mInflatedView.findViewById(R.id.pbLoadProgress_FP);

        llProfileEditable   = (LinearLayout)mInflatedView.findViewById(R.id.ll_profile_editable);
        rlProfile           = (RelativeLayout)mInflatedView.findViewById(R.id.rl_profile);

    }


    private void setListeners(){
        tvSaveEdiatable.setOnClickListener(this);
        ivUserAvatar.setOnClickListener(this);
        tvAddEditable.setOnClickListener(this);
    }

    private void switchToProfileMode(){
        setFields(mUser);
        llProfileEditable.setVisibility(View.GONE);
        rlProfile.setVisibility(View.VISIBLE);
        changeRightImage(android.R.drawable.ic_menu_edit);
        isEditableMode = false;
        ivUserAvatar.setOnClickListener(null);
    }


    private void switchToEditableMode(){
        setEditableFields(mUser);
        llProfileEditable.setVisibility(View.VISIBLE);
        rlProfile.setVisibility(View.GONE);
        changeRightImage(android.R.drawable.ic_menu_close_clear_cancel);
        isEditableMode = true;
        ivUserAvatar.setOnClickListener(this);
    }


    private void requestUser(){
        pbLoading.setVisibility(View.VISIBLE);
        RestClient.getInstance().getUserService().getUser(mUser.get_id(), this);
    }


    @Override
    public void success(RunnerUser _user, Response _response) {
            pbLoading.setVisibility(View.GONE);
            mUser = _user;
            switchToProfileMode();
    }

    @Override
    public void failure(RetrofitError _error) {
        ErrorProccessor.processError(getActivity(), _error);
        pbLoading.setVisibility(View.GONE);
    }

    private void setFields(RunnerUser _user){

        tvName.setText(_user.getFullName());
        tvEmail.setText(_user.getEmail());
        tvBirthDay.setText(_user.getBirthDate());
        ivGender.setImageResource(getGenderImageResource(_user.getGender()));
    }

    private void setEditableFields(RunnerUser _user){
        etNameEditable.setText(_user.getFullName());
        etEmailEditable.setText(_user.getEmail());
        tvBirthDateEditable.setText(_user.getBirthDate());
    }


    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(mActivity, this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private int getGenderImageResource(String _gender){
        if(_gender == null) return R.drawable.male;

        return _gender.equals(MALE) ? R.drawable.male : R.drawable.feamle;
    }
    private boolean checkFields() {
        boolean allValid = true;
        FormEditText[] allFields = {etEmailEditable, etNameEditable};
        for (FormEditText field : allFields) {
            allValid = field.testValidity() && allValid;
            if (!field.testValidity()) {
                field.requestFocus();
            }
        }
        return allValid;
    }

    private String formatDate(String _date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T05':HH:mm.ss'Z'");
        Date date = null;
    try {
        date = dateFormat.parse(_date);
    }catch (ParseException e){
        e.printStackTrace();
    }
        dateFormat.applyPattern("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    private void saveUser(){

        if (checkFields()) {
            if (tvBirthDateEditable.getText().equals(getString(R.string.empty_date))){
                Toast.makeText(mActivity , mActivity.getString(R.string.set_date_first)
                        , Toast.LENGTH_SHORT).show();
                return;
            }
            mUser.setFullName(etNameEditable.getText().toString());
            mUser.setEmail(etEmailEditable.getText().toString());
            mUser.setGender(swGenderEditable.isChecked() ? GENDER_FEMALE : GENDER_MALE);


            setFields(mUser);
            switchToProfileMode();
            // not implemented

            requestSaveUser(mUser);
        }
    }

    private void requestSaveUser(RunnerUser _user){

    }


    private void setImage(String _imagePath) {
        Bitmap bitmap = BitmapDecoder.decodeBitmap(_imagePath, 200, 200);
        ivUserAvatar.setImageBitmap(bitmap);
    }

    private void requestNewImage() {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, GALLERY_IMAGE_RESULT);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
        }
    }

    private void handleMode(){

        if(!isEditableMode) {
            switchToEditableMode();
        }else{
            switchToProfileMode();
        }
    }

    private String getFilePathFromProvider(Uri uri) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return getFilePathFromProviderForPreKitKat(uri);
        } else {
            return getFilePathFromProviderForKitKat(uri);
        }
    }

    private String getFilePathFromProviderForPreKitKat(Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getActivity().getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getFilePathFromProviderForKitKat(Uri uri) {
        String log = CalleryMediaGet.getPath(mActivity, uri);
        Log.d("cs_c", log);
        return log;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String filePath = "";
            try {
                if (requestCode == GALLERY_IMAGE_RESULT) {
                    if (data.getData() != null) {
                        filePath = getFilePathFromProvider(data.getData());
                    }
                } else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
                    int takeFlags = data.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    getActivity().getContentResolver().takePersistableUriPermission(data.getData(), takeFlags);
                    filePath = getFilePathFromProvider(data.getData());
                }
                if (!filePath.isEmpty()) {
                    setImage(filePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date = String.format("%d/%d/%d", year, monthOfYear, dayOfMonth);
        tvBirthDateEditable.setText(date);
    }

    @Override
    public CharSequence getTitle() {
       return mActivity.getString(R.string.profile);
    }

    @Override
    protected int getRightToolbarImage() {
        return android.R.drawable.ic_menu_edit;
    }

    @Override
    protected OnClickRightToolbarListener getRightOnClickListener() {
        return this;
    }

    @Override
    public void onRightClick() {
        handleMode();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvSaveEditable_FP:
                saveUser();
                break;
            case R.id.ivUserAvatar_FP:
                requestNewImage();
                break;
            case R.id.tvAddEditable_FP:
                showDateDialog();
                break;
        }
    }
}
