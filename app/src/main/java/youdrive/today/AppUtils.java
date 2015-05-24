package youdrive.today;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.dd.CircularProgressButton;

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
        }, 2000);
    }

    public static void error(Context context, Handler handler, String text, final CircularProgressButton button) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();

        button.setProgress(-1);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setProgress(0);
            }
        }, 2000);
    }

    public static void success(CircularProgressButton button) {
        button.setProgress(100);
    }

    public static ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setEnabled(false);
        }
    };

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

}

