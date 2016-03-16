package com.budivinictvo.quiz.model;

import java.io.Serializable;

/**
 * Created by Администратор on 01.01.2015.
 */
public class QuestionDetails implements Serializable{

    private String image;
    private String description;
    private String title;

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
