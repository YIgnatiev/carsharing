package com.budivnictvo.rssnews.app.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewFragment;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budivnictvo.rssnews.app.MainActivity;
import com.budivnictvo.rssnews.app.OnRSSLoadCallback;
import com.budivnictvo.rssnews.app.R;
import com.budivnictvo.rssnews.app.core.AppConstants;
import com.budivnictvo.rssnews.app.data.RssItem;
import com.budivnictvo.rssnews.app.html.HtmlHelper;
import com.budivnictvo.rssnews.app.utils.ResponseCallback;
import com.budivnictvo.rssnews.app.utils.RssApi;
import com.budivnictvo.rssnews.app.utils.RssParser;
import com.github.kevinsawicki.http.HttpRequest;
import com.squareup.picasso.Picasso;

import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Администратор on 17.12.2014.
 */


public class DetailRssFragment extends Fragment implements View.OnClickListener {
    private TextView tvDetailReadInBrowser;
    private TextView tvDetailText;
    private TextView tvDetailHeader;
    private TextView tvDetailDate;
    private ImageView ivDetailImage;
    private ImageView ivDetailVk;
    private ImageView ivDetailFacebook;
    private ImageView ivDetailTwitter;
    private RssItem mCurrentItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mCurrentItem = (RssItem) getArguments().getSerializable(AppConstants.RSS_ITEM);
        findUI(rootView);
        setListeners();
        setData(mCurrentItem);
        return rootView;
    }

    private void setData(RssItem _item) {
        tvDetailHeader.setText(_item.getTitle());
        tvDetailText.setText(_item.getDescription());
        tvDetailDate.setText(_item.getPubDate());
        Picasso.with(getActivity()).load(_item.getImageUrl()).into(ivDetailImage);
    }


    private void findUI(View _rootView) {
        tvDetailHeader = (TextView) _rootView.findViewById(R.id.tvDetailHeader);
        tvDetailText = (TextView) _rootView.findViewById(R.id.tvDetailText);
        tvDetailDate = (TextView) _rootView.findViewById(R.id.tvDetailDate);
        tvDetailReadInBrowser = (TextView) _rootView.findViewById(R.id.tvDetailReadInBrowser);
        ivDetailImage = (ImageView) _rootView.findViewById(R.id.ivDetailImage);
        ivDetailFacebook = (ImageView) _rootView.findViewById(R.id.ivDetailFacebook);
        ivDetailTwitter = (ImageView) _rootView.findViewById(R.id.ivDetailTwitter);
        ivDetailVk = (ImageView) _rootView.findViewById(R.id.ivDetailVk);


    }

    private void setListeners() {
        tvDetailReadInBrowser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        final AsyncRSSLoader loader = new AsyncRSSLoader(this);

        RssApi.getHtml(mCurrentItem.getLink(), new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                  StringReader reader = new StringReader(response);
                  loader.execute(reader);
            }

            @Override
            public void onFailure(Object object) {
                Toast.makeText(getActivity(), "Connection error" , Toast.LENGTH_SHORT).show();
            }
        });


//        Bundle bundle = new Bundle();
//        bundle.putString(AppConstants.RSS_LINK, mCurrentItem.getLink());
//
//
//
//
//        WebFragment fragment = new WebFragment();
//        fragment.setArguments(bundle);
//        MainActivity mainActivity = (MainActivity)getActivity();
//        mainActivity.replaceFragmentWithBackstack(fragment);
    }

    private class AsyncRSSLoader extends AsyncTask<StringReader, Void, List<TagNode>> {
        private ProgressDialog dialog;
        private Fragment mFragment;

        public AsyncRSSLoader(Fragment fragment) {
            this.mFragment = fragment;
        }


        @Override
        protected void onPreExecute() {
            this.dialog = new ProgressDialog(mFragment.getActivity());
            this.dialog.setMessage(mFragment.getResources().getString(R.string.loading));
            if (!this.dialog.isShowing()) {
                this.dialog.show();
            }
        }


        @Override
        protected List<TagNode> doInBackground(StringReader... data) {

            List list = null;
            try {

                HtmlHelper helper = new HtmlHelper(data[0]);
                list = helper.getLinksByTag("p");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;

        }

        @Override
        protected void onPostExecute(List<TagNode> _data) {
            StringBuilder builder = new StringBuilder();
            for (TagNode node : _data) {
                builder.append(node.getText());
            }
            String text = builder.toString();
            tvDetailText.setText(text);
            this.dialog.dismiss();
        }

    }


}
