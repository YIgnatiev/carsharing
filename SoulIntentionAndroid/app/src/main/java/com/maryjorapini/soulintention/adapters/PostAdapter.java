package com.maryjorapini.soulintention.adapters;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.twitter.TwitterSocialNetwork;
import com.maryjorapini.soulintention.DateFormater;
import com.maryjorapini.soulintention.FragmentReplacer;
import com.maryjorapini.soulintention.R;
import com.maryjorapini.soulintention.SocialNetworks;
import com.maryjorapini.soulintention.core.rest.RetrofitAdapter;
import com.maryjorapini.soulintention.custom_views.swipe_item.PostSwipeView;
import com.maryjorapini.soulintention.custom_views.swipe_item.SwipeListener;
import com.maryjorapini.soulintention.data_models.response.Post.PostResponseModel;
import com.maryjorapini.soulintention.data_models.response.SessionMobileResponseModel;
import com.maryjorapini.soulintention.fragments.DetailFragment;
import com.maryjorapini.soulintention.global.SocialNetworkVariables;

import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Sasha on 24.10.2014.
 */
public class PostAdapter extends BaseAdapter implements OnPostingCompleteListener ,OnLoginCompleteListener{
    private final LayoutInflater mInflater;
    private List<PostResponseModel> mComponentsList;
    private PostResponseModel mWaitingForSuccessFacebookModel;
    private PostResponseModel mWaitingForSuccessTwitterModel;
    private FragmentActivity mContext;
    private Fragment mFragment;
    private Callback<SessionMobileResponseModel> mPostFavouriteCallback;
    private Callback<SessionMobileResponseModel> mDeleteFavouriteCallback;
    public NextPageCallback nextPageCallback;
    private int repeatPosition;
    private SocialNetworks mSocialNetworks;
    private ProgressDialog mDialog;
    public static boolean isJustOncePressed = true;
    private String mMessage;

    public PostAdapter(final FragmentActivity _context, final List<PostResponseModel> _list, final Fragment _fragment) {
        this.mContext = _context;
        this.mComponentsList = _list;
        this.mInflater = LayoutInflater.from(_context.getApplicationContext());
        this.mFragment = _fragment;
        this.nextPageCallback = (NextPageCallback) _fragment;
        mSocialNetworks = new SocialNetworks(_fragment.getParentFragment());
        implementDeleteFavouriteCallBack();
        implementPostFavouriteCallBack();
    }

    @Override
    public int getCount() {
        return mComponentsList.size();
    }

    @Override
    public PostResponseModel getItem(final int _position) {
        return mComponentsList.get(_position);
    }

    @Override
    public long getItemId(final int _position) {
        return _position;
    }

    @Override
    public View getView(final int _position, View _convertView, ViewGroup _parent) {

        if (_position == getCount() - 1 && nextPageCallback != null && _position != repeatPosition) {
            nextPageCallback.nextPage();
            repeatPosition = _position;
        }

        ViewHolder holder = null;
        if (_convertView == null) {
            _convertView = mInflater.inflate(R.layout.item_list, _parent, false);
            holder = new ViewHolder();
            holder.initHolder(_convertView);
            _convertView.setTag(holder);
        } else {
            holder = (ViewHolder) _convertView.getTag();
        }
        holder.setData(getItem(_position));
        ((PostSwipeView) _convertView).setStateCurrentItem(getItem(_position));
        ((PostSwipeView) _convertView).setOnItemClickListener(new SwipeListener() {
            @Override
            public void onItemClick() {
                FragmentReplacer.replaceCurrentFragment(mContext, DetailFragment.newInstance(getItem(_position), mSocialNetworks));
            }
        });

        return _convertView;
    }

    private class ViewHolder {

        public TextView tvTitle;
        public TextView tvInfo;
        public TextView tvData;
        public ImageView ivLiked;
        public ImageView ivFacebook;
        public ImageView ivTwitter;

