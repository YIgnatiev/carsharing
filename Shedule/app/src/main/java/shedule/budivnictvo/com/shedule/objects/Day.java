package shedule.budivnictvo.com.shedule.objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Администратор on 25.11.2014.
 */
public class Day  {
    private ArrayList<Lesson> workingday;
    private boolean skip;


    private Date date;
    public Day (Date date){
        this.date =date;
        workingday = new ArrayList<Lesson>();
    }
    public Day (String strDate){
        workingday = new ArrayList<Lesson>();
        parseDate(strDate);
    }
    public void addLesson(Lesson lesson){
        workingday.add(lesson);
    }
    public Lesson getLesson (int index){
        return workingday.get(index);
    }
    public void parseDate(String strDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
       try {
           date = format.parse(strDate);
       }catch (ParseException e){
           e.printStackTrace();
       }
    }

    public int getDayOfWeek(){
        int day = date.getDay();

        if (day == 0){
            day = 6;
        }else{
            day--;
        }

       return day;
    }

    public int getDayOfMonth(){
        return date.getDate();
    }

    public ArrayList<Lesson> getArrayOfLessons(){
        return this.workingday;
    }

    public Date getDate (){
        return date;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }


}
