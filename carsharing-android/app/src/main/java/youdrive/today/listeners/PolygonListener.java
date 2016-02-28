package youdrive.today.listeners;

import youdrive.today.response.PolygonResponse;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/19/15.
 */
public interface PolygonListener {
    void onPolygonSuccess(PolygonResponse coordList);
    void onPolygonFailed();
}
