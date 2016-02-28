package com.vakoms.meshly.models;

import java.util.List;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/22/15.
 */
public class BaseResponse<T> {

    private int status;
    private List<Error> error;
    T data;

    public T getData() {
        return data;
    }


    public int getStatus() {
        return status;
    }

    public List<Error> getError() {
        return error;

    }

    public class Error {
        String message;
        int code;

        public String getMessage() {
            return message;
        }

        public int getCode() {
            return code;
        }
    }



}
