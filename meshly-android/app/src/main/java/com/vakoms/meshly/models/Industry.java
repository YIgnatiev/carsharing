package com.vakoms.meshly.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import meshly.vakoms.com.meshly.BR;

/**
 * Created by Oleh Makhobey on 21.05.2015.
 * tajcig@ya.ru
 */
public class Industry extends BaseObservable {

    private boolean isChoosen;
    private String code;
    private String name;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isChoosen() {
        return isChoosen;
    }
    @Bindable
    public void setIsChoosen(boolean isChoosen) {
        this.isChoosen = isChoosen;
        notifyPropertyChanged(BR.isChoosen);
    }

}
