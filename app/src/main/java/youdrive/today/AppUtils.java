package youdrive.today;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;

/**
 * Created by psuhoterin on 20.05.15.
 */
public class AppUtils {

    public static void error(String text, final CircularProgressButton button) {
        Toast.makeText(button.getContext(), text, Toast.LENGTH_LONG).show();

        button.setProgress(-1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setProgress(0);
            }
        }, 1500);
    }

    public static void success(final CircularProgressButton button, final String string) {
        button.setProgress(100);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setProgress(0);
                button.setIdleText(string);
            }
        }, 1500);
    }

    public static ButterKnife.Setter<View, Boolean> ENABLED = new ButterKnife.Setter<View, Boolean>() {
        @Override
        public void set(View view, Boolean value, int index) {
            view.setEnabled(value);
        }
    };

    public static void about(Context context){
        context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://youdrive.today/")));
    }

    public static String toKm(int meters) {
        return meters * 0.001 + " ";
    }

    public static String toTime(int seconds) {
        long minute = TimeUnit.SECONDS.toMinutes(seconds);
        return minute + " ";
    }

    public static void success(CircularProgressButton button) {
        button.setProgress(100);
    }
}

