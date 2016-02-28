package com.example.troubleshooter;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

class AsyncLoadJSON extends AsyncTask<String ,Void ,JSONObject>{
    private ProgressDialog dialog;
    private Activity activity;
    public AsyncLoadJSON(Activity activity){
        this.activity = activity;
    }
    @Override
    protected void onPreExecute() {
        this.dialog = new ProgressDialog(activity);
        this.dialog.setMessage(activity.getResources().getString(R.string.loading));
        if (!this.dialog.isShowing()) {
            this.dialog.show();
        }
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject jsonObject = null;
        HttpRequest request = HttpRequest.get(params[0]);

        if (request.code() == 200) {
            String response = request.body();
            try {
                jsonObject = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {


        AppContext.setJsonObject(jsonObject);
        Intent intent;
        if (AppContext.isInitialized) {
            intent = new Intent(AppContext.JSON_ACTION);
        }else {
            intent = new Intent(AppContext.JSON_LIST_ACTION);
            AppContext.isInitialized = true;
        }
        activity.sendBroadcast(intent);
        this.dialog.dismiss();
    }

}




