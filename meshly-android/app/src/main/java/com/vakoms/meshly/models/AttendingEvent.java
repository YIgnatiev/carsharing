package com.vakoms.meshly.models;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 8/31/15.
 */
public class AttendingEvent {


    private String name;
    private String website;
    private boolean isBoth;

    public AttendingEvent(){

    }

    public AttendingEvent(String name, String website, boolean isBoth) {
        this.name = name;
        this.website = website;
        this.isBoth = isBoth;
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public boolean isBoth() {
        return isBoth;
    }
}
