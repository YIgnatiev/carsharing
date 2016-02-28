package pro.theboard.fragments.cards_fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lapioworks.cards.R;
import com.lapioworks.cards.databinding.FragmentQuestionWithImageBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pro.theboard.constants.Constants;
import pro.theboard.custom_views.RoundedDrawable;
import pro.theboard.models.cards.Answer;
import pro.theboard.utils.BitmapDecoder;
import pro.theboard.utils.CalleryMediaGet;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 22/03/15.
 */

public class CardWithUploadPhotoFragment extends BaseFragment<FragmentQuestionWithImageBinding> {


    private File photoFile;
    private AlertDialog dialog;
    private Answer mAnswer;
    private boolean mIsAnswered = false;


    @Override
    protected void initCardView(LayoutInflater inflater, ViewGroup container) {

        b = DataBindingUtil.inflate(inflater, R.layout.fragment_question_with_image, container, false);
        b.setListener(this);
        b.ivPhoto.setOnTouchListener(this::handleTouch);
        setQuestion();
        mAnswer = new Answer(mCardModel.getHash());
        subscribeToNetwork();
        handleScroll(b.scrollView);
    }

    private float localX;
    private float localY;
    private boolean handleTouch(View v, MotionEvent event){

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                localX = event.getRawX();
                localY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:

                if(X-localX == 0 && Y-localY == 0){
                    showChooserDialog();
                }
                break;


        }

        return this.onTouch(b.getRoot(),event);
    }

    private void setQuestion() {
        float radius = getResources().getDimension(R.dimen.corner_radius);
        RoundedDrawable drawable = new RoundedDrawable(getResources().getColor(mColorResource), radius);
        if (isPromo()) {
            b.tvSubmit.setBackground(getResources().getDrawable(R.drawable.background_button_promo));
            b.tvSubmit.setText("Poista");
        }
        b.llBottom.setBackground(drawable);
        setData(b.tvQuestion, b.llQuestionLayout);

    }


    @Override
    protected void handleNetwork(Boolean isNetworkActive) {
        b.tvSubmit.setEnabled(isNetworkActive);
        if (!isNetworkActive)
            Toast.makeText(mActivity, "No internet connection", Toast.LENGTH_SHORT).show();
    }


    private void dispathcChoosePictureIntent() {

        if (Build.VERSION.SDK_INT < 19) {
            Intent pickerIntent = new Intent();
            pickerIntent.setType("image/*");
            pickerIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(pickerIntent, Constants.GALLERY_IMAGE_RESULT);
        } else {
            Intent pickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            pickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
            pickerIntent.setType("image/*");
            startActivityForResult(pickerIntent, Constants.GALLERY_KITKAT_INTENT_CALLED);
        }

    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, Constants.PHOTO_IMAGE_RESULT);
            }
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalCacheDir();

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
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
                switch (requestCode) {
                    case Constants.GALLERY_IMAGE_RESULT:
                        if (data.getData() != null) {
                            filePath = getFilePathFromProvider(data.getData());
                        }
                        break;
                    case Constants.GALLERY_KITKAT_INTENT_CALLED:
//                        int takeFlags = data.getFlags()
//                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
//                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                        getActivity().getContentResolver().takePersistableUriPermission(data.getData(), takeFlags);
                        filePath = getFilePathFromProvider(data.getData());
                        break;
                    case Constants.PHOTO_IMAGE_RESULT:
                        Toast.makeText(mActivity, "Photo", Toast.LENGTH_SHORT).show();
                        filePath = photoFile.getAbsolutePath();
                }


                if (!filePath.isEmpty()) {
                    setImage(filePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //"android.permission.READ_EXTERNAL_STORAGE"


            }
        }
    }


    private void showChooserDialog() {
        if (dialog == null) {

            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_select_photo_source, null);
            dialogView.findViewById(R.id.imagebutton_Gallery_DialogSelectPhotoSource).setOnClickListener(this::onPicture);
            dialogView.findViewById(R.id.imagebutton_Camera_DialogSelectPhotoSource).setOnClickListener(this::onCamera);
            dialog = new AlertDialog
                    .Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
                    .setView(dialogView)
                    .create();
        }
        dialog.show();
    }

    private void setImage(String _imagePath) {
        mIsAnswered = true;
        Bitmap bitmap = BitmapDecoder.decodeBitmap(_imagePath, 200, 200);
        BitmapDecoder.toBase64(bitmap, mAnswer::addData);
        b.ivPhoto.setImageBitmap(bitmap);
    }

    private void checkForGalleryPermission() {
        mActivity.checkImagePermissionPermission((isGranted) -> {
            if (isGranted) dispathcChoosePictureIntent();
        });
    }

    public void onAnswer(View view) {
        if (mIsAnswered)
            sendAnswer(mCardModel, mAnswer);
        else
            Toast.makeText(mActivity, getString(R.string.choose_photo_first), Toast.LENGTH_SHORT).show();
    }

    public void onPicture(View view) {
        dialog.dismiss();
        checkForGalleryPermission();
    }

    public void onCamera(View view) {
        dialog.dismiss();
        dispatchTakePictureIntent();
    }


}
