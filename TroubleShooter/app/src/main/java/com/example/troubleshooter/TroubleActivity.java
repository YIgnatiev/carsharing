package com.example.troubleshooter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.troubleshooter.database.SQLCommands;
import com.example.troubleshooter.database.URLDatabaseHelper;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TroubleActivity extends Activity {

    private FragmentBroadcastReciever fragmentBroadcastReciever;
    private URLDatabaseHelper dbHelper;
    private AsyncLoadJSON loader;
    private String url;
    private int positive;
    private int negative;
    private String positiveText;
    private String negativeText;
    private String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trouble_activity_layout);
    }

    private void init() {
        dbHelper = new URLDatabaseHelper(this);
        fragmentBroadcastReciever = new FragmentBroadcastReciever();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppContext.BROADCAST_ACTION);
        filter.addAction(AppContext.JSON_ACTION);
        registerReceiver(fragmentBroadcastReciever, filter);

        int orient = this.getResources().getConfiguration().orientation;
        if (orient == 1) {
            //Portret
            final Fragment fragment = new TroubleListFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.portrait_fragment_frame, fragment);
            ft.commitAllowingStateLoss();
        } else {
            //LANDSCAPE;

            final Fragment fragment = new TroubleListFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            ft.replace(R.id.list_fragment_frame, fragment);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

           init();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(fragmentBroadcastReciever);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (AppContext.backButtonCantCloseApp) {
            init();
            AppContext.backButtonCantCloseApp = false;
        }
        else
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:{
                getActionBar().setDisplayHomeAsUpEnabled(false);
                AppContext.backButtonCantCloseApp = false;
                init();
                return true;
            }
            default:
                return false;
        }
    }

    private void startNewFragment() {

        Bundle bundle = new Bundle();
        bundle.putInt(AppContext.BUNDLE_YES, positive);
        bundle.putInt(AppContext.BUNDLE_NO, negative);
        bundle.putString(AppContext.BUNDLE_YES_TEXT, positiveText);
        bundle.putString(AppContext.BUNDLE_NO_TEXT, negativeText);
        bundle.putString(AppContext.BUNDLE_URL, url);
        bundle.putString(AppContext.BUNDLE_TEXT, text);

        Fragment fragment = new TroubleQuestionsFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        int orient = this.getResources().getConfiguration().orientation;
        if (orient == 1) {
            ft.replace(R.id.portrait_fragment_frame, fragment);
        } else {
            ft.replace(R.id.question_fragment_frame, fragment);
        }

        ft.commitAllowingStateLoss();


    }

    private boolean loadFromDb(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {SQLCommands._ID,
                SQLCommands.COLUMN_CASE_YES_ID,
                SQLCommands.COLUMN_CASE_NO_ID,
                SQLCommands.COLUMN_CASE_YES_TEXT,
                SQLCommands.COLUMN_CASE_NO_TEXT,
                SQLCommands.COLUMN_CASE_IMAGE,
                SQLCommands.COLUMN_CASE_TEXT
        };
        Cursor cursor = db.query(
                SQLCommands.TABLE_NAME_CASE,
                projection,
                "_id = " + id,
                null,
                null,
                null,
                null);

        if (cursor.getCount() == 0) {
            return false;
        }

        int positiveColumn = cursor.getColumnIndex(SQLCommands.COLUMN_CASE_YES_ID);
        int negativeColumn = cursor.getColumnIndex(SQLCommands.COLUMN_CASE_NO_ID);
        int positiveTextColumn = cursor.getColumnIndex(SQLCommands.COLUMN_CASE_YES_TEXT);
        int negativeTextColumn = cursor.getColumnIndex(SQLCommands.COLUMN_CASE_NO_TEXT);
        int textColumn = cursor.getColumnIndex(SQLCommands.COLUMN_CASE_TEXT);
        int urlColumn = cursor.getColumnIndex(SQLCommands.COLUMN_CASE_IMAGE);

        cursor.moveToFirst();

        positive = cursor.getInt(positiveColumn);
        negative = cursor.getInt(negativeColumn);
        positiveText = cursor.getString(positiveTextColumn);
        negativeText = cursor.getString(negativeTextColumn);
        text = cursor.getString(textColumn);
        url = cursor.getString(urlColumn);
        return true;


    }

    private void writeToCaseDB(JSONObject json) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            JSONObject jsonObject = json.getJSONObject("case");
            text = jsonObject.getString("text");
            url = jsonObject.getString("image");
            int id = jsonObject.getInt("id");
            if (jsonObject.has("answers")) {
                JSONArray jsonArray = jsonObject.getJSONArray("answers");
                JSONObject answerYesObject = jsonArray.getJSONObject(0);

                positive = answerYesObject.getInt("caseId");
                positiveText = answerYesObject.getString("text");
                JSONObject answerNoObject = jsonArray.getJSONObject(1);

                negative = answerNoObject.getInt("caseId");
                negativeText = answerNoObject.getString("text");

                values.put(SQLCommands.COLUMN_CASE_YES_ID, positive);
                values.put(SQLCommands.COLUMN_CASE_YES_TEXT,positiveText);
                values.put(SQLCommands.COLUMN_CASE_NO_ID, negative);
                values.put(SQLCommands.COLUMN_CASE_NO_TEXT, negativeText);
            }
            values.put(SQLCommands._ID, id);
            values.put(SQLCommands.COLUMN_CASE_TEXT, text);
            values.put(SQLCommands.COLUMN_CASE_IMAGE, url);

            db.insert(SQLCommands.TABLE_NAME_CASE, null, values);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        startNewFragment();
    }

    public class FragmentBroadcastReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppContext.BROADCAST_ACTION)) {
                int caseId = intent.getIntExtra(AppContext.CASE_ID, 0);

                if (!loadFromDb(caseId)) {
                    loader = new AsyncLoadJSON(TroubleActivity.this);
                    loader.execute(AppContext.DETAILS_URL + caseId);
                    return;
                }
                startNewFragment();
            }
            if(intent.getAction().equals(AppContext.JSON_ACTION)){
                writeToCaseDB(AppContext.getJsonObject());
            }

        }
    }


}
