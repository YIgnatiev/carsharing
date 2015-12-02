package youdrive.today.activities;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.widget.Toast;

import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.databinding.ActivityRegistrationNewBinding;
import youdrive.today.fragments.RegisterOffertFragment;
import youdrive.today.models.RegistrationUser;
import youdrive.today.network.ApiClient;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/30/15.
 */


public class RegistrationNewActivity extends BaseActivity{

    private ActivityRegistrationNewBinding b;

    public RegistrationUser mUser;
    public String userId;
    public String price;
    private ProgressDialog pdLoading;
    public ApiClient mClient;


    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_registration_new);
        mClient = new  ApiClient();
        pdLoading = new ProgressDialog(this);
        pdLoading.setMessage("Загрузка...");
        pdLoading.setCancelable(false);
        startFragment();
    }

    private void startFragment(){
        getFragmentManager()
                .beginTransaction().add(R.id.flContainer, new RegisterOffertFragment())
                .commitAllowingStateLoss();
    }


    public void startFragmentLeft(Fragment fragment){
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.move_right_in, R.animator.move_right_out, R.animator.move_left_in, R.animator.move_left_out)
                    .replace(R.id.flContainer, fragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
    }



    public void onCreateFailure(Throwable throwable){
       hideProgress();
        Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
    }





    public void showProgress(){
        pdLoading.show();
    }

    public void hideProgress(){
        pdLoading.hide();
    }





}
