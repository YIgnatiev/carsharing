package ru.lead2phone.ru.lead2phone.utils;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/15/15.
 */
public class CustomPhoneStateListener extends PhoneStateListener {



    public CustomPhoneStateListener() {
        super();

    }
    public static boolean isCalling = false;
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state) {

            case TelephonyManager.CALL_STATE_IDLE:
                isCalling= false;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                isCalling = true;
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                isCalling = true;
                break;
            default:
                break;
        }
    }
}
