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
public class WellcomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private final int FIRST_SCREEN = 0;
    private final int SECOND_SCREEN = 1;
    private final int THIRD_SCREEN = 2;
    private final int FOURTH_SCREEN = 3;

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
        b.viewPager.setAdapter(new WelcomeAdapter(getFragmentManager()));


    }

    public void onBack(View view) {
        b.viewPager.setCurrentItem(FOURTH_SCREEN, true);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == FIRST_SCREEN) {
            float alpha = (float) positionOffsetPixels / b.getRoot().getWidth();

            if (mFirst != null && mFirst.getView() != null)
                mFirst.getView().setAlpha(1 - alpha);
        }

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class WelcomeAdapter extends FragmentPagerAdapter {

        public WelcomeAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {


            switch (position) {
                case FIRST_SCREEN:
                    mFirst = new AboutFirst();
                    return mFirst;
                case SECOND_SCREEN:
                    return new AboutSecond();
                case THIRD_SCREEN:
                    return new AboutThird();
                case FOURTH_SCREEN:
                    return new AboutFourth();
                default:
                    throw new RuntimeException("Unknown screen");
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
