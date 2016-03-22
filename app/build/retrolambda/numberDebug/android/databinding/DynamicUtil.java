package android.databinding;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
public class DynamicUtil {
    @SuppressWarnings("deprecation")
    public static ColorStateList getColorStateListFromResource(View root, int resourceId) {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            return root.getContext().getColorStateList(resourceId);
        }
        return root.getResources().getColorStateList(resourceId);
    }
    @SuppressWarnings("deprecation")
    public static Drawable getDrawableFromResource(View root, int resourceId) {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            return root.getContext().getDrawable(resourceId);
        }
        return root.getResources().getDrawable(resourceId);
    }
}