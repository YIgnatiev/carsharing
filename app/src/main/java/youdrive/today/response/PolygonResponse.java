package youdrive.today.response;

import java.util.List;

import youdrive.today.models.Coord;
import youdrive.today.models.SimpleCar;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/19/15.
 */
public class PolygonResponse extends BaseResponse {


    private List<Coord>[] area;
    private List<SimpleCar> cars;


    public List<SimpleCar> getCars(){
        return cars;
    }

    public List<Coord> getArea() {
        return area[0];
    }

}


