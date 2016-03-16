package shedule.budivnictvo.com.shedule;

import android.app.Application;
import org.json.JSONArray;

public class AppContext extends Application {

    public static final String LESSON_START_TIME = "times_urok";
    public static final String LESSON_END_TIME = "time_min";
    public static final String LESSON_CLASSROOM = "num";
    public static final String LESSON_SUBJECT = "nazvan_kurs";
    public static final String LESSON_DATE = "date_urok";

    public static final String BROADCAST_JSON = "budivnictvo.com.broadcastJson";

    private static JSONArray jsonArray;

    public static void setArray(JSONArray jsonArray){
        AppContext.jsonArray = jsonArray;
    }
    public static JSONArray getArray(){
        return jsonArray;
    }

}
