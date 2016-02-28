package com.morfitrun.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.morfitrun.R;
import com.morfitrun.api.ErrorProccessor;
import com.morfitrun.api.RestClient;
import com.morfitrun.data_models.race_model.Race;
import com.morfitrun.data_models.race_model.RaceFullModel;
import com.morfitrun.fragments.timer_fragments.TimerBaseFragment;
import com.morfitrun.global.Constants;
import com.morfitrun.global.DateWorker;
import com.morfitrun.global.RaceDrawer;
import com.morfitrun.listeners.OnClickRightToolbarListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import static com.morfitrun.global.Constants.*;
/**
 * Created by vasia on 12.03.2015.
 */
public class RaceViewFragment extends BaseFragment implements OnMapReadyCallback,
        View.OnClickListener, OnClickRightToolbarListener, Callback<RaceFullModel> , BluetoothSPP.BluetoothConnectionListener ,BluetoothSPP.BluetoothStateListener{

    private TextView tvOrganizer;
    private TextView tvSchedule;
    private TextView tvNextRun;
    private TextView tvDeadline;
    private TextView tvAvailablePlaces;
    private TextView tvRaceDescription;
    private TextView tvInviteUser;

    private Button btnEdit;
    private Button btnStart;
    private MapView mapView;
    private GoogleMap mGoogleMap;
    private Race mRace;
    private String mRaceId;
    private String mTitle;

    public static final RaceViewFragment newInstance(final String _raceId, final String _title) {
        final RaceViewFragment fragment = new RaceViewFragment();
        final Bundle args = new Bundle();
        args.putString(Constants.RACE_ID, _raceId);
        args.putString(Constants.TITLE, _title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.fragment_race_view, container, false);
        Log.d("tag", "on create view");
        findViews();
        prepareInputData();
        setListener();
        initMap(savedInstanceState);
        getRace(mRaceId);
        initBluetooth();

        return mInflatedView;
    }

    private void prepareInputData(){
        final Bundle bundle = getArguments();
        if (bundle != null) {
            mRaceId = bundle.getString(Constants.RACE_ID);
            mTitle = bundle.getString(Constants.TITLE);
        }
    }

    private void getRace(String _raceId){
        mActivity.showProgress(getString(R.string.please_wait));
        RestClient.getInstance().getRaceService().getRace(_raceId, this);
    }

    @Override
    public void success(RaceFullModel _raceFull, Response _response) {
        mActivity.hideProgress();
        mRace = _raceFull.getRace();

        final RaceDrawer drawer = new RaceDrawer(mActivity, _raceFull.getPoints(), mGoogleMap);
        drawer.drawPrimaryLinePath();
        drawer.cameraMoveTo();
        mRace.getId();
        fillRaceData();
    }

    @Override
    public void failure(RetrofitError _error) {
        mActivity.hideProgress();
        ErrorProccessor.processError(getActivity(), _error);
    }


    private void fillRaceData(){
        tvSchedule.setText(String.valueOf(mRace.getLimit()));
        tvNextRun.setText(mRace.getRepeat());
        tvDeadline.setText(mRace.getDeadline());
        tvRaceDescription.setText(mRace.getDescription());
        tvSchedule.setText(mRace.getRepeat());
        tvNextRun.setText(DateWorker.formatServerDate(mRace.getStarDate()));
        tvDeadline.setText(DateWorker.formatServerDate(mRace.getDeadline()));
        tvAvailablePlaces.setText(Integer.toString(mRace.getLimit()));
    }

    @Override
    protected void findViews() {
        tvOrganizer = (TextView) mInflatedView.findViewById(R.id.tvOrganizer_FRV);
        tvSchedule = (TextView) mInflatedView.findViewById(R.id.tvSchedule_FRV);
        tvNextRun = (TextView) mInflatedView.findViewById(R.id.tvNextRun_FRV);
        tvDeadline = (TextView) mInflatedView.findViewById(R.id.tvDeadline_FRV);
        tvAvailablePlaces = (TextView) mInflatedView.findViewById(R.id.tvAvailablePlaces_FRV);
        tvRaceDescription = (TextView) mInflatedView.findViewById(R.id.tvRaceDescription_FRV);
        tvInviteUser = (TextView) mInflatedView.findViewById(R.id.tvInviteUser_FRV);

        btnEdit = (Button) mInflatedView.findViewById(R.id.btnEdit_FRV);
        btnStart = (Button) mInflatedView.findViewById(R.id.btnStart_FRV);

        mapView = (MapView) mInflatedView.findViewById(R.id.map_FRV);
    }

    private void setListener(){
        btnEdit.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        tvInviteUser.setOnClickListener(this);
    }

    @Override
    public CharSequence getTitle() {
        if (mTitle == null)
            return "Race view";
        return mTitle;
    }

    @Override
    protected OnClickRightToolbarListener getRightOnClickListener() {
        return this;
    }

    @Override
    public void onMapReady(GoogleMap _googleMap) {
        mGoogleMap = _googleMap;
        mGoogleMap.setMyLocationEnabled(true);
//        mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    @Override
    protected void replaceFragmentWithBackStack(BaseFragment _fragment) {
        super.replaceFragmentWithBackStack(_fragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEdit_FRV:
                if (mRace != null)
                  replaceFragmentWithBackStack(EditRaceFragment.newInstance(mRace.getId()));
                break;
            case R.id.btnStart_FRV:
                replaceFragmentWithBackStack(new TimerBaseFragment());
                break;
            case R.id.tvInviteUser_FRV:
                inviteUser();
        }
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

    @Override
    protected int getRightToolbarImage() {
        return R.drawable.selector_registered_runners;
    }

    @Override
    public void onRightClick() {
        replaceFragmentWithBackStack(RegisteredRunnersFragment.getInstance(mRaceId));
    }

    // bluetooth methods

    private void initBluetooth(){
        mActivity.mBluetoothSPP = new BluetoothSPP(mActivity);

        mActivity.mBluetoothSPP.setBluetoothConnectionListener(this);
        mActivity.mBluetoothSPP.setBluetoothStateListener(this);

    }

    private void inviteUser(){
        //try to find bluetooth device


        if(!mActivity.mBluetoothSPP.isBluetoothAvailable()){

            // here we requesting for enabling bluetooth

            Toast.makeText(mActivity,"Turn on your bluetooth",Toast.LENGTH_SHORT).show();


        }else{
            mActivity.mBluetoothSPP.setupService();
            mActivity.mBluetoothSPP.startService(BluetoothState.DEVICE_ANDROID);
            // Ugly activity
            Intent intent = new Intent(mActivity, DeviceList.class);
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {



        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK) {

                // if everything is Ok we will try to connect
                mActivity.mBluetoothSPP.connect(data);

            }




        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                mActivity.mBluetoothSPP.setupService();
                mActivity.mBluetoothSPP.startService(BluetoothState.DEVICE_ANDROID);

            } else {
                // Do something if user doesn't choose any device (Pressed back)
            }
        }





    }

    private byte[] raceToBytes() {
        byte[] bytesArray = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(mRace);
        bytesArray = byteArrayOutputStream.toByteArray();
    }catch (IOException e){
            e.printStackTrace();
        }
        return bytesArray;
    }


    // bluetooth listeners

    @Override
    public void onDeviceConnected(String _name, String _address) {

        byte[] raceBytes = raceToBytes();
        if(raceBytes !=null) mActivity.mBluetoothSPP.send(raceBytes, true);
    }

    @Override
    public void onDeviceDisconnected() {

    }

    @Override
    public void onDeviceConnectionFailed() {

    }

    @Override
    public void onServiceStateChanged(int i) {

    }



}
