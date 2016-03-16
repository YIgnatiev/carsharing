package com.morfitrun.activity;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import com.morfitrun.R;
import com.morfitrun.fragments.BaseFragment;
import com.morfitrun.fragments.RecordFragment;
import com.morfitrun.fragments.RunsFragment;
import com.morfitrun.global.Constants;
import com.morfitrun.global.ProgressDialogManager;
import com.morfitrun.listeners.OnClickLeftToolbarListener;
import com.morfitrun.services.record_service.RecordService;
import com.morfitrun.services.record_service.RecordServiceBinder;
import com.morfitrun.widgets.MorFitToolBar;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class MainActivity extends ActionBarActivity implements OnClickLeftToolbarListener, ServiceConnection {

    private MorFitToolBar mToolbar;
    private ProgressDialogManager pbManager;
    private BaseFragment mCurrentBaseFragment;
    public BluetoothSPP mBluetoothSPP ;
    private boolean modeOffline = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
        Intent intent = getIntent();
        modeOffline = intent.getBooleanExtra(Constants.MODE_OFFLINE, true);
        replaceTopNavigationFragment(new RunsFragment());
        pbManager = new ProgressDialogManager(this);
        bindToRecordService();
    }

    public  void setModeOffline(final boolean _modeOffline){
        modeOffline = _modeOffline;
    }

    public boolean isModeOffline(){
        return modeOffline;
    }

    private void bindToRecordService(){
        Intent intent = new Intent(this, RecordService.class);
        bindService(intent, this, 0);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        final RecordService recordService = ((RecordServiceBinder)service).getService();
        replaceFragmentWithBackStack(RecordFragment.newInstance(recordService.getRaceId()));
        unbindService(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private final void initActionBar() {
        mToolbar = (MorFitToolBar) findViewById(R.id.tbMain_AM);
        setSupportActionBar(mToolbar);
    }

    public final MorFitToolBar getToolBar() {
        return mToolbar;
    }

    @Override
    public void onLeftClick() {
        onBackPressed();
    }

    public final int getLeftToolbarImage() {
        final int backStackEntryCount = getFragmentManager().getBackStackEntryCount();

        if (backStackEntryCount > 0) {
            return R.drawable.background_back_button_selector;
        } else {
            return -1;
        }
    }

    @Override
    public void onBackPressed() {
        if (mCurrentBaseFragment != null && mCurrentBaseFragment.isNeedGoBack()){
            mCurrentBaseFragment.onBack();
            return;
        }

        final int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public final void replaceFragmentWithBackStack(final BaseFragment _fragment) {

        mCurrentBaseFragment = _fragment;
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                mToolbar.setLeftImage(getLeftToolbarImage());
                mToolbar.setOnClickLeftListener(MainActivity.this);
            }
        });

        getFragmentManager().beginTransaction()
                .replace(R.id.flContainer_AM, _fragment)
                .addToBackStack(null)
                .commit();
    }

    public final void replaceTopNavigationFragment(final BaseFragment _fragment) {
        mCurrentBaseFragment = _fragment;
        getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getFragmentManager().beginTransaction()
                .replace(R.id.flContainer_AM, _fragment)
                .commit();
    }

    public final void showProgress(final String _message) {
        pbManager.showProgressDialog(_message);
    }

    public final void hideProgress() {
        pbManager.hideProgressDialog();
    }


}
