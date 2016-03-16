package com.budivnictvo.rssnews.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.budivnictvo.rssnews.app.R;
import com.budivnictvo.rssnews.app.core.AppConstants;

/**
 * Created by Администратор on 29.12.2014.
 */
public class WebFragment extends Fragment{

    private WebView webView;
    private String mLink;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_webview,container,false);
        findViews(rootView);
        mLink = getArguments().getString(AppConstants.RSS_LINK);
        setWebView();
        return rootView;
    }
    private void findViews(View _rootView){
        webView = (WebView)_rootView.findViewById(R.id.wv_main);
    }
    private void setWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new RssWebViewClient());
        webView.loadUrl(mLink);
    }
    private class RssWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }

}
