package youdrive.today.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.databinding.DataBindingUtil;
import android.support.v13.app.FragmentPagerAdapter;
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
public class WellcomeActivity extends BaseActivity {

    ActivityWellcomeBinding b;
    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_wellcome);
        b.setListener(this);
        setPager();

        b.cpIndicator.setViewPager(b.viewPager);
    }


    private void setPager(){
        b.viewPager.setAdapter(new WellcomeAdapter(getFragmentManager()));


    }


    public void onBack(View view){
       onBackPressed();
    }


    private class WellcomeAdapter extends FragmentPagerAdapter{

        public WellcomeAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {


            switch (position) {
                case 0: return new AboutFirst();
                case 1: return new AboutSecond();
                case 2: return new AboutThird();
                default: return new AboutFourth();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }





}
