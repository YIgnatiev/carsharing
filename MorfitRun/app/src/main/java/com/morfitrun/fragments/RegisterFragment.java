package com.morfitrun.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.morfitrun.R;
import com.morfitrun.activity.LoginActivity;
import com.morfitrun.activity.MainActivity;
import com.morfitrun.api.ErrorProccessor;
import com.morfitrun.api.RestClient;
import com.morfitrun.data_models.User;
import com.morfitrun.global.BitmapDecoder;
import com.morfitrun.global.CalleryMediaGet;
import static com.morfitrun.global.Constants.*;
import com.morfitrun.global.GlobalDataStorage;

import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Администратор on 31.12.2014.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, Callback<User> {
    private FormEditText etFullName;
    private FormEditText etEmail;
    private FormEditText etPassword;
    private FormEditText etConfirmPassword;
    private TextView tvSubmit;
    private TextView tvAdd;
    private TextView tvDate;
    private ImageView ivUserAvatar;
    private ImageView ivBack;
    private LoginActivity mActivity;
    private FrameLayout flRoundImage;
    private String mImagePath;
    private SwitchCompat swGender;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (LoginActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);
        findViews(rootView);
        setListeners();

        return rootView;
    }

    private void findViews(View _rootView) {
        etFullName = (FormEditText) _rootView.findViewById(R.id.etName_FR);
        etEmail = (FormEditText) _rootView.findViewById(R.id.etEmail_FR);
        etPassword = (FormEditText) _rootView.findViewById(R.id.etPassword_FR);
        etConfirmPassword = (FormEditText) _rootView.findViewById(R.id.etPasswordConfirm_FR);
        tvSubmit = (TextView) _rootView.findViewById(R.id.tvSubmit_FR);
        tvAdd = (TextView) _rootView.findViewById(R.id.tvAdd_FR);
        ivUserAvatar = (ImageView) _rootView.findViewById(R.id.ivUserAvatar_FR);
        flRoundImage = (FrameLayout) _rootView.findViewById(R.id.frRoundImage);
        ivBack = (ImageView) _rootView.findViewById(R.id.ivBack_FR);
        tvDate = (TextView) _rootView.findViewById(R.id.tvBirthDateFR);
        swGender = (SwitchCompat) _rootView.findViewById(R.id.swGender_FR);
    }


    private void setListeners() {
        tvSubmit.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        flRoundImage.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private boolean checkFields() {
        boolean allValid = true;
        FormEditText[] allFields = {etConfirmPassword, etPassword, etEmail, etFullName};
        for (FormEditText field : allFields) {
            allValid = field.testValidity() && allValid;
            if (!field.testValidity()) {
                field.requestFocus();
            }
        }
        return allValid;
    }

    private void registerNewUser() {
        if (checkFields()) {
            String name = etFullName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String birthDay = tvDate.getText().toString();
            String gender = swGender.isChecked() ? GENDER_FEMALE : GENDER_MALE;
            String confirmPassword = etConfirmPassword.getText().toString();
            if (password.equals(confirmPassword)) {
                sendRequestForNewUser(name, email, password, birthDay, gender);
            }
        }
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


    private void setImage(String _imagePath) {
        Bitmap bitmap = BitmapDecoder.decodeBitmap(_imagePath, 200, 200);
        ivUserAvatar.setImageBitmap(bitmap);
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(mActivity, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void sendRequestForNewUser(String _name, String _email,
                                       String _password, String _birthDay, String _gender) {
        final User user = new User();
        user.setFullName(_name);
        user.setEmail(_email);
        user.setPassword(_password);
        user.setBirthDay(_birthDay);
        user.setGender(_gender);
        GlobalDataStorage.setCurrentUser(user);
        RestClient.getInstance().getSignUpService().signUp(user, this);
        mActivity.showProgress(mActivity.getResources().getString(R.string.login_registering));
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date = String.format("%d/%d/%d", year, monthOfYear, dayOfMonth);
        tvDate.setText(date);
    }

    @Override
    public void onClick(View _view) {
        switch (_view.getId()) {
            case R.id.tvAdd_FR:
                showDateDialog();
                break;
            case R.id.tvSubmit_FR:
                registerNewUser();
                break;
            case R.id.frRoundImage:
                requestNewImage();
                break;
            case R.id.ivBack_FR:
                mActivity.onBackPressed();
                break;
        }

    }

    @Override
    public final void success(User user, Response response) {
        mActivity.hideProgress();
        GlobalDataStorage.setCurrentUserId(user.getuId());
        Toast.makeText(mActivity, mActivity.getResources()
                .getString(R.string.login_registering_completed),
                Toast.LENGTH_LONG).show();
        getFragmentManager().popBackStack();
    }

    @Override
    public final void failure(final RetrofitError _error) {
        mActivity.hideProgress();
        ErrorProccessor.processError(mActivity, _error);
    }
}
