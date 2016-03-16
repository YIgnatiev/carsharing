package shedule.budivnictvo.com.shedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import shedule.budivnictvo.com.shedule.objects.Day;
import shedule.budivnictvo.com.shedule.objects.Lesson;
import shedule.budivnictvo.com.shedule.objects.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.zip.Inflater;


public class ScheduleActivity extends Activity implements View.OnClickListener ,AdapterView.OnItemClickListener ,Animation.AnimationListener ,DialogInterface.OnShowListener{


    private final String CUSTOMER_SETTINGS = "customer_settings";

    private final String USER_TYPE = "customer_choice";
    private final String USER_ID = "customer_id";
    private final String USER_PASSWORD = "customer_password";




    private  String PASSWORD_ADMIN = "99999";
    private  String PASSWORD_TEACHER = "1";
    private  String PASSWORD_CHILD = "1";
    private  String PASSWORD_CONSUMER = "1";

    private String MASTER_CODE = "1914";

    private final int USER_CHILD =    0;
    private final int USER_TEACHER =  1;
    private final int USER_CONSUMER = 2;
    private final int USER_ADMIN =    3;

    private ArrayList<Day> month =null;
    private int offset;

    private int WEEK_MODE = 100;
    private int MONTH_MODE = 200;

    private int MODE ;



    private AsyncRequest request;
    private WeekAdapter weekAdapter;
    private ListView listView;
    private GridView gridView;
    private LinearLayout linearLayout;
    private ImageView leftArrow;
    private ImageView rightArrow;
    private ImageView downArrow;
    private TextView tvMonth;
    private boolean isAuthorised = false;
    public static Calendar today;
    private LinearLayout weekLayout;
    private Day day;
    private Calendar currentDay;
    private Animation animation;
    private Animation animationUpDown;
    private User currentUser;
    private boolean isAdmin = false;
    private Toast customToast;
    private AlertDialog.Builder builder;
    private AlertDialog adDialog;
    private ArrayAdapter<String> mSpinnerAdapter;
    private EditText edPassword;
    private EditText edID;
    private Spinner spinner;
    private ArrayList<String> mUserArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        registerBroadcast();


        setContentView(R.layout.activity_shedule);
        initUI();
        MODE = WEEK_MODE;
        setListeners();
        mUserArray = new ArrayList<String>();
        initDate();
        initToast();
        initWeekAdapter(month);

