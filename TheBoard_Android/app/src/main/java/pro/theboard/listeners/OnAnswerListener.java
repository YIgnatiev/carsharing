package pro.theboard.listeners;

import android.app.Fragment;

import pro.theboard.models.cards.Answer;
import pro.theboard.models.cards.Model;


public interface OnAnswerListener {
    void onAnswer(Model _questionModel, Answer _answer);
    void replaceFragment(Fragment fragment);

}
