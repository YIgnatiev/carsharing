package youdrive.today.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
/**
 * Created by Sergio on 06/09/16.
 */
public class InstanceIDServices extends FirebaseInstanceIdService{
    private static final String TAG = "IIDService";
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
//     [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        getApplicationContext();
        SharedPreferences preferences = getSharedPreferences("temp", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pushToken", token);
        editor.putBoolean("isNeedSendTokenToServer", true);
        editor.apply();
//        Log.d(TAG, "Refreshed token: " + token);
        // TODO: Implement this method to send token to your app server.

    }
}
