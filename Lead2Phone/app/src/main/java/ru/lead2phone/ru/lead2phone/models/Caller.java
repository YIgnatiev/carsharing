package ru.lead2phone.ru.lead2phone.models;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 8/27/15.
 */
public class Caller {

    private String number;
    private String name;
    private int id;
    private long time;



    public String getNumber() {
        return number;
    }

    public long getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
