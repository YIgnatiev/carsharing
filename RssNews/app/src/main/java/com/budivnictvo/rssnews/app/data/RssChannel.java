package com.budivnictvo.rssnews.app.data;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Администратор on 18.12.2014.
 */
public class RssChannel implements Serializable{

    private String name;

    private int logoId;

    private int arrayOfCategoriesResourceID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArrayOfCategoriesResourceID() {
        return arrayOfCategoriesResourceID;
    }

    public void setArrayOfCategoriesResourceID(int arrayOfCategoriesResourceID) {
        this.arrayOfCategoriesResourceID = arrayOfCategoriesResourceID;
    }

    public int getLogoId() {
        return logoId;
    }

    public void setLogoId(int mLogoId) {
        this.logoId = mLogoId;
    }
}
