package com.morfitrun.global;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Виталий on 19/03/2015.
 */
public class ProgressDialogManager {

    private ProgressDialog mProgressDialog;

    public ProgressDialogManager(final Context _context) {
        mProgressDialog = new ProgressDialog(_context);
    }

    public final void showProgressDialog(final String _message) {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(_message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    public final void hideProgressDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
