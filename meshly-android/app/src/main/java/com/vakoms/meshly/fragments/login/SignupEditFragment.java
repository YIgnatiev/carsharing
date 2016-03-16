package com.vakoms.meshly.fragments.login;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vakoms.meshly.LoginActivity;
import com.vakoms.meshly.databases.UserDAO;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.fragments.IndustriesFragment;
import com.vakoms.meshly.interfaces.PhotoDialogListener;
import com.vakoms.meshly.interfaces.PhotoUpdateListener;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.models.UserToken;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.BitmapUtil;
import com.vakoms.meshly.utils.KeyboardUtil;
import com.vakoms.meshly.utils.P;

import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.DialogSelectPhotoSourceBinding;
import meshly.vakoms.com.meshly.databinding.FragmentSignupEditBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SignupEditFragment extends BaseFragment<LoginActivity> implements PhotoDialogListener,
        IndustriesFragment.IndustriesListener, PhotoUpdateListener {

    private AlertDialog adPhotoChoose;

    private FragmentSignupEditBinding b;
    private UserMe mUser;
    private String mUserImage;


    public static SignupEditFragment getInstance(UserMe user) {
        SignupEditFragment fragment = new SignupEditFragment();
        fragment.setUser(user);
        return fragment;
    }

    public void setUser(UserMe user) {
        mUser = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (b == null) {
            b = FragmentSignupEditBinding.inflate(inflater);
            b.setListener(this);
        }

        return b.getRoot();
    }


    @Override
    public void onPause() {
        super.onPause();
        hideKeyBoard();

    }

    private boolean checkFields() {

        boolean isImageSet = true;
        boolean isNameSet = true;
        boolean isJobSet = true;
        boolean isCompanySet = true;
        boolean isIndustriesSet = true;


        if (mUserImage == null) {
            Toast.makeText(getActivity(), "Choose your profile photo", Toast.LENGTH_SHORT).show();
            isImageSet = false;
        }
        if (mUser.getUsername() == null || mUser.getUsername().isEmpty()) {
            b.etName.setError("Enter your name first");
            isNameSet = false;
        }
        if (mUser.getJob() == null || mUser.getJob().isEmpty()) {
            b.etPosition.setError("Enter your job position");
            isJobSet = false;
        }
        if (mUser.getCompany() == null || mUser.getCompany().isEmpty()) {
            b.etCompany.setError("Enter your company name");
            isCompanySet = false;
        }
        if (mUser.getIndustries() == null)
            if (isImageSet) {
                Toast.makeText(getActivity(), "Choose your industries", Toast.LENGTH_SHORT).show();
                isIndustriesSet = false;
            }

        return isImageSet && isNameSet && isJobSet && isCompanySet && isIndustriesSet;
    }

    private void setData() {
        mUser.setUsername(b.etName.getText().toString());
        mUser.setCompany(b.etCompany.getText().toString());
        mUser.setJob(b.etPosition.getText().toString());
    }

    private void hideKeyBoard() {
        KeyboardUtil.hideKeyBoard(b.etCompany, mActivity);
        KeyboardUtil.hideKeyBoard(b.etName, mActivity);
        KeyboardUtil.hideKeyBoard(b.etPosition, mActivity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {

                case BitmapUtil.CAMERA_REQUEST:
                    BitmapUtil.cropPhoto(BitmapUtil.capturedImageUri, this);
                    break;
                case BitmapUtil.GALLERY_REQUEST:
                    BitmapUtil.capturedImageUri = data.getData();
                    BitmapUtil.cropPhoto(BitmapUtil.capturedImageUri, this);
                    break;

                case BitmapUtil.CROP_IMAGE_REQUEST:
                    BitmapUtil.handleCrop(data, b.ivPicture, this);
                    break;
            }
    }

    private void showDialog() {
        if (adPhotoChoose == null) {
            View rootView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_select_photo_source, null);
            DialogSelectPhotoSourceBinding b = DataBindingUtil.bind(rootView);
            b.setListener(this);
            adPhotoChoose = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                    .setView(rootView)
                    .setCancelable(true)
                    .create();
        }
        adPhotoChoose.show();
    }


    // liseteners
    public void onIndustriesClicked(View view) {

        IndustriesFragment fragment = new IndustriesFragment();
        fragment.setIndustries(mUser.getIndustries());
        fragment.setIndustriesListener(this);
        mActivity.replaceFragmentLeft(fragment);
    }

    public void onPictureClicked(View view) {
        showDialog();
    }

    public void onDoneClicked(View view) {
        setData();
        if (checkFields()) {
            registerUser();
        }
    }















    @Override
    public void onDialogPhotoGallery(View view) {
        BitmapUtil.takePhotoFromGallery(this);
        adPhotoChoose.dismiss();
    }

    @Override
    public void onDialogPhotoCamera(View view) {


        mActivity.checkForCameraPermission(() -> BitmapUtil.takePhotoByCamera(this));

        adPhotoChoose.dismiss();
    }

    @Override
    public void onDialogPhotoCancel(View view) {
        adPhotoChoose.dismiss();
    }


    //network
    private void registerUser() {

        mActivity.showProgress();
        Subscription subscription = RetrofitApi.getInstance().meshly().registerUser(mUser)
                .timeout(3, TimeUnit.SECONDS)
                .flatMap(userResponse -> {
                    saveTokens(userResponse);
                    UserMe justImage = new UserMe();
                    justImage.setImageBase64(mUserImage);
                    return RetrofitApi.getInstance().user().uploadUserImage(justImage);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onRegisterSuccess, this::handleError);
            mSubscriptions.add(subscription);

    }


    public void saveTokens(BaseResponse<UserMe> response) {
        UserToken tokens = response.getData().getTokens();
        P.saveTokens(tokens.access_token, tokens.refresh_token, tokens.expires_in, tokens.token_type);
    }

    public void handleError(Throwable error) {
        mActivity.stopProgress();
        Toast.makeText(mActivity, "Network error", Toast.LENGTH_SHORT).show();

    }


    public void onRegisterSuccess(UserMe userMeResponse) {
        mActivity.stopProgress();

        UserDAO.getInstance().saveUserMe(mActivity.getContentResolver(), userMeResponse);

        mActivity.checkIfWellcomeScreenNeeded();
    }




    // callbacks
    @Override
    public void onIndustriesChanged(List<String> _industries) {
        mUser.setIndustries(_industries);
        String industries = _industries.toString().replace("[", "").replace("]", "").replace(", ", ",\n");
        b.tvInterests.setText(industries);
    }

    @Override
    public void onPhotoUpdate(String base64Image) {
        mUserImage = base64Image;
    }


}