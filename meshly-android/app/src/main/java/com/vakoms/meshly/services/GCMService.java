package com.vakoms.meshly.services;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.IntentService;
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
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.receivers.GCMReceiver;
import com.vakoms.meshly.utils.BitmapUtil;
import com.vakoms.meshly.constants.Constants;
import com.vakoms.meshly.utils.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import meshly.vakoms.com.meshly.R;


/**
 * Created by taras.melko on 9/15/14.
 * Updated by Sviat Kashchin on 26.01.2014
 */
public class GCMService extends IntentService {
    public static final String TAG = "GCMService";

    static final public String GCM_RESULT = "MainActivity";
    public static final int NOTIFICATION_ID = 0;

    private Bundle pushData;
    private Handler handler;
    private Bitmap pushImage;
    private LocalBroadcastManager broadcaster;

    public GCMService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        pushData = intent.getExtras();

        dumpIntent(intent);

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!pushData.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

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
        }

        GCMReceiver.completeWakefulIntent(intent);
    }

    public static void dumpIntent(Intent i) {
        Bundle bundle = i.getExtras();

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
}
