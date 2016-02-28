package com.carusselgroup.dwt.utils;

import com.carusselgroup.dwt.model.Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class SearchFilter  {


    public static HashMap<String, String> mStatusMap = new HashMap<String, String>(){{
        put("Used","0");
        put("Demo","1");
        put("New","2");}};


    public static Set<String> getStatusSet() {
        Set<String> uniqueStatusSet = new TreeSet<>();
        uniqueStatusSet.add("Used");
        uniqueStatusSet.add("Demo");
        uniqueStatusSet.add("New");
        return uniqueStatusSet;
    }

    public static String getStatusValue(String key){
        return mStatusMap.get(key);
    }

    public static String paramsToString(HashMap <String, String> params){
        String result = "";
        if (params!=null)
        for (String key : params.keySet()){
            result += "&" + key + "=" + params.get(key);
        }
        return result;
    }

    public static Set<String> getBrandSet(ArrayList<Car> cars) {
        Set<String> uniqueBrandSet = new TreeSet<>();
        for (Car car : cars) {
            uniqueBrandSet.add(car.getBrand());
        }
        return uniqueBrandSet;
    }

    public static Set<String> getModelSet(ArrayList<Car> cars) {
        Set<String> uniqueModelSet = new TreeSet<>();
        for (Car car : cars) {
            uniqueModelSet.add(car.getModel());
        }
        return uniqueModelSet;
    }

    public static Set<String> getLocationSet(ArrayList<Car> cars) {
        Set<String> uniqueLocationSet = new TreeSet<>();
        for (Car car : cars) {
            uniqueLocationSet.add(car.getLocation());
        }
        return uniqueLocationSet;
    }
}
