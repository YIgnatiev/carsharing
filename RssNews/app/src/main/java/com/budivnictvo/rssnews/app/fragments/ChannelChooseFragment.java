package com.budivnictvo.rssnews.app.fragments;


import android.app.Fragment;
import android.content.res.TypedArray;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.budivnictvo.rssnews.app.MainActivity;
import com.budivnictvo.rssnews.app.R;
import com.budivnictvo.rssnews.app.adapters.ChannelAdapter;
import com.budivnictvo.rssnews.app.core.AppConstants;
import com.budivnictvo.rssnews.app.data.RssChannel;

import static com.budivnictvo.rssnews.app.core.AppConstants.*;

/**
 * Created by Администратор on 18.12.2014.
 */
public class ChannelChooseFragment extends Fragment implements AdapterView.OnItemClickListener  {
    private GridView gvChanels;
    private RssChannel[] mChannels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_channel_choose,container,false);
        findUI(rootView);
        initChannelsURL(getArguments().getInt(COUNTRY_CHOSEN));
        setAdapters();
        setListeners();
        setActionBar();
        return rootView;
    }


    private void findUI(View _rootView){
    gvChanels = (GridView)_rootView.findViewById(R.id.gvChooseChannel);
    }

    private void initChannelsURL(int _choice) {
        TypedArray typedArrayLinks  = null;

       switch (_choice){
           case RUSSIAN:
               typedArrayLinks = getResources().obtainTypedArray(R.array.rss_group_russian);
               break;
           case UKRAINEAN:
                typedArrayLinks = getResources().obtainTypedArray(R.array.rss_group_ukrinean);
               break;
       }
        mChannels = new RssChannel[typedArrayLinks.length()];

        if (typedArrayLinks != null) {
            for(int i =0 ; i < typedArrayLinks.length(); i++){
                RssChannel channel = new RssChannel();
                int channelId = typedArrayLinks.getResourceId(i,0);
                TypedArray channelArray = getResources().obtainTypedArray(channelId);

                int channelNameId = channelArray.getResourceId(0,0);
                int channelLogoId = channelArray.getResourceId(1,0);
                int categoriesArrayId = channelArray.getResourceId(2,0);
                channel.setName(getString(channelNameId));
                channel.setLogoId(channelLogoId);
                channel.setArrayOfCategoriesResourceID(categoriesArrayId);
                mChannels[i] = channel;
            }

        }
    }




    private void setListeners(){
        gvChanels.setOnItemClickListener(this);
    }

    private void setAdapters(){
        ChannelAdapter adapter = new ChannelAdapter(getActivity(),R.layout.channel_item, mChannels);
        gvChanels.setAdapter(adapter);
    }

    private void setActionBar(){
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.setBackButtonVisible(true);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        RssChannel channel =  mChannels[i];
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.CHANNEL_CHOSEN , channel);
        CategoryChooseFragment fragment = new CategoryChooseFragment();
        fragment.setArguments(bundle);
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.replaceFragmentWithBackstack(fragment);
    }

}
