package com.vakoms.meshly.models;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/12/15.
 */
public class LinkedInMassage {

    private String comment;
    private LinkedInVisibility visibility;

    public void setComment(String comment) {
        this.comment = comment;
    }

    public class LinkedInVisibility {

        private String code = "anyone";
    }

}

