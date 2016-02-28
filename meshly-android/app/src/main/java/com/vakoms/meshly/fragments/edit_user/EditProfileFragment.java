package com.vakoms.meshly.fragments.edit_user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.databases.UserDAO;
import com.vakoms.meshly.databases.UserProvider;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.fragments.IndustriesFragment;
import com.vakoms.meshly.fragments.people.PeopleDetailFragment;
import com.vakoms.meshly.interfaces.DialogDeleteListener;
import com.vakoms.meshly.interfaces.Function;
import com.vakoms.meshly.interfaces.PhotoDialogListener;
import com.vakoms.meshly.interfaces.PhotoUpdateListener;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.BitmapUtil;
import com.vakoms.meshly.utils.KeyboardUtil;
import com.vakoms.meshly.views.ResideMenu;

import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.DialogDeleteUserBinding;
import meshly.vakoms.com.meshly.databinding.DialogSelectPhotoSourceBinding;
import meshly.vakoms.com.meshly.databinding.FragmentEditProfileBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import rx.schedulers.Schedulers;

/**
 * Created by Oleh Makhobey on 28.04.2015.
 * tajcig@ya.ru
 */

public class EditProfileFragment extends BaseFragment<MainActivity> implements IndustriesFragment.IndustriesListener,
        EditUserFragment.OnFragmentListener, DialogDeleteListener,
        PhotoDialogListener, PhotoUpdateListener {


    private AlertDialog adPhotoChoose;
    private AlertDialog adDeleteUser;
    private UserMe mUser;
    private UserMe imageUser;
    private UserDAO mDao;
    private FragmentEditProfileBinding b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (b == null) {
            b = FragmentEditProfileBinding.inflate(inflater);
            mDao = UserDAO.getInstance();
            mUser = mActivity.mUser;
            b.setListener(this);
            b.setUser(mUser);
            checkFields();

        }
        return b.getRoot();
    }


    public void checkFields() {

        listenToEditText(b.etName, "Name can't be empty", mUser::setUsername);
        listenToEditText(b.etIntrouduction, "Introduction can't be empty" , mUser::setSummary);
        listenToEditText(b.etTitle,"Title can't be empty", mUser::setJob);
        listenToEditText(b.etHomeCity,"Homecity can't be empty", mUser::setHomeCity);
        listenToEditText(b.etCompanyName, "Company can't be empty" , mUser::setCompany);
        listenToEditText(b.etCompanyDescription,"Company description can't be empty",mUser::setCompanyDescription);
        listenToEditText(b.etCompanyWebsite, "Company website can't be empty", mUser::setCompanyWebsite);




    }



    private void listenToEditText(EditText editText ,String error ,  Function<String> function ){

        WidgetObservable.text(editText)
                .map(OnTextChangeEvent::text)
                .map(t -> t.length() > 0)
                .doOnNext(bool -> {
                    if (!bool) ;//editText.setError(error);
                    else function.apply(editText.getText().toString());
                })
                .onErrorReturn(throwable -> {
                    Toast.makeText(mActivity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    return false;
                })
                .subscribe();
    }


    @Override
    public void onResume() {
        super.onResume();
        mActivity.getResideMenu().setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
    }

    @Override
    public void onPause() {
        super.onPause();
        closeKeyboard();
        mActivity.getResideMenu().getSwipeDirections().clear();
        mActivity.getResideMenu().setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

    }




    private void startIndustriesFragment() {
        IndustriesFragment fragment = new IndustriesFragment();
        fragment.setIndustries(mUser.getIndustries());
        fragment.setIndustriesListener(this);
       mActivity.replaceFragmentLeft(fragment);
    }




    private void closeKeyboard() {
        KeyboardUtil.hideKeyBoard(b.etName, mActivity);
        KeyboardUtil.hideKeyBoard(b.etTitle, mActivity);
        KeyboardUtil.hideKeyBoard(b.etCompanyName, mActivity);
        KeyboardUtil.hideKeyBoard(b.etCompanyWebsite, mActivity);
        KeyboardUtil.hideKeyBoard(b.etCompanyDescription, mActivity);
        KeyboardUtil.hideKeyBoard(b.etHomeCity, mActivity);
        KeyboardUtil.hideKeyBoard(b.etIntrouduction, mActivity);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {

                case BitmapUtil.CROP_IMAGE_REQUEST:
                    BitmapUtil.handleCrop(data, b.ivPicture, this);

                    return;
                case BitmapUtil.CAMERA_REQUEST:
                    BitmapUtil.cropPhoto(BitmapUtil.capturedImageUri, this);
                    break;
                case BitmapUtil.GALLERY_REQUEST:
                    BitmapUtil.capturedImageUri = data.getData();
                    BitmapUtil.cropPhoto(BitmapUtil.capturedImageUri, this);
                    break;

            }
    }










    private void updateFresco(String path) {









        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        Uri uri= Uri.parse(path);

// combines above two lines
        imagePipeline.evictFromCache(uri);
//        As above, evictFromDiskCache(Uri) assumes you are using the default cache key factory. Users with a custom factory should use evictFromDiskCache(ImageRequest) instead.
//
//                Clearing the cache
//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        imagePipeline.clearMemoryCaches();
//        imagePipeline.clearDiskCaches();

// combines above two lines
//        imagePipeline.clearCaches();


    }

    private void showChoosePhotoDialog() {
        if (adPhotoChoose == null) {
            View rootView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_select_photo_source, null);
            DialogSelectPhotoSourceBinding b = DataBindingUtil.bind(rootView);
            b.setListener(this);
            adPhotoChoose = new AlertDialog
                    .Builder(getActivity(), R.style.MyDialogTheme)
                    .setView(rootView)
                    .setCancelable(true)
                    .create();
            adPhotoChoose.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        adPhotoChoose.show();

    }

    private void showDeleteDialog() {

        if (adDeleteUser == null) {
            View rootView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_delete_user, null);
            DialogDeleteUserBinding b = DataBindingUtil.bind(rootView);
            b.setListener(this);
            adDeleteUser = new AlertDialog
                    .Builder(getActivity(), R.style.MyDialogTheme)
                    .setView(rootView)
                    .create();
            adDeleteUser.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        adDeleteUser.show();
    }

    @Override
    public void onIndustriesChanged(List<String> _industries) {
        mUser.setIndustries(_industries);
        b.tvIndustries.setText(mUser.getIndustriesString());
    }


    private void saveUserChanges() {
        mActivity.showProgress();
        Subscription subscription = RetrofitApi
                .getInstance()
                .user()
                .updateUser(mUser)
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSaveUserSuccess, this::handleError);

        mSubscriptions.add(subscription);

    }



    // network calls

    private void updatePicture(UserMe user){

        Subscription subscription  = RetrofitApi
                .getInstance()
                .user()
                .uploadUserImage(user)
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSaveChangePictureSuccess,this::handleError);

        mSubscriptions.add(subscription);
    }


    private void onSaveChangePictureSuccess(UserMe userMe){

        updateFresco(userMe.getPicture());
        mActivity.refreshUser(mUser);
        mActivity.stopProgress();
    }

    private void onSaveUserSuccess(UserMe user){
        mDao.saveUserMe(mActivity.getContentResolver(), user);
        mActivity.getContentResolver().notifyChange(UserProvider.USER_ME_URI,null,false);
        if(imageUser == null)mActivity.stopProgress();
        else updatePicture(imageUser);

    }






    private void deleteUser() {
        adDeleteUser.dismiss();
        mActivity.showProgress();

        Subscription subscription = RetrofitApi
                .getInstance()
                .user()
                .removeUser(mUser.getId())
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLogoutSuccess,this::handleError);

        mSubscriptions.add(subscription);
    }


    public void logout() {
        mActivity.showProgress();
        Subscription subscription = RetrofitApi
                .getInstance()
                .user()
                .logoutUser(mUser.getId())
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLogoutSuccess, this::handleError);


        mSubscriptions.add(subscription);

    }


    private void onLogoutSuccess(BaseResponse response) {

        mActivity.logout();
        mActivity.stopProgress();

    }




    @Override
    protected void handleError(Throwable throwable) {
        mActivity.handleError(throwable);
    }


    // parent fragment callback
    @Override
    public void onMenuClicked() {
        closeKeyboard();
        mActivity.openMainMenu();
    }

    @Override
    public void onSaveClicked() {
        closeKeyboard();
        saveUserChanges();
    }





    //listeners

    public void onShowMe(View view){
        mActivity.replaceFragmentLeft(PeopleDetailFragment.getInstance(mUser.getId()));

    }

    public void onPictureClicked(View view) {
        showChoosePhotoDialog();
    }


    public void onIndustriesClicked(View view) {
        startIndustriesFragment();
    }

    public void onRootClicked(View view) {
        closeKeyboard();
    }

    public void onLogoutClicked(View view) {
        logout();
    }

    public void onDeleteClicked(View view) {
        showDeleteDialog();
    }

    @Override
    public void onDialogDeleteCancel(View view) {
        adDeleteUser.dismiss();
    }

    @Override
    public void onDialogDeleteUser(View view) {
        deleteUser();
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

    @Override
    public void onPhotoUpdate(String base64Image) {
        imageUser = new UserMe();
        imageUser.setImageBase64(base64Image);
    }


}