        currentUser = readPreferences();
        createDialog();
        if(!checkForPassword(currentUser)){
            adDialog.show();
        }else{
            loadSchedule();
        }

    }



    private void initUI(){
        listView = (ListView) findViewById(R.id.listview);
        gridView = (GridView) findViewById(R.id.gridView);
        linearLayout = (LinearLayout)findViewById(R.id.emptyView);
        leftArrow  = (ImageView)findViewById(R.id.ivLeftArrow);
        rightArrow = (ImageView)findViewById(R.id.ivRightArrow);
        downArrow = (ImageView)findViewById(R.id.ivDownArrow);
        weekLayout = (LinearLayout)findViewById(R.id.linLayoutWeek);
        tvMonth    = (TextView)findViewById(R.id.tvMonth);
        animation = new AnimationTranslate(AnimationTranslate.RIGHT);
        animationUpDown = new AnimationUpDown(AnimationUpDown.UP);
    }

    private void initDate(){
        today = new GregorianCalendar();
        currentDay = new GregorianCalendar();


        if(MODE == MONTH_MODE) {
            month = getMonthArray(currentDay);
        }else {
            month = getWeekArray(currentDay);
        }
        setMonth(currentDay, tvMonth);


    }

    private void setMonth(Calendar calendar , TextView textView){
        String month;
        switch (calendar.get(Calendar.MONTH)){
            case 0: month = getString(R.string.january);
                break;
            case 1: month = getString(R.string.february);
                break;
            case 2: month = getString(R.string.march);
                break;
            case 3: month = getString(R.string.april);
                break;
            case 4: month = getString(R.string.may);
                break;
            case 5: month = getString(R.string.june);
                break;
            case 6: month = getString(R.string.july);
                break;
            case 7: month = getString(R.string.august);
                break;
            case 8: month = getString(R.string.september);
                break;
            case 9: month = getString(R.string.october);
                break;
            case 10: month = getString(R.string.november);
                break;
            default: month = getString(R.string.december);
                break;
        }
        textView.setText(month);
    }

    private void createDialog(){

        View viewGroup = getLayoutInflater().inflate(R.layout.dialog_textview, new LinearLayout(this));
        edID = (EditText)viewGroup.findViewById(R.id.et_dialog);
        edPassword = (EditText) viewGroup.findViewById(R.id.et_password);
        spinner = (Spinner)viewGroup.findViewById(R.id.spinner);
        mUserArray.add(getString(R.string.pupil));
        mUserArray.add(getString(R.string.parent));
        mSpinnerAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, mUserArray);
        spinner.setAdapter(mSpinnerAdapter);
        builder = new AlertDialog.Builder(this);
        builder.setView(viewGroup).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ScheduleActivity.this, "Canceled", Toast.LENGTH_SHORT).show();

            }
        });
        adDialog = builder.create();
        adDialog.setOnShowListener(this);
    }

    private void initToast(){
        customToast = new Toast(this);
        View layout = getLayoutInflater().inflate(R.layout.toast_view , (ViewGroup)findViewById(R.id.rl_toast));
        customToast.setView(layout);
        customToast.setDuration(Toast.LENGTH_SHORT);
    }
    private void setListeners(){
        leftArrow.setOnClickListener(this);
        rightArrow.setOnClickListener(this);
        downArrow.setOnClickListener(this);
        animation.setAnimationListener(this);
        animationUpDown.setAnimationListener(this);
    }

    private String getURL(User user){
        String URL = "select d.id_child, d.id_consumer, r.KOD_prep,  \n" +
                "        r.id_gruppa, g.num, r.date_urok, r.times_urok, g.time_min,   \n" +
                "        g.KOD_kurs, k.nazvan nazvan_kurs, p.kod, p.nazvan nazvan_prep, r.id_room  \n" +
                "  from dogovor d  \n" +
                "   left join gruppa g on g.id=d.id_gruppa  \n" +
                "   left join kurs   k on k.kod=g.KOD_kurs  \n" +
                "   left join t_rasp r on r.id_gruppa=d.id_gruppa  \n" +
                "   left join prep   p on p.kod=r.KOD_prep  \n" +
                "where r.date_urok is not null and g.id is not null   \n" +
                getUserFilter(user.getUserType(), user.getUserId())+ " \n" +
                "and r.date_urok  >= '" + getBeginDate(currentDay) + "'  and r.date_urok <= '" + getEndDate(currentDay) + "'\n" +
                " order by r.date_urok, r.times_urok";
        return URL;
    }

    private String getUserFilter(int user , int userId ){

        switch (user){
            case USER_ADMIN:
                    return " ";
            case USER_TEACHER:
                return "and r.KOD_prep = "    + userId ;
            case USER_CHILD:
                return "and d.id_child = "    + userId;
            case USER_CONSUMER:
                return "and d.id_consumer = " + userId;
            default:
                return "";
        }



    }

    private void initWeekAdapter(ArrayList<Day> week) {
        weekAdapter = new WeekAdapter(this, R.layout.week_item,week);
        gridView.setAdapter(weekAdapter);
        gridView.setOnItemClickListener(this);
    }

    private void setDayAdapter(Day day) {
        LessonAdapter adapter = new LessonAdapter(this, R.layout.list_item, day.getArrayOfLessons());
        listView.setAdapter(adapter);

    }



    private void registerBroadcast() {
        JsonReceiver receiver = new JsonReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppContext.BROADCAST_JSON);
        registerReceiver(receiver, filter);
    }
    //DATE METHODS



    public static int getDayOfMonth(Calendar calendar) {
    return calendar.get(Calendar.DAY_OF_MONTH);


    }

    private Day getDayFromMonthArray(int day){

        int postion = day + offset - 1;
        return month.get(postion);
    }

    private ArrayList<Day> getMonthArray(Calendar calendar){
        ArrayList<Day> initedMonth = new ArrayList<Day>();
        Calendar firstItem = (Calendar)calendar.clone();
        Calendar lastItem = (Calendar)calendar.clone();

        int currentMonth = calendar.get(Calendar.MONTH);
        firstItem.set(Calendar.DAY_OF_MONTH,1);

        int mondayOffset = firstItem.get(Calendar.DAY_OF_WEEK) - 2;
        if(mondayOffset == -1){
            mondayOffset = 6;
        }
        offset = mondayOffset;
        firstItem.add(Calendar.DAY_OF_MONTH, - mondayOffset);

        int lastDay = lastItem.getActualMaximum(Calendar.DAY_OF_MONTH);
        lastItem.set(Calendar.DAY_OF_MONTH, lastDay);

        int sundayOffset = lastItem.get(Calendar.DAY_OF_WEEK) - 2;
        if(sundayOffset == -1){
            sundayOffset = 6;
        }

        lastItem.add(Calendar.DAY_OF_MONTH,(6 - sundayOffset));

        while(true){
            Date date = new Date(firstItem.getTimeInMillis());
            Day currentDay = new Day(date);

            if(date.getMonth() != currentMonth){
                currentDay.setSkip(true);
            }

            initedMonth.add(currentDay);

            if (firstItem.equals(lastItem))
                break;
            firstItem.add(Calendar.DAY_OF_MONTH, 1);
        }

        return initedMonth;
    }

    private ArrayList<Day> getWeekArray(Calendar calendar){
        ArrayList<Day> initedWeek = new ArrayList<Day>();
        Calendar firstItem = (Calendar)calendar.clone();
        firstItem.set(Calendar.DAY_OF_WEEK , 2);

        for(int i = 0; i < 7; i++){
            initedWeek.add(new Day (firstItem.getTime()));
            firstItem.add(Calendar.DAY_OF_WEEK, 1);
        }






        return initedWeek;
    }


    private String getBeginDate(Calendar calendar){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar newDate = (Calendar)calendar.clone();
        String currentDate;
        if(MODE == MONTH_MODE) {
            int dayOfMonth = newDate.get(Calendar.DAY_OF_MONTH);
            newDate.add(Calendar.DAY_OF_MONTH, -(dayOfMonth - 1));
            currentDate = format.format(newDate.getTime());
        }else{
            newDate.set(Calendar.DAY_OF_WEEK, 2);
            currentDate = format.format(newDate.getTime());
        }
        return currentDate;
    }

    private String getEndDate(Calendar calendar){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar newDate = (Calendar)calendar.clone();
        String currentDate;
        if(MODE == MONTH_MODE) {
            int quantityDaysInMonth = newDate.getMaximum(Calendar.DAY_OF_MONTH);
            int dayOfMonth = newDate.get(Calendar.DAY_OF_MONTH);
            newDate.add(Calendar.DAY_OF_MONTH, +quantityDaysInMonth - dayOfMonth);
            currentDate = format.format(newDate.getTime());
        }else{
            newDate.set(Calendar.DAY_OF_WEEK , 7);
            newDate.add(Calendar.DAY_OF_MONTH, 1);
            currentDate  = format.format(newDate.getTime());
        }
        return currentDate;
    }



    ///Checking for internet
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    private void loadSchedule(){

        if ( isNetworkConnected()) {
            request = new AsyncRequest(this);
            request.execute(getURL(currentUser));
        }
    }

    private ArrayList<Day> jsonParser(JSONArray jsonArray , ArrayList<Day> week) {

        Day day = null;
        int currentDay = 0;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String date = jsonObject.getString(AppContext.LESSON_DATE);

                day = new Day(date);









                Lesson lesson = new Lesson();

                lesson.setClassroom(jsonObject.getString(AppContext.LESSON_CLASSROOM));
                lesson.setSubject(jsonObject.getString(AppContext.LESSON_SUBJECT));
                lesson.setBeginingTime(jsonObject.getString(AppContext.LESSON_START_TIME));
                lesson.setEndingTime(jsonObject.getString(AppContext.LESSON_END_TIME));
                if(MODE == MONTH_MODE) {
                    getDayFromMonthArray(day.getDayOfMonth()).addLesson(lesson);
                }else{
                    month.get(day.getDayOfWeek()).addLesson(lesson);
                }

            } catch (JSONException e) {

            }
        }
        return week;
    }

