package com.morfitrun.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.morfitrun.R;
import com.morfitrun.adapter.RegisteredRunnersListAdapter;
import com.morfitrun.api.ErrorProccessor;
import com.morfitrun.api.RestClient;
import com.morfitrun.data_models.Runner;
import com.morfitrun.data_models.RunnerUser;
import com.morfitrun.data_models.User;

import com.morfitrun.listeners.OnClickLeftToolbarListener;
import com.morfitrun.listeners.OnClickRightToolbarListener;


import static com.morfitrun.global.Constants.*;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Admin on 13.03.2015.
 */
public class RegisteredRunnersFragment extends BaseFragment implements
        TabHost.OnTabChangeListener, AdapterView.OnItemClickListener, View.OnClickListener, Callback<List<Runner>>
         {


    private TabHost mTabs;
    private ListView lvUsers;
    private TextView tvAddRunner;
    private ProgressBar pbLoad;
    private RegisteredRunnersListAdapter mAdapter;
    private List<Runner> mRunners;
    private String mRaceId;

    public static RegisteredRunnersFragment getInstance(String _raceId) {
        RegisteredRunnersFragment fragment = new RegisteredRunnersFragment();
        Bundle bundle = new Bundle();
        bundle.putString(RACE_ID, _raceId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);
    }

     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         if(getArguments() != null) {
             mRaceId = getArguments().getString(RACE_ID, "0");
         }

     }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.fragment_registered_runners, container, false);
        findViews();
        setListeners();
        initListViews();
        setupTabs();
        getRunners(mRaceId);
        return mInflatedView;
    }

    @Override
    protected void findViews() {
        lvUsers = (ListView) mInflatedView.findViewById(R.id.lvUsers_FRegRun);
        tvAddRunner = (TextView) mInflatedView.findViewById(R.id.tvAddRunner_FRegRun);
        mTabs = (TabHost) mInflatedView.findViewById(R.id.tabHost);
        pbLoad = (ProgressBar) mInflatedView.findViewById(R.id.pbLoadProgress_FRegRun);
    }


    private void setListeners() {
        tvAddRunner.setOnClickListener(this);
    }

    private void setupTabs() {


        mTabs.setup();
        mTabs.setOnTabChangedListener(this);
        TabHost.TabSpec spec = mTabs.newTabSpec(TAB_ALL);

        spec.setContent(R.id.lvUsers_FRegRun);

        View viewAll = LayoutInflater.from(mActivity).inflate(R.layout.item_tab, null);
        TextView tvTabWon = (TextView) viewAll.findViewById(R.id.tvTab_ItemTab);
        tvTabWon.setText(getString(R.string.tab_all));
        spec.setIndicator(viewAll);

        mTabs.addTab(spec);

        spec = mTabs.newTabSpec(TAB_NEW);
        spec.setContent(R.id.lvUsers_FRegRun);

        View viewNew = LayoutInflater.from(mActivity).inflate(R.layout.item_tab, null);
        TextView tvTabBidding = (TextView) viewNew.findViewById(R.id.tvTab_ItemTab);
        tvTabBidding.setText(getString(R.string.tab_new));
        spec.setIndicator(viewNew);
        mTabs.addTab(spec);

        spec = mTabs.newTabSpec(TAB_CANCELED);
        spec.setContent(R.id.lvUsers_FRegRun);

        View viewCanceled = LayoutInflater.from(mActivity).inflate(R.layout.item_tab, null);
        TextView tvTabCanceled = (TextView) viewCanceled.findViewById(R.id.tvTab_ItemTab);
        tvTabCanceled.setText(getString(R.string.tab_canceled));
        spec.setIndicator(viewCanceled);
        mTabs.addTab(spec);

        mTabs.setCurrentTab(1);

    }

    private void initListViews() {
        if (mAdapter == null) {
            mAdapter = new RegisteredRunnersListAdapter(mActivity, R.layout.item_registered_runners, new ArrayList<RunnerUser>());

            pbLoad.setVisibility(View.VISIBLE);
        }
        lvUsers.setAdapter(mAdapter);
        lvUsers.setAdapter(mAdapter);
        lvUsers.setOnItemClickListener(this);
        mAdapter.notifyDataSetChanged();
    }

    private void getRunners(String _raceId) {
        RestClient.getInstance().getRunnersService().getRunners(_raceId, this);
    }

    @Override
    public void success(List<Runner> runners, Response response) {
        mRunners = runners;
        changeListViewContent(getAllUsers());
        pbLoad.setVisibility(View.GONE);
        mTabs.setCurrentTabByTag(TAB_ALL);
    }

    @Override
    public void failure(RetrofitError _error) {
        ErrorProccessor.processError(getActivity(), _error);
        pbLoad.setVisibility(View.GONE);
    }

    private List<RunnerUser> getAllUsers() {

        List<RunnerUser> list = new ArrayList<>();
        if (mRunners != null) {
            for (Runner runner : mRunners) {
                list.add(runner.getUserId());
            }
        }
        return list;

    }

    private List<RunnerUser> getCanceledUsers() {
        List<RunnerUser> list = new ArrayList<>();
        if (mRunners != null) {
            for (Runner runner : mRunners) {
                if (runner.getStatus() == USER_STATUS_CANCELED)
                    list.add(runner.getUserId());
            }
        }
        return list;
    }

    private List<RunnerUser> getNewUsers() {
        List<RunnerUser> list = new ArrayList<>();
        if (mRunners != null) {
            for (Runner runner : mRunners) {
                if (runner.getStatus() == USER_STATUS_NEW)
                    list.add(runner.getUserId());
            }
        }
        return list;
    }

    private void changeListViewContent(List<RunnerUser> list) {
        mAdapter.clear();
        mAdapter.addAll(list);
        mAdapter.notifyDataSetChanged();

    }

    private void addNewUser() {
      mActivity.replaceFragmentWithBackStack(AddRunnerFragment
                      .getInstance(mRaceId));
    }


    @Override
    public CharSequence getTitle() {
        return mActivity.getString(R.string.registered_runners);

    }

    @Override
    protected OnClickRightToolbarListener getRightOnClickListener() {
        return null;
    }

    @Override
    protected int getRightToolbarImage() {
        return super.getRightToolbarImage();
    }

    @Override
    public void onTabChanged(String tabId) {

        switch (tabId) {
            case TAB_ALL:
                changeListViewContent(getAllUsers());
                break;
            case TAB_NEW:
                changeListViewContent(getNewUsers());
                break;
            case TAB_CANCELED:
                changeListViewContent(getCanceledUsers());
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RunnerUser user = mAdapter.getItem(position);
        mActivity.replaceFragmentWithBackStack(ViewProfileFragment.getInstance(user));
    }

    @Override
    public void onClick(View v) {
        addNewUser();
    }



}
