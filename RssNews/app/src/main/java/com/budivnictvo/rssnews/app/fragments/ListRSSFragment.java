package com.budivnictvo.rssnews.app.fragments;


import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.budivnictvo.rssnews.app.MainActivity;
import com.budivnictvo.rssnews.app.R;
import com.budivnictvo.rssnews.app.adapters.ListRSSAdapter;
import com.budivnictvo.rssnews.app.core.AppConstants;
import com.budivnictvo.rssnews.app.data.RssCategory;
import com.budivnictvo.rssnews.app.data.RssItem;


import java.util.ArrayList;

/**
 * Created by Администратор on 16.12.2014.
 */
public class ListRSSFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView lvRssList;
    private ArrayList<RssItem> mRssItems;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list,container,false);
        findViews(rootView);
        setItems();
        setListeners();
        return rootView;
    }

    private void findViews(View _rootView){
        lvRssList = (ListView)_rootView.findViewById(R.id.lvItems);
    }

    private void setItems(){
        RssCategory category =  (RssCategory)getArguments().getSerializable(AppConstants.RSS_CATEGORY);
        mRssItems = category.getmListItems();
        setAdapters();
    }


    private void setAdapters(){
        ListRSSAdapter adapter = new ListRSSAdapter(getActivity(), R.id.lvItems,R.layout.list_item, mRssItems);
        lvRssList.setAdapter(adapter);
    }

    private void setListeners(){
        lvRssList.setOnItemClickListener(this);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        RssItem item = mRssItems.get(position);
        DetailRssFragment fragment =  new DetailRssFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.RSS_ITEM , item);
        fragment.setArguments(bundle);
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.replaceFragmentWithBackstack(fragment);

    }
}
