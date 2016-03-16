package com.vakoms.meshly.fragments.wellcome;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vakoms.meshly.WelcomeActivity;
import com.vakoms.meshly.databases.MeshlyColumns;
import com.vakoms.meshly.databases.UserDAO;
import com.vakoms.meshly.databases.UserProvider;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.models.UserMe;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentWelcomeBinding;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/5/15.
 */
public class WelcomeFragment extends BaseFragment<WelcomeActivity>  implements LoaderManager.LoaderCallbacks<Cursor> {
    private  FragmentWelcomeBinding b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b= DataBindingUtil.inflate(inflater, R.layout.fragment_welcome,null,false);
        getLoaderManager().restartLoader(1, null, this);

        return b.getRoot();
    }

    private void setData(){

        String text = getString(R.string.welcome_user,mActivity.mUser.username);
        b.tvHeyUser.setText(Html.fromHtml(text));
    }


    @Override
    protected void handleError(Throwable throwable) {

    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] columns = MeshlyColumns.getColumns(UserMe.class);
        return new CursorLoader(mActivity, UserProvider.USER_ME_URI, columns, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        mActivity.mUser = UserDAO.getInstance().getUserMe(data);
        mActivity.initPager();
        setData();


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
