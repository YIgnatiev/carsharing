package com.vakoms.meshly.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.vakoms.meshly.constants.Constants.TIME_DAY;
import static com.vakoms.meshly.constants.Constants.TIME_HOUR;
import static com.vakoms.meshly.constants.Constants.TIME_MINUTE;
import static com.vakoms.meshly.constants.Constants.TIME_MONTH;
import static com.vakoms.meshly.constants.Constants.TIME_WEEK;

/**
 * Created by Oleh Makhobey on 17.04.2015.
 * tajcig@ya.ru
 */
public class TimeFormatterUtil {

       public static final String WEEK = "w";
       public static final String DAY  = "D";
       public static final String HOUR = "H";
       public static final String MINUTE = "m";
       public static final String SECOND = "s";
       public static final String MONTH = "M";

       public static final String MONTH_DAY = "MMMM d";
       public static final String HOURS_MINUTES_SECONDS  = "HH:mm:ss";

       private static SimpleDateFormat dateFormat;

   static {
        dateFormat = new SimpleDateFormat(SECOND, Locale.UK);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }


      //  private static final SimpleDateFormat dateInFormatterClean = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    2015-05-26T10:59:33.811Z
        public static String format(long timeInMillis , String pattern) {
            dateFormat.applyPattern(pattern);
            return  dateFormat.format(new Date(timeInMillis));
        }

    private static String getTimeRemaining(long _timeMillis){

        if(_timeMillis < TIME_MINUTE){
            String second = TimeFormatterUtil.format(_timeMillis, TimeFormatterUtil.SECOND);
            return (second.equals("1") ? second + " second" : second + " seconds");
        }else if(_timeMillis < TIME_HOUR){
            String minute =  TimeFormatterUtil.format(_timeMillis, TimeFormatterUtil.MINUTE);
            return (minute.equals("1") ? minute + " minute" : minute + " minutes");
        }else if (_timeMillis < TIME_DAY){
            String hour = TimeFormatterUtil.format(_timeMillis, TimeFormatterUtil.HOUR);
            return (hour.equals("1") ? hour + " hour" : hour + " hours");
        }else if (_timeMillis < TIME_WEEK){

                    String day =TimeFormatterUtil.format(_timeMillis, TimeFormatterUtil.DAY);
            return (day.equals("1") ? day + " day" : day + " days");
        }else if(_timeMillis < TIME_MONTH) {
            String week = TimeFormatterUtil.format(_timeMillis, TimeFormatterUtil.WEEK);
            return (week.equals("1") ? week + " week" : week + " weeks");
        }else{
            String month = TimeFormatterUtil.format(_timeMillis, TimeFormatterUtil.MONTH);
            return (month.equals("1") ? month+ " month" : month+ " months");
        }

    }




    public static String parseTime(long timeInSeconds){
        long timeDifference = System.currentTimeMillis() - (timeInSeconds *1000);
        if(timeDifference > 0){
            return getTimeRemaining(timeDifference);
        }else {
            return getTimeRemaining(-timeDifference);
        }
    }


    public static long parseDateFromServer(String _dateFromServer){

        dateFormat.applyPattern("yyyy-MM-dd'T'HH.mm.ss.SSSZ");
        try {
        return dateFormat.parse(_dateFromServer).getTime();
        }catch (ParseException e){
            e.printStackTrace();
            return System.currentTimeMillis();
        }

    }

}
