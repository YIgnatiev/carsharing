package com.budivinictvo.quiz.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.budivinictvo.quiz.R;
import com.budivinictvo.quiz.activity.QuizActivity;
import com.budivinictvo.quiz.adapters.AnswersAdapter;
import com.budivinictvo.quiz.core.ConstantsApp;
import com.budivinictvo.quiz.core.ImageLoadCallback;
import com.budivinictvo.quiz.core.QuizApi;
import com.budivinictvo.quiz.core.TimerCallback;
import com.budivinictvo.quiz.model.Answer;
import com.budivinictvo.quiz.model.AnswerVariants;
import com.budivinictvo.quiz.model.Question;
import com.budivinictvo.quiz.model.QuestionDetails;
import com.daimajia.slider.library.Animations.BaseAnimationInterface;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by Администратор on 02.01.2015.
 */
public class QuestionFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener, TimerCallback {
    public static final String TAG = "com.budivnictvo.quiz.QuestionFragment";
    private final int BUTTON_MODE = 1;
    private final int CHECKBOX_MODE = 2;
    private TextView textViewTimer;
    private TextView textViewCounter;
    private TextView textViewHeader;
    private ListView listViewAnswers;
    private TextView textViewSubmit;
    private QuizActivity activity;
    public int timeRemaining = 60000;
    private AnswersAdapter adapter;
    private SliderLayout mSliderLayout;
    private boolean isImageLoaded = false;

