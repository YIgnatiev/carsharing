package com.budivinictvo.quiz.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.budivinictvo.quiz.R;
import com.budivinictvo.quiz.activity.QuizActivity;
import com.budivinictvo.quiz.core.ConstantsApp;
import com.budivinictvo.quiz.core.QuizApi;
import com.budivinictvo.quiz.core.ResponseCallback;
import com.budivinictvo.quiz.model.*;
import com.budivinictvo.quiz.utils.Network;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Администратор on 01.01.2015.
 */
public class StartFragment extends Fragment implements View.OnClickListener{
    private TextView textViewLoad;
    public static final String TAG = "com.budivinictvo.quiz.StarterFragment";
    private QuizActivity mActivity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.start_fragment_layout, container, false);
        mActivity= (QuizActivity)getActivity();
        mActivity.isStarterFragmentActive = true;

//        mActivity.hideMenu();
        findViews(rootView);
        setListeners();


        return rootView;
    }

    private void findViews(View _rootView){
        textViewLoad = (TextView)_rootView.findViewById(R.id.textView_load_StartFragment);

    }

    private void setListeners(){
        textViewLoad.setOnClickListener(this);
    }

    private void turnOffProgressBarr(){
       mActivity.turnOffProgressDialog();
    }

    private User getUser (){
        User user =  (User)getActivity()
                .getIntent()
                .getSerializableExtra(ConstantsApp.USER_INTENT);
        return user;
    }

    private void saveQuestionsIntoFile(String questionFile , ArrayList<Question> questions) {
        try{
        FileOutputStream fos = getActivity().openFileOutput(questionFile, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(questions);


        }catch (IOException e){
            Log.v("error", e.getMessage());
        }

    }

    private void requestQuestions(){

        QuizApi.postGetTests(getUser(), new ResponseCallback() {
            @Override
            public void onFailure(Object object) {
              turnOffProgressBarr();
                if(object != null) {
                    try {
                        com.budivinictvo.quiz.model.Error error = (com.budivinictvo.quiz.model.Error) object;
                        String[] errors = getResources().getStringArray(R.array.errors);
                        showDialog(errors[error.getCode() - 1] , error.getCode());
                    } catch (ClassCastException e) {
                        showDialog((String) object , 0);
                    }
                }else {
                    Toast.makeText(getActivity(), "Ошибка подключения", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(Object object) {
                turnOffProgressBarr();
                ArrayList<Question> questions = (ArrayList<Question>)object;
                saveQuestionsIntoFile(ConstantsApp.QUESTIONS_FILE, questions);
                QuizActivity activity = (QuizActivity)getActivity();
                activity.startQuiz();
            }
        });
    }

    private void loadQuiz(){
        if(isConnected()){
            mActivity.showProgressDialog();
            requestQuestions();

        }
    }

    private void showDialog(String _message  ,final int error){
        if (error ==9 || error  == 12){
            mActivity.logOut();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(_message)
                .setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                }).create()
                .show();


    }

    private boolean isConnected(){
        boolean connected = false;
        if (Network.isInternetConnectionAvailable(mActivity)){
            connected = true;
        } else {
            showAlertDialog("Отсутсвует интернет соединение. Пожалуйста, включите и попробуйте снова", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   loadQuiz();

                }
            });
        }
        return connected;
    }

    public final void showAlertDialog(final String _message, final DialogInterface.OnClickListener _tryAgainListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(_message)
                .setCancelable(false)
                .setPositiveButton("Попробовать еще раз", _tryAgainListener)
                .setNegativeButton("Выход", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mActivity.finish();
                    }
                });
        builder.create().show();
    }
    @Override
    public void onClick(View v) {
       mActivity.requestToServer();


    }
}
