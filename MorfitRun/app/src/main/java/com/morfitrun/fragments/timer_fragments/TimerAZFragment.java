package com.morfitrun.fragments.timer_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.morfitrun.R;
import com.morfitrun.adapter.RegisteredRunnersListAdapter;
import com.morfitrun.data_models.RunnerUser;
import com.morfitrun.data_models.User;
import com.morfitrun.fragments.BaseFragment;
import com.morfitrun.fragments.ViewProfileFragment;
import com.morfitrun.listeners.OnClickRightToolbarListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Admin on 17.03.2015.
 */
public class TimerAZFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private ListView lvUsers;


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.fragment_timer_az, container, false);
        findViews();
        setListeners();
        initListView();
        return mInflatedView;
    }

    private void setListeners(){

    }

    private void initListView(){
        RegisteredRunnersListAdapter adapter = new RegisteredRunnersListAdapter(mActivity,R.layout.item_registered_runners,new ArrayList<RunnerUser>());
        lvUsers.setAdapter(adapter);
        lvUsers.setOnItemClickListener(this);
    }


    @Override
    protected void findViews() {
        lvUsers =(ListView)mInflatedView.findViewById(R.id.lvUsers_FT);
    }

    @Override
    public CharSequence getTitle() {
        return mActivity.getString(R.string.a_z);
    }

    @Override
    protected OnClickRightToolbarListener getRightOnClickListener() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RunnerUser user =  (RunnerUser)parent.getItemAtPosition(position);
        mActivity.replaceFragmentWithBackStack(ViewProfileFragment.getInstance(user));
    }
}
