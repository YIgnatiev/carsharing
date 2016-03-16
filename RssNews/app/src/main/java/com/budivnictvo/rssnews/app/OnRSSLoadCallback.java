package com.budivnictvo.rssnews.app;

import com.budivnictvo.rssnews.app.data.RssChannel;
import com.budivnictvo.rssnews.app.data.RssItem;

import java.util.ArrayList;

/**
 * Created by Администратор on 16.12.2014.
 */
public interface OnRSSLoadCallback {
    public void onRSSLoad(ArrayList<RssItem> _date);
}
