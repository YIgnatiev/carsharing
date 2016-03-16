package ru.lead2phone.ru.lead2phone.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.lead2phone.ru.lead2phone.R;
import ru.lead2phone.ru.lead2phone.rest.LeadToPhoneApi;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 8/26/15.
 */
public class AboutFragment extends BaseFragment implements Callback<String>{


    @Bind(R.id.tvText_FragmentAbout)TextView tvAbout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about,null);
        ButterKnife.bind(this, view);
        getAboutText();
        return view;
    }


    private void setData(String str){
        tvAbout.setText(str);
    }

    @OnClick(R.id.tvBack_FragmentAbout)
        public void onBackPressed(){
            mActivity.onBackPressed();
        }


    private void getAboutText(){
        mActivity.showProgres();
        LeadToPhoneApi.getInstance(mActivity).about(this);
    }


    @Override
    public void success(String s, Response response) {
        setData(s);
        mActivity.hideProgress();

    }

    @Override
    public void failure(RetrofitError error) {
        tvAbout.setText(Html.fromHtml(getString(R.string.about_text)));
        mActivity.hideProgress();
    }
}
