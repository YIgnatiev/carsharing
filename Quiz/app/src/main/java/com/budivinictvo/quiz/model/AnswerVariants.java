package com.budivinictvo.quiz.model;

import java.io.Serializable;

/**
 * Created by Администратор on 01.01.2015.
 */
public class AnswerVariants implements Serializable{
    int id;
    int answer_id;
    String title;

    public int getId() {
        return id;
    }

    public int getAnswer_id() {
        return answer_id;
    }

    public String getTitle() {
        return title;
    }
}
