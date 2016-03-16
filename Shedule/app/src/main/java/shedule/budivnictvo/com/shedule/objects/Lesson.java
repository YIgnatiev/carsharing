package shedule.budivnictvo.com.shedule.objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Администратор on 25.11.2014.
 */
public class Lesson {
    private Date beginDate;
    private int lessonDuration;

    private String subject;
    private String classroom;

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEndingTime() {

        long endLesson = beginDate.getTime() + lessonDuration*60*1000;
        return parseDate(new Date(endLesson));
    }

    public void setEndingTime(String endingTime) {
        lessonDuration = Integer.parseInt(endingTime);
    }

    public String getBeginingTime() {
        return parseDate(beginDate);
    }

    public void setBeginingTime(String beginingTime) {
        beginingTime = beginingTime.substring(0,8);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            beginDate = format.parse(beginingTime);
        }catch(ParseException e){
            e.printStackTrace();
        }

    }

   public String parseDate (Date date){
       SimpleDateFormat format = new SimpleDateFormat("HH:mm");
       return format.format(date);
   }

}
