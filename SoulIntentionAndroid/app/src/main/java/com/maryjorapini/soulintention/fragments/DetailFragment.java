package com.maryjorapini.soulintention.fragments;




import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.twitter.TwitterSocialNetwork;
import com.maryjorapini.soulintention.DateFormater;
import com.maryjorapini.soulintention.MainFragmentActivity;
import com.maryjorapini.soulintention.R;
import com.maryjorapini.soulintention.SocialNetworks;
import com.maryjorapini.soulintention.core.rest.RetrofitAdapter;
import com.maryjorapini.soulintention.data_models.response.Post.PostResponseModel;
import com.maryjorapini.soulintention.data_models.response.SessionMobileResponseModel;
import com.maryjorapini.soulintention.global.Constants;
import com.maryjorapini.soulintention.global.SocialNetworkVariables;

import java.util.Date;
import java.util.Random;

import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */

public class DetailFragment extends android.support.v4.app.Fragment implements View.OnClickListener ,OnPostingCompleteListener, RatingBar.OnRatingBarChangeListener , OnLoginCompleteListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private PostResponseModel mResponseModel;



    private ImageView ivDetailFacebook;
    private ImageView ivIvDetailTwitter;
    private ImageView abDetailLike;

    private RatingBar rbDetail;
    private TextView  tvDetailHeader;
    private TextView  tvDetailText;
    private TextView  tvDetailDate;
    private Random mRandom;


    private retrofit.Callback<SessionMobileResponseModel> mPostFavouriteCallback;
    private retrofit.Callback<SessionMobileResponseModel> mPostRateCallback;
    private retrofit.Callback<SessionMobileResponseModel> mDeleteFavouriteCallback;
    private  SocialNetworks mSocialNetworks;
    private ProgressDialog mDialog;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param responseModel Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(PostResponseModel responseModel , SocialNetworks mSocialNetworks) {
        DetailFragment fragment = new DetailFragment();

        Bundle args = new Bundle();
        args.putSerializable(Constants.PARAM_POST_RESPONSE, responseModel);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mResponseModel = (PostResponseModel)getArguments().getSerializable(Constants.PARAM_POST_RESPONSE);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        findUI(view);
        setListenersToViews();
        implementDeleteFavouriteCallBack();
        implementPostFavouriteCallBack();
        implementPostRateCallBack();
        mRandom = new Random();
        setRandomFont();
        setRandomBackground();
        setData(mResponseModel);
        mSocialNetworks = new SocialNetworks(this);
        ((MainFragmentActivity) getActivity()).showDetailFragmentAB();
        return view;
    }
    private void findUI(View _view){

        ivDetailFacebook        = (ImageView)_view.findViewById(R.id.ivDetailFacebook);
        ivIvDetailTwitter       = (ImageView)_view.findViewById(R.id.ivDetailTwitter);
        rbDetail                = (RatingBar)_view.findViewById(R.id.rbDetail);
        tvDetailHeader          = (TextView) _view.findViewById(R.id.tvDetailHeader);
        tvDetailText            = (TextView) _view.findViewById(R.id.tvDetailText);
        tvDetailDate            = (TextView)_view.findViewById(R.id.tvDetailDate);


        abDetailLike = (ImageView)getActivity().getActionBar().getCustomView().findViewById(R.id.ivLike_AB);
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage(getActivity().getString(R.string.posting_repost));
        mDialog.setCancelable(false);
        setActionBarLikeIcon(mResponseModel);
    }

    private void setListenersToViews(){
        ivDetailFacebook.setOnClickListener(this);
        ivIvDetailTwitter.setOnClickListener(this);
        abDetailLike.setOnClickListener(this);
        rbDetail.setOnRatingBarChangeListener(this);
    }

    private void setData( PostResponseModel _model){
        tvDetailHeader.setText(_model.getTitle());
        tvDetailText.setText(_model.getDetails());

        if (_model.getRate() != null){
            rbDetail.setRating(_model.getRate().getRate());
        }

        Date date = DateFormater.dateParser(_model.getUpdatedAt());
        tvDetailDate.setText(DateFormater.dateFormat(date));
        if(mResponseModel.isFacebookPressed){
            setFacebookPosted(mResponseModel);
        }
        if(mResponseModel.isTwitterPressed){
            setTwitterPosted(mResponseModel);
        }

    }

    private void setRandomFont(){
        String typeFacePath = null;

        switch(mRandom.nextInt(3)){
            case 1:typeFacePath = "fonts/architects_daughter.ttf";
                break;
            case 2:typeFacePath = "fonts/CoveredByYourGrace.ttf";
                break;
            case 3: typeFacePath = "fonts/indie_flower.ttf";
                break;
            default:typeFacePath = "fonts/shadows_into_light_two_regular.ttf";
        }
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), typeFacePath);
        tvDetailHeader.setTypeface(typeface);
    }

    private void setRandomBackground(){
        TypedArray typedArray = getResources().obtainTypedArray(R.array.background_random_color_array);
        int randomIndex = mRandom.nextInt(typedArray.length());
        int backgroundColorIndex = typedArray.getResourceId(randomIndex,0);
        tvDetailHeader.setBackgroundResource(backgroundColorIndex);
    }

    private String getPostMessage(){

        StringBuilder builder = new StringBuilder();
        builder.append(mResponseModel.getTitle());
        builder.append("\n");
        builder.append(mResponseModel.getDetails());

        return builder.toString();
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ivDetailFacebook:
                checkForLogin(FacebookSocialNetwork.ID);
                break;
            case R.id.ivDetailTwitter:
                checkForLogin(TwitterSocialNetwork.ID);
                break;

            case R.id.ivLike_AB:
                setLike(mResponseModel);
                setActionBarLikeIcon(mResponseModel);
                break;
        }
    }

    private void setActionBarLikeIcon(PostResponseModel _model){
        if (_model.isFavourite()){
            abDetailLike.setImageResource(R.drawable.ic_favorite_swype_press);
        } else {
            abDetailLike.setImageResource(R.drawable.ic_favorite_swype);
        }
    }

    private void setFacebookPosted(PostResponseModel _model){
        SocialNetworkVariables.getFacebookPostInfo().add(_model.getId());
        _model.isFacebookPressed = true;
        ivDetailFacebook.setImageResource(R.drawable.ic_facebook_share_select);
        ivDetailFacebook.setClickable(false);
    }

    private void setTwitterPosted(PostResponseModel _model){
        SocialNetworkVariables.getTwitterPostInfo().add(_model.getId());
        _model.isTwitterPressed = true;
        ivIvDetailTwitter.setImageResource(R.drawable.ic_twitter_share_select);
        ivIvDetailTwitter.setClickable(false);
    }

    private void setLike(PostResponseModel _model){
        if (_model.isFavourite()){
            _model.setFavourite(false);
            makeDeleteFavouriteRequest(_model.getId());
        } else {
            _model.setFavourite(true);
            makePostFavouriteRequest(_model.getId());
        }
    }

    private void implementPostFavouriteCallBack() {

        mPostFavouriteCallback = new retrofit.Callback<SessionMobileResponseModel>() {
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

        mDeleteFavouriteCallback = new retrofit.Callback<SessionMobileResponseModel>() {
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


    private void implementPostRateCallBack() {

        mPostRateCallback = new retrofit.Callback<SessionMobileResponseModel>() {
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


    public void checkForLogin(int socialNetworkId){
        if(mSocialNetworks.checkForLogin(socialNetworkId)) {
            makeRepost(socialNetworkId);
        }else {
            mSocialNetworks.requestLogin(socialNetworkId,this);
        }
    }

    public void makeRepost(int socialNetworkId){
        mSocialNetworks.makeRepost(socialNetworkId,getPostMessage(),this);
    }


    private void makePostFavouriteRequest(final int _postId) {
        RetrofitAdapter.getInterface().postFavourite(_postId, mPostFavouriteCallback);
    }
    private void makePostRateRequest(final int _postId, final int _rate) {
        RetrofitAdapter.getInterface().postRate(_postId, _rate, mPostRateCallback);
    }

    private void makeDeleteFavouriteRequest(final int _postId) {
        RetrofitAdapter.getInterface().deleteFavourite(_postId, mDeleteFavouriteCallback);
    }

    @Override
    public void onPostSuccessfully(int networkId) {
        mDialog.dismiss();
        Toast.makeText(getActivity(), getString(R.string.reposted_successfully), Toast.LENGTH_SHORT).show();
        switch (networkId){
            case FacebookSocialNetwork.ID:
                setFacebookPosted(mResponseModel);
                break;
            case TwitterSocialNetwork.ID:
                setTwitterPosted(mResponseModel);
                break;
            default:
                Toast.makeText(getActivity(),getString(R.string.error_default),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(int i, String s, String s2, Object o) {
        mDialog.dismiss();
        if (s2.trim().endsWith("187")) {
            Toast.makeText(getActivity(), getString(R.string.already_reposted), Toast.LENGTH_SHORT).show();
            setTwitterPosted(mResponseModel);
        }else if(s2.trim().equals("Duplicate status message")){
            setFacebookPosted(mResponseModel);
            Toast.makeText(getActivity(), getString(R.string.already_reposted), Toast.LENGTH_SHORT).show();
        }else if (s2.trim().equals("java.io.EOFException")){
            Toast.makeText(getActivity(), getString(R.string.problem_with_connection), Toast.LENGTH_SHORT).show();

        }


        else {

            Toast.makeText(getActivity(), s2, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoginSuccess(int _socialNetworkId) {
        makeRepost(_socialNetworkId);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
       if (b) makePostRateRequest(mResponseModel.getId(), (int) v);
    }
}
