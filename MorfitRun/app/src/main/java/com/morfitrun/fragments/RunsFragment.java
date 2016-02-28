package com.morfitrun.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.morfitrun.R;
import com.morfitrun.activity.LoginActivity;
import com.morfitrun.adapter.RacesListAdapter;
import com.morfitrun.api.ErrorProccessor;
import com.morfitrun.api.RestClient;
import com.morfitrun.data_models.SignOutCallback;
import com.morfitrun.data_models.race_model.Race;
import com.morfitrun.database.DBManager;
import com.morfitrun.global.Constants;
import com.morfitrun.global.UserPreference;
import com.morfitrun.listeners.OnClickRightToolbarListener;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasia on 11.03.2015.
 */
public class RunsFragment extends BaseFragment implements View.OnClickListener, Callback<List<Race>>,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        OnClickRightToolbarListener, BluetoothSPP.OnDataReceivedListener {

    private Button btnCreateRaces;
    private ListView lvRacesContainer;
    private ProgressBar pbLoadProgress;
    private ArrayList<Race> mRacesData;
    private RacesListAdapter mRaceListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.fragment_runs, container, false);
        findViews();
        setListener();
        if (mActivity.isModeOffline())
            readRaceFromDatabase();
        else getAllRaceRequest();
        return mInflatedView;
    }

    @Override
    protected void findViews() {
        btnCreateRaces = (Button) mInflatedView.findViewById(R.id.btnCreateRaces_FR);
        lvRacesContainer = (ListView) mInflatedView.findViewById(R.id.lvRacesContainer);
        pbLoadProgress = (ProgressBar) mInflatedView.findViewById(R.id.pbLoadProgress_FR);
    }

    private void setListener() {
        btnCreateRaces.setOnClickListener(this);
        lvRacesContainer.setOnItemClickListener(this);
        lvRacesContainer.setOnItemLongClickListener(this);
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.races);
    }

    @Override
    protected OnClickRightToolbarListener getRightOnClickListener() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateRaces_FR:
                replaceFragmentWithBackStack(new CreateRaceFragment());
                break;
        }
    }

    @Override
    protected void replaceFragmentWithBackStack(BaseFragment _fragment) {
        super.replaceFragmentWithBackStack(_fragment);
    }

    private void getAllRaceRequest() {
        pbLoadProgress.setVisibility(View.VISIBLE);
        RestClient.getInstance().getRaceService().getAllRace(this);
    }

    @Override
    public void success(List<Race> _raceList, Response _response) {
        pbLoadProgress.setVisibility(View.INVISIBLE);
        mRacesData = (ArrayList<Race>) _raceList;
        prepareRaceList();
    }

    @Override
    public void failure(RetrofitError _error) {
        pbLoadProgress.setVisibility(View.INVISIBLE);
        ErrorProccessor.processError(mActivity, _error);
    }

    private void readRaceFromDatabase() {
        mRacesData = DBManager.getRace(mActivity);
        prepareRaceList();
    }

    private void prepareRaceList() {
        mRaceListAdapter = new RacesListAdapter(mActivity, mRacesData);
        lvRacesContainer.setAdapter(mRaceListAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DBManager.writeToDatabase(mActivity, mRacesData);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        replaceFragmentWithBackStack(RaceViewFragment.newInstance(
                mRacesData.get(position).getId(),
                mRacesData.get(position).getTitle()));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int _position, long id) {
        if (!mActivity.isModeOffline())
            startDeleteDialog(mRacesData.get(_position).getId(), _position);
        return true;
    }

    private void startDeleteDialog(final String _raceId, final int _position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setNegativeButton(getString(R.string.cancel), null)
                .setTitle(getString(R.string.delete_notification))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRace(_raceId, _position);
                    }
                });
        dialog.show();
    }

    private void deleteRace(final String _raceId, final int _position) {
        RestClient.getInstance().getRaceService().deleteRace(_raceId, new Callback<Race>() {
            @Override
            public void success(Race race, Response response) {
                Toast.makeText(getActivity(), "delete race", Toast.LENGTH_SHORT).show();
                mRacesData.remove(_position);
                mRaceListAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError _error) {
                ErrorProccessor.processError(mActivity, _error);
            }
        });
    }


    @Override
    protected int getRightToolbarImage() {
        return android.R.drawable.ic_menu_set_as;
    }


    @Override
    public void onRightClick() {
//        receiveViaBluetooth();
        UserPreference.clearPreference(mActivity);
        RestClient.getInstance().getSingOutService().signOut(new Callback<SignOutCallback>() {
            @Override
            public void success(SignOutCallback signOutCallback, Response response) {
                Log.d("tag", "singOut");
                startLoginActivity();
            }

            @Override
            public void failure(RetrofitError _error) {
                ErrorProccessor.processError(mActivity, _error);
                startLoginActivity();
            }
        });

    }

    private void startLoginActivity() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        intent.putExtra(Constants.LOG_OUT, true);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    private Race createRaceObject(byte[] _bytes) {
        Race race = null;
        ObjectInputStream objectInputStream = null;

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(_bytes);
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);

            race = (Race) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return race;
    }

    private void addReceivedRace(Race _race) {

        if (mRaceListAdapter == null) {
            mRaceListAdapter = new RacesListAdapter(mActivity, new ArrayList<Race>());
            lvRacesContainer.setAdapter(mRaceListAdapter);
        }

        mRaceListAdapter.add(_race);
        mRaceListAdapter.notifyDataSetChanged();
    }

    // bluetoothMethods

    private void receiveViaBluetooth() {

        // method for initializing bluetooth , and setting OnDataReceiveListener

        if (mActivity.mBluetoothSPP == null) {

            mActivity.mBluetoothSPP = new BluetoothSPP(mActivity);
            mActivity.mBluetoothSPP.startService(BluetoothState.DEVICE_ANDROID);
            mActivity.mBluetoothSPP.setOnDataReceivedListener(this);
        }
    }


    @Override
    public void onDataReceived(byte[] bytes, String s) {
        Race race = createRaceObject(bytes);

        if (race != null) addReceivedRace(race);

        Toast.makeText(mActivity, "data received", Toast.LENGTH_SHORT).show();
    }


}
