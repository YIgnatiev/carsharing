package com.maryjorapini.soulintention.core;

/**
 * Created by Sasha on 20.11.2014.
 */
public abstract class APIConstants {
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ SERVER URLs ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static final String SERVER_URL                       = "http://soulintention.elasticbeanstalk.com";
//    public static final String SERVER_URL                       = "http://134.249.164.53:7791";

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ API urls ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-----
    public static final int API_START_MOBILE                    = 1;
    public static final int API_END_MOBILE                      = 2;
    public static final int API_POST                            = 3;
    public static final int API_POST_FAVOURITE                  = 4;
    public static final int API_GET_FAVOURITE                   = 5;
    public static final int API_FAVOURITE_ID                    = 6;


    public static final String URL_START_MOBILE                 = "/startMobile";
    public static final String URL_END_MOBILE                   = "/endMobile";
    public static final String URL_POST                         = "/post";
    public static final String URL_FAVOURITE                    = "/favourite";
    public static final String URl_FAVOURITE_ID                 = "/favouriteId";
    public static final String API_ABOUT                        = "/about";
    public static final String API_SEARCH                       = "/searchPost";
    public static final String API_RATE                         = "/rate";
    public static final String API_DEVICE_TOKEN                 = "/deviceToken";




    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ DEFAULTS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static final String SEPARATOR_SLASH                  = "/";
    public static final String SEPARATOR_EQUALS                 = "=";
    public static final String SEPARATOR_APMERSANT              = "&";
    public static final String SEPARATOR_QUESTION               = "?";


    public static final String ERROR_RESULT                     = "error";
    public static final String ERROR_DESCRIPTION                = "error_description";
    public static final String ERROR_CODE                       = "error_code";
    public static final String OK_RESULT                        = "ok";
}
