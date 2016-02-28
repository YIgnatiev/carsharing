package com.budivinictvo.quiz.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budivinictvo.quiz.adapters.MenuAdapter;
import com.budivinictvo.quiz.core.ConstantsApp;
import com.budivinictvo.quiz.R;
import com.budivinictvo.quiz.core.QuizApi;
import com.budivinictvo.quiz.core.ResponseCallback;
import com.budivinictvo.quiz.core.TimerCallback;
import com.budivinictvo.quiz.fragments.AccountFragment;
import com.budivinictvo.quiz.fragments.QuestionFragment;
import com.budivinictvo.quiz.fragments.StartFragment;
import com.budivinictvo.quiz.fragments.StatisticsFragment;
import com.budivinictvo.quiz.model.Answer;
import com.budivinictvo.quiz.model.PostAnswer;
import com.budivinictvo.quiz.model.Question;
import com.budivinictvo.quiz.model.User;
import com.budivinictvo.quiz.utils.CountDownTimerWithPause;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Администратор on 31.12.2014.
 */
public class QuizActivity extends Activity implements AdapterView.OnItemClickListener ,View.OnClickListener {
    public boolean readyToExit;
    public boolean isStatisticsNeeded =true;
    public boolean isQuetionFragmentActive;
    public boolean isStarterFragmentActive;
    public User mUser;
    private DrawerLayout mDrawerLayout;
    private ArrayList<Question> mQuestions;
    private int mPosition = 0;
    private ProgressDialog mProgressDialog;
    private PostAnswer[] answers;
    private ActionBar mActionBar;
    private ImageView imageViewMenu;
    private TextView textViewActionBarText;
    private ListView menuList;
    public CountDownTimerWithPause timer;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity_layout);
        initDrawerLayout();
        initUser();
        mActionBar = getActionBar();
        initActionBar(mActionBar);
        startFragment(new StartFragment() , StartFragment.TAG);
        findActionBarUI(mActionBar);
        setListeners();

    }

    public void setTimer(final TimerCallback callback , int timeRemaining) {
       if(timeRemaining == 60000) {
           timer = new CountDownTimerWithPause(timeRemaining, 1000, true) {

               public void onTick(long millisUntilFinished) {
                   callback.onTick(millisUntilFinished);
               }

               public void onFinish() {
                   callback.onFinish();
               }
           }.create();
       }
    }

    public void showMenu(){
        imageViewMenu.setVisibility(View.VISIBLE);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout_QuizActivity);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if(timer!=null) {
                    timer.pause();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if(isQuetionFragmentActive && timer!=null) {
                    timer.resume();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        String[] menuItems = getResources().getStringArray(R.array.menu_items_for_drawer);
        MenuAdapter adapter = new MenuAdapter(this, R.layout.drawer_item, menuItems);
        menuList = (ListView) findViewById(R.id.listView_drawer_QuizActivity);
        menuList.setAdapter(adapter);
        menuList.setOnItemClickListener(this);
    }

    private void initActionBar(ActionBar _actionBar) {
        View cView = getLayoutInflater().inflate(R.layout.action_bar_quiz, new RelativeLayout(this));
        _actionBar.setDisplayShowHomeEnabled(false);
        _actionBar.setDisplayShowTitleEnabled(false);
        _actionBar.setDisplayHomeAsUpEnabled(false);
        _actionBar.setDisplayShowCustomEnabled(true);
        _actionBar.setCustomView(cView);
        _actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    private void findActionBarUI(ActionBar _actionBar){
        imageViewMenu =  (ImageView)_actionBar.getCustomView().findViewById(R.id.imageViewActionBarMenu);
        textViewActionBarText = (TextView)_actionBar.getCustomView().findViewById(R.id.textViewActionBarQuizText);
    }

    public void setActionBarTitle(String _title){
        textViewActionBarText.setText(_title);
    }

    private void setListeners(){
        imageViewMenu.setOnClickListener(this);
    }

    private void startFragment(Fragment fragment ,String _tag) {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

                .replace(R.id.frameLayout_quiz, fragment ,_tag )
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void startFragmentWithTag(String _tag) {
        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentByTag(_tag);
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment == null) {
            switch (_tag) {
                case AccountFragment.TAG:
                    fragment = new AccountFragment();
                    break;
                case QuestionFragment.TAG:
                    fragment = new QuestionFragment();
                    break;
                case StatisticsFragment.TAG:
                    fragment = new StatisticsFragment();
                    break;
            }
        }
            transaction
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.frameLayout_quiz, fragment, _tag);
        if (isQuetionFragmentActive || isStarterFragmentActive){
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
    }

    private void initUser() {
        mUser = (User) getIntent().getSerializableExtra(ConstantsApp.USER_INTENT);

    }

    @Override
    public void onBackPressed() {
        if(readyToExit){
            finish();
            return;
        }
        if (isStarterFragmentActive){

            startFragmentWithTag(StartFragment.TAG);
            readyToExit = true;
            return;
        }

        if (isQuetionFragmentActive) {
            finish();
        } else {
           changeFragment(0);
        }
    }

    public int getPosition() {
        return mPosition;
    }

    public int getQuantity() {
        return mQuestions.size();
    }

    public void startQuiz() {
        isStatisticsNeeded = true;
        mPosition = 0;
        mQuestions = readQuestionsFromFile(ConstantsApp.QUESTIONS_FILE);
        if (mQuestions != null) {
            answers = new PostAnswer[mQuestions.size()];
        }
        nextQuestion(null);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        if (hasFocus){
            if (timer != null)
                timer.resume();
        }else {
            if (timer != null)
                timer.pause();
        }
    }

    private boolean checkForLastQuestion() {
        return mPosition == mQuestions.size();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            this.mProgressDialog = new ProgressDialog(this);
            this.mProgressDialog.setCancelable(false);
        }
        this.mProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_loading_questions));

        if (!this.mProgressDialog.isShowing()) {
            this.mProgressDialog.show();
        }


    }

    public void turnOffProgressDialog() {
        this.mProgressDialog.dismiss();
    }

    private void myshowDialog(String _message ,final int error){
        if (error ==9 || error  == 12){
            logOut();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(_message)
                .setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create()
                .show();

    }


    public void requestToServer() {
        isStarterFragmentActive = false;
        showProgressDialog();
        QuizApi.postAnswers(mUser, answers, new ResponseCallback() {
            @Override
            public void onFailure(Object object) {
                turnOffProgressDialog();
                if (object != null) {
                    try {
                        com.budivinictvo.quiz.model.Error error = (com.budivinictvo.quiz.model.Error) object;
                        String[] errors = getResources().getStringArray(R.array.errors);
                        myshowDialog(errors[error.getCode() - 1] , error.getCode());


                    } catch (ClassCastException e) {
                        myshowDialog((String ) object , 0);
                    }
                }else {
                    Toast.makeText(QuizActivity.this,"Ошибка подключения",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(Object object) {
                turnOffProgressDialog();
                ArrayList<Question> questions = (ArrayList<Question>) object;
                saveQuestionsIntoFile(ConstantsApp.QUESTIONS_FILE, questions);
                startQuiz();
            }
        });

    }

    private ArrayList<Question> readQuestionsFromFile(String questionFile) {
        try {
            FileInputStream fis = openFileInput(questionFile);
            ObjectInputStream is = new ObjectInputStream(fis);
            ArrayList<Question> questions = (ArrayList<Question>) is.readObject();
            fis.close();
            is.close();
            return questions;
        } catch (IOException e) {
            Log.v("error", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.v("error", e.getMessage());
        }
        return null;
    }

    private void saveQuestionsIntoFile(String questionFile, ArrayList<Question> questions) {
        try {
            FileOutputStream fos = openFileOutput(questionFile, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(questions);


        } catch (IOException e) {
            Log.v("error", e.getMessage());
        }

    }

    public void nextQuestion(Answer[] _answers) {


        if (_answers != null) {
            answers[mPosition - 1].answers = _answers;
        }
        if (checkForLastQuestion()) {
            requestToServer();
            return;
        }
        if (mQuestions != null) {
            answers[mPosition] = new PostAnswer();
            answers[mPosition].question_id = mQuestions.get(mPosition).getQuestion_id();
            QuestionFragment fragment = new QuestionFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstantsApp.QUESTION_INTENT, mQuestions.get(mPosition));
            fragment.setArguments(bundle);

            startFragment(fragment, QuestionFragment.TAG);
            mPosition++;
        }
    }

    public void logOut() {

        SharedPreferences sharedPreferences = getSharedPreferences(ConstantsApp.USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onClick(View v) {


            if (mDrawerLayout.isDrawerOpen(menuList)){
                mDrawerLayout.closeDrawers();
            }else {
                mDrawerLayout.openDrawer(menuList);
            }

    }

    public void changeFragment(int position){
        switch (position) {
        case 0:
        startFragmentWithTag(QuestionFragment.TAG);
        break;
        case 1:
        startFragmentWithTag(StatisticsFragment.TAG);
        break;
        case 2:
        startFragmentWithTag(AccountFragment.TAG);
        break;
        case 3:
        logOut();
        break;
    }
    mDrawerLayout.closeDrawers();
    }

    private void changeFragmentWhenStarterFragmentActive(int _position){
        readyToExit = false;
        switch (_position) {
            case 0:
                requestToServer();


                break;
            case 1:



                startFragmentWithTag(StatisticsFragment.TAG);
                break;
            case 2:
                startFragmentWithTag(AccountFragment.TAG);
                break;
            case 3:
                logOut();
                break;
        }
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(isStarterFragmentActive) {
           changeFragmentWhenStarterFragmentActive(position);
        }else{
            changeFragment(position);
        }

    }


}

