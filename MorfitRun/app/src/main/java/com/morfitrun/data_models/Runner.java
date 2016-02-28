package com.morfitrun.data_models;

/**
 * Created by Admin on 17.03.2015.
 */
public class Runner {

    private String _id;
    private String raceId;
    private  RunnerUser userId;
    private int runnerId;
    private int position;
    private long time;
    private int status;

    public Runner(){

    }

    public Runner(int _position, long _time){
        this.position = _position;
        this.time = _time;

    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    public void setUserId(RunnerUser userId) {
        this.userId = userId;
    }

    public void setRunnerId(int _runnerId){
        this.runnerId = _runnerId;
    }


    public String get_id() {
        return _id;
    }

    public String getRaceId() {
        return raceId;
    }

    public RunnerUser getUserId() {
        return userId;
    }

    public int getRunnerId() {
        return runnerId;
    }

//    public int get_v() {
//        return _v;
//    }

    public int get_position() {
        return position;
    }

    public long getTime() {
        return time;
    }

    public int getStatus() {
        return status;
    }
}
