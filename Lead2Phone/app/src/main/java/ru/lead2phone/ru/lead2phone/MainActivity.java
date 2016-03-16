package ru.lead2phone.ru.lead2phone;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import ru.lead2phone.ru.lead2phone.fragments.ListFragment;
import ru.lead2phone.ru.lead2phone.fragments.LoginFragment;
import ru.lead2phone.ru.lead2phone.utils.Preferences;
import ru.lead2phone.ru.lead2phone.utils.UpdateService;


public class MainActivity extends Activity {

    private Intent mServiceIntent;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Загрузка...");
        mProgressDialog.setCancelable(false);
        mServiceIntent = new Intent(this, UpdateService.class);
        startLoginFragment();
    }



    public void startUpdateService(){
        startService(mServiceIntent);
    }

    public void stopUpdateService(){
        stopService(mServiceIntent);
        UpdateService.isDestroyed = true;
    }

    private void startLoginFragment(){
        if(checkLogin()){
            replaceFragmentWithoutBackStack(new ListFragment());
        }else replaceFragmentWithoutBackStack(new LoginFragment());
    }
    private boolean checkLogin(){
        return Preferences.isLogged();
    }


    public void showProgres(){
        mProgressDialog.show();
    }

    public void hideProgress(){
        mProgressDialog.dismiss();
    }

    public void replaceFragment(Fragment fragment){
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in,android.R.animator.fade_out)
                .replace(R.id.flContainer,fragment)
                .addToBackStack(null)
                .commit();
    }

    public void replaceFragmentWithoutBackStack(Fragment fragment){
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.flContainer, fragment)
                .commit();
    }


    public void call(String _number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + _number));
        startActivity(callIntent);

    }
}
