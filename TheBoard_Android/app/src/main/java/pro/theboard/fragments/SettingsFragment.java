package pro.theboard.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lapioworks.cards.R;

import pro.theboard.MainActivity;
import pro.theboard.custom_views.CustomSwitch;
import pro.theboard.utils.Preferences;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 7/13/15.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener,CustomSwitch.OnCheckedChangeListener {


    private MainActivity mActivity;
    private View mRootView;
    private CustomSwitch csLocation;
    private CustomSwitch csNotification;


    @Override
    public void onAttach(Activity activity) {
        mActivity = (MainActivity) activity;
        super.onAttach(activity);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_settings, container, false);
        findViews();
        setListeners();
        setData();
        return mRootView;
    }

    private void setListeners() {
        csNotification.setOnCheckedChangeListener(this);
        csLocation.setOnCheckedChangeListener(this);
    }

    private void findViews() {
        csLocation = findView(R.id.csEnableLocation);
        csNotification = findView(R.id.csEnableNotification);
    }

    private <T extends View> T findView(int ressourceId) {
        return (T) mRootView.findViewById(ressourceId);
    }

    private void setData() {
        csLocation.setChecked(Preferences.getLocationPreference());
        csNotification.setChecked(Preferences.getNotificationPreference());
        mActivity.setToolbarTitle(getString(R.string.settings));
    }


    private void switchLocation(boolean _isEnable){
        Preferences.saveLocationPreference(_isEnable);
    }

    private void switchNotification(boolean _isEnable){
        Preferences.saveNotificationPreference(_isEnable);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStateChanged(View view, boolean isChecked) {
        switch (view.getId()){
            case R.id.csEnableLocation:
                switchLocation(isChecked);
                break;
            case R.id.csEnableNotification:
                switchNotification(isChecked);
                break;
        }
    }
}
