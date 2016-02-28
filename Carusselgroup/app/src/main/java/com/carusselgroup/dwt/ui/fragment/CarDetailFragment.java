package com.carusselgroup.dwt.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carusselgroup.dwt.R;
import com.carusselgroup.dwt.adapter.CarPhotosAdapter;
import com.carusselgroup.dwt.model.Car;
import com.carusselgroup.dwt.model.ImageCar;
import com.carusselgroup.dwt.rest.CarsNewApi;
import com.carusselgroup.dwt.rest.IResponse;
import com.carusselgroup.dwt.ui.activity.MainActivity;
import com.carusselgroup.dwt.utils.CalleryMediaGet;
import com.carusselgroup.dwt.utils.CustomLinearLayout;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarDetailFragment extends Fragment implements OnItemLongClickListener, AdapterView.OnItemClickListener  , View.OnClickListener{

    public static final String CAR_TAG = "com.carusselgroup.dwt.ui.fragment.CAR_TAG";
    public static final String TEMPORARY_FILE_NAME = "tmp_image.png";
    private static final int GALLERY_IMAGE_RESULT = 0;
    private static final int GALLERY_KITKAT_INTENT_CALLED = 3;

    private final int REQUIRED_WIDTH = 400;
    private final int REQUIRED_HEIGHT = 400;
    private final int NINETY_DEGREE_LEFT = 90;
    private final int NINETY_DEGREE_RIGHT = 270;
    private View.OnClickListener mPhotoSourceDialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
            switch (v.getId()) {
                case R.id.imagebutton_Gallery_DialogSelectPhotoSource:
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
                    break;
                case R.id.imagebutton_Camera_DialogSelectPhotoSource:
                    dispatchTakePictureIntent();
                    break;
            }
        }
    };
    private static final int CAMERA_IMAGE_RESULT = 1;
    private static final int DELETE_OPTION = 0;
    private static final int SAVE_OPTION = 1;
    private static final int COPY_OPTION = 2;
    private Car mCar;
    private View listFooter, footerPlaceholder;
    private TextView carModel, carDesciption;
    private TextView tvBack;
    private DynamicListView photosListView;
    private CarPhotosAdapter mPhotosListAdapter;
    private Uri mPhotoFileUri;
    private String mPhotoPath;
    private boolean isDraging = false;
    private int mFromPos = -1;
    private int mFromImgId;
    private int mToImgId;
    private int mToPos;
    private boolean isReordering = false;
    private OnDragListener mDragAndDropListener = new OnDragListener() {

        @Override
        public void OnSwap(final int newPos, final int oldPos) {
            if (mFromPos == -1) {
                mFromPos = oldPos + 1;
                mFromImgId = mPhotosListAdapter.getItem(oldPos).id;
            }
            Log.d("cs_c","OnSwap oldPos: " + oldPos + " newPos: " + newPos);
            mToPos = newPos + 1;
            mToImgId = mPhotosListAdapter.getItem(newPos).id;
            if (isReordering && isDraging) {
                mPhotosListAdapter.hardSwap(oldPos, newPos);
            }
        }
    };
    private MainActivity mActivity;
    private AlertDialog dialog;
    private CustomLinearLayout parentLayout;

    public static CarDetailFragment newInstance(Car car) {
        CarDetailFragment fragment = new CarDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(CAR_TAG, car);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mCar = args.getParcelable(CAR_TAG);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_car_detail, container, false);
        listFooter = getFooterView(inflater);

        carModel = (TextView) rootView.findViewById(R.id.textview_CarModel_CarDetailFragment);
        carDesciption = (TextView) rootView.findViewById(R.id.textview_CarDescription_CarDetailFragment);

        tvBack =  (TextView) rootView.findViewById(R.id.textView_back_DetailFragment);
        tvBack.setOnClickListener(this);



        getCarDetail();
        initViews(rootView);
        return rootView;
    }

    private void getCarDetail() {

        if (mCar != null) {
            mActivity.progressDialogShow();
            CarsNewApi.getCarDetail(mCar.getId(), new IResponse() {
                @Override
                public void onSuccess(Object response) {
                    mCar = (Car) response;
                    mActivity.progressDialogHide();
                    carModel.setText(mCar.getBrand() + " " + mCar.getModel());
                    carDesciption.setText(mCar.getCharacteristics());
                    mPhotosListAdapter = new CarPhotosAdapter(getActivity(), mCar.getImages());
                    photosListView.setAdapter(mPhotosListAdapter);
                    mPhotosListAdapter.notifyDataSetChanged();
                    mPhotosListAdapter.setmDragListener(mDragAndDropListener);
                }

                @Override
                public void onFailure(Object response) {
                    mActivity.progressDialogHide();
                }
            });

        }
    }

    private View getFooterView(LayoutInflater inflater) {
        View footer = inflater.inflate(R.layout.layout_car_detail_list_footer, new FrameLayout(getActivity()));
        footerPlaceholder = footer.findViewById(R.id.layout_ImagePlaceholder_CarDetailFragment);
        return footer;
    }

    private void initViews(View v) {
        photosListView = (DynamicListView) v.findViewById(R.id.listview_CarPhotosList_CarDetailFragment);
        photosListView.addFooterView(listFooter);
        photosListView.enableDragAndDrop();
        photosListView.setOnItemLongClickListener(this);
        photosListView.setOnItemClickListener(this);
        photosListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //    Log.d("cs_c", "MotionEvent.ACTION_DOWN:");
                        isReordering = true;
                        break;
                }
                return false;
            }
        });
        parentLayout = (CustomLinearLayout) v.findViewById(R.id.parent_LinearLayout_CustomLinearLayout);
        parentLayout.setmTouchListener(new OnTouchDetection() {
            @Override
            public void OnTouchUp() {
                if (isDraging)
                    sendReorderRequest();
                isReordering = false;
                isDraging = false;
            }
        });
        registerForContextMenu(photosListView);


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        photosListView.startDragging(position - photosListView.getHeaderViewsCount());
        isDraging = true;
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView) parent;
        if (position >= listView.getCount() - listView.getFooterViewsCount()) {
            showCarPhotoDialog();
        } else {
            showContextMenu(position - listView.getHeaderViewsCount());
        }
    }

    private void showCarPhotoDialog() {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);

            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_select_photo_source, null);

            View galleryButton = dialogView.findViewById(R.id.imagebutton_Gallery_DialogSelectPhotoSource);
            galleryButton.setOnClickListener(mPhotoSourceDialogListener);

            View cameraButton = dialogView.findViewById(R.id.imagebutton_Camera_DialogSelectPhotoSource);
            cameraButton.setOnClickListener(mPhotoSourceDialogListener);

            builder.setView(dialogView);
            builder.setTitle(getString(R.string.dialog_select_photo_source_title));
            dialog = builder.create();
        }
        dialog.show();
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
                } else if (requestCode == CAMERA_IMAGE_RESULT) {
                    filePath = mPhotoPath;
                } else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
                    int takeFlags = data.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    // Check for the freshest data.
                    getActivity().getContentResolver().takePersistableUriPermission(data.getData(), takeFlags);
                    filePath = getFilePathFromProvider(data.getData());

                }
                if (!filePath.isEmpty()) {
                    createDialog(filePath);
                    //uploadPhoto(filePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadPhoto(final String filePath) throws FileNotFoundException {
        int position = mPhotosListAdapter.getCount() + 1;
        mActivity.progressDialogShow();
        CarsNewApi.uploadPhoto(mCar.getId(),
                -1,
                position,
                false,
                filePath,
                new IResponse() {
                    @Override
                    public void onSuccess(Object response) {
                        ImageCar uploadedImage = (ImageCar) response;
                        uploadedImage.setUploadedFilePath(filePath);
                        mPhotosListAdapter.addItem(uploadedImage);
                        mPhotosListAdapter.notifyDataSetChanged();
                        mActivity.progressDialogHide();
                    }

                    @Override
                    public void onFailure(Object response) {
                        mActivity.progressDialogHide();
                    }
                });
    }


    private void createDialog(final String _filePath) {

        View viewGroup = mActivity.getLayoutInflater().inflate(R.layout.dialog_layout, new LinearLayout(mActivity));

        ImageView imageViewRotateLeft = (ImageView) viewGroup.findViewById(R.id.imageView_dialog_rotate_left);
        ImageView imageViewRotateRight = (ImageView)viewGroup.findViewById(R.id.imageView_dialog_rotate_right);
        ImageView imageViewClose = (ImageView) viewGroup.findViewById(R.id.imageView_dialog_close);
        final ImageView imageViewMainImage = (ImageView)viewGroup.findViewById(R.id.imageView_dialog_mainImage);
        ImageView imageViewSave = (ImageView)viewGroup.findViewById(R.id.imageView_dialog_save);

        Bitmap bitmap = decodeBitmap(_filePath , REQUIRED_WIDTH, REQUIRED_HEIGHT);
        saveToFile(bitmap,TEMPORARY_FILE_NAME);
        imageViewMainImage.setImageBitmap(bitmap);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(viewGroup);

        final AlertDialog adDialog = builder.create();
        //adDialog.setOnShowListener(this);

        adDialog.show();

          View.OnClickListener dialogListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.imageView_dialog_close:
                        adDialog.cancel();
                        break;
                    case R.id.imageView_dialog_rotate_left:
                        rotateImage(imageViewMainImage,_filePath, NINETY_DEGREE_RIGHT);
                        break ;
                    case R.id.imageView_dialog_rotate_right:
                        rotateImage(imageViewMainImage, _filePath, NINETY_DEGREE_LEFT);
                        break;
                    case R.id.imageView_dialog_save:
                        adDialog.cancel();
                        try {

                            uploadPhoto(mActivity.getFilesDir()+ "/"+ TEMPORARY_FILE_NAME);
                        }catch(FileNotFoundException ex){
                            ex.printStackTrace();
                            }
                        break;
                }
            }
        };
        imageViewClose.setOnClickListener(dialogListener);
        imageViewRotateLeft.setOnClickListener(dialogListener);
        imageViewRotateRight.setOnClickListener(dialogListener);
        imageViewSave.setOnClickListener(dialogListener);

    }






    private void rotateImage( ImageView imageView,String _filePath,  int degree) {

        //_filePath += SMALL_IMAGE_END;

       Matrix mat = new Matrix();


        Bitmap bitmap =  ((BitmapDrawable)imageView.getDrawable()).getBitmap() ;
        mat.postRotate(degree);
       Bitmap bMapRotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);

        imageView.setImageBitmap(bMapRotate);


        saveToFile(bMapRotate,TEMPORARY_FILE_NAME);
    }

    private void saveToFile(Bitmap bitmap ,String tempImage) {
        try {
            FileOutputStream fOut = getActivity().openFileOutput(tempImage, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void showContextMenu(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.car_details_photo_context_menu_items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DELETE_OPTION:
                        deletePhoto(position);
                        break;
                    case SAVE_OPTION:
                        saveImage(position);
                        break;
                    case COPY_OPTION:
                        copyUrl(position);
                        break;
                }
            }
        });

        builder.create().show();
    }

    private void deletePhoto(final int position) {
        mActivity.progressDialogShow();
        CarsNewApi.deleteCarImage(
                mCar.getId(),
                mPhotosListAdapter.getItem(position).id,
                new IResponse() {
                    @Override
                    public void onSuccess(Object response) {
                        mPhotosListAdapter.removeItem(position);
                        mActivity.progressDialogHide();
                    }

                    @Override
                    public void onFailure(Object response) {
                        mActivity.progressDialogHide();
                    }
                }
        );
    }

    private void saveImage(int position) {
        try {
            DownloadManager downloadManager = (DownloadManager) mActivity.getSystemService(mActivity.getApplicationContext().DOWNLOAD_SERVICE);
            Uri Download_Uri = Uri.parse(mPhotosListAdapter.getItem(position).getOrigUrl());

            DownloadManager.Request request = new DownloadManager.Request(Download_Uri)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, Download_Uri.getLastPathSegment())
                    .setTitle(mCar.getBrand() + " " + mCar.getModel())
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            downloadManager.enqueue(request);
        } catch (Exception e) {
        }
    }

    private void copyUrl(int position) {
        ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(mActivity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("image URL", mPhotosListAdapter.getItem(position).getOrigUrl());
        clipboard.setPrimaryClip(clip);
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
    public void onPause() {
        super.onPause();
        mActivity.progressDialogHide();
    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
            }
            if (photoFile != null) {
                mPhotoFileUri = Uri.fromFile(photoFile);
                mPhotoPath = photoFile.getAbsolutePath();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoFileUri);
                startActivityForResult(intent, CAMERA_IMAGE_RESULT);
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

    private void sendReorderRequest() {
        if (mFromPos == -1 || mFromPos == mToPos)
            return;
        mActivity.progressDialogShow();
        Log.d("cs_c", "mFromPos: " + mFromPos + " mToPos : " + mToPos);
        CarsNewApi.reOrderCar(mCar.getId(),
                mFromImgId,
                mToPos,
                new IResponse() {
                    @Override
                    public void onSuccess(Object response) {
                        CarsNewApi.reOrderCar(mCar.getId(),
                                mToImgId,
                                mFromPos,
                                new IResponse() {
                                    @Override
                                    public void onSuccess(Object response) {
                                        getCarDetail();
                                        mFromPos = -1;
                                    }

                                    @Override
                                    public void onFailure(Object response) {
                                        mActivity.progressDialogHide();
                                        Toast.makeText(mActivity, "Faild", Toast.LENGTH_SHORT).show();
                                        mFromPos = -1;
                                    }
                                });
                    }

                    @Override
                    public void onFailure(Object response) {
                        mActivity.progressDialogHide();
                        Toast.makeText(mActivity, "Faild", Toast.LENGTH_SHORT).show();
                        mFromPos = -1;
                    }
                });
    }






    public static int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeBitmap(String _filePahth, int reqWidth, int reqHeight) {


        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        Bitmap first = BitmapFactory.decodeFile(_filePahth,options);
        // Calculate inSampleSize

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap second = BitmapFactory.decodeFile(_filePahth , options);

        return second;
    }



    public interface OnDragListener {
        public void OnSwap(int oldPos, int newPos);
    }

    public interface OnTouchDetection {
        public void OnTouchUp();
    }

    @Override
    public void onClick(View v) {
        getActivity().onBackPressed();
    }
}