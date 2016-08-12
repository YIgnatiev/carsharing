package youdrive.today.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/25/15.
 */
public class BaseFragment<T extends Activity> extends Fragment {

    protected T mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity = (T) getActivity();
        super.onCreate(savedInstanceState);
    }
}
