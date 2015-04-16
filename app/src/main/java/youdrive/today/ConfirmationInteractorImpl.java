package youdrive.today;

import android.util.Log;

/**
 * Created by psuhoterin on 16.04.15.
 */
public class ConfirmationInteractorImpl implements ConfirmationInteractor {

    @Override
    public void invite(String request, ConfirmationActionListener listener) {
        Log.d("MyLog", "Request " + request);
        listener.onSuccess();
    }
}
