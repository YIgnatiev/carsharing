package youdrive.today.activities;

import android.databinding.DataBindingUtil;

import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.databinding.ActivityRegistrationNewBinding;
import youdrive.today.fragments.RegisterOffertFragment;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/30/15.
 */


public class RegistrationNewActivity extends BaseActivity{

    private ActivityRegistrationNewBinding b;
    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_registration_new);
        startFragment();
    }

    private void startFragment(){
        getFragmentManager()
                .beginTransaction().add(R.id.flContainer,new RegisterOffertFragment())
                .commitAllowingStateLoss();
    }


    public void startFragmentLeft(){
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer,new RegisterOffertFragment())
                    .setCustomAnimations(R.animator.move_right_in, R.animator.move_right_out,R.animator.move_left_in, R.animator.move_left_out)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
    }

}