        public void initHolder(View _convertView) {
            this.ivLiked = (ImageView) _convertView.findViewById(R.id.ivSwipeLike_IL);
            this.tvTitle = (TextView) _convertView.findViewById(R.id.tvTitle_ILP);
            this.tvInfo = (TextView) _convertView.findViewById(R.id.tvInfo_ILP);
            this.tvData = (TextView) _convertView.findViewById(R.id.tvDate_ILP);

            this.ivFacebook = (ImageView) _convertView.findViewById(R.id.ivSwipeFacebook_IS);
            this.ivTwitter = (ImageView) _convertView.findViewById(R.id.ivSwipeTwitter_IS);
        }

        public void setData(PostResponseModel _data) {
            setLikeListener(_data);
            setFacebookPostedListener(_data);
            setTwitterPostedListener(_data);
            if (_data.isFavourite()) {
                ivLiked.setImageResource(R.drawable.ic_favorite_swype_press);
            } else {
                ivLiked.setImageResource(R.drawable.ic_favorite_swype);
            }

            Date date = null;
            if (_data.getPost_on_date() != null){
                date = DateFormater.dateParser(_data.getPost_on_date());
            }else{
                date = DateFormater.dateParser(_data.getCreatedAt());
            }

            tvData.setText(DateFormater.dateFormat(date));

            tvInfo.setText(_data.getDetails());
            tvTitle.setText(_data.getTitle());

            if (_data.isFacebookPressed) {
                this.ivFacebook.setImageResource(R.drawable.ic_facebook_swype_press);
                this.ivFacebook.setClickable(false);
            } else {
                this.ivFacebook.setImageResource(R.drawable.ic_facebook_swype);
                this.ivFacebook.setClickable(true);
            }
            if (_data.isTwitterPressed) {
                this.ivTwitter.setImageResource(R.drawable.ic_twitter_swype_press);
                this.ivTwitter.setClickable(false);
            } else {
                this.ivTwitter.setImageResource(R.drawable.ic_twitter_swype);
                this.ivTwitter.setClickable(true);
            }

        }

