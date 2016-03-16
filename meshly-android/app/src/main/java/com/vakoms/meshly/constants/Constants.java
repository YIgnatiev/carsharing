package com.vakoms.meshly.constants;

//import meshly.vakoms.com.meshly.BuildConfig;

import meshly.vakoms.com.meshly.BuildConfig;

/**
 * Created by taras.melko on 9/4/14.
 */
public class Constants {


    public static final String OPPORTUNITIES = "#opportunities";
    public static final String PEOPLE = "#people";
    public static final String EVENTS       = "#events";
    public static final String MESSAGES = "meshly.inbox";
    public static final String ABOUT        = "meshly.about";
    public static final String EDIT_PROFILE = "meshly.edit_profile";

// sync adapter
    public static final String TYPE =  "sync_type";
    public static final int TYPE_USER_ME = 0;
    public static final int TYPE_OPPORTUNITIES = 1;
    public static final int TYPE_PEOPLE = 2;
    public static final int TYPE_EVENTS  = 3;
    public static final int TYPE_MY_OPPORTUNITIES = 4;
    public static final String PEOPLE_PAGINATION ="people_pagination";
    public static final String PEOPLE_SEARCH = "people_search";
    public static final String WALL_PAGE = "wall_page";




// end sync adapter


    public static final String LOCATION_GEONAME_API = "http://api.geonames.org";


    public static final String STATUS_SAVE  = "status.save";
    public static final String STATUS_CANCEL  = "status.cancel";

    public static final String DEEPLINKING_ACTION = "meshly_deeplinking_action";

    public static final String GCM_MESSAGE_ID = "message_id";
    public static final String GCM_MESSAGE = "message";
    public static final String GCM_TIMESTAMP = "time";
    public static final String GCM_CHAT_ID = "chat_id";
    public static final String GCM_NAME = "name";
    public static final String GCM_USER_ID = "user_id";

    public static final int DELAY = 100;

    public static final String CLIENT_ID = "S4P6Z9WRWH";
    public static final String CLIENT_SECRET = "2QUuhFR56HPm1dqldpV6wgftqdQcuB";
    public static final String PROJECT_GCM_NUMBER = "132638428377";

    public static final String API_URL = BuildConfig.API_URL;
//    public static final String API_URL = "https://dev.api.meshly.io/";
    public static final String SEGMENT_API_KEY = BuildConfig.SEGMENT_API_KEY;
//    public static final String SEGMENT_API_KEY = "hGtAnbKKZx2lsygaJGF6fnlbqc6ygf98";

    public static final String REDIRECT_URI = API_URL + "successauth";




    public static final String DEBUG = "DEBUG";

    public static final String AUTHORIZATION_URL = API_URL + "auth/linkedin/login";

    public static final String PRIVACY_POLICY_URL = "http://meshly.io";

    public static final String SHARE_TEXT = "I just started using meshly. Join at http://mesh.ly and check out my profile!";
    public static final int FINDER_FETCH_LIMIT = 10;
    public static final int FINDER_PROFILES_LEFT_BEFORE_NEW_FETCH = 5;
    public static final int NETWORK_DEFAULT_SEARCH_RANGE = 18000000; //18000 km, it means everywhere
    public static String[] privacyCasesForRequest = new String[]{"all", "connections", "nobody"};

    public static final long TIME_SECOND = 1000;

    public static final long TIME_MINUTE = TIME_SECOND * 60;

    public static final long TIME_HOUR = TIME_MINUTE * 60;

    public static final long TIME_DAY = TIME_HOUR * 24;

    public static final long TIME_WEEK = TIME_DAY * 7;

    public static final long TIME_MONTH = TIME_DAY * 30;


    public static final String LINKED_IN = "linked_in_tag";
    public static final String TWITTER = "twitter_tag";
    public static final String FACEBOOK = "facebook_tag";
    public static final String XING = "xing_tag";
    public static final String VK = "vk_tag";
    public static final String VIADEO = "viadeo_tag";

    public static final int CROPPED_IMAGE_HEIGHT = 500;
    public static final int CROPPED_IMAGE_WIDTH = 500;

    public static final String FACEBOOK_AVATAR_URL = "http://graph.facebook.com/%s/picture?type=large";

    public static final String FOLLOW = "follow";
    public static final String UNFOLLOW = "unfollow";

}