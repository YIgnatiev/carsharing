package com.vakoms.meshly.fragments.opportunities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.fragments.IndustriesFragment;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.wall.WallModel;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.KeyboardUtil;
import com.vakoms.meshly.utils.P;
import com.vakoms.meshly.views.CustomSwitch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentAddPostBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Oleh Makhobey on 28.04.2015.
 * tajcig@ya.ru
 */
public class AddWallPostFragment extends BaseFragment<MainActivity> implements CustomSwitch.OnCheckedChangeListener
        , View.OnFocusChangeListener, IndustriesFragment.IndustriesListener, AdapterView.OnItemClickListener {






    EditText etFocusedEditText;
    ProgressDialog dialog;

    private int mTime;
    private WallModel mWallModel;
    private ArrayList<String> mSkills = new ArrayList<>();
    private List<String> mIndustries = new ArrayList<>();
    private AlertDialog mDialog;
    private FragmentAddPostBinding b;

    public static AddWallPostFragment getEditWallPostInstance(WallModel _model) {
        AddWallPostFragment fragment = new AddWallPostFragment();
        fragment.setWallModel(_model);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(b == null) {
            b = FragmentAddPostBinding.inflate(inflater);
            b.setListener(this);
            dialog = new ProgressDialog(mActivity);
            if (mWallModel != null) setData();
            else setLikeAddNewDeal();
        }
        return b.getRoot();
    }




    private void setLikeAddNewDeal(){
        b.tvDelete.setVisibility(View.GONE);
    }


    private void setData() {
        b.etTitle.setText(mWallModel.getTitle());

        b.etDescription.setText(mWallModel.getDescription());

        b.tvIndustries.setText(listToString(mWallModel.getIndustries()));

        int postion = getPositionFromInt(mWallModel.getTime());

        String[] timeArray = getResources().getStringArray(R.array.open_for_array);
        b.tvOpenFor.setText(timeArray[postion]);
        mTime = mWallModel.getTime();
        mIndustries = mWallModel.getIndustries();
        b.tvPost.setText(getString(R.string.edit));
        b.tvDelete.setVisibility(View.VISIBLE);
        b.tvTitle.setText(getString(R.string.edit_opportunity));
        showDetails();
    }

    private boolean checkFields() {
        if (b.etTitle.getText().toString().isEmpty()) {
            b.etTitle.setError("Enter title first");
            return false;
        }
        if (b.etDescription.getText().toString().isEmpty()) {
            b.etTitle.setError("Enter description first");
            return false;
        }
        return true;

    }

    public void setWallModel(WallModel _model) {
        this.mWallModel = _model;
    }

    private void addOpportunity() {
        closeKeyboard();
        WallModel model = new WallModel();
        model.setLat(String.valueOf(P.GPS.getLatitude()));
        model.setLng(String.valueOf(P.GPS.getLongitude()));
        model.setTitle(b.etTitle.getText().toString());
        model.setInterval(mTime);
        model.setDescription(b.etDescription.getText().toString());
        model.setSkills(mSkills);
        model.setIndustries(mIndustries);

        if (mWallModel == null) sendWallPost(model);
        else editWallPost(model);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        ListView listView = new ListView(mActivity);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.open_for_array));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        builder.setView(listView);
        mDialog = builder.create();
        mDialog.show();
    }

    private void sendWallPost(WallModel _model) {
        mActivity.showProgress();
        Subscription sendSubscription =
                RetrofitApi.getInstance()
                        .wall()
                        .createWallPost(_model)
                        .timeout(3, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onResponseSuccess, this::handleError);

        mSubscriptions.add(sendSubscription);

    }

    private void editWallPost(WallModel _model) {

        mActivity.showProgress();
        Subscription editSubscription = RetrofitApi.getInstance().wall()
                .updateWallPost(_model.getId(),_model)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onResponseSuccess, this::handleError);

        mSubscriptions.add(editSubscription);

    }

    private void deleteOpportunity() {

        mActivity.showProgress();
        Subscription deleteSubscription = RetrofitApi
                .getInstance().wall()
                .deleteWallPost(mWallModel.getId())
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onResponseSuccess, this::handleError);


        mSubscriptions.add(deleteSubscription);
    }

    public  void handleError(Throwable throwable){
     mActivity.handleError(throwable);
    }

    private void showDetails() {
        b.llDescription.setVisibility(View.VISIBLE);
        b.vwDescriptionLine.setVisibility(View.VISIBLE);
        b.llIndustries.setVisibility(View.VISIBLE);
        b.vwIndustriesLine.setVisibility(View.VISIBLE);

        b.tvAddDetails.setVisibility(View.GONE);
        b.vwAddDeatailsLine.setVisibility(View.GONE);

    }

    private String listToString(List<String> _list) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < _list.size(); i++) {
            builder.append(_list.get(i));

            if (i != _list.size() - 1) builder.append(", ");
        }
        return builder.toString();
    }

    private void closeKeyboard() {

            KeyboardUtil.hideKeyBoard(b.etDescription, mActivity);
            KeyboardUtil.hideKeyBoard(b.etTitle, mActivity);

    }

    private void startIndustriesFragment() {
        IndustriesFragment fragment = new IndustriesFragment();
        fragment.setIndustries(mIndustries);
        fragment.setIndustriesListener(this);


        mActivity.replaceFragmentLeft(fragment);

    }


    private void onResponseSuccess(BaseResponse response) {
        closeKeyboard();
        mActivity.stopProgress();
        mActivity.onBackPressed();

    }

    public void onBack(View view){
        closeKeyboard();
        mActivity.onBackPressed();

    }

    public void onPost(View view){
        if (checkFields()) addOpportunity();
    }

    public void onAddDetails(View view){
        showDetails();

    }

    public void onIndustries(View view){
        startIndustriesFragment();

    }

    public void onOpenFor(View view){
        showDialog();

    }

    public void onDeletePost(View view){
        deleteOpportunity();

    }

    public void onRoot(View view){
        closeKeyboard();
    }

    @Override
    public void onStateChanged(View view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.csFacebook_FragmentAddPost:
                break;
            case R.id.csTwitter_FragmentAddPost:
                break;
            case R.id.csLinkedIn_FragmentAddPost:
                break;

        }
    }

    private int getTime(int _positon) {
        switch (_positon) {
            case 0:
                return 1;
            case 1:
                return 7;
            case 2:
                return 30;
            default:
                return 0;
        }
    }

    private int getPositionFromInt(int _timeString) {
        switch (_timeString) {
            case 1:
                return 0;
            case 7:
                return 1;
            case 30:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        etFocusedEditText = (EditText) v;
    }

    @Override
    public void onIndustriesChanged(List<String> _industries) {

        mIndustries = _industries;
        b.tvIndustries.setText(listToString(_industries));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String openFor = (String) parent.getItemAtPosition(position);
        b.tvOpenFor.setText(openFor);
        mTime = getTime(position);
        mDialog.dismiss();
    }

}
