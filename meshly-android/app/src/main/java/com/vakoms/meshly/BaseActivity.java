package com.vakoms.meshly;


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.vakoms.meshly.databases.UserProvider;

import meshly.vakoms.com.meshly.R;
import rx.functions.Function;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by taras.melko on 9/4/14.
 */
public abstract class BaseActivity extends AppCompatActivity {


    public FragmentManager mFragmentManager;
    private static final long SYNC_FREQUENCY = 60 ;
    public CompositeSubscription subscriptions = new CompositeSubscription();

    public static final String AUTHORITY = UserProvider.AUTH;
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.vakoms.meshly.databases.UserProvider.account";
    // The account name
    // Instance fields
    public static Account mAccount ;
    private static final String PREF_SETUP_COMPLETE = "setup_complete";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getFragmentManager();

    }

    public void replaceFragment(Fragment fragment) {

//        for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
//            getSupportFragmentManager().popBackStack();
//        }
        while (mFragmentManager.getBackStackEntryCount() != 0)
            mFragmentManager.popBackStackImmediate();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.flContainer, fragment,fragment.getClass().getName())
                .addToBackStack(null)
                .commit();

//        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

    }


    public void replaceFragmentWithBackStack(Fragment fragment){
        mFragmentManager
                .beginTransaction()
                .replace(R.id.flContainer, fragment)
                .addToBackStack(null)
                .commit();

    }

    public void replaceFragmentDown(Fragment fragment) {

        mFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.animator.enter_down, R.animator.exit_down, R.animator.pop_enter_down, R.animator.pop_exit_down)
                .replace(R.id.flContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
    public int getBackStackCount(){
        return mFragmentManager.getBackStackEntryCount();
    }



    public void replaceFragmentLeft(Fragment fragment){
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.move_right_in, R.animator.move_right_out, R.animator.move_left_in, R.animator.move_left_out)
                .replace(R.id.flContainer, fragment)
                .addToBackStack(null)
                .commit();
    }






    public static void сreateSyncAccount(Context context ,String userName) {



        mAccount= new Account(userName,ACCOUNT_TYPE);

        boolean newAccount = false;
        boolean setupComplete = PreferenceManager
                .getDefaultSharedPreferences(context).getBoolean(PREF_SETUP_COMPLETE, false);

        // Create account, if it's missing. (Either first run, or user has deleted account.)


        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(mAccount, null, null)) {
            // Inform the system that this account supports sync
            ContentResolver.setIsSyncable(mAccount, UserProvider.AUTH, 1);
            // Inform the system that this account is eligible for auto sync when the network is up
            ContentResolver.setSyncAutomatically(mAccount, UserProvider.AUTH, true);
            // Recommend a schedule for automatic synchronization. The system may modify this based
            // on other scheduled syncs and network utilization.



//            ContentResolver.addPeriodicSync(
//                    mAccount, UserProvider.AUTH, new Bundle(),SYNC_FREQUENCY);


            newAccount = true;
        }

        // Schedule an initial sync if we detect problems with either our account or our local
        // data has been deleted. (Note that it's possible to clear app data WITHOUT affecting
        // the account list, so wee need to check both.)
        if (newAccount || !setupComplete) {
            TriggerRefresh(new Bundle());
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putBoolean(PREF_SETUP_COMPLETE, true).commit();
        }


    }



    public  void logOutAdapter(Context context){
        ContentResolver.removePeriodicSync(mAccount,UserProvider.AUTH,new Bundle());
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        //accountManager.clearPassword(mAccount);
    }

    @Override
    protected void onStop() {
        subscriptions.unsubscribe();
        super.onStop();
    }

    public static void TriggerRefresh(Bundle bundle) {


        ContentResolver.setIsSyncable(mAccount, UserProvider.AUTH, 1);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, UserProvider.AUTH, bundle);
    }


    public abstract void showProgress();

    public abstract void stopProgress();



    public abstract void handleError(Throwable throwable);



}
