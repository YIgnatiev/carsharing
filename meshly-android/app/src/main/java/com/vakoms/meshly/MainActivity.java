package com.vakoms.meshly;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.bugsnag.android.Severity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.vakoms.meshly.constants.Constants;
import com.vakoms.meshly.databases.MeshlyColumns;
import com.vakoms.meshly.databases.UserDAO;
import com.vakoms.meshly.databases.UserProvider;
import com.vakoms.meshly.fragments.AboutFragment;
import com.vakoms.meshly.fragments.chat.ChatFragment;
import com.vakoms.meshly.fragments.chat.InboxFragment;
import com.vakoms.meshly.fragments.edit_user.EditUserFragment;
import com.vakoms.meshly.fragments.events.EventsFragment;
import com.vakoms.meshly.fragments.events.EventsPageFragment;
import com.vakoms.meshly.fragments.events.UserListFragment;
import com.vakoms.meshly.fragments.opportunities.OpportunitiesFragment;
import com.vakoms.meshly.fragments.opportunities.WallFragment;
import com.vakoms.meshly.fragments.people.PeopleDetailFragment;
import com.vakoms.meshly.fragments.people.PeopleFragment;
import com.vakoms.meshly.gcm.RegistrationIntentService;
import com.vakoms.meshly.interfaces.AddMessageListener;
import com.vakoms.meshly.interfaces.InboxListener;
import com.vakoms.meshly.interfaces.VoidFunction;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.models.chat.ChatResponseModel;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.services.GCMService;
import com.vakoms.meshly.utils.Logger;
import com.vakoms.meshly.utils.P;
import com.vakoms.meshly.utils.ReactiveLocation;
import com.vakoms.meshly.views.ResideMenu;

import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.ActivityMainBinding;
import meshly.vakoms.com.meshly.databinding.DialogEnableLocationBinding;
import meshly.vakoms.com.meshly.databinding.ItemProfileBinding;
import retrofit.RetrofitError;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.vakoms.meshly.constants.Constants.EVENTS;
import static com.vakoms.meshly.constants.Constants.GCM_CHAT_ID;
import static com.vakoms.meshly.constants.Constants.GCM_MESSAGE;
import static com.vakoms.meshly.constants.Constants.GCM_MESSAGE_ID;
import static com.vakoms.meshly.constants.Constants.GCM_NAME;
import static com.vakoms.meshly.constants.Constants.GCM_TIMESTAMP;
import static com.vakoms.meshly.constants.Constants.GCM_USER_ID;
import static com.vakoms.meshly.constants.Constants.OPPORTUNITIES;
import static com.vakoms.meshly.constants.Constants.PEOPLE;


public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String SOCIAL_NETWORK_TAG = "SocialNetworkTag";
    public static final int LOCATION_PERMISSION_REQUEST = 9;


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    private static final String REGISTRATION_COMPLETE = "reg_complete";


    private final int PHOTO_PERMISSION =11;

    private VoidFunction mPermissionListener;

    private Bundle mBundle;

    private ResideMenu resideMenu;

    private BroadcastReceiver receiver;
    private InboxListener inboxListener;
    private AddMessageListener addMessageListener;



    private Subscription mLocationSubscription;
    private Subscription mUpdateLocationSubscription;

    ReactiveLocation mReactiveLocation;


    private AlertDialog locationDialog;
    private UserDAO mDao;
    public UserMe mUser;


    public ActivityMainBinding b;
    private ItemProfileBinding bProfile;

    public final ObservableInt messagesCount = new ObservableInt(0);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDao = UserDAO.getInstance();
        b = DataBindingUtil.setContentView(this, R.layout.activity_main);
        resideMenu = new ResideMenu(this);
        resideMenu.attachToActivity(this);
        resideMenu.setScaleValue(0.8f);

        checkDeepLinkingAction();

        setUpReceiver();
        getGCMToken();


        checkIfLoggedWithAnotherDevice();
        checkForPermission();
