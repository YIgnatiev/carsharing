package pro.theboard;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.lapioworks.cards.R;

import pro.theboard.listeners.Function;
import pro.theboard.models.cards.Answer;
import pro.theboard.utils.FontOverride;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 7/29/15.
 */
public abstract class BaseActivity extends YouTubeBaseActivity {

    private final int IMAGE_PERMISSION = 9;
    private Function<Boolean> mImagePermissoin;
    private static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";

    protected CompositeSubscription mSubscription = new CompositeSubscription();

    public abstract void setToolbarTitle(String _title);

    @Override
    protected void onResume() {
        super.onResume();
        mSubscription = new CompositeSubscription();

        FontOverride.setDefaultFont(this, "MONOSPACE", getString(R.string.font_regular));
    }


    public abstract void getQuestions();

    public abstract void sendAnswer(Answer _answer);


    @Override
    protected void onStop() {
        mSubscription.unsubscribe();
        super.onStop();
    }


    public void checkImagePermissionPermission(Function<Boolean> photoPermission) {
        mImagePermissoin = photoPermission;

        int permissionCheck = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, IMAGE_PERMISSION);
            }
        } else mImagePermissoin.apply(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case IMAGE_PERMISSION:
                if (mImagePermissoin != null)
                    mImagePermissoin.apply(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
                break;
        }
    }
}
