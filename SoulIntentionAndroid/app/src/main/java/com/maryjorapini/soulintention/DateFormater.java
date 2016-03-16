package com.maryjorapini.soulintention;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Администратор on 27.11.2014.
 */
public class DateFormater {
    public static Date dateParser(String _strDate){

        Date date = null;
        SimpleDateFormat format = new  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            date = format.parse(_strDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;

    }

    public static String dateFormat(Date _date){
        SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy");
        String formatedDate = format.format(_date);
        return formatedDate;
    }
}
