package com.budivinictvo.quiz.model;

import java.io.Serializable;

/**
 * Created by Администратор on 31.12.2014.
 */
public class Question implements Serializable{
    private int id;
    private int question_type;
    private int answer_type;
    private QuestionDetails[] question;
    private String question_title;
    private int question_id;

    private AnswerVariants[] answers;


    public int getQuestion_id(){
        return this.question_id;
    }

    public String getQuestion_title(){
        return this.question_title;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return question_type;
    }

    public QuestionDetails[] getQuestion() {
        return question;
    }

    public int getAnswer_type() {
        return answer_type;
    }

    public AnswerVariants[] getAnswerVariants() {
        return answers;
    }


}
