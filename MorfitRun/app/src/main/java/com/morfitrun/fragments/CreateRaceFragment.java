package com.morfitrun.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.andreabaccega.widget.FormEditText;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.morfitrun.R;
import com.morfitrun.api.ErrorProccessor;
import com.morfitrun.api.RestClient;
import com.morfitrun.data_models.race_model.Race;
import com.morfitrun.data_models.race_model.RaceId;
import com.morfitrun.global.Constants;
import com.morfitrun.listeners.OnClickRightToolbarListener;
import com.morfitrun.listeners.OnDateCompletedListener;
import com.morfitrun.listeners.OnNumberCompletedListener;
import com.morfitrun.widgets.DateDialog;
import com.morfitrun.widgets.NumberDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import java.util.Calendar;

/**
 * Created by vasia on 12.03.2015.
 */
public class CreateRaceFragment extends BaseFragment implements View.OnClickListener, OnMapReadyCallback, OnDateCompletedListener, OnNumberCompletedListener, Callback<RaceId> {

    private FormEditText etRunTitle, etDescription;
    private TextView tvAddStartOn, tvAddDeadline, tvAddRepeat, tvAddLimit, tvStartOn, tvRepeat, tvLimit, tvDeadline;
    private Button btnCreate;
    private GoogleMap mGoogleMap;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.fragment_create_race, container, false);
        findViews();
        setListener();
        initMap(savedInstanceState);
        return mInflatedView;
    }

    @Override
    protected void findViews() {

        mapView = (MapView) mInflatedView.findViewById(R.id.map_FCR);
        etRunTitle = (FormEditText) mInflatedView.findViewById(R.id.etRunTitle_FCR);
        etDescription = (FormEditText) mInflatedView.findViewById(R.id.etDescription_FCR);

        tvAddStartOn = (TextView) mInflatedView.findViewById(R.id.tvAddStartOn_FCR);
        tvAddDeadline = (TextView) mInflatedView.findViewById(R.id.tvAddDeadline_FCR);
        tvAddRepeat = (TextView) mInflatedView.findViewById(R.id.tvAddRepeat_FCR);
        tvAddLimit = (TextView) mInflatedView.findViewById(R.id.tvAddLimit_FCR);

        btnCreate = (Button) mInflatedView.findViewById(R.id.btnCreate_FCR);

        tvStartOn = (TextView) mInflatedView.findViewById(R.id.tvStartOnData_FCR );
        tvRepeat = (TextView) mInflatedView.findViewById(R.id.tvRepeatData_FCR);
        tvLimit = (TextView) mInflatedView.findViewById(R.id.tvLimitData_FCR);
        tvDeadline = (TextView) mInflatedView.findViewById(R.id.tvDeadlineData_FCR);
    }

    @Override
    public CharSequence getTitle() {
        return getResources().getString(R.string.create_race);
    }

    @Override
    protected OnClickRightToolbarListener getRightOnClickListener() {
        return null;
    }

    private void setListener(){
        tvAddDeadline.setOnClickListener(this);
        tvAddLimit.setOnClickListener(this);
        tvAddRepeat.setOnClickListener(this);
        tvAddStartOn.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvAddStartOn_FCR:
                startDateDialog(getString(R.string.start_on), Constants.DATE_START_ON);
                break;
            case R.id.tvAddDeadline_FCR:
                startDateDialog(getString(R.string.deadline), Constants.DEADLINE);
                break;
            case R.id.tvAddRepeat_FCR:
                startDateDialog(getString(R.string.repeat), Constants.DATE_REPEAT);
                break;
            case R.id.tvAddLimit_FCR:
                startNumberDialog(getString(R.string.limit), Constants.LIMIT, Constants.MAX_RACE_PEOPLE);
                break;
            case R.id.btnCreate_FCR:
                onClickBtnCreate();
                break;
        }
    }

    private void startDateDialog(final String _title, final int _requestCode){
        DateDialog dialog = new DateDialog(getActivity(), _title, _requestCode, this, Calendar.getInstance(), null, Calendar.getInstance());
        dialog.show();
    }

    private void startNumberDialog(final String _title, final int _requestCode, int _maxValue){
        (new NumberDialog(
                getActivity(),
                _title,
                _requestCode,
                _maxValue,
                this
        )).show();
    }

    private void onClickBtnCreate(){
        if (checkEditFields() && checkTextFields()){
            final Race race = new Race(
                    etRunTitle.getText().toString(),
                    etDescription.getText().toString(),
                    tvStartOn.getText().toString(),
                    tvDeadline.getText().toString(),
                    tvRepeat.getText().toString(),
                    Integer.parseInt(tvLimit.getText().toString()));

            RestClient.getInstance().getRaceService().addRace(race, this);
            race.getId();
        }
    }

    private boolean checkEditFields() {
        boolean allValid = true;
        FormEditText[] allFields = {etDescription, etRunTitle};
        for (FormEditText field : allFields) {
            allValid = field.testValidity() && allValid;
            if (!field.testValidity()) {
                field.requestFocus();
            }
        }
        return allValid;
    }

    private boolean checkTextFields(){
        TextView[] allFields = {tvStartOn, tvDeadline, tvRepeat, tvLimit};
        for (TextView allField : allFields) {
            if (allField.getText().equals("")) {
                Toast.makeText(getActivity(), "All fields must not be empty", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void replaceFragmentWithBackStack(BaseFragment _fragment) {
        super.replaceFragmentWithBackStack(_fragment);
    }

    @Override
    public void onDateCompleted(int _requestCode, int _year, int _monthOfYear, int _dayOfMonth) {
        final String date = String.format("%d-%d-%d", _dayOfMonth, _monthOfYear, _year);
        switch (_requestCode){
            case Constants.DATE_START_ON:
                tvStartOn.setText(date);
                break;
            case Constants.DATE_REPEAT:
                tvRepeat.setText(date);
                break;
            case Constants.DEADLINE:
                tvDeadline.setText(date);
                break;
        }
    }

    @Override
    public void onNumberCompleted(int _requestCode, int _value) {
        switch (_requestCode){
            case Constants.LIMIT:
                tvLimit.setText(Integer.toString(_value));
                break;
        }
    }

    @Override
    public void success(RaceId _raceId, Response response) {
        Toast.makeText(getActivity(), "create race", Toast.LENGTH_SHORT).show();
        replaceFragmentWithBackStack(RecordFragment.newInstance(_raceId.getId()));
    }

    @Override
    public void failure(RetrofitError _error) {
        ErrorProccessor.processError(mActivity, _error);
    }

    private void initMap(Bundle savedInstanceState){
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap _googleMap) {
        mGoogleMap = _googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }
}