    private  Question question;
    private View footerLayout;
    private View headerLayout;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.question_fragment_layout, container, false);
        findViews(rootView);
        setDisable();
        setListeners();
        question = (Question) getArguments().getSerializable(ConstantsApp.QUESTION_INTENT);
        setData(question);

        setActionBar();
        isTimerEnded();
        return rootView;
    }

    private void setActionBar() {
        activity.setActionBarTitle(getString(R.string.tests));
    }

    private void findViews(View _rootView) {
        headerLayout = LayoutInflater.from(getActivity()).inflate(R.layout.question_fragment_header, null);
        textViewHeader = (TextView) headerLayout.findViewById(R.id.textView_header_QuizFragment);

        textViewTimer = (TextView) headerLayout.findViewById(R.id.textView_timer_QuizFragment);
        textViewCounter = (TextView) headerLayout.findViewById(R.id.textView_counter_QuizFragment);
        mSliderLayout = (SliderLayout) headerLayout.findViewById(R.id.imageView_logo_QuizFragment);
        listViewAnswers = (ListView) _rootView.findViewById(R.id.listView_questions_QuizFragment);
        footerLayout = LayoutInflater.from(getActivity()).inflate(R.layout.question_fragment_footer, null);
        textViewSubmit = (TextView) footerLayout.findViewById(R.id.textView_submit_answer_QuizFragment);


        activity = (QuizActivity) getActivity();
        activity.showMenu();
        activity.isQuetionFragmentActive = true;

    }

    private void startTimer() {
        if (isImageLoaded) {
            activity.setTimer(this, timeRemaining);
        }
    }

    private void setDisable() {


        textViewTimer.setEnabled(false);
        textViewCounter.setEnabled(false);

    }

    private void isTimerEnded() {
        if (timeRemaining == 1) {
            onFinish();
        }
    }

    private void setListeners() {

        textViewSubmit.setOnClickListener(this);
        listViewAnswers.setOnItemClickListener(this);
        listViewAnswers.setSelector(R.color.transparent);

    }

    private void setData(Question _question) {
        setQuestion(_question.getQuestion());
        textViewHeader.setText(_question.getQuestion_title());
        setQuestionsPosition();
        switch (_question.getType()) {
            case BUTTON_MODE:

                listViewAnswers.addFooterView(footerLayout, null, false);
                listViewAnswers.addHeaderView(headerLayout, null, false);
                textViewSubmit.setVisibility(View.GONE);
                setAnswersForButton(_question.getAnswerVariants());

                break;
            case CHECKBOX_MODE:
                listViewAnswers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listViewAnswers.addHeaderView(headerLayout, null, false);
                listViewAnswers.addFooterView(footerLayout, null, false);
                textViewSubmit.setEnabled(false);
                setAnswersForButton(_question.getAnswerVariants());
                break;
        }

    }

    private void setQuestion(QuestionDetails[] _question) {


        textViewHeader.setText(_question[0].getTitle());

        setSliderLayout(_question);
    }

    private void setSliderLayout(final QuestionDetails[] _questions) {

        for ( int i = 0 ; i < _questions.length ; i++) {
                final int position = i;
            mSliderLayout.stopAutoCycle();
            final BaseSliderView sliderView = new BaseSliderView(activity) {
                private ImageLoader holder = new ImageLoader();
                private boolean isLoaded = false;

                @Override
                public View getView() {


                    View v = LayoutInflater.from(getContext()).inflate(R.layout.slider_image_layout, null);
                    final ImageView target = (ImageView) v.findViewById(R.id.daimajia_slider_image);
                    final ProgressBar mProgressBar = (ProgressBar) v.findViewById(R.id.myloading_bar);
                    mProgressBar.setVisibility(View.GONE);
                    TextView description = (TextView) v.findViewById(R.id.description);
                    description.setText(getDescription());


                    if (_questions[position].getImage() == null) {
                        target.setImageResource(R.drawable.no_photo);

                        if (position == 0 && !isImageLoaded){
                            isImageLoaded = true;
                            startTimer();
                        }




                        if (_questions.length > 1) {
                            mSliderLayout.startAutoCycle(5000, 5000, true);
                        }

                    } else if (holder.getImage(ConstantsApp.IMAGE_URL + _questions[position].getImage()) == null) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        QuizApi.getImage(ConstantsApp.IMAGE_URL + _questions[position].getImage(), new ImageLoadCallback() {
                            @Override
                            public void onLoaded(Bitmap image) {
                                target.setImageBitmap(image);

                                mProgressBar.setVisibility(View.GONE);
                                holder.setImage(ConstantsApp.IMAGE_URL + _questions[position].getImage(), image);
                                if (!isImageLoaded && position ==0) {
                                    isImageLoaded = true;

                                   startTimer();
                                    if (_questions.length > 1) {
                                        mSliderLayout.startAutoCycle(5000, 5000, true);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(String error) {
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else{
                        target.setImageBitmap(holder.getImage(ConstantsApp.IMAGE_URL + _questions[position].getImage()));
                    }



                    bindEventAndShow(v, target);
                    return v;
                }

                class ImageLoader {
                    private HashMap<String, Bitmap> images;

                    public ImageLoader() {
                        images = new HashMap<String, Bitmap>();
                    }

                    private Bitmap getImage(String _key) {
                        return images.get(_key);
                    }

                    private void setImage(String _key, Bitmap _image) {
                        images.put(_key, _image);
                    }

                }

            };

            sliderView.description(_questions[position].getDescription());

            sliderView.setScaleType(BaseSliderView.ScaleType.CenterInside);





            mSliderLayout.addSlider(sliderView);


        }
        PagerIndicator indicator = (PagerIndicator) headerLayout.findViewById(R.id.custom_indicator);
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        mSliderLayout.setSliderTransformDuration(300, new AccelerateInterpolator());


        mSliderLayout.setCustomIndicator(indicator);


    }

    private void setQuestionsPosition() {
        textViewCounter.setText(activity.getPosition() + "/" + activity.getQuantity());
    }

    private void setAnswersForButton(AnswerVariants[] _answerVariants) {
        adapter = new AnswersAdapter(activity, R.layout.question_item, _answerVariants);

        listViewAnswers.setAdapter(adapter);
    }

    public void onTick(long millisUntilFinished) {
        if (millisUntilFinished / 1000 > 9) {
            textViewTimer.setText("00:" + millisUntilFinished / 1000);
        } else {
            textViewTimer.setText("00:0" + millisUntilFinished / 1000);
        }
        timeRemaining = (int) millisUntilFinished / 1000;
    }

    public void onFinish() {
        textViewSubmit.setEnabled(true);
        textViewTimer.setText("00:00");
        setAnswersClickable();
    }

    private void setAnswersClickable() {
        adapter.setItemsEnabled(true);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        SparseBooleanArray sparseBooleanArray = listViewAnswers.getCheckedItemPositions();
        if (sparseBooleanArray.size() == 0) {
            Toast.makeText(getActivity(), getString(R.string.choose_answers), Toast.LENGTH_SHORT).show();
        } else {
            Answer[] answerArray = new Answer[sparseBooleanArray.size()];
            for (int i = 0; i < sparseBooleanArray.size(); i++) {
                if (sparseBooleanArray.valueAt(i)) {
                    Answer answer = new Answer();
                    answer.answer_id = question.getAnswerVariants()[sparseBooleanArray.keyAt(i)-1].getAnswer_id();
                    answerArray[i] = answer;
                }
            }
            activity.nextQuestion(answerArray);
        }
    }

    @Override
    public void onPause() {
        if (activity.timer != null)
            activity.timer.pause();
        super.onPause();
    }

    @Override
    public void onStop() {
        if (activity.timer != null)
            activity.timer.pause();
        super.onStop();
    }

    @Override
    public void onStart() {
        if (activity.timer != null)
            activity.timer.resume();
        super.onStart();
    }

    @Override
    public void onResume() {
        if (activity.timer != null)
            activity.timer.resume();
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position -= listViewAnswers.getHeaderViewsCount();
        SparseBooleanArray sparseBooleanArray = ((ListView) parent).getCheckedItemPositions();

        switchBackground((TextView) view.findViewById(R.id.textView_answer_item));
        if (sparseBooleanArray == null) {
            Answer[] answerArray = new Answer[1];
            Answer answer = new Answer();
            answer.answer_id = question.getAnswerVariants()[position].getAnswer_id();
            answerArray[0] = answer;
            activity.nextQuestion(answerArray);
        }
    }

    private void switchBackground(TextView textView) {
        if (textView.isActivated()) {
            textView.setBackground(getResources().getDrawable(R.drawable.checkbox_clicked));
            textView.setActivated(true);
        } else {

            textView.setBackground(getResources().getDrawable(R.drawable.checkbox_background_selector));
            textView.setActivated(false);
        }

    }

}
