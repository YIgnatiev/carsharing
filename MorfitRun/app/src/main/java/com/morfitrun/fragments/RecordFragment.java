package com.morfitrun.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.*;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.morfitrun.R;
import com.morfitrun.api.ErrorProccessor;
import com.morfitrun.api.RestClient;
import com.morfitrun.data_models.race_model.Points;
import com.morfitrun.data_models.race_model.PostPoints;
import com.morfitrun.global.ConnectHelper;
import com.morfitrun.global.Constants;
import com.morfitrun.listeners.OnClickRightToolbarListener;
import com.morfitrun.services.record_service.RecordService;
import com.morfitrun.services.record_service.RecordServiceBinder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import java.util.ArrayList;

/**
 * Created by vasia on 24.03.2015.
 */
public class RecordFragment extends BaseFragment implements View.OnClickListener, OnMapReadyCallback, Callback<PostPoints>, ServiceConnection {

    private TextView tvDistance, tvElevation, tvTimer;
    private Button btnStop, btnPlayPause;
    private MapView mapView;
    private String mRaceId;
    private GoogleMap mGoogleMap;
    private RecordService mRecordService;


    public static final RecordFragment newInstance(final String _raceId) {
        final RecordFragment fragment = new RecordFragment();
        final Bundle args = new Bundle();
        args.putString(Constants.RACE_ID, _raceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setNeedGoBack(boolean _goBack) {
        super.setNeedGoBack(_goBack);
    }

    @Override
    public void onBack() {
        super.onBack();
        startCloseDialog();
    }

    private void startCloseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setTitle(getString(R.string.close_dialog_title))
                .setPositiveButton(getString(R.string.save_and_exit), mCloseDialogClickListener)
                .setNegativeButton(getString(R.string.cancel), mCloseDialogClickListener);
        builder.show();
    }

    private DialogInterface.OnClickListener mCloseDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case Dialog.BUTTON_POSITIVE:
                    stopAndSaveData();
                    break;
                case Dialog.BUTTON_NEGATIVE:

                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(mActivity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.fragment_record, container, false);
        findViews();
        setListener();
        if (!ConnectHelper.isLocationEnabled(mActivity))
            startLocationPermissionDialog();

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity) == ConnectionResult.SUCCESS){
            initMap(savedInstanceState);
        }
        return mInflatedView;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d("tag", "service connection");
        mRecordService = ((RecordServiceBinder)service).getService();
        contactService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d("tag", "Service Disconnected");

    }

    private void contactService(){
        mRecordService.setRecordFragment(this);
        mRecordService.setTimerTextView(tvTimer);
        if (mRecordService.getRaceId() == null)
            mRecordService.setRaceId(mRaceId);
        tvDistance.setText(mRecordService.getCurrentDistance());
        tvElevation.setText(mRecordService.getCurrentElevation());
        syncPlayButton();
        setNeedGoBack(mRecordService.isRecordRuning());
        drawMap(mRecordService.getAllPoints());
    }

    private void drawMap(ArrayList<Points> _points){
        final ArrayList<Points> points = _points;
        if (points.size() >= 2){
            for (int i = 1; i < points.size(); i++){
                drawPrimaryLinePath(points.get(i).getLatitude(), points.get(i).getLongitude(), points.get(i - 1).getLatitude(), points.get(i - 1).getLongitude());
            }
        }

    }

    @Override
    protected void findViews() {
        tvDistance      = (TextView) mInflatedView.findViewById(R.id.tvDistance_FR);
        tvElevation     = (TextView) mInflatedView.findViewById(R.id.tvElevation_FR);
        tvTimer         = (TextView) mInflatedView.findViewById(R.id.tvTimer_FR);
        btnStop         = (Button) mInflatedView.findViewById(R.id.btnStop_FR);
        btnPlayPause    = (Button) mInflatedView.findViewById(R.id.btnPlayPause_FR);
        mapView         = (MapView) mInflatedView.findViewById(R.id.map_FR);
    }

    private void setListener(){
        btnPlayPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStop_FR:
                if (mRecordService.isTimerRunning())
                    mRecordService.startPauseTimer();
                stopAndSaveData();
                break;
            case R.id.btnPlayPause_FR:
                onClickBtnPlayPause();
                break;
        }
    }

    private void stopAndSaveData() {
        final ArrayList<Points> pointsList = mRecordService.getAllPoints();
        Points[] points = new Points[pointsList.size()];
        for (int i = 0; i < pointsList.size(); i++) {
            points[i] = pointsList.get(i);
        }
        mActivity.showProgress(getString(R.string.please_wait));
        RestClient.getInstance().getRaceService().addPoints(new PostPoints(points, mRecordService.getRaceId()), this);

    }

    @Override
    public void success(PostPoints points, Response response) {
        mActivity.hideProgress();
        mActivity.stopService(new Intent(mActivity, RecordService.class));
        Toast.makeText(mActivity, "add points success", Toast.LENGTH_SHORT).show();
        setNeedGoBack(false);
        replaceTopNavigationFragment(new RunsFragment());
    }

    @Override
    public void failure(RetrofitError _error) {
        mActivity.hideProgress();
        ErrorProccessor.processError(mActivity, _error);
    }

    private void onClickBtnPlayPause() {
        if (!ConnectHelper.isLocationEnabled(mActivity)) {
            startLocationPermissionDialog();
            return;
        }
        mRecordService.startPauseTimer();
        syncPlayButton();
        setNeedGoBack(mRecordService.isRecordRuning());
    }

    private void syncPlayButton(){
        if (mRecordService.isTimerRunning()) {
            btnPlayPause.setText(getString(R.string.pause));
        } else {
            btnPlayPause.setText(getString(R.string.play));
        }
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.record_track);
    }

    @Override
    protected OnClickRightToolbarListener getRightOnClickListener() {
        return null;
    }

    @Override
    protected void replaceTopNavigationFragment(BaseFragment _fragment) {
        super.replaceTopNavigationFragment(_fragment);
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        final Bundle bundle = getArguments();
        if (bundle != null) {
            mRaceId = bundle.getString(Constants.RACE_ID);
        }
        Intent intent = new Intent(mActivity, RecordService.class);
        mActivity.startService(intent);
        mActivity.bindService(intent, this, 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("tag", "on stop record fragment");
        mActivity.unbindService(this);
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
        if (mRecordService != null && !mRecordService.isRecordRuning())
            mActivity.stopService(new Intent(mActivity, RecordService.class));
        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }

    private void startLocationPermissionDialog(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity)
                .setMessage(getString(R.string.dialog_location_permission_title))
                .setTitle(getString(R.string.location_permission_request))
                .setIcon(android.R.drawable.ic_menu_mylocation)
                .setPositiveButton(getString(R.string.settings), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveToLocationPermission();
                    }
                })
                .setNegativeButton(getString(R.string.ignore), null);
        dialog.show();
    }

    private void moveToLocationPermission(){
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }


    public void drawPrimaryLinePath(final double _latitude,
                                    final double _longitude,
                                    final double _previousLatitude,
                                    final double _previousLongitude) {

        final PolylineOptions options = new PolylineOptions();
        options.color(Color.parseColor("#CC0000FF"));
        options.width(5);
        options.visible( true );
        options.add(new LatLng(_latitude, _longitude));
        options.add(new LatLng(_previousLatitude, _previousLongitude));
        mGoogleMap.addPolyline( options );
        cameraMoveTo(_latitude, _longitude);
    }

    public void setDistance(final String _distance){
        try {
            tvDistance.setText(_distance);
        }
        catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    public void setElevation(final String _elevation){
        try {
            tvElevation.setText(_elevation);
        }
        catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    private void cameraMoveTo(final double _latitude, final double _longitude){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(_latitude, _longitude))
                .zoom(15)
                .build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
