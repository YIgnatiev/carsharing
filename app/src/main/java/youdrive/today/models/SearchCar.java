package youdrive.today.models;


public class SearchCar {

    private float lat;
    private float lon;
    private int radius;

    public SearchCar (float lat, float lon,int radius){
        this.lat = lat;
        this.lon =lon;
        this.radius =radius;
    }
}
