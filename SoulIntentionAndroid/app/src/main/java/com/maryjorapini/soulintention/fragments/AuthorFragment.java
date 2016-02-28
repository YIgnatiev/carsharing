package com.maryjorapini.soulintention.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maryjorapini.soulintention.R;
import com.maryjorapini.soulintention.core.rest.RetrofitAdapter;
import com.maryjorapini.soulintention.core.rest.RetrofitInterface;
import com.maryjorapini.soulintention.data_models.response.Post.AboutAuthorModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorFragment extends android.support.v4.app.Fragment {
    private FragmentActivity mActivity;
    private Callback<List<AboutAuthorModel>> callback;
   private TextView tvAbout;
    private ImageView ivPhoto;
    private ProgressBar pbAuthor;



    public AuthorFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment ------------fragment author
        View view = inflater.inflate(R.layout.fragment_author, container, false);
        implementCallBack();
        makeAboutRequest();
        findUI(view);
        return view;
    }

    private void findUI(View _view) {
       tvAbout         = (TextView) _view.findViewById(R.id.tvAbout_FA);
        ivPhoto         = (ImageView) _view.findViewById(R.id.ivAuthor_FA);
        pbAuthor        = (ProgressBar)_view.findViewById(R.id.pbAuthor);

    }

    private void fillData(final AboutAuthorModel _model){
       tvAbout.setText(Html.fromHtml(_model.getAboutInfo()));
        Picasso.with(mActivity).load( _model.getImageUrl()).into(ivPhoto, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                pbAuthor.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });

    }
    private void implementCallBack() {

        callback = new Callback<List<AboutAuthorModel>>() {
            @Override
            public void failure(RetrofitError error) {

                Log.e("RETROFIT", error.toString());

            }


            @Override
            public void success(List<AboutAuthorModel> responseModel, Response response) {
//                Log.d("RETROFIT", responseModel.toString());
                fillData(responseModel.get(0));

            }
        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
        implementCallBack();
    }

    private void makeAboutRequest() {
        RetrofitInterface retrofitInterface = RetrofitAdapter.getInterface();
        retrofitInterface.getAbout(callback);
    }
}
