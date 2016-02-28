package com.budivnictvo.rssnews.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Администратор on 27.11.2014.
 */
public class DateFormater {
    public static Date dateParser(String _strDate){

        Date date = null;
        SimpleDateFormat format = new  SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z" , Locale.US);
        try {
            date = format.parse(_strDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;

    }

    public static String dateFormat(Date _date){
        SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMM yyyy 'г.'");
        String formatedDate = format.format(_date);
        return formatedDate;
    }
}
