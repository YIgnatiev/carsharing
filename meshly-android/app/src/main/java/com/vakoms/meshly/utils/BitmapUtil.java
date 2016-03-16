package com.vakoms.meshly.utils;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.android.camera.CropImageIntentBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vakoms.meshly.interfaces.PhotoUpdateListener;
import com.vakoms.meshly.views.CircleDrawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import static com.vakoms.meshly.constants.Constants.CROPPED_IMAGE_HEIGHT;
import static com.vakoms.meshly.constants.Constants.CROPPED_IMAGE_WIDTH;

/**
 * Created by Sviatoslav Kashchin on 17.02.15.
 */
public class BitmapUtil {

    private static final int imageWidth = 1000;
    private static final int imageHeight = 1000;
    public static final int CAMERA_REQUEST = 700;
    public static final int GALLERY_REQUEST = 300;

    public static final int CROP_IMAGE_REQUEST = 500;

    public static Uri capturedImageUri;
    private static File croppedImageFile;
    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff142A32;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        //TODO: calculate best size for device
        output = Bitmap.createScaledBitmap(output, 128, 128, true);
        return output;
    }




    public static Bitmap decodeSampledBitmapFromFile(String path) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, imageWidth, imageHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);

    }


    public static Bitmap getBitmapFromUri(Uri uri, ContentResolver contentResolver) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    private static byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
        return bos.toByteArray();

    }

    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {

        byte[] array = getByteArray(bitmap);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeByteArray(array, 0, array.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(array, 0, array.length, options);

    }

    public static Bitmap decodeSampledBitmapFromBytes(byte[] byteArray, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

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

    public static void saveBase64ImageAsync(Bitmap _bitmap) {

    }


    public static Bitmap getBitmapFromBase64(String _image) {
        if (_image.equals("")) return null;
        byte[] bitmapArray = Base64.decode(_image, Base64.DEFAULT);

        return decodeSampledBitmapFromBytes(bitmapArray, CROPPED_IMAGE_WIDTH, CROPPED_IMAGE_HEIGHT);

    }






    public static void cropPhoto(Uri _uri, Fragment activity) {
        //Uri outputUri = getUriFromFolder();

        performCrop(_uri,activity);
    }

    public static void handleCrop(Intent data,SimpleDraweeView imageView ,PhotoUpdateListener callback) {

        Bitmap thePic = BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath());


           // capturedImageUri = result.getData();

        //   Bitmap bitmap = decodeSampledBitmapFromFile(uri.getPath());

         new ConvertToBase64Task(callback).execute(thePic);

        //Log.v("crop bitmap", String.valueOf(bitmap));

//
//            RoundingParams roundingParams = RoundingParams
//                    .asCircle()
//                    .setRoundingMethod(RoundingParams.RoundingMethod.OVERLAY_COLOR);
//
//            roundingParams.setRoundAsCircle(true);
//                    b.ivPicture.getHierarchy().setRoundingParams(roundingParams);


           imageView.setImageDrawable(new CircleDrawable(thePic));


    }



    public static void takePhotoByCamera(Fragment _fragment) {
        capturedImageUri = getUriFromFolder();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
        _fragment.startActivityForResult(intent, CAMERA_REQUEST);
    }

    public static void takePhotoFromGallery(Fragment _fragment) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        _fragment.startActivityForResult(intent, GALLERY_REQUEST);
    }





    private static void performCrop(Uri picUri , Fragment activity) {
        try {

            croppedImageFile = new File(activity.getActivity().getFilesDir(), "avatar.jpg");
            Uri croppedImage = Uri.fromFile(croppedImageFile);


            CropImageIntentBuilder cropImage = new CropImageIntentBuilder(400, 400, croppedImage);
            cropImage.setSourceImage(picUri);


            activity.startActivityForResult(cropImage.getIntent(activity.getActivity()), CROP_IMAGE_REQUEST);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
           Log.v("Crop", "Whoops - your device doesn't support the crop action!");

        }
    }




    private static Uri getUriFromFolder() {
        Calendar cal = Calendar.getInstance();

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/meshly/");
        boolean success = dir.exists() && dir.isDirectory();

        if (!dir.exists()) {//create folders
            success = dir.mkdirs();
        }
        if (success) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/meshly/",
                    (cal.getTimeInMillis() + ".jpg"));
            if (!file.exists()) {//create file for new photo
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return Uri.fromFile(file);
        }
        return null;
    }





}