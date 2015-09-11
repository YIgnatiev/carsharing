package youdrive.today.views;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/11/15.
 */
public class PicassoMarker implements Target {
    MarkerOptions mMarker;

    public PicassoMarker(MarkerOptions marker) {
        mMarker = marker;
    }

    @Override
    public int hashCode() {
        return mMarker.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PicassoMarker) {
            MarkerOptions marker = ((PicassoMarker) o).mMarker;
            return mMarker.equals(marker);
        } else {
            return false;
        }
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        mMarker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}

