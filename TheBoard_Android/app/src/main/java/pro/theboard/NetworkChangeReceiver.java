package pro.theboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Oleh Makhobey on 20.11.2015.
 * tajcig@ya.ru
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

    }


    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());

    }
}


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//        ...
//
//        // Register to receive messages.
//        // We are registering an observer (mMessageReceiver) to receive Intents
//        // with actions named "custom-event-name".
//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
//                new IntentFilter("custom-event-name"));
//    }
//
//// Our handler for received Intents. This will be called whenever an Intent
//// with an action named "custom-event-name" is broadcasted.
//private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        // Get extra data included in the Intent
//        String message = intent.getStringExtra("message");
//        Log.d("receiver", "Got message: " + message);
//    }
//};
//
//    @Override
//    protected void onDestroy() {
//        // Unregister since the activity is about to be closed.
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
//        super.onDestroy();
//    }
//
//SenderActivity.java
//
//        The second activity that sends/broadcasts notifications.
//@Override
//public void onCreate(Bundle savedInstanceState) {
//
//        ...
//
//        // Every time a button is clicked, we want to broadcast a notification.
//        findViewById(R.id.button_send).setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        sendMessage();
//        }
//        });
//        }
//
//// Send an Intent with an action named "custom-event-name". The Intent sent should
//// be received by the ReceiverActivity.
//private void sendMessage() {
//        Log.d("sender", "Broadcasting message");
//        Intent intent = new Intent("custom-event-name");
//        // You can also include some extra data.
//        intent.putExtra("message", "This is my message!");
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent
