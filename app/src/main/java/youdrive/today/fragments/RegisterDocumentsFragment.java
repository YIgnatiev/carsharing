package youdrive.today.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import youdrive.today.R;
import youdrive.today.activities.RegistrationNewActivity;
import youdrive.today.databinding.FragmentRegisterDocumentsBinding;
import youdrive.today.databinding.ItemImageBinding;
import youdrive.today.models.RegistrationUser;
import youdrive.today.response.RegistrationModel;
import youdrive.today.response.UploadCareResponse;
import youdrive.today.response.UploadGroupResponse;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/25/15.
 */
public class RegisterDocumentsFragment extends BaseFragment<RegistrationNewActivity> {
    private FragmentRegisterDocumentsBinding b;
    private String parameter = "files[%d]";
    private Subscription uploadingSubscription;
    private Subscription uploadGroupSubscription;
    private Subscription mUpdateSubscription;
    private static final int ACTIVITY_CHOOSE_FILE = 3;
    private Map<String, String> params;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (b == null) {
            b = DataBindingUtil.inflate(inflater, R.layout.fragment_register_documents, container, false);
            b.setListener(this);
            b.tvForvard.setEnabled(false);
            params = new HashMap<>();
        }
        return b.getRoot();
    }

    @Override
    public void onStop() {
        if (uploadGroupSubscription != null) uploadGroupSubscription.unsubscribe();
        if (uploadingSubscription != null) uploadingSubscription.unsubscribe();
        if (mUpdateSubscription != null) mUpdateSubscription.unsubscribe();
        super.onStop();
    }

    private void onUploadGroupSuccess(UploadGroupResponse response) {
        mActivity.mUser.setDocuments_storage_url(response.getUrl());
        updateUser(mActivity.userId,mActivity.mUser);
    }

    private void onUploadGroupsFailed(Throwable t) {
        mActivity.hideProgress();
        Toast.makeText(mActivity, "Oшибка сети", Toast.LENGTH_SHORT).show();
    }

    private void chooseFile() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ACTIVITY_CHOOSE_FILE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == ACTIVITY_CHOOSE_FILE) {
            Uri uri = data.getData();
            String filePath = getRealPathFromURI(uri);
            addImageView(filePath);
        }
    }

    public void onUploadSuccess(UploadCareResponse file, ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
        addParams(file.getFile());
    }

    public void addParams(String id) {
        String param = String.format(parameter, params.size() + 1);
        params.put(param, id);
        if (params.size() >= 2) b.tvForvard.setEnabled(true);
        if (params.size() > 5) b.btnLoad.setEnabled(false);
    }

    public void onUploadFailure(Throwable t, ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(mActivity, "failure", Toast.LENGTH_SHORT).show();
    }

    private void addImageView(String filePath) {
        Bitmap bitmap = decodeSampledBitmapFromFile(filePath);
        ItemImageBinding item = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.item_image, null, false);
        item.ivThumb.setImageBitmap(bitmap);
        b.glImages.addView(item.getRoot());
        uploadFile(filePath, item.pbLoading);
    }

    private void uploadFile(String filePath, ProgressBar bar) {
        uploadingSubscription = mActivity.mClient.uploadFile(new File(filePath))
                .retry(3)
                .timeout(15, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onUploadSuccess(response, bar), error -> onUploadFailure(error, bar));
    }

    public static Bitmap decodeSampledBitmapFromFile(String path) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, 300, 300);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mActivity.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void updateUser(String userId, RegistrationUser user) {
        user.setOnline_contract_signed(true);
        mActivity.showProgress();
        mUpdateSubscription = mActivity.mClient
                .updateUser(userId, user)
                .subscribe(this::onUpdateSuccess, mActivity::onCreateFailure);
    }

    public void onUpdateSuccess(RegistrationModel model) {
        mActivity.hideProgress();
        mActivity.mUser = model.getData();
        mActivity.startFragmentLeft(new RegisterPaymentsFragment());
    }

    public void onForvard(View view) {
        mActivity.showProgress();
        uploadGroupSubscription = mActivity.mClient
                .uploadGroup(params)
                .retry(3)
                .timeout(15, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUploadGroupSuccess, this::onUploadGroupsFailed);
    }

    public void onLoad(View view) {
        mActivity.getWriteExternalPermission(this::chooseFile);
    }

    public void onBack(View view) {
        mActivity.getFragmentManager().popBackStack();
    }
}



