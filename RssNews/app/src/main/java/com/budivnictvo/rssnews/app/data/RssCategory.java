package com.budivnictvo.rssnews.app.data;


import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Администратор on 19.12.2014.
 */
public class RssCategory implements Serializable{
    private String name;
    private String rssUrl;
    private int iconId;
    private ArrayList<RssItem> mListItems;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRssUrl() {
        return rssUrl;
    }

    public void setRssUrl(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public ArrayList<RssItem> getmListItems() {
        return mListItems;
    }

    public void setmListItems(ArrayList<RssItem> mListItems) {
        if (mListItems == null){
            mListItems = new ArrayList<RssItem>();
        }
        this.mListItems = mListItems;
    }
}
