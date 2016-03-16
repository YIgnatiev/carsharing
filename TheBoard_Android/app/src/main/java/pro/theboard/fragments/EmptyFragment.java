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
 * on 7/17/15.
 */
public class EmptyFragment extends Fragment {



    private BaseActivity mActivity;
    private TextView tvText;
    private View mRootView;

    @Override
    public void onAttach(Activity activity) {
        mActivity = (BaseActivity) activity;
        super.onAttach(activity);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_empty, container, false);
        findViews();
        setListeners();
        setData();
        return mRootView;
    }

    private void setListeners() {

    }

    private void findViews() {
       tvText= findView(R.id.tvText_FragmentEmpty);
    }

    private <T extends View> T findView(int ressourceId) {
        return (T) mRootView.findViewById(ressourceId);
    }

    private void setData() {
        tvText.setText(Html.fromHtml(getString(R.string.empty_message)));
        mActivity.setToolbarTitle(getString(R.string.empty_action_bar));
    }

}
