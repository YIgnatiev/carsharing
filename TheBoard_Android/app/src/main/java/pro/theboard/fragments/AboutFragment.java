package pro.theboard.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lapioworks.cards.R;

import pro.theboard.BaseActivity;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 7/12/15.
 */
public class AboutFragment extends Fragment implements View.OnClickListener {

    private BaseActivity mActivity;
    private TextView tvAbout;
    private TextView tvLinkFull;
    private TextView tvLinkShort;
    private View mRootView;

    @Override
    public void onAttach(Activity activity) {
        mActivity = (BaseActivity) activity;
        super.onAttach(activity);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_about, container, false);
        findViews();
        setListeners();
        setData();
        return mRootView;
    }

    private void setListeners() {

    }

    private void findViews() {
        tvAbout = findView(R.id.tvAbout_FragmentAbout);
        tvLinkFull = findView(R.id.tvLink_FragmentAbout);
        tvLinkShort = findView(R.id.tvLinkShort_FragmentAbout);
    }

    private <T extends View> T findView(int ressourceId) {
        return (T) mRootView.findViewById(ressourceId);
    }

    private void setData() {
        tvAbout.setText(Html.fromHtml(getString(R.string.about_message)));
        mActivity.setToolbarTitle(getString(R.string.about_action_bar));
    }

    @Override
    public void onClick(View v) {

    }
}
