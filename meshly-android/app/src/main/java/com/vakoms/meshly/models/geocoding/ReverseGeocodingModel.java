package com.vakoms.meshly.models.geocoding;


/**
 * Created by Oleh Makhobey on 16.05.2015.
 * tajcig@ya.ru
 */
public class ReverseGeocodingModel {


           String countryName;
           String countryCode;
           String adminName1;

    public String getCountry() {
        if(countryName == null) return "";
        else return countryName;
    }
    public String getCity(){
        if(adminName1 == null)return "";
        else return adminName1 + ", ";
    }
}
