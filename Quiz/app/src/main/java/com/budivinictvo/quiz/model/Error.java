package com.budivinictvo.quiz.model;

/**
 * Created by Администратор on 01.01.2015.
 */
public class Error {
    private String message;
    private int code;

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public void  setCode(int _code){
        this.code = _code;
    }
}
