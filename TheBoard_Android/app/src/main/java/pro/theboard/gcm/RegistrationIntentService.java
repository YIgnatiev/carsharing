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

package pro.theboard.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.lapioworks.cards.R;

import java.io.IOException;

import pro.theboard.models.retrofit.StatusResponse;
import pro.theboard.rest.RetrofitApi;
import pro.theboard.utils.Preferences;

import static pro.theboard.constants.Constants.REGISTRATION_COMPLETE;
import static pro.theboard.constants.Constants.SENT_TOKEN_TO_SERVER;


public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

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
                        .getToken(getString(R.string.gcm_server_api_key),
                                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);



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
        if(Preferences.getNotificationPreference()) {
            RetrofitApi.getInstance(getApplicationContext()).sendGcmToken(token).subscribe(this::onRegisterDeviceSuccess,this::onFailure);
        }else {
            Log.v("Location", "notifications aro not permitted");
        }

    }



    private void onRegisterDeviceSuccess(StatusResponse statusResponse) {
        Log.v("GCM", "Gcm registered successfully");

    }

    private void onFailure(Throwable error) {
        Log.v("GCM", "Gcm is not registered");
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
