package youdrive.today.response;

import java.util.List;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/19/15.
 */
public class PolygonResponse extends BaseResponse {


    List<Coord>[] area;
    public List<Coord> getArea() {
        return area[0];
    }

}


