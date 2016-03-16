package com.morfitrun.fragments.timer_fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.morfitrun.R;
import com.morfitrun.fragments.BaseFragment;
import com.morfitrun.listeners.OnClickRightToolbarListener;

import static com.morfitrun.global.Constants.TAB_A_Z;
import static com.morfitrun.global.Constants.TAB_OFFLINE;
import static com.morfitrun.global.Constants.TAB_TIMER;
import static com.morfitrun.global.Constants.TIMER_AZ;
import static com.morfitrun.global.Constants.TIMER_FRAGMENT_OFFLINE_MODE;
import static com.morfitrun.global.Constants.TIMER_FRAGMENT_ONLINE_MODE;


/**
 * Created by Admin on 16.03.2015.
 */
public class TimerBaseFragment extends BaseFragment implements TabHost.OnTabChangeListener{


    private TabHost  mTabs;
    private Fragment frTimerOnline;
    private Fragment frTimerOffline;
    private Fragment frAZ;

    @Override
    protected void findViews() {
        mTabs = (TabHost) mInflatedView.findViewById(R.id.tabHost);
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.fragment_timer_base, container, false);
        findViews();
        setListeners();
        setupTabs();

        return mInflatedView;
    }
    private void setListeners(){

    }
    private void setupTabs() {


        mTabs.setup();
        mTabs.setOnTabChangedListener(this);
        TabHost.TabSpec spec = mTabs.newTabSpec(TAB_TIMER);

        spec.setContent(android.R.id.tabcontent);

        View viewAll = LayoutInflater.from(mActivity).inflate(R.layout.item_tab,null);
        TextView tvTabWon = (TextView)viewAll.findViewById(R.id.tvTab_ItemTab);
        tvTabWon.setText(getString(R.string.tab_timer));
        spec.setIndicator(viewAll);

        mTabs.addTab(spec);

        spec = mTabs.newTabSpec(TAB_A_Z);
        spec.setContent(android.R.id.tabcontent);

        View viewNew = LayoutInflater.from(mActivity).inflate(R.layout.item_tab,null);
        TextView tvTabBidding =(TextView) viewNew.findViewById(R.id.tvTab_ItemTab);
        tvTabBidding.setText(getString(R.string.tab_a_z));
        spec.setIndicator(viewNew);
        mTabs.addTab(spec);

        spec = mTabs.newTabSpec(TAB_OFFLINE);
        spec.setContent(android.R.id.tabcontent);

        View viewCanceled = LayoutInflater.from(mActivity).inflate(R.layout.item_tab,null);
        TextView tvTabCanceled = (TextView)viewCanceled.findViewById(R.id.tvTab_ItemTab);
        tvTabCanceled.setText(getString(R.string.tab_offline));
        spec.setIndicator(viewCanceled);
        mTabs.addTab(spec);
        mTabs.setCurrentTabByTag(TAB_OFFLINE);


    }






    private void startOnlineTimerFragment(){

        if(frTimerOnline != null){
            replaceFragment(frTimerOnline,TIMER_FRAGMENT_ONLINE_MODE);
        }else{
            frTimerOnline = new TimerFragment();
            replaceFragment(frTimerOnline,TIMER_FRAGMENT_ONLINE_MODE);
        }
    }


    private void startOfflineTimerFragment(){

        if(frTimerOffline != null){
            replaceFragment(frTimerOffline,TIMER_FRAGMENT_OFFLINE_MODE);
        }else{
            frTimerOffline = new TimerFragment();
            replaceFragment(frTimerOffline,TIMER_FRAGMENT_OFFLINE_MODE);
        }
    }

    private void startAZFragment(){

        if(frAZ!= null){
            replaceFragment(frAZ,TIMER_AZ);
        }else{
            frAZ = new TimerAZFragment();
            replaceFragment(frAZ,TIMER_AZ);
        }
    }

    private void replaceFragment(Fragment _fragment,String _tag){
        getFragmentManager()
                .beginTransaction()

                .replace(android.R.id.tabcontent, _fragment, _tag)
                .commitAllowingStateLoss();
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
    public void onTabChanged(String tabId) {

        switch (tabId){
            case TAB_TIMER:
                startOnlineTimerFragment();
                break;
            case TAB_A_Z:
                startAZFragment();
                break;
            case TAB_OFFLINE:
                startOfflineTimerFragment();
                break;
        }
    }
    @Override
    protected int getLeftToolbarImage() {
        return R.drawable.background_back_button_selector;
    }




}
