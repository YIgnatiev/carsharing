package com.vakoms.meshly.databases;

import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.vakoms.meshly.utils.P;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/16/15.
 */
public class AuthenticatorService extends Service {
    public static final String ACCOUNT_NAME = P.getUsername();

        // Instance field that stores the authenticator object
        private Authenticator mAuthenticator;


    public static Account getAccount(String accountType) {
        // Note: Normally the account name is set to the user's identity (username or email
        // address). However, since we aren't actually using any user accounts, it makes more sense
        // to use a generic string in this case.
        //
        // This string should *not* be localized. If the user switches locale, we would not be
        // able to locate the old account, and may erroneously register multiple accounts.
        final String accountName = ACCOUNT_NAME;
        return new Account(accountName, accountType);
    }
        @Override
        public void onCreate() {
            // Create a new authenticator object
            mAuthenticator = new Authenticator(this);
        }
        /*
         * When the system binds to this Service to make the RPC call
         * return the authenticator's IBinder.
         */
        @Override
        public IBinder onBind(Intent intent) {
            return mAuthenticator.getIBinder();
        }
    }