//  MENU METHODS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shedule, menu);

       return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
           case R.id.menu_settings:
                adDialog.show();
                return true;

            case R.id.message:
                Toast.makeText(this, "Message" , Toast.LENGTH_SHORT).show();
                return true;
            default:return false;
        }
    }

// PREFERENCES METHODS

    public User readPreferences(){
        SharedPreferences preferences =  getSharedPreferences(CUSTOMER_SETTINGS,Context.MODE_PRIVATE);
        User user = new User();
        user.setUserType(preferences.getInt(USER_TYPE, -1));
        user.setPassword(preferences.getString(USER_PASSWORD , null));
        user.setUserId(preferences.getInt(USER_ID , -1));

        return user;
    }

    public void writePreferences(User user){
        SharedPreferences sharedPreferences =  getSharedPreferences(CUSTOMER_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(USER_TYPE, user.getUserType());
        editor.putInt(USER_ID , user.getUserId());
        editor.putString(USER_PASSWORD , user.getPassword());
        editor.commit();
    }



    private String encryptPassword(String password) {
        MessageDigest md = null;
        String encryptedPassword;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        encryptedPassword = sb.toString();
        return  encryptedPassword;
    }



    private boolean checkForPassword(User user){




        if(user.getPassword() == null ||user.getUserId() == -1){
            adDialog.show();
            return false;
        }

        if(user.getPassword().equals("1914" ) && !isAdmin){
            mUserArray.add(getString(R.string.teacher));
            mUserArray.add(getString(R.string.admin));
            mSpinnerAdapter.notifyDataSetChanged();
            TextView toastText = (TextView)customToast.getView().findViewById(R.id.tv_toast);
            toastText.setText("Администратор");
            customToast.show();
            isAdmin = true;
        }
        String userPassword = null;
        switch (user.getUserType()){
            case USER_TEACHER:
                userPassword = encryptPassword(PASSWORD_TEACHER);
                break;

            case USER_ADMIN:
                userPassword = encryptPassword(PASSWORD_ADMIN);
                break;

            case USER_CHILD:
                userPassword = encryptPassword(PASSWORD_CHILD);
                break;

            case USER_CONSUMER:
                userPassword = encryptPassword(PASSWORD_CONSUMER);
                break;
        }
        String encrypted = encryptPassword(user.getPassword());
        boolean result = encrypted.equals(userPassword);
        if(!result && isAuthorised){

            customToast.show();
        }
        return result;
    }

//  ARROW BUTTONS LISTENER
    @Override
    public void onClick(View view) {
        request = new AsyncRequest(this);
        switch(view.getId()) {
            case R.id.ivLeftArrow:
                if(MODE == MONTH_MODE) {
                    currentDay.add(Calendar.MONTH, -1);
                }else{
                    currentDay.add(Calendar.WEEK_OF_YEAR,-1);
                }
                switchSchedule(currentDay);
                break;
            case R.id.ivRightArrow:
                if (MODE == MONTH_MODE) {
                    currentDay.add(Calendar.MONTH, 1);
                }else{
                    currentDay.add(Calendar.WEEK_OF_YEAR,1);
                }
                switchSchedule(currentDay);
                break;
            case R.id.ivDownArrow:

                if (MODE == MONTH_MODE){
                    MODE = WEEK_MODE;
                    downArrow.setImageResource(R.drawable.selector_arrow_down);
                    month = getWeekArray(currentDay);
                }else{
                    MODE = MONTH_MODE;
                    downArrow.setImageResource(R.drawable.selector_arrow_up);
                    month = getMonthArray(currentDay);
                }

                View listView = (View)gridView.getParent();
                listView.startAnimation(animationUpDown);




        }



    }

    public void switchSchedule(Calendar calendar){
        if (MODE == MONTH_MODE) {
            month = getMonthArray(currentDay);
        }else{
            month = getWeekArray(currentDay);
        }
        setMonth(currentDay,tvMonth);

        if (isNetworkConnected() && checkForPassword(currentUser)){

            request.execute(getURL(currentUser));
        }else{
            weekAdapter.notifyDataSetChanged();
        }
    }

    public void setCalendar(JSONArray jsonArray){
        if(MODE == MONTH_MODE) {
            month = getMonthArray(currentDay);
        }else {
            month = getWeekArray(currentDay);
        }
        jsonParser(jsonArray , month);
        initWeekAdapter(month);
    }
//  DAY BUTTON LISTENER
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        this.day = month.get(i);

        if (this.day.getArrayOfLessons().size() == 0){
            linearLayout.setVisibility(View.VISIBLE);
            TextView empty  = (TextView)linearLayout.getChildAt(0);
            empty.setText(getString(R.string.no_lessons));
        }else {
            linearLayout.setVisibility(View.GONE);
        }

        listView.startAnimation(animation);
        view.setPressed(true);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
    if(animation instanceof AnimationUpDown) {
        if (isNetworkConnected() && checkForPassword(currentUser)) {

            request.execute(getURL(currentUser));
        }
    }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        if (animation instanceof AnimationTranslate){
            setDayAdapter(this.day);
        }else {
            initWeekAdapter(month);
        }

    }

    public class JsonReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            JSONArray jsonArray = AppContext.getArray();
            setCalendar(jsonArray);

            linearLayout.setVisibility(View.GONE);
            if (MODE == MONTH_MODE) {
                if (currentDay.get(Calendar.MONTH) != today.get(Calendar.MONTH)) {
                    day = getDayFromMonthArray(1);
                } else {
                    day = getDayFromMonthArray(getDayOfMonth(currentDay));
                }
            }else{
                if( currentDay.get(Calendar.WEEK_OF_YEAR) != today.get(Calendar.WEEK_OF_YEAR)){
                    day = month.get(0);
                }else {
                    int dayIndex;
                    if (currentDay.get(Calendar.DAY_OF_WEEK)== 1){
                        dayIndex = 6;
                    }
                    else {
                        dayIndex = currentDay.get(Calendar.DAY_OF_WEEK ) - 2;
                    }
                    day = month.get(dayIndex);
                }

            }

            setDayAdapter(day);

        }
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        Button positive = adDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAuthorised  = true;
                if (edID.getText().length() == 0) {
                    edID.setBackgroundResource(R.drawable.dialog_background_error);
                    currentUser.setUserId(-1);
                } else {
                    edID.setBackgroundResource(R.drawable.dialog_background_selector);
                    currentUser.setUserId(Integer.parseInt(edID.getText().toString()));
                }
                if (edPassword.getText().length() == 0) {
                    edPassword.setBackgroundResource(R.drawable.dialog_background_error);
                    currentUser.setPassword(null);
                } else {
                    edPassword.setBackgroundResource(R.drawable.dialog_background_selector);
                    currentUser.setPassword(edPassword.getText().toString());
                }

                currentUser.setUserType(spinner.getSelectedItemPosition());
                if (checkForPassword(currentUser)){
                    writePreferences(currentUser);
                    adDialog.dismiss();
                    loadSchedule();
                }
            }
        });
    }
}
