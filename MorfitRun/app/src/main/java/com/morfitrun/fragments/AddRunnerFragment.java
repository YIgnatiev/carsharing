package com.morfitrun.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.morfitrun.R;
import com.morfitrun.api.ErrorProccessor;
import com.morfitrun.api.RestClient;
import com.morfitrun.data_models.NewRunner;
import com.morfitrun.data_models.Runner;
import com.morfitrun.data_models.User;
import com.morfitrun.listeners.OnClickRightToolbarListener;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import static com.morfitrun.global.Constants.*;
/**
 * Created by Admin on 20.03.2015.
 */
public class AddRunnerFragment extends BaseFragment implements View.OnClickListener , Callback<NewRunner> {


    private FormEditText etEmail;
    private FormEditText etRunId;
    private TextView tvAdd;



    public static AddRunnerFragment getInstance(String _raceId){
        AddRunnerFragment fragment = new AddRunnerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(RACE_ID, _raceId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.fragment_add_runner, container,false);
        findViews();
        setListeners();

        return mInflatedView;
    }

    @Override
    protected void findViews() {
        etEmail = (FormEditText)mInflatedView.findViewById(R.id.etEmail_FA);
        etRunId = (FormEditText)mInflatedView.findViewById(R.id.etRunId_FA);
        tvAdd = (TextView)mInflatedView.findViewById(R.id.tvAdd_FA);
    }

    private void setListeners(){
        tvAdd.setOnClickListener(this);
    }


    private boolean checkFields() {
        boolean allValid = true;
        FormEditText[] allFields = {etRunId,  etEmail};
        for (FormEditText field : allFields) {
            allValid = field.testValidity() && allValid;
            if (!field.testValidity()) {
                field.requestFocus();
            }
        }
        return allValid;
    }

    private void addNewRunner() {
        if (checkFields()) {

            NewRunner newRunner = new NewRunner();
            newRunner.setEmail(etEmail.getText().toString());

            int runnerId = Integer.parseInt(etRunId.getText().toString());
            newRunner.setRunnerId(runnerId);
            String raceId = getArguments().getString(RACE_ID);
            newRunner.setRaceId(raceId);
            requestSaveRunner(newRunner);
        }
    }


    private void requestSaveRunner(NewRunner _runner){

            RestClient.getInstance().getAddRunnerSevice().addRunner(_runner,this);

    }

    @Override
    public void success(NewRunner runner, Response response) {

      int i = 0 ;

        mActivity.onBackPressed();
    }

    @Override
    public void failure(RetrofitError _error) {
        ErrorProccessor.processError(getActivity(), _error);
        //pbLoad.setVisibility(View.GONE);
        }

    @Override
    public CharSequence getTitle() {
        return mActivity.getString(R.string.add_runner);

    }

    @Override
    protected OnClickRightToolbarListener getRightOnClickListener() {
        return null;
    }

    @Override
    public void onClick(View v) {
        addNewRunner();
    }


}
