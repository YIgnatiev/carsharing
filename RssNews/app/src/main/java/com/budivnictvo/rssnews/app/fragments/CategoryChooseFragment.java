package com.budivnictvo.rssnews.app.fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.budivnictvo.rssnews.app.MainActivity;
import com.budivnictvo.rssnews.app.OnRSSLoadCallback;
import com.budivnictvo.rssnews.app.R;
import com.budivnictvo.rssnews.app.adapters.CategoryAdapter;
import com.budivnictvo.rssnews.app.core.AppConstants;
import com.budivnictvo.rssnews.app.data.RssCategory;
import com.budivnictvo.rssnews.app.data.RssChannel;
import com.budivnictvo.rssnews.app.data.RssItem;

import com.budivnictvo.rssnews.app.utils.Network;
import com.budivnictvo.rssnews.app.utils.ResponseCallback;
import com.budivnictvo.rssnews.app.utils.RssApi;
import com.budivnictvo.rssnews.app.utils.RssParser;

import java.util.ArrayList;


/**
 * Created by Администратор on 19.12.2014.
 */
public class CategoryChooseFragment extends Fragment implements AdapterView.OnItemClickListener  {
    private ListView lvCategory;
    private RssChannel mChannel;
    private RssCategory[] mCategories;
    private RssCategory mCurrentCategory;
    private int currentPossition;
    private MainActivity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_category_choose, container, false);
        activity =(MainActivity)getActivity();
        mChannel = (RssChannel) getArguments().getSerializable(AppConstants.CHANNEL_CHOSEN);
        findUI(rootView);
        initCategoryArray();
        setAdapters();
        setListeners();
        return rootView;
    }

    private void findUI(View _rootVeiw) {
        lvCategory = (ListView) _rootVeiw.findViewById(R.id.lvCategory);

    }

    private void initCategoryArray() {
        TypedArray typedArray = getResources().obtainTypedArray(mChannel.getArrayOfCategoriesResourceID());
        mCategories = new RssCategory[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            RssCategory category = new RssCategory();
            int categoryArrayId = typedArray.getResourceId(i, 0);
            TypedArray categoryArray = getResources().obtainTypedArray(categoryArrayId);
            int categoryNameId = categoryArray.getResourceId(0, 0);
            category.setRssUrl(categoryArray.getString(1));
            category.setIconId(mChannel.getLogoId());
            category.setName(getString(categoryNameId));
            mCategories[i] = category;
        }
    }

    private void setAdapters() {
        CategoryAdapter adapter = new CategoryAdapter(getActivity(), R.layout.category_item, mCategories);
        lvCategory.setAdapter(adapter);
    }

    private void loadData(String _url) {
        activity.showProgressDialog();
        RssApi.getRss(_url,new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                activity.turnOffProgressDialog();
                RssParser parser = new RssParser();
                parser.parseXml(response);

                Fragment fragment =  new ListRSSFragment() ;

                mCurrentCategory.setmListItems(parser.getItems());
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.RSS_CATEGORY , mCurrentCategory);
                fragment.setArguments(bundle);
                MainActivity activity = (MainActivity)getActivity();
                activity.replaceFragmentWithBackstack(fragment);
            }

            @Override
            public void onFailure(Object object) {
                activity.turnOffProgressDialog();
                Toast.makeText(getActivity(),"Connection error",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setListeners(){
        lvCategory.setOnItemClickListener(this);
    }


    private boolean isConnected(){
        boolean connected = false;
        if (Network.isInternetConnectionAvailable(getActivity())){
            connected = true;
        } else {
            showAlertDialog("Отсутсвует интернет соединение. Пожалуйста, включите и попробуйте снова", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                  getNews();

                }
            });
        }
        return connected;
    }

    public final void showAlertDialog(final String _message, final DialogInterface.OnClickListener _tryAgainListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(_message)
                .setCancelable(false)
                .setPositiveButton("Попробовать еще раз", _tryAgainListener)
                .setNegativeButton("Выход", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finish();
                    }
                });
        builder.create().show();

    }

    private void getNews(){
        if(isConnected()) {
            mCurrentCategory = mCategories[currentPossition];
            loadData(mCurrentCategory.getRssUrl());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
       currentPossition = position;
        getNews();
    }


}
