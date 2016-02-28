package ru.lead2phone.ru.lead2phone.fragments;

import android.app.Activity;
import android.app.Fragment;

import ru.lead2phone.ru.lead2phone.MainActivity;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 8/26/15.
 */
public abstract class BaseFragment extends Fragment{
    protected MainActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity)activity;
    }
}
