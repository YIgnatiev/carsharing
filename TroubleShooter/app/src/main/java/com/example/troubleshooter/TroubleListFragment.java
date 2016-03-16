package com.example.troubleshooter;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.troubleshooter.database.SQLCommands;
import com.example.troubleshooter.database.URLDatabaseHelper;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;

public class TroubleListFragment extends Fragment {
    private ArrayList<String> data;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private URLDatabaseHelper dbHelper;
    private ListEnableBroadcast listEnableBroadcast;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trouble_list_fragment, container, false);
        initBroadcast();

        data = ((AppContext) getActivity().getApplicationContext()).getData();
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);

        if (!isNetworkConnected()){
            listView.setEmptyView(view.findViewById(R.id.list_empty_view));
            return view;
        }

        listView.setOnItemClickListener(new ListClickListener());
        dbHelper = new URLDatabaseHelper(getActivity());

        if(!AppContext.isInitialized) {
            AsyncLoadJSON loaderAsyncTask = new AsyncLoadJSON(getActivity());

            loaderAsyncTask.execute(AppContext.SCENARIOS_URL);


        }
        return view;
    }
    private void initBroadcast(){
        listEnableBroadcast = new ListEnableBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppContext.LIST_ACTION);
        filter.addAction(AppContext.JSON_LIST_ACTION);
        getActivity().registerReceiver(listEnableBroadcast, filter);
    }
    private void writeToDb(JSONObject jsonObject){

      SQLiteDatabase  db = dbHelper.getWritableDatabase();
        db.execSQL(SQLCommands.SQL_DELETE_ENTRIES);
        ContentValues values = new ContentValues();

        db.execSQL(SQLCommands.CREATE_SCEN_TABLE);
        db.execSQL(SQLCommands.CREATE_CASE_TABLE);

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("scenarios");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject o = jsonArray.getJSONObject(i);

                String text = o.getString("text");
                int caseId = o.getInt("caseId");

                data.add(text);

                values.put(SQLCommands._ID, caseId);
                values.put(SQLCommands.COLUMN_SCEN_TEXT, text);
                db.insert(SQLCommands.TABLE_NAME_SCEN, null, values);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setAdapter(adapter);

    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(listEnableBroadcast);
        super.onDestroy();
    }
    private boolean isNetworkConnected() {

            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni == null) {

                return false;
            } else
                return true;


    }
    private int getIdFromDb (int position){

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(SQLCommands.TABLE_NAME_SCEN,
               new String[]{SQLCommands._ID},
                null,
                null,
                null,
                null,
                null
                );
        cursor.moveToPosition(position);
        int  caseID = cursor.getInt(cursor.getColumnIndex(SQLCommands._ID));
        return caseID;

    }
//    private class LoaderAsyncTask extends AsyncTask<String, Long, JSONObject> {
//        private ProgressDialog dialog;
//
//        @Override
//        protected void onPreExecute() {
//            this.dialog = new ProgressDialog(getActivity());
//            this.dialog.setMessage(getResources().getString(R.string.loading));
//            if (!this.dialog.isShowing()) {
//                this.dialog.show();
//            }
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... params) {
//            JSONObject jsonObject = null;
//            HttpRequest request = HttpRequest.get(params[0]);
//            if (request.code() == 200) {
//                String response = request.body();
//                try {
//                    jsonObject = new JSONObject(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            return jsonObject;
//        }
//        @Override
//        protected void onPostExecute(JSONObject jsonObject) {
//            writeToDb(jsonObject);
//            this.dialog.dismiss();
//        }
//    }
    private class ListClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int orient = getActivity().getResources().getConfiguration().orientation;
            if (orient == 1) {
                AppContext.backButtonCantCloseApp = true;
            }else{
                listView.setEnabled(false);
                listView.setAlpha(0.5f);

            }
            Intent intent = new Intent(AppContext.BROADCAST_ACTION);
            int caseId = getIdFromDb(position);
            intent.putExtra(AppContext.CASE_ID,caseId);
            getActivity().sendBroadcast(intent);
        }
    }

    public class ListEnableBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();

            if (intent.getAction().equals(AppContext.LIST_ACTION)) {
                listView.setEnabled(true);
                listView.setAlpha(1f);
            }

            else if (intent.getAction().equals(AppContext.JSON_LIST_ACTION)) {
                writeToDb(AppContext.getJsonObject());
            }
            }


            }


}
