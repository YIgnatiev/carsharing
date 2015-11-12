package youdrive.today.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.databinding.ActivityWellcomeBinding;
import youdrive.today.fragments.AboutFirst;
import youdrive.today.fragments.AboutFourth;
import youdrive.today.fragments.AboutSecond;
import youdrive.today.fragments.AboutThird;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/29/15.
 */
public class WellcomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    ActivityWellcomeBinding b;
    private AboutFirst mFirst;
    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_wellcome);
        b.setListener(this);
        setPager();

        b.cpIndicator.setViewPager(b.viewPager);
        b.cpIndicator.setOnPageChangeListener(this);

    }

    private void setPager() {
        b.viewPager.setAdapter(new WellcomeAdapter(getFragmentManager()));


    }

    public void onBack(View view) {
        onBackPressed();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(position == 0) {
            float alpha =  (float) positionOffsetPixels / b.getRoot().getWidth();

            if(mFirst != null && mFirst.getView() != null)
            mFirst.getView().setAlpha(1-alpha);
        }

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class WellcomeAdapter extends FragmentPagerAdapter {

        public WellcomeAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {


            switch (position) {
                case 0:
                    mFirst = new AboutFirst();
                    return  mFirst;
                case 1:
                    return new AboutSecond();
                case 2:
                    return new AboutThird();
                default:
                    return new AboutFourth();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public void showMessage(String message) {
        Snackbar.make(b.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    public void showErrorMessage(String message) {
        Snackbar.make(b.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }


}