//        setUpReceiver();
//        getGCMToken();
        checkForMessage();
        getLoaderManager().restartLoader(1, null, this);

    }


    protected void checkForPermission(){

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED)checkGPS();
        else if(permissionCheck == PackageManager.PERMISSION_DENIED)requstForPermission();

    }




    private void requstForPermission(){

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                             Manifest.permission.ACCESS_FINE_LOCATION},
                             LOCATION_PERMISSION_REQUEST);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) checkGPS();

                else new AlertDialog.Builder(this, R.style.MyDialogTheme).setMessage("Meshly cannot receive your location now").show();

                break;

            case  PHOTO_PERMISSION:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    mPermissionListener.apply();

                else
                    new AlertDialog.Builder(this, R.style.MyDialogTheme).setMessage("Meshly cannot take your photo").show();

                break;
            }

    }













    public void checkForCameraPermission(VoidFunction function){
        int permissionCheck  = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA");


        if (permissionCheck == PackageManager.PERMISSION_GRANTED)  function.apply();
        else if(permissionCheck == PackageManager.PERMISSION_DENIED) {
            mPermissionListener = function;
            requstForCameraPermission();
        }

    }




    private void requstForCameraPermission(){

        ActivityCompat.requestPermissions(this,
                new String[]{"android.permission.CAMERA", "android.hardware.camera", "android.hardware.camera.autofocus"}, PHOTO_PERMISSION);


    }







    public void refreshUser(UserMe user){
        bProfile.setUser(user);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(GCMService.GCM_RESULT));
    }


    @Override
    protected void onResume() {
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(receiver, new IntentFilter(REGISTRATION_COMPLETE));


        super.onResume();


    }

