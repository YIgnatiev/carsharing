package youdrive.today.response;


public class SearchCarResponse extends BaseResponse {
    private float lat;
    private float lon;
    private int radius;
    public float getLonResponse() {
        return lon;
    }
    public float getLatResponse() {
        return lat;
    }
    public int getRadiusResponse() {
        return radius;
    }


}
