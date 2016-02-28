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

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.utils.BitmapUtil;
import com.vakoms.meshly.constants.Constants;
import com.vakoms.meshly.utils.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import meshly.vakoms.com.meshly.R;


public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    static final public String GCM_RESULT = "MainActivity";
    public static final int NOTIFICATION_ID = 0;

    private Bundle pushData;
    private Handler handler;
    private Bitmap pushImage;
    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        pushData = data;

        dumpIntent(data);

//        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
//        String messageType = gcm.getMessageType(intent);

        if (!pushData.isEmpty()) {
//            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                //received subscribe eve
                if (pushData.getString("action") != null){
                    if (pushData.getString("action").equals("subscribedToEvent")) {
                        showBestNotification();
                    } else { //if app in foreground - don't show notification, just show icon near menu
                        if (isAppOnForeground(getApplicationContext())) {
                            sendResult(pushData.getString("message"),
                                    pushData.getString("chatId"),
                                    pushData.getString("timestamp"),
                                    pushData.getString("username"),
                                    pushData.getString("fromId"),
                                    pushData.getString("messageId"));
                        } else {
                            showBestNotification();
                        }
                    }
                }
            }
//        }

//        GCMReceiver.completeWakefulIntent(intent);
//        sendNotification(message);
    }
    // [END receive_message]


    public static void dumpIntent( Bundle bundle) {
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            Logger.i("DATA", "--Dumping Intent start--");

            while (it.hasNext()) {
                String key = it.next();
                Logger.i("DATA", "[" + key + "=" + bundle.get(key) + "]");
            }

            Logger.i("DATA", "--Dumping Intent end--");
        }
    }

    public void showBestNotification() {
        new AvatarDownloaderForNotification().execute();
    }

    public void sendResult(String message, String chat_id, String timestamp, String name, String user_id, String id) {
        Intent intent = new Intent(GCM_RESULT);
        intent.putExtra(Constants.GCM_MESSAGE, message);
        intent.putExtra(Constants.GCM_MESSAGE_ID, id);
        intent.putExtra(Constants.GCM_CHAT_ID, chat_id);
        intent.putExtra(Constants.GCM_TIMESTAMP, timestamp);
        intent.putExtra(Constants.GCM_NAME, name);
        intent.putExtra(Constants.GCM_USER_ID, user_id);
        broadcaster.sendBroadcast(intent);
    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    private class AvatarDownloaderForNotification extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL url;
            try {
                url = new URL(pushData.getString("userIcon"));
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return image;
            } catch (IOException e) {
                Logger.e(TAG, "Unable to download image for push" + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            pushImage = BitmapUtil.getCroppedBitmap(bitmap);

            handler.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    if (pushData.getString("message") != null) {
                        intent.putExtra("action", "chat");
                        intent.putExtra("user_id", pushData.getString("fromId"));
                        intent.putExtra("chat_id", pushData.getString("chatId"));

                        showMessageNotification(intent);
                    } else if (pushData.getString("action").equals("subscribedToEvent")) {
                        intent.putExtra("action", "event");
                        intent.putExtra("eventId", pushData.getString("eventId"));

                        showEventNotification(intent);
                    }
                }
            });
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    //  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showEventNotification(Intent intent) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification n = new Notification.Builder(getApplicationContext())
                .setContentTitle(pushData.getString("username"))
                .setContentText(String.format("Subscribed to \"%s\"", pushData.getString("eventName")))
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(pushImage)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setOnlyAlertOnce(false)
                .build();

        notificationManager.notify(NOTIFICATION_ID, n);
    }

    // @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showMessageNotification(Intent intent) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification n = new Notification.Builder(getApplicationContext())
                .setContentTitle(pushData.getString("username"))
                .setContentText(pushData.getString("message"))
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(pushImage)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setOnlyAlertOnce(false)
                .build();

        notificationManager.notify(NOTIFICATION_ID, n);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_gallery)
                .setContentTitle("Hello world")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* of notification */, notificationBuilder.build());
    }
}
