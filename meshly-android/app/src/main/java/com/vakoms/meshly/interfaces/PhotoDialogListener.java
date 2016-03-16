package com.vakoms.meshly.interfaces;

import android.view.View;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/23/15.
 */
public interface PhotoDialogListener {
    
   void onDialogPhotoGallery(View view);

   void onDialogPhotoCamera(View view);

   void onDialogPhotoCancel(View view);
}
