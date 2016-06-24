package youdrive.today.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import com.dd.CircularProgressButton;

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



    public static void about(Context context){
        context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://youdrive.today/")));
    }


    public static void restore(Context context){
        context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://youdrive.today/password_reset")));
    }

    public static String toKm(int meters) {
        return String.format("%.2f км", meters * 0.001);
    }

    public static String toTime(int seconds) {
        long minute = seconds/60;
        return (String.valueOf(minute) + " мин");
    }

    public static void success(CircularProgressButton button) {
        button.setProgress(100);
    }
}

