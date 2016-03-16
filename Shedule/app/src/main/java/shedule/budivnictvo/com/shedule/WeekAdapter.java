package shedule.budivnictvo.com.shedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import shedule.budivnictvo.com.shedule.objects.Day;

import java.util.List;

/**
 * Created by Администратор on 11.11.2014.
 */
public class WeekAdapter extends ArrayAdapter{

    private List<Day> data;
    private Context context;
    private int resource;
    private boolean isInited = false;

    public WeekAdapter(Context context, int resource, List<Day> data) {
        super(context,  resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resource,parent, false);
        Day day = data.get(position);
        TextView txtStartLesson = (TextView)convertView.findViewById(R.id.txt_day_of_week);
       if(!isInited) {
           switch (day.getDayOfWeek()) {
               case 1:
                   txtStartLesson.setText(context.getResources().getString(R.string.monday));
                   break;
               case 2:
                   txtStartLesson.setText(context.getResources().getString(R.string.tuesday));
                   break;
               case 3:
                   txtStartLesson.setText(context.getResources().getString(R.string.wednesday));
                   break;
               case 4:
                   txtStartLesson.setText(context.getResources().getString(R.string.thursday));
                   break;
               case 5:
                   txtStartLesson.setText(context.getResources().getString(R.string.friday));
                   break;
               case 6:
                   txtStartLesson.setText(context.getResources().getString(R.string.saturday));
                   break;
               case 0:
                   txtStartLesson.setText(context.getResources().getString(R.string.sunday));
                   isInited = true;
               default:
                   break;
           }
       }else{
           txtStartLesson.setVisibility(View.INVISIBLE);
       }

        ImageView ivDate = (ImageView)convertView.findViewById(R.id.ivDate);
        TextView tvDate = (TextView)convertView.findViewById(R.id.tvDate);
        tvDate.setText(String.valueOf(day.getDayOfMonth()));
        ivDate.setFocusable(false);

        if (day.getArrayOfLessons().size() != 0){
            ivDate.setImageResource(R.drawable.btn_selector_with_lessons);
        }
        if(day.isSkip()){
            ivDate.setVisibility(View.INVISIBLE);
            tvDate.setVisibility(View.INVISIBLE);

        }
        if(ScheduleActivity.today.getTimeInMillis() == day.getDate().getTime()){
            ivDate.setImageResource(R.drawable.btn_red);
        }



        return convertView;
    }

}