        private void setLikeListener(final PostResponseModel _data) {
            this.ivLiked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setLike(_data);
                }
            });
        }

        private void setFacebookPostedListener(final PostResponseModel _data) {
            this.ivFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (isJustOncePressed) {
                        isJustOncePressed = false;
                        mWaitingForSuccessFacebookModel = _data;
                        mDialog = new ProgressDialog(mFragment.getActivity());
                        mDialog.setMessage(mContext.getString(R.string.posting_repost));
                        mDialog.setCancelable(false);


                        final StringBuilder builder = new StringBuilder();
                        builder.append(_data.getTitle());
                        builder.append("\n");
                        builder.append(_data.getDetails());
                        mMessage = builder.toString();

                      checkForLogin(FacebookSocialNetwork.ID);

                    }
                }

            });
        }

        private void setTwitterPostedListener(final PostResponseModel _data) {
            this.ivTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isJustOncePressed) {
                        isJustOncePressed = false;
                        mWaitingForSuccessTwitterModel = _data;

                        mDialog = new ProgressDialog(mContext);
                        mDialog.setMessage(mContext.getString(R.string.posting_repost));
                        mDialog.setCancelable(false);


                        final StringBuilder builder = new StringBuilder();
                        builder.append(mContext.getString(R.string.post_title));
                        builder.append("\n");
                        builder.append(_data.getTitle());
                        builder.append("\n");
                        builder.append(_data.getDetails());
                        mMessage = builder.toString();
                        checkForLogin(TwitterSocialNetwork.ID);

                    }
                }
            });
        }
    }

    public void checkForLogin(int socialNetworkId){
        if(mSocialNetworks.checkForLogin(socialNetworkId)) {
            makeRepost(socialNetworkId);
        }else {
            mSocialNetworks.requestLogin(socialNetworkId,this);
        }
    }

    public void makeRepost(int socialNetworkId){
        mSocialNetworks.makeRepost(socialNetworkId, mMessage,this);
    }

    public void updateList(final List<PostResponseModel> _list) {
        mComponentsList = _list;
        notifyDataSetChanged();
    }

    private void setLike(PostResponseModel _model) {
        if (_model.isFavourite()) {
            _model.setFavourite(false);
            makeDeleteFavouriteRequest(_model.getId());
        } else {
            _model.setFavourite(true);
            makePostFavouriteRequest(_model.getId());
        }
        notifyDataSetChanged();
    }

    private void setFacebookPosted(PostResponseModel _model) {
        SocialNetworkVariables.getFacebookPostInfo().add(_model.getId());
        findModel(_model).isFacebookPressed = true;
        notifyDataSetChanged();
    }

    private PostResponseModel findModel(PostResponseModel _model ){
        for (PostResponseModel model : mComponentsList){
            if (_model.getId() == model.getId()){
                return model;
            }
        }
        return null;
    }

    private void setTwitterPosted(PostResponseModel _model) {
        SocialNetworkVariables.getTwitterPostInfo().add(_model.getId());
        findModel(_model).isTwitterPressed = true;
        notifyDataSetChanged();
    }

    private void implementPostFavouriteCallBack() {

        mPostFavouriteCallback = new Callback<SessionMobileResponseModel>() {
            @Override
            public void failure(RetrofitError error) {
                Log.e("RETROFIT", error.toString());
            }

            @Override
            public void success(SessionMobileResponseModel responseModel, Response response) {
                Log.d("RETROFIT", responseModel.toString());
            }
        };
    }

    private void implementDeleteFavouriteCallBack() {

        mDeleteFavouriteCallback = new Callback<SessionMobileResponseModel>() {
            @Override
            public void failure(RetrofitError error) {
                Log.e("RETROFIT", error.toString());
            }


            @Override
            public void success(SessionMobileResponseModel responseModel, Response response) {
                Log.d("RETROFIT", responseModel.toString());
            }
        };
    }

    private void makePostFavouriteRequest(final int _postId) {
        RetrofitAdapter.getInterface().postFavourite(_postId, mPostFavouriteCallback);
    }

    private void makeDeleteFavouriteRequest(final int _postId) {
        RetrofitAdapter.getInterface().deleteFavourite(_postId, mDeleteFavouriteCallback);
    }

   @Override
    public void onPostSuccessfully(int networkId) {
        mDialog.dismiss();
        isJustOncePressed = true;
        switch (networkId){
            case FacebookSocialNetwork.ID:
                setFacebookPosted(mWaitingForSuccessFacebookModel);
                break;
            case TwitterSocialNetwork.ID:
                setTwitterPosted(mWaitingForSuccessTwitterModel);
                break;
            default:
                Toast.makeText(mFragment.getActivity(),mFragment.getString(R.string.error_default),Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(mFragment.getActivity(), mFragment.getString(R.string.reposted_successfully), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int i, String s, String s2, Object o) {
        mDialog.dismiss();
        isJustOncePressed = true;
        if (s2.trim().endsWith("187")  ) {
            Toast.makeText(mFragment.getActivity(), mFragment.getString(R.string.already_reposted), Toast.LENGTH_SHORT).show();
            setTwitterPosted(mWaitingForSuccessTwitterModel);
        }else if(s2.trim().equals("Duplicate status message")){
            setFacebookPosted(mWaitingForSuccessFacebookModel);
            Toast.makeText(mFragment.getActivity(), mFragment.getString(R.string.already_reposted), Toast.LENGTH_SHORT).show();
        }
        else if (s2.trim().equals("java.io.EOFException")){
            Toast.makeText(mFragment.getActivity(), mFragment.getString(R.string.problem_with_connection), Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(mFragment.getActivity(), s2, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onLoginSuccess(int _socialNetworkId) {
        Toast.makeText(mContext,"login success" , Toast.LENGTH_SHORT).show();
        isJustOncePressed = true;
        makeRepost(_socialNetworkId);
    }
}
