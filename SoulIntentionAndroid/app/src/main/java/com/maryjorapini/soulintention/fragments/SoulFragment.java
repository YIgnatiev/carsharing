package com.maryjorapini.soulintention.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.maryjorapini.soulintention.MainFragmentActivity;
import com.maryjorapini.soulintention.R;
import com.maryjorapini.soulintention.adapters.NextPageCallback;
import com.maryjorapini.soulintention.adapters.PostAdapter;
import com.maryjorapini.soulintention.core.rest.RetrofitAdapter;
import com.maryjorapini.soulintention.core.rest.RetrofitInterface;
import com.maryjorapini.soulintention.custom_views.swipe_item.StateItem;
import com.maryjorapini.soulintention.data_models.response.FavouriteIdResponseModel;
import com.maryjorapini.soulintention.data_models.response.Post.PostResponseModel;
import com.maryjorapini.soulintention.global.Constants;
import com.maryjorapini.soulintention.global.Network;
import com.maryjorapini.soulintention.global.ProgressDialogWorker;
import com.maryjorapini.soulintention.global.SocialNetworkVariables;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoulFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SoulFragment extends android.support.v4.app.Fragment implements NextPageCallback ,SwipeRefreshLayout.OnRefreshListener ,AbsListView.OnScrollListener, View.OnClickListener ,PopupMenu.OnMenuItemClickListener {

    private String mPostType;

    private Callback<List<PostResponseModel>> mSoulCallback;
    private Callback<List<PostResponseModel>> mSearchCallback;
    private Callback<List<PostResponseModel>> mFavouriteCallback;
    private Callback<List<FavouriteIdResponseModel>> mGetFavouriteIDCallback;

    private RetrofitInterface retrofitInterface;

    private FragmentActivity mActivity;
    private ListView lvPost;
    private TextView tvEmpty;
    private SwipeRefreshLayout srLayout;
    private View footer;
    private ImageView ivSort;

    private PopupMenu popupMenu;
    private List<PostResponseModel> mPostList;
    private PostAdapter mPostAdapter;
    private ProgressBar pbPost;
    private  SearchView mSearchView;
    private final int   PAGE_SIZE           = 15;
    private boolean     mIsFirstListItem    = true;
    private boolean     mIsLastListItem     = false;
    private int         mOffset             = 0;
    private int         mLimit              = PAGE_SIZE;
    private boolean     mUsingSort           = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param _postType type of list.
     * @return A new instance of fragment SoulFragment.
     */
    public static SoulFragment newInstance(String _postType) {
        SoulFragment fragment = new SoulFragment();
        Bundle args = new Bundle();
        args.putString(Constants.POST_TYPE, _postType);
        fragment.setArguments(args);
        return fragment;
    }

    public SoulFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
        if (getArguments() != null) {
            mPostType = getArguments().getString(Constants.POST_TYPE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mOffset = 0;
        mSearchView.clearFocus();
        mIsFirstListItem = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        footer = inflater.inflate(R.layout.web_footer, null);
        findUI(rootView);
        initPostAdapter();
        retrofitInterface = RetrofitAdapter.getInterface();
        setListeners();
        initRefreshLayout();
        implementSoulCallBack();
        implementSearchCallBack();
        return rootView;
}

    private void implementSearchCallBack() {
        mSearchCallback = new Callback<List<PostResponseModel>>() {
            @Override
            public void failure(RetrofitError error) {
                Log.e("RETROFIT", error.toString());
            }


            @Override
            public void success(List<PostResponseModel> responseModel, Response response) {
                if(responseModel.isEmpty()){
                    tvEmpty.setText(getString(R.string.empty_view_message_favorite));
                    tvEmpty.setVisibility(View.VISIBLE);
                }else {
                    tvEmpty.setVisibility(View.GONE);
                }
                ProgressDialogWorker.dismissDialog();

                if (mIsFirstListItem){
                    mPostList =  getPostList(responseModel);
                }else {
                    mPostList.addAll(getPostList(responseModel));
                }
                mPostAdapter.updateList(mPostList);
            }
        };
    }

    private void setListeners() {
        ((ImageView)mActivity.getActionBar().getCustomView().findViewById(R.id.ivSort_AB)).setOnClickListener(this);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                mSearchView.setQuery("", false);
                mSearchView.setIconified(true);
                mSearchView.clearFocus();
                ProgressDialogWorker.createDialog(mActivity);
                makeSearchRequest(s);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

    private void findUI(View _view) {
        lvPost          = (ListView) _view.findViewById(R.id.lvPostList_FP);
        srLayout        = (SwipeRefreshLayout)_view.findViewById(R.id.srLayout);
        tvEmpty         =  (TextView)_view.findViewById(R.id.tvEmpty);
        pbPost          = (ProgressBar)_view.findViewById(R.id.pbPost);
        mSearchView = (SearchView)mActivity.getActionBar().getCustomView().findViewById(R.id.swSearch_AB);

        tvEmpty.setVisibility(View.GONE);

    }

   private void showPopUp(View _view) {
        if (popupMenu == null) {
            popupMenu = new PopupMenu(getActivity(), _view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.sorting_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
        }
        popupMenu.show();
    }



    private void initRefreshLayout(){
        srLayout.setColorSchemeResources(R.color.base_purple_color);
        srLayout.setOnRefreshListener(this);
        lvPost.setOnScrollListener(this);


    }
    @Override
    public void onResume() {
        super.onResume();
        mSearchView.setIconified(true);
        setSortingMethod(mUsingSort);
    }

    private void initPostAdapter() {
        mPostList       = new ArrayList<PostResponseModel>();
        mPostAdapter    = new PostAdapter(mActivity, mPostList, this);
        lvPost.addFooterView(footer);
        lvPost.setAdapter(mPostAdapter);
    }

    private void implementSoulCallBack() {

        mSoulCallback = new Callback<List<PostResponseModel>>() {
            @Override
            public void failure(RetrofitError error) {
                Log.e("RETROFIT", error.toString());
            }


            @Override
            public void success(List<PostResponseModel> responseModel, Response response) {
                pbPost.setVisibility(View.GONE);
                if (responseModel.size() < PAGE_SIZE){
                    mIsLastListItem = true;
                }else {
                    mIsLastListItem = false;
                }

                ProgressDialogWorker.dismissDialog();
                srLayout.setRefreshing(false);
                if (mIsFirstListItem){
                    mPostList =  getPostList(responseModel);
                }else {
                    mPostList.addAll(getPostList(responseModel));
                }


                if(mPostList.isEmpty()){
                    tvEmpty.setText(getString(R.string.empty_view_message_favorite));
                    tvEmpty.setVisibility(View.VISIBLE);
                }else {
                    tvEmpty.setVisibility(View.GONE);
                }


                checkForReposts(mPostList);
                mPostAdapter.updateList(mPostList);

            }
        };
    }

    private void checkForReposts(List<PostResponseModel> mPostList){
        for (PostResponseModel model: mPostList){
            model.isFacebookPressed     = SocialNetworkVariables.getFacebookPostInfo().contains(model.getId());
            model.isTwitterPressed      = SocialNetworkVariables.getTwitterPostInfo().contains(model.getId());
        }
    }

    private void makeGetPostRequest() {
        if (isConnected()){
            if (mOffset > 0) ProgressDialogWorker.createDialog(mActivity);
            if (pbPost.getVisibility() == View.GONE) ProgressDialogWorker.createDialog(mActivity);
            if (mPostType.equals(Constants.POST_TYPE_FAVOURITE)) {
                if (!mUsingSort) {
                    retrofitInterface.getFavourites(mLimit, mOffset, mSoulCallback);
                } else {
                    retrofitInterface.getFavourites(mLimit, mOffset, Constants.PARAM_RATE, mSoulCallback);
                }
            } else {
                if (!mUsingSort) {
                    retrofitInterface.getPost(mLimit, mOffset, mSoulCallback);
                } else {
                    retrofitInterface.getPost(mLimit, mOffset, Constants.PARAM_RATE, mSoulCallback);
                }
            }
        }
    }

    private void makeSearchRequest(final String _title) {
        mOffset = 0;
        if (mPostType.equals(Constants.POST_TYPE_FAVOURITE)) {
            retrofitInterface.getFavouritesSearch(mLimit, mOffset, _title, mSoulCallback);
        } else {
            retrofitInterface.getSearch(_title, mLimit, mOffset, mSearchCallback);
        }
    }

    private final List<PostResponseModel> getPostList(final List<PostResponseModel> _favoriteList) {
        List<PostResponseModel> list = new ArrayList<PostResponseModel>();
        for (PostResponseModel model : _favoriteList){
            model.stateItem = StateItem.IDLE;
            list.add(model);
        }
        return list;
    }


    @Override
    public void nextPage() {
        if (mPostList.size() >= PAGE_SIZE && !mIsLastListItem) {
            mOffset = mPostList.size();
            makeGetPostRequest();
    }
    }

    @Override
    public void onRefresh() {

        mOffset = 0;
        mIsFirstListItem = true;
        ((MainFragmentActivity)mActivity).hideSoftKeyboard();
        makeGetPostRequest();


}

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if((firstVisibleItem == 0 && visibleItemCount > 0 && lvPost.getChildAt(0).getTop() >= 0) || mPostList.isEmpty()){
            srLayout.setEnabled(true);
            mIsFirstListItem = true;
        }else{
            srLayout.setEnabled(false);
            mIsFirstListItem = false;
        }
    }



    private void showSortButton (){

        if (mUsingSort){
            ((ImageView)mActivity.getActionBar().getCustomView().findViewById(R.id.ivSort_AB)).setImageResource(R.drawable.filter_fav);
        } else {
            ((ImageView)mActivity.getActionBar().getCustomView().findViewById(R.id.ivSort_AB)).setImageResource(R.drawable.filter_cal);

        }
    }

    private void setSortingMethod(boolean _newUsingSort){
        mUsingSort = _newUsingSort;
        showSortButton();
        makeGetPostRequest();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivSort_AB:
                if (mSearchView.isIconified()) showPopUp(view);
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()){
            case R.id.sort_by_favourites:
               setSortingMethod(true);
                return true;
            case R.id.sort_by_calendar:
              setSortingMethod(false);
               return true;
            default:
                return false;
        }
    }

    public final void showAlertDialog(final String _message, final DialogInterface.OnClickListener _tryAgainListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(_message)
                .setCancelable(false)
                .setPositiveButton("Try again", _tryAgainListener)
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mActivity.finish();
                    }
                });
        builder.create().show();
    }
    private boolean isConnected(){
        boolean connected = false;
        if (Network.isInternetConnectionAvailable(mActivity)){
            connected = true;
        } else {
            showAlertDialog("No internet connection. Please, turn on and try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    makeGetPostRequest();
                }
            });
        }
        return connected;
    }
}
