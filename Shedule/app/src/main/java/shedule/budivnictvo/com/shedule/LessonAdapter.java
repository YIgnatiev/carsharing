package shedule.budivnictvo.com.shedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import shedule.budivnictvo.com.shedule.objects.Day;
import shedule.budivnictvo.com.shedule.objects.Lesson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Администратор on 10.11.2014.
 */
public class LessonAdapter extends ArrayAdapter<Lesson> {
    private ArrayList<Lesson> lessons;
    private Context context;
    private int resource;
    public LessonAdapter(Context context, int resource, ArrayList<Lesson>  lessons) {
        super(context, resource, lessons);
        this.context = context;
        this.resource = resource;
        this.lessons = lessons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resource,parent, false);

        Lesson lesson = lessons.get(position);

        TextView txtStartLesson = (TextView)convertView.findViewById(R.id.starttime);
        txtStartLesson.setText(lesson.getBeginingTime());

        TextView txtEndLesson = (TextView)convertView.findViewById(R.id.endtime);
        txtEndLesson.setText(lesson.getEndingTime());

        TextView txtSubject = (TextView)convertView.findViewById(R.id.txt_subject);
        txtSubject.setText(lesson.getSubject());

        TextView txtClassroom = (TextView)convertView.findViewById(R.id.txt_classroom);
        txtClassroom.setText(lesson.getClassroom());
        return convertView;
    }
}
