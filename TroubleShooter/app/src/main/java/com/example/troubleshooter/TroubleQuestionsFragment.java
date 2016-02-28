package com.example.troubleshooter;



import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.troubleshooter.database.URLDatabaseHelper;
import com.github.kevinsawicki.http.HttpRequest;

import java.io.InputStream;

public class TroubleQuestionsFragment extends Fragment {

    private ImageView imageView;
    private TextView textView;
    private Button btn_positive;
    private Button btn_negative;

    private CaseInfo caseinfo;

    LoadImage loadImage;
    URLDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trouble_question_fragment_layout, container, false);
        caseinfo = new CaseInfo();
        loadImage = new LoadImage();
        dbHelper = new URLDatabaseHelper(getActivity());
        imageView = (ImageView) view.findViewById(R.id.img_question);
        textView = (TextView) view.findViewById(R.id.txt_question);

        btn_negative = (Button) view.findViewById(R.id.btn_negative);
        btn_positive = (Button) view.findViewById(R.id.btn_positive);
        btn_negative.setOnClickListener(new MyOnClickListener());
        btn_positive.setOnClickListener(new MyOnClickListener());
        loadFromBundle(getArguments());
        loadComponentInfo();
        return view;
    }

    private void loadComponentInfo() {
        textView.setText(caseinfo.getText());

        if (caseinfo.getPositiveID() != 0) {
            loadImage.execute(caseinfo.getUrl());
            btn_negative.setText(caseinfo.getNegativeText());
            btn_positive.setText(caseinfo.getPositiveText());
        } else {
            lastQuestionInit();
        }
    }

    private void lastQuestionInit() {

        btn_positive.setVisibility(View.GONE);
        btn_negative.setVisibility(View.GONE);

        int orient = this.getResources().getConfiguration().orientation;
        if (orient == 1) {
            //Portret

            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Intent intent = new Intent(AppContext.LIST_ACTION);
            getActivity().sendBroadcast(intent);
        }
    }

    private void loadFromBundle(Bundle bundle) {
        caseinfo.setPositiveText(bundle.getString(AppContext.BUNDLE_YES_TEXT));
        caseinfo.setNegativeText(bundle.getString(AppContext.BUNDLE_NO_TEXT));
        caseinfo.setPositiveID(bundle.getInt(AppContext.BUNDLE_YES));
        caseinfo.setNegativeID(bundle.getInt(AppContext.BUNDLE_NO));
        caseinfo.setText(bundle.getString(AppContext.BUNDLE_TEXT));
        caseinfo.setUrl(bundle.getString(AppContext.BUNDLE_URL));
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_negative: {

                    Intent intent = new Intent(AppContext.BROADCAST_ACTION);
                    intent.putExtra(AppContext.CASE_ID, caseinfo.getNegativeID());
                    getActivity().sendBroadcast(intent);
                }
                break;
                case R.id.btn_positive: {

                    Intent intent = new Intent(AppContext.BROADCAST_ACTION);
                    intent.putExtra(AppContext.CASE_ID, caseinfo.getPositiveID());
                    getActivity().sendBroadcast(intent);
                }
                break;
                default:
                    break;
            }
        }
    }
    private class LoadImage extends AsyncTask<String, Void, Bitmap> {
        private ProgressDialog dialog;

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            HttpRequest request = HttpRequest.get(params[0]);

            if (request.code() == 200) {
                InputStream input = request.stream();
                bitmap = BitmapFactory.decodeStream(input);
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute() {

            this.dialog = new ProgressDialog(getActivity());
            this.dialog.setMessage(getActivity().getResources().getString(R.string.loading));
            if (!this.dialog.isShowing()) {
                this.dialog.show();
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            this.dialog.dismiss();

        }
    }
}
