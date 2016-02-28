package com.morfitrun.fragments.timer_fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.morfitrun.R;
import com.morfitrun.adapter.TimerAdapter;
import com.morfitrun.data_models.Runner;
import com.morfitrun.fragments.BaseFragment;
import com.morfitrun.fragments.RunsFragment;
import com.morfitrun.global.Stopwatch;
import com.morfitrun.listeners.OnClickRightToolbarListener;

import java.util.ArrayList;


/**
 * Created by Admin on 17.03.2015.
 */
public class TimerFragment extends BaseFragment implements View.OnClickListener ,AdapterView.OnItemClickListener {


    private ListView lvRunners;
    private TextView tvSplit;
    private TextView tvStart;
    private TextView tvFinish;
    private TextView tvTimer;
    private Stopwatch stopwatch;
    private TimerAdapter mAdapter;
    private int mPosition;
    private boolean isTimerRunning;
    private boolean isTimerMode; //dialog flag

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mInflatedView != null) return mInflatedView;   // if already inited - return;

        mInflatedView = inflater.inflate(R.layout.fragment_timer, container, false);
        findViews();
        initListView();
        setListeners();
        return mInflatedView;
    }


    @Override
    protected void findViews() {
        lvRunners = (ListView) mInflatedView.findViewById(R.id.lvRunners_FT);
        tvSplit = (TextView) mInflatedView.findViewById(R.id.tvSplit_FT);
        tvStart = (TextView) mInflatedView.findViewById(R.id.tvStart_FT);
        tvFinish = (TextView) mInflatedView.findViewById(R.id.tvFinish_FT);
        tvTimer = (TextView) mInflatedView.findViewById(R.id.tvTimer_FT);

    }

    private void setListeners() {
        tvSplit.setOnClickListener(this);
        tvStart.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
        lvRunners.setOnItemClickListener(this);
    }

    private void initListView() {
        if (mAdapter == null) {
            mAdapter = new TimerAdapter(mActivity, R.layout.item_timer, new ArrayList<Runner>());
        }
        View header = LayoutInflater.from(mActivity).inflate(R.layout.item_timer_header, new LinearLayout(mActivity));
        lvRunners.addHeaderView(header);
        lvRunners.setAdapter(mAdapter);
    }

    private void startTimer() {

        tvStart.setText(getString(R.string.reset));

        if (stopwatch == null) {
            stopwatch = new Stopwatch(tvTimer);
        }
        if (!isTimerRunning) {
            isTimerRunning = true;
            stopwatch.start();
        }

    }

    private void splitTimer() {
        if (isTimerRunning) {
            mPosition++;
            Runner runner = new Runner(mPosition, stopwatch.getTime());
            mAdapter.add(runner);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void finishTimer() {
        resetTimer();


        mActivity.replaceTopNavigationFragment(new RunsFragment());
    }

    private void resetTimer() {
        if (isTimerRunning) {
            isTimerRunning = false;
            mPosition = 0;

            stopwatch.stop();
            stopwatch = null;

            mAdapter.clear();
            mAdapter.notifyDataSetChanged();

            tvTimer.setText(R.string.timer_zero);
            tvStart.setText(getString(R.string.start));
        }
    }

    private void showDialog(boolean isTimerMode , Runner runner) {
        View viewGroup = mActivity.getLayoutInflater().inflate(R.layout.dialog_timer, new LinearLayout(mActivity));
        FrameLayout flTimer = (FrameLayout) viewGroup.findViewById(R.id.flTimer_DFT);
        FrameLayout flPosition = (FrameLayout) viewGroup.findViewById(R.id.flPosition_DFT);
        if (isTimerMode){
            flTimer.setEnabled(true);
            flPosition.setEnabled(false);
        }else {
            flTimer.setEnabled(false);
            flPosition.setEnabled(true);
        }
        flPosition.setOnClickListener(this);
        flTimer.setOnClickListener(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(viewGroup);

        final AlertDialog adDialog = builder.create();

        adDialog.show();
    }


    private void switchViewEnable(View _view){
        if(_view.isEnabled()){
            _view.setEnabled(false);
        }else {
            _view.setEnabled(true);
        }
    }

    @Override
    public CharSequence getTitle() {
        return mActivity.getString(R.string.timer);
    }

    @Override
    protected OnClickRightToolbarListener getRightOnClickListener() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSplit_FT:
                splitTimer();
                break;
            case R.id.tvStart_FT:
                if (!isTimerRunning) {
                    startTimer();
                } else {
                    resetTimer();
                }
                break;
            case R.id.tvFinish_FT:
                finishTimer();
                break;
            case R.id.flPosition_DFT:
                switchViewEnable(v);
                break;
            case R.id.flTimer_DFT:
                switchViewEnable(v);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Runner runner = (Runner)parent.getItemAtPosition(position); // you probably must do some action with runner in dialog
        showDialog(isTimerMode , runner);
    }
}
