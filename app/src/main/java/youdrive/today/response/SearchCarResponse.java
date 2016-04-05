package youdrive.today.response;


public class SearchCarResponse extends BaseResponse {
    private float lat;
    private float lon;
    private int radius;
    public float getLonResponse() {
        return lat;
    }
    public float getLatResponse() {
        return lon;
    }
    public int getRadiusResponse() {
        return radius;
    }


}
