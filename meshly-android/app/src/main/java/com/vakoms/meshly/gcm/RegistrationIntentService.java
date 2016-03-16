/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vakoms.meshly.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.constants.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Subscription;


public class RegistrationIntentService extends IntentService   {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    private static final String   SENT_TOKEN_TO_SERVER  = "send_token";
    public static final String   GCM_TOKEN = "gcm.token";
    private static final String   REGISTRATION_COMPLETE  = "registration";
    private Subscription mSubscription;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {

            synchronized (TAG) {

                String token = InstanceID
                        .getInstance(this)
                        .getToken(Constants.PROJECT_GCM_NUMBER,
                                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                sharedPreferences.edit().putString(GCM_TOKEN, token).apply();

                Log.i(TAG, "GCM Registration Token: " + token);

                sendRegistrationToServer(token);

                // Subscribe to topic channels
                subscribeTopics(token);

                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.
                sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
                // [END register_for_gcm]
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }


        LocalBroadcastManager
                .getInstance(this)
                .sendBroadcast(new Intent(REGISTRATION_COMPLETE));
    }



    private void sendRegistrationToServer(String token) {
        NewUser user = new NewUser();
        user.setAndroidToken(token);
        mSubscription = RetrofitApi
                .getInstance()
                .user()
                .updateAndroidGcmToken(user)
                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribe(this::onSuccess,this::onFailure);
    }


    public void onSuccess(BaseResponse response){
        Log.v("gcm", "token updated");
    }
    public void onFailure(Throwable t){
        Log.v("gcm", "token failed " + t.getMessage());
    }

    @Override
    public void onDestroy() {
        mSubscription.unsubscribe();
        super.onDestroy();

    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }



}
