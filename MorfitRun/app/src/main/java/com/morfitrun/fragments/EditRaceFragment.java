package com.morfitrun.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.morfitrun.R;
import com.morfitrun.api.ErrorProccessor;
import com.morfitrun.api.RestClient;
import com.morfitrun.data_models.race_model.Race;
import com.morfitrun.data_models.race_model.RaceFullModel;
import com.morfitrun.global.Constants;
import com.morfitrun.global.DateWorker;
import com.morfitrun.global.RaceDrawer;
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
 * Created by vasia on 16.03.2015.
 */
public class EditRaceFragment extends BaseFragment implements View.OnClickListener, OnDateCompletedListener, OnNumberCompletedListener, OnMapReadyCallback, Callback<RaceFullModel> {

    private EditText etRunTitle, etDescription;
    private TextView tvAddStartOn, tvAddDeadline, tvAddRepeat, tvAddLimit, tvStartOn, tvRepeat, tvLimit, tvDeadline;
    private Button btnCancelNext, btnModify, btnCancelAll;
    private MapView mapView;
    private GoogleMap mGoogleMap;
    private Race mRace;

    public static final EditRaceFragment newInstance(final String _raceId) {
        final EditRaceFragment fragment = new EditRaceFragment();
        final Bundle args = new Bundle();
        args.putString(Constants.RACE_ID, _raceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.fragment_edit_race, container, false);
        getRace();
        findViews();
        setListener();
        initMap(savedInstanceState);
        return mInflatedView;
    }

    private void getRace(){
        final Bundle bundle = getArguments();
        if (bundle != null) {
            final String raceId = bundle.getString(Constants.RACE_ID);
            RestClient.getInstance().getRaceService().getRace(raceId, this);
            mActivity.showProgress(getString(R.string.please_wait));
        }
    }

    @Override
    public void success(RaceFullModel _raceFullModel, Response _response) {
        mActivity.hideProgress();
        mRace = _raceFullModel.getRace();

        final RaceDrawer drawer = new RaceDrawer(mActivity, _raceFullModel.getPoints(), mGoogleMap);
        drawer.drawPrimaryLinePath();
        drawer.cameraMoveTo();
        fillRaceData();
    }

    @Override
    public void failure(RetrofitError _error) {
        mActivity.hideProgress();
        ErrorProccessor.processError(getActivity(), _error);
    }

    private void fillRaceData(){
        if (mRace != null) {
            etRunTitle.setText(mRace.getTitle());
            etDescription.setText(mRace.getDescription());
            tvStartOn.setText(DateWorker.formatServerDate(mRace.getStarDate()));
            tvDeadline.setText(DateWorker.formatServerDate(mRace.getDeadline()));
            tvRepeat.setText(mRace.getRepeat());
            tvLimit.setText(Integer.toString(mRace.getLimit()));
        }
    }

    @Override
    protected void findViews() {
        mapView = (MapView) mInflatedView.findViewById(R.id.map_FER);

        etRunTitle = (EditText) mInflatedView.findViewById(R.id.etRunTitle_FER);
        etDescription = (EditText) mInflatedView.findViewById(R.id.etDescription_FER);

        tvAddStartOn = (TextView) mInflatedView.findViewById(R.id.tvAddStartOn_FER);
        tvAddDeadline = (TextView) mInflatedView.findViewById(R.id.tvAddDeadline_FER);
        tvAddRepeat = (TextView) mInflatedView.findViewById(R.id.tvAddRepeat_FER);
        tvAddLimit = (TextView) mInflatedView.findViewById(R.id.tvAddLimit_FER);

        tvStartOn = (TextView) mInflatedView.findViewById(R.id.tvStartOnData_FER );
        tvRepeat = (TextView) mInflatedView.findViewById(R.id.tvRepeatData_FER);
        tvLimit = (TextView) mInflatedView.findViewById(R.id.tvLimitData_FER);
        tvDeadline = (TextView) mInflatedView.findViewById(R.id.tvDeadlineData_FER);

        btnCancelAll = (Button) mInflatedView.findViewById(R.id.btnCancelAll_FER);
        btnCancelNext = (Button) mInflatedView.findViewById(R.id.btnCancelNext_FER);
        btnModify = (Button) mInflatedView.findViewById(R.id.btnModify_FER);
    }

    private void setListener(){
        btnModify.setOnClickListener(this);
        btnCancelNext.setOnClickListener(this);
        btnCancelAll.setOnClickListener(this);

        tvAddDeadline.setOnClickListener(this);
        tvAddLimit.setOnClickListener(this);
        tvAddRepeat.setOnClickListener(this);
        tvAddStartOn.setOnClickListener(this);
    }

    @Override
    public CharSequence getTitle() {
        return getResources().getString(R.string.edit_race);
    }

    @Override
    protected void replaceFragmentWithBackStack(BaseFragment _fragment) {
        super.replaceFragmentWithBackStack(_fragment);
    }

    @Override
    protected OnClickRightToolbarListener getRightOnClickListener() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancelAll_FER:

                break;
            case R.id.btnModify_FER:
                replaceFragmentWithBackStack(RecordFragment.newInstance(mRace.getId()));
                break;
            case R.id.btnCancelNext_FER:

                break;
            case R.id.tvAddStartOn_FER:
                startDateDialog(getResources().getString(R.string.start_on), Constants.DATE_START_ON);
                break;
            case R.id.tvAddDeadline_FER:
                startDateDialog(getResources().getString(R.string.deadline), Constants.DEADLINE);
                break;
            case R.id.tvAddRepeat_FER:
                startDateDialog(getResources().getString(R.string.repeat), Constants.DATE_REPEAT);
                break;
            case R.id.tvAddLimit_FER:
                startNumberDialog(getResources().getString(R.string.limit), Constants.LIMIT, Constants.MAX_RACE_PEOPLE);
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

    @Override
    public void onDateCompleted(int _requestCode, int _year, int _monthOfYear, int _dayOfMonth) {
        final String date = String.format("%d-%d-%d", _year, _monthOfYear, _dayOfMonth);
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
                tvLimit.setText("" + _value);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap _googleMap) {
        mGoogleMap = _googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    private void initMap(Bundle savedInstanceState){
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
        fillRaceData();
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
