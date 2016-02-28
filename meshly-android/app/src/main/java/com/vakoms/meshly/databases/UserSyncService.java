package com.vakoms.meshly.databases;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/14/15.
 */
public class UserSyncService extends Service {
        private static final Object sSyncAdapterLock = new Object();
        private static UserSyncAdapter sSyncAdapter = null;
        @Override
        public void onCreate() {
            Log.i("syncadapter", "Service created");
            synchronized (sSyncAdapterLock) {
                if (sSyncAdapter == null) {
                    sSyncAdapter = new UserSyncAdapter(getApplicationContext(), true);

                } }
        }

        @Override

        public IBinder onBind(Intent intent) {
            return sSyncAdapter.getSyncAdapterBinder();
        }


}
