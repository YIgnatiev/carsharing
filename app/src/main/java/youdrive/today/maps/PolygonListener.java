package youdrive.today.maps;

import java.util.List;

import youdrive.today.response.Coord;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/19/15.
 */
public interface PolygonListener {
    void onPolygonSuccess(List<Coord> coordList);
    void onPolygonFailed();
}