private void checkIfLoggedWithAnotherDevice(){
    if(P.isLoggedWithAnotherDevice()) showLogoutMessageWithForceLogout();
}

    @Override
    protected void onStop() {
        super.onStop();


        if(mLocationSubscription !=  null)mLocationSubscription.unsubscribe();
        if(mUpdateLocationSubscription != null)mUpdateLocationSubscription.unsubscribe();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);


    }

    private void checkForMessage() {

        if (getIntent().getExtras() != null) {
            mBundle = getIntent().getExtras();
            String action = mBundle.getString("action");
            if (action == null) return;
            getIntent().putExtra("action", "confirmedAction");


            switch (action) {
                case "chat":
                    handlePushNotificationChat(mBundle);
                    break;
                case "event":
                    handlePushNotificationEvent(mBundle);
                    break;
                case "confirmedAction":
                    Logger.v("chat", "push notification has been confirmed.");
                    break;
                default:
                    Logger.e("chat", "unknown action + " + action + " + from push notification");
                    ((MeshlyApplication)getApplication()).notifyError(Severity.ERROR, "unknown action + " + action + " + from push notification");

                    break;
            }


        } else {
            Logger.v("chat", "intent or extras is null");
        }
    }


    private void checkDeepLinkingAction() {

        String action = getIntent().getStringExtra(Constants.DEEPLINKING_ACTION);

        if (action != null && action.contains("/#")) {
            if (action.startsWith("http://")) {
                action = action.substring("http://".length());
            }
            String[] array = action.split("/");
            switch (array.length) {
                case 2:
                    startFragment(array[1], "");
                    break;
                case 3:
                    startFragment(array[1], array[2]);
                    break;
                default:
                    startFragment(OPPORTUNITIES, "");
                    openMainMenu();
            }
        } else {
            startFragment(OPPORTUNITIES, "");
            openMainMenu();
        }
    }


    private void handlePushNotificationEvent(Bundle bundle) {
        EventsFragment eventFrag = new EventsFragment();
        eventFrag.setScrollToId(bundle.getString("eventId"));
        changeFragment(eventFrag);
    }

    private void handlePushNotificationChat(final Bundle bundle) {
        String userId = bundle.getString("user_id");
        Subscription userSubscription = RetrofitApi
                .getInstance().user()
                .getUserWithExtraData(userId)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .map(list -> list.get(0))
                .map(NewUser::getId)
                .subscribe(this::startChatFragment, this::handleError);

        subscriptions.add(userSubscription);

    }

    private void startChatFragment(String userId) {
        replaceFragment(ChatFragment.newInstance(mBundle.getString("chat_id"), userId));
    }


    //location methods


    private void checkGPS() {
        if (!isGpsEnabled()) {
            DialogEnableLocationBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_enable_location, null, false);
            binding.setListener(this);
            locationDialog = new AlertDialog
                    .Builder(this, R.style.MyDialogTheme)
                    .setView(binding.getRoot())
                    .setCancelable(false)
                    .create();
            locationDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            locationDialog.show();
        } else {
            updateLocation();
        }
    }

    public boolean isGpsEnabled() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = false;
        try {
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception ex) {
            String gps =ex.getMessage();
            Log.v("location", "gps " +gps);
        }


        return isGpsEnabled;
    }


    private void updateLocation() {
        mReactiveLocation =  ReactiveLocation.getInstance(this);
        if(P.GPS.getLocation() == null) {
            mReactiveLocation.getFastLocation().subscribe(this::handleLocationSuccess, this::onLocationFailure);
        }else {
            mLocationSubscription = mReactiveLocation
                    .subscribeForLocationUpdates()
                    .timeout(10, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleLocationSuccess, this::onLocationFailure);
        }
    }




    private void handleLocationSuccess(Location location) {
        P.GPS.saveLocation(location);
        BaseActivity.TriggerRefresh(new Bundle());
      mUpdateLocationSubscription =
         RetrofitApi
                 .getInstance()
                 .user()
                 .updateUserLocation(location)
                 .subscribeOn(Schedulers.newThread())
                 .timeout(3, TimeUnit.SECONDS)
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(this::onLocationSuccess, this::handleError);


    }

    private void onLocationSuccess(BaseResponse response){

    }

    private void onLocationFailure(Throwable throwable){
       if(throwable != null){
           throwable.getMessage();
       }
    }





    private void setUpReceiver() {


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Fragment frag = getFragmentManager().findFragmentById(R.id.flContainer);
                if (frag instanceof ChatFragment) {
                    addMessageListener.addMessage(

                            intent.getExtras().get(GCM_USER_ID).toString(),
                            intent.getExtras().get(GCM_TIMESTAMP).toString(),
                            intent.getExtras().get(GCM_NAME).toString(),
                            intent.getExtras().get(GCM_MESSAGE).toString(),
                            intent.getExtras().get(GCM_CHAT_ID).toString(),
                            intent.getExtras().get(GCM_MESSAGE_ID).toString());
                } else {
//                    showNewIncome();
                    P.saveNewMessage("message");
                }
                if (frag instanceof InboxFragment) {
                    inboxListener.onInboxUpdate();
                }
            }
        };
    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }


    public void onOpportunitiesClicked(View view) {
        Fragment fragment = getFragmentManager().findFragmentByTag(WallFragment.class.getName());
        if (fragment == null) changeFragment(new WallFragment());

        resideMenu.closeMenu();

    }

    public void onPeopleClicked(View view) {
        Fragment fragment = getFragmentManager().findFragmentByTag(PeopleFragment.class.getName());
        if (fragment == null) changeFragment(new PeopleFragment());

        resideMenu.closeMenu();

    }

    public void onEventsClicked(View view) {
        Fragment fragment = getFragmentManager().findFragmentByTag(EventsFragment.class.getName());

        if (fragment == null) changeFragment(new EventsFragment());
        resideMenu.closeMenu();

    }

    public void onMessagesClicked(View view) {
        Fragment fragment = getFragmentManager().findFragmentByTag(InboxFragment.class.getName());

        if (fragment == null) changeFragment(new InboxFragment());
        resideMenu.closeMenu();

    }

    public void onAboutClicked(View view) {



        checkGPS();
        Fragment fragment = getFragmentManager().findFragmentByTag(AboutFragment.class.getName());

        if (fragment == null) changeFragment(new AboutFragment());
        resideMenu.closeMenu();

    }

    public void onEditUserClicked(View view) {
        changeFragment(new EditUserFragment());
        resideMenu.closeMenu();
    }


    public void onFollowingClicked(View view) {

        changeFragment(UserListFragment.getUserFollowersInstance());
        resideMenu.closeMenu();

    }


    public void onEnableLocationClicked(View view) {
        locationDialog.dismiss();
        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(myIntent);
    }


    private void startFragment(String _action, String _id) {
        switch (_action) {
            case OPPORTUNITIES:
                if (_id.isEmpty()) changeFragment(new WallFragment());
                else changeFragment(OpportunitiesFragment.getInstance(_id));
                break;
            case PEOPLE:
                if (_id.isEmpty()) changeFragment(new PeopleFragment());
                else changeFragment(PeopleDetailFragment.getInstance(_id));
                break;
            case EVENTS:
                if (_id.isEmpty()) changeFragment(new EventsFragment());
                else changeFragment(EventsPageFragment.instance(_id));
                break;
            default:
                changeFragment(new WallFragment());
        }

        resideMenu.closeMenu();

    }


    public void logout() {
        P.clearData();
        logOutAdapter(this);
        getContentResolver().delete(UserProvider.USER_ME_URI, null, null);

        Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        replaceFragment(targetFragment);
    }

    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    public void openMainMenu() {
        resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
    }


    private void setUpMenu(UserMe user) {
        bProfile = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_profile, null, false);
        bProfile.setActivity(this);


        resideMenu.addMenuItem(bProfile.getRoot(), ResideMenu.DIRECTION_LEFT);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
    }


    public void setUpProfile(UserMe user) {
        сreateSyncAccount(this, user.getUsername());
        TriggerRefresh(new Bundle());
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {

            if (!resideMenu.isOpened())
                openMainMenu();
            return true;
        }

        // let the system handle all other key events
        return super.onKeyDown(keyCode, event);
    }


    private long lastTimePressed ;
    @Override
    public void onBackPressed() {

        if (getBackStackCount() > 1 && !resideMenu.isOpened()) {
            getFragmentManager().popBackStack();
        } else if (!resideMenu.isOpened()) {
            openMainMenu();
        } else if (lastTimePressed > System.currentTimeMillis() - 2000){
                finish();
        }
        else{
            lastTimePressed = System.currentTimeMillis();
            Snackbar.make(bProfile.getRoot(), "Press again to close meshly", Snackbar.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Fragment fragment = getFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);
        if (fragment != null && requestCode != 131075) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }


    }


    public void setInboxListener(InboxListener listener) {
        inboxListener = listener;
    }

    public void setAddMessageistener(AddMessageListener listener) {
        addMessageListener = listener;
    }


    @Override
    public void showProgress() {

        b.progressBar.setVisibility(View.VISIBLE);
        b.progressBackground.setVisibility(View.VISIBLE);

    }

    @Override
    public void stopProgress() {
        b.progressBar.setVisibility(View.INVISIBLE);
        b.progressBackground.setVisibility(View.INVISIBLE);

    }


    ///Chat

    public void checkNewMessages(UserMe _user) {

        Subscription chatSubscription = RetrofitApi
                .getInstance()
                .chat()
                .getUserChats(_user.getId(), true)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::onCheckMessageSuccess, this::handleError);
        subscriptions.add(chatSubscription);
    }

    private void onCheckMessageSuccess(ChatResponseModel chatResponseModel) {

        if (chatResponseModel.hasNewMessages(mUser.getId())) {
            messagesCount.set(chatResponseModel.getAllIDForNewOtherMsg(mUser.getId()).size());

            for (String id : chatResponseModel.getAllIDForNewConnectionsMsg(mUser.getId())) {
                if (!P.getNewMessage().equals(id)) {
                    P.saveNewMessage(id);
                }
            }
            for (String id : chatResponseModel.getAllIDForNewOtherMsg(mUser.getId())) {
                if (!P.getNewMessage().equals(id)) {
                    P.saveNewMessage(id);
                }
            }
        } else {
            messagesCount.set(0);
        }
    }




    public void getGCMToken() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (checkPlayServices() && !sharedPreferences.contains(RegistrationIntentService.GCM_TOKEN)) {//no need to register token on each start of the app
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            this.startService(intent);
        }
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {

            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("GCM", "This device is not supported.");
            }
            return false;
        }
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] columns = MeshlyColumns.getColumns(UserMe.class);
        return new CursorLoader(this, UserProvider.USER_ME_URI, columns, null, null, null);

    }


    private boolean isInited;
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        UserMe user = mDao.getUserMe(data);
        mUser = user;
        if(!isInited){

        setUpMenu(user);
        setUpProfile(user);
        checkNewMessages(user);
            isInited = true;
        }
        bProfile.setUser(mUser);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //mAdapter.changeCursor(null);
    }



    public void handleError(Throwable throwable) {

        stopProgress();
        Snackbar snackbar =  Snackbar.make(bProfile.getRoot(),"Network error", Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.error));
        snackbar.show();



        try {
            RetrofitError error = (RetrofitError) throwable;
            int status = error.getResponse().getStatus();
            if(status == 403){

                 showLogoutMessageWithForceLogout();
            }
        }catch (Exception ex){

            Log.v("sync" , "unknow error " + ex.getMessage());
        }

    }




    public void showLogoutMessageWithForceLogout() {

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.logout_LoginError))
                .setMessage(getString(R.string.logout_multiply_signin))
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {logout();})
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}