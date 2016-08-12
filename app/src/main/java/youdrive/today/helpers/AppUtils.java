package youdrive.today.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import youdrive.today.R;
import youdrive.today.models.ReferralRules;

/**
 * Created by psuhoterin on 20.05.15.
 */
public class AppUtils {

    public static void error(String text, final CircularProgressButton button) {
        Toast.makeText(button.getContext(), text, Toast.LENGTH_LONG).show();

        button.setProgress(-1);

        new Handler().postDelayed(() -> button.setProgress(0), 1500);
    }

    public static void success(final CircularProgressButton button, final String string) {
        button.setProgress(100);

        new Handler().postDelayed(() -> {
            button.setProgress(0);
            button.setIdleText(string);
        }, 1500);
    }

    /***
     * Текст для шаринга
     */
    public static String getShareText(Context cntx, ReferralRules referralRules) {
        return cntx.getString(R.string.referral_sms_text,
                referralRules.referral_code,
                referralRules.invitee_bonus_part,
                referralRules.max_invitee_bonus / 100,
                referralRules.registration_link);
    }


    public static void about(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://youdrive.today/")));
    }


    public static void restore(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://youdrive.today/password_reset")));
    }

    public static String toKm(int meters) {
        return String.format("%.2f км", meters * 0.001);
    }

    public static String toTime(int seconds) {
        long minute = seconds / 60;
        return (String.valueOf(minute) + " мин");
    }

    public static void success(CircularProgressButton button) {
        button.setProgress(100);
    }

    /***
     * Входит ли точка в область
     */
    public static boolean isPointInPolygon(double latitude, double longitude, PolygonOptions polygonOptions) {
        int intersectCount = 0;
        int size = polygonOptions.getPoints().size();
        for (int i = 0; i < size - 1; i++) {
            if (rayCastIntersect(latitude, longitude, polygonOptions.getPoints().get(i), polygonOptions.getPoints().get(i + 1))) {
                intersectCount++;
            }
        }

        return (intersectCount % 2) == 1; // odd = inside, even = outside;
    }

    private static boolean rayCastIntersect(double latitude, double longitude, LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = latitude;
        double pX = longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY) || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX);               // Rise over run
        double bee = (-aX) * m + aY;                // y = mx + b
        double x = (pY - bee) / m;                  // algebra is neat!

        return x > pX;
    }
}

