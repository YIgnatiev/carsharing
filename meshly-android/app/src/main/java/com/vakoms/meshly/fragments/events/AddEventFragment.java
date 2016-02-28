package com.vakoms.meshly.fragments.events;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.fragments.CategoriesSkillsFragment;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Event;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.KeyboardUtil;
import com.vakoms.meshly.utils.P;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentAddEventBinding;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by taras.melko on 12.09.2014.
 */
public class AddEventFragment extends BaseFragment<MainActivity>  {


    private Event mEvent = new Event();

    boolean dateTime; //todo: wtf is this?


    private FragmentAddEventBinding b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (b == null) {
            b = DataBindingUtil.inflate(inflater, R.layout.fragment_add_event, container, false);
            b.setListener(this);
        }
        return b.getRoot();
    }


    private void setupDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(b.datePicker.getYear(), b.datePicker.getMonth(), b.datePicker.getDayOfMonth(), b.timePicker.getCurrentHour(), b.timePicker.getCurrentMinute());
        Date date = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM HH:mm");
        if (dateTime) {
            b.tvBegins.setText(format.format(date) + "");
            P.saveAddEventsBegin(format.format(date) + "");
            P.saveBegin(date.getTime() / 1000);
        } else {
            b.tvEnds.setText(format.format(date) + "");
            P.saveAddEventsEnd(format.format(date) + "");
            P.saveEnd(date.getTime() / 1000);
        }
    }

    private void showDialog() {
        b.llDatePicker.setVisibility(View.VISIBLE);
        b.ivBack.setVisibility(View.VISIBLE);

    }

    private void hideDialog() {
        b.llDatePicker.setVisibility(View.GONE);
        b.ivBack.setVisibility(View.GONE);
    }


    @Override
    public void onPause() {
        super.onPause();


        hideKeyBoard();
        hideDialog();
    }

    private void hideKeyBoard() {
        KeyboardUtil.hideKeyBoard(b.etTitle, mActivity);
        KeyboardUtil.hideKeyBoard(b.etWebsite, mActivity);
    }


    private boolean checkFields() {
        //TODO implement checkFields

        Toast.makeText(mActivity, "Temporary unavailable", Toast.LENGTH_SHORT).show();
        return false;
    }






    public void onBack(View view) {
        mActivity.onBackPressed();

    }

    public void onBeginAt(View view) {
        dateTime = true;
        showDialog();
    }

    public void onEndAt(View view) {
        dateTime = false;
        showDialog();
    }

    public void onSend(View view) {
        if (checkFields())
            sendData();
    }

    public void onTvDone(View view) {
        setupDate();

    }

    public void onCancel(View view) {
        hideDialog();

    }

    public void onAdd(View view) {
        mActivity.replaceFragment(AddEventEditFieldFragment.newInstance("Address"));

    }

    public void onDescription(View view) {
        mActivity.replaceFragment(AddEventEditFieldFragment.newInstance("Description"));

    }

    public void onCategories(View view) {
        mActivity.replaceFragment(new CategoriesSkillsFragment());

    }

    public void sendData() {
        mActivity.showProgress();

        RetrofitApi.getInstance()
                .events()
                .sendEvent(mEvent)
                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::onAddEventSuccess, this::handleError);


    }


    private void onAddEventSuccess(Event event) {
        Toast.makeText(mActivity, "Event successfully added", Toast.LENGTH_SHORT).show();

    }

    public void handleError(Throwable throwable) {
      mActivity.handleError(throwable);
    }








}




