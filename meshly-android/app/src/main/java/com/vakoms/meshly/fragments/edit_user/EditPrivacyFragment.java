package com.vakoms.meshly.fragments.edit_user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.databases.UserDAO;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.fragments.PrivacyPolicyInfoFragment;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Privacy;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.constants.Constants;
import com.vakoms.meshly.utils.P;
import com.vakoms.meshly.utils.TimeFormatterUtil;

import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentEditPrivacyBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.vakoms.meshly.constants.Constants.TIME_HOUR;
import static com.vakoms.meshly.constants.Constants.TIME_SECOND;

/**
 * Created by Oleh Makhobey on 09.05.2015.
 * tajcig@ya.ru
 */
public class EditPrivacyFragment extends BaseFragment<MainActivity>  {



    private long currentHiddenState;
    private String currentTimestampState;
    private String currentLocationState;
    private String currentMessagingState;
    private CountDownTimer mCountDownTimer;
    private FragmentEditPrivacyBinding b;
    private UserMe mUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(b ==null) {
            mUser = mActivity.mUser;
            b = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_privacy, container, false);
            b.setListener(this);
            setData();

        }
        return b.getRoot();
    }


    @Override
    public void onPause() {
        super.onPause();
        cancelTimer();
    }


    @Override
    public void onResume() {
        super.onResume();
        startTimer(currentHiddenState);
    }


    private void setData() {

        b.tvPrivacyFooter.setText(Html.fromHtml(getString(R.string.privacy_hint)));
        currentHiddenState = System.currentTimeMillis();
        fillFromPrefs();
        b.tvTimestampValue.setText(getValueForDisplay(currentTimestampState));
        b.tvLocationValue.setText(getValueForDisplay(currentLocationState));
        b.tvMessagingValue.setText(getValueForDisplay(currentMessagingState));
    }






    public void startTimer(long valueFromServer) {
        long currentTime = System.currentTimeMillis();
        long dif = valueFromServer - currentTime;

        cancelTimer();
        if (dif <= 0) {

            b.tvHiddenFromEveryone.setText("");
            return;
        }

        mCountDownTimer = new CountDownTimer(dif, TIME_SECOND) {

            public void onTick(long millisUntilFinished) {

                String time = TimeFormatterUtil.format(millisUntilFinished, TimeFormatterUtil.HOURS_MINUTES_SECONDS);
                currentHiddenState -= TIME_SECOND;
                b.tvHiddenFromEveryone.setText(time);
            }

            public void onFinish() {
                b.tvHiddenFromEveryone.setText("");
            }
        }.start();
    }

    public void cancelTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }


    public void updatePrivacy() {


        Privacy privacy = new Privacy();
        privacy.chat = currentLocationState;
        privacy.hidden = currentHiddenState;
        privacy.lastSeen = currentTimestampState;
        privacy.lastSeen = currentLocationState;


        mUser.setPrivacy(privacy);
        UserDAO dao = UserDAO.getInstance();
        dao.saveUserMe(mActivity.getContentResolver(), mUser);


        Subscription subscription = RetrofitApi.getInstance()
                .user()
                .updatePrivacy(privacy)
                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPrivacySuccess, this::handleError);

        mSubscriptions.add(subscription);

    }


    public void onPrivacySuccess(BaseResponse response) {
        mActivity.stopProgress();
        Toast.makeText(getActivity(), "Your privacy settings has been updated", Toast.LENGTH_SHORT).show();
    }


    public void handleError(Throwable throwable) {
        mActivity.handleError(throwable);
    }


    private void showDialog(final int buttonId, String title, String[] cases) {
        AlertDialog.Builder builderSingle;
        builderSingle = new AlertDialog.Builder(
                getActivity());
        builderSingle.setTitle(title);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_selectable_list_item);
        for (int i = 0; i < cases.length; i++) {
            arrayAdapter.add(cases[i]);
        }
        builderSingle.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        switch (buttonId) {
                            case R.id.rlHideFromEveryOne:
                                String hint = which == 0 ? "" : strName;

                                switch (which) {
                                    case 0:
                                        currentHiddenState = 0;//System.currentTimeMillis();
                                        break;
                                    case 1:
                                        currentHiddenState = (System.currentTimeMillis() + 2 * TIME_HOUR);
                                        break;
                                    case 2:
                                        currentHiddenState = (System.currentTimeMillis() + 6 * TIME_HOUR);
                                        break;
                                    case 3:
                                        currentHiddenState = (System.currentTimeMillis() + 12 * TIME_HOUR);
                                        break;
                                }

                                startTimer(currentHiddenState);

                                break;
                            case R.id.rlTimestamp:
                                b.tvTimestampValue.setText(strName);
                                currentTimestampState = Constants.privacyCasesForRequest[which];
                                break;
                            case R.id.rlLocation:
                                b.tvLocationValue.setText(strName);
                                currentLocationState = Constants.privacyCasesForRequest[which];
                                break;
                            case R.id.rlMessaging:
                                b.tvMessagingValue.setText(strName);
                                currentMessagingState = Constants.privacyCasesForRequest[which];
                                break;
                        }
                        updatePrivacy();
                    }
                });
        builderSingle.show();
    }



    public void fillFromPrefs() {
        Privacy privacy = new Privacy();
        P.fillPrivacy(privacy);
        currentHiddenState = privacy.getHidden();
        currentTimestampState = privacy.getLastSeen();
        currentLocationState = privacy.getLocation();
        currentMessagingState = privacy.getChat();
    }

    public String getValueForDisplay(String valueFromServer) {
        String[] arrayForDisplay = getActivity().getResources().getStringArray(R.array.cases);
        if (Constants.privacyCasesForRequest[0].equals(valueFromServer)) {
            return arrayForDisplay[0];
        } else if (Constants.privacyCasesForRequest[1].equals(valueFromServer)) {
            return arrayForDisplay[1];
        } else if (Constants.privacyCasesForRequest[2].equals(valueFromServer)) {
            return arrayForDisplay[2];
        }
        return "";
    }

    public void showInfoPrivacy() {
        mActivity.replaceFragmentLeft(new PrivacyPolicyInfoFragment());
    }


    public String getHiddenState(long valueFromServer) {
        String[] arrayForDisplay = getActivity().getResources().getStringArray(R.array.cases_hide_from);
        long currentTime = System.currentTimeMillis();
        long dif = valueFromServer - currentTime;
        long hr = 3600000;
        String result = "";
        if (dif <= 0) {
            result = "";//arrayForDisplay[0];
        } else if (dif < 2 * hr) {
            result = arrayForDisplay[1];
        } else if (dif < 4 * hr) {
            result = arrayForDisplay[2];
        } else if (dif < 12 * hr) {
            result = arrayForDisplay[3];
        }
        return result;
    }




    //click listeners
    public void onTimeStamp(View view) {
        showDialog(view.getId(), getString(R.string.timestamp_popup_title), getActivity().getResources().getStringArray(R.array.cases));

    }

    public void onLocation(View view) {
        showDialog(view.getId(), getString(R.string.location_popup_title), getActivity().getResources().getStringArray(R.array.cases));

    }

    public void onMessaging(View view) {
        showDialog(view.getId(), getString(R.string.messaging_popup_title), getActivity().getResources().getStringArray(R.array.cases));

    }

    public void onHideFromEveryOne(View view) {
        showDialog(view.getId(), getString(R.string.how_long), getActivity().getResources().getStringArray(R.array.cases_hide_from));

    }

    public void onPrivacy(View view) {
        showInfoPrivacy();

    }


}
