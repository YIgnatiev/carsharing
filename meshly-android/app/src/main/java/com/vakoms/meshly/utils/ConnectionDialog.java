package com.vakoms.meshly.utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import meshly.vakoms.com.meshly.R;


/**
 * Created by taras.melko on 8/26/14.
 */
public class ConnectionDialog extends DialogFragment {

    private View mView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.connection_dialog, null);

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        mView.findViewById(R.id.connection_dialog_settings_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                getActivity().startActivity(myIntent);
                dismiss();
            }
        });

        mView.findViewById(R.id.connection_dialog_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return mView;
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

}
