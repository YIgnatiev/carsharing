package pro.theboard.constants;

import com.lapioworks.cards.BuildConfig;

/**
 * Created by Admin on 22.03.2015.
 */
public class Constants {

    public static final String OVERRIDED_FONT_PATH = "roboto_regular.ttf";

//    public static final String BASE_URL            = "http://app.theboard.pro/api/v1";
//    public static final String BASE_URL            = "http://demo.bind.ee/board/api/v1";
    public static final String BASE_URL              = BuildConfig.BASE_URL;



    public static final String SENT_TOKEN_TO_SERVER  = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public static final String GET_CARDS           = "/cards";
    public static final String GET_PROMO_CARDS     =  "/cards/promoted";
    public static final String DELETE_PROMO        =  "/cards/promotion-delete/{card_hash}";


    public static final String CARD_VIEWED         = "/cards/seen/{card_hash}";
    public static final String CUSTOMER            = "/customer";
    public static final String REGISTER_DEVICE     = "/customer/register_device";

    public static final String CARDS_FRAGMENT      = "com.lapioworks.CardsFragment";

    public static final String TYPE_BASIC          = "basic";
    public static final String TYPE_CHECKBOX       = "checkbox";
    public static final String TYPE_RADIO          = "radio";
    public static final String TYPE_IMAGE          = "image";
    public static final String TYPE_RANGE          = "range";
    public static final String TYPE_SINGLE         = "single";
    public static final String TYPE_TEXT           = "text";
    public static final String TYPE_PASSWORD       = "password";



    public static final int GALLERY_IMAGE_RESULT           = 0;
    public static final int GALLERY_KITKAT_INTENT_CALLED   = 3;
    public static final int PHOTO_IMAGE_RESULT             = 5;



    public static final String CONTENT_TYPE = "Content-Type";
    public static final String URL_ENCODED = "application/x-www-form-urlencoded";
    public static final String X_BOARD_APP_ID = "x-board-appid";
//    public static final String API_KEY = "1182a315ee7e37bf3b9141ec54293e8d";
    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String TYPE_ANDROID = "1";

            public static final String YOUTUBE_KEY = "AIzaSyCWimb_uoFPcBcR-ciQ1QJ5ZhqRUfbNYEg";
    public static final int TIME_SECOND = 1000;
    public static final int TIME_MINUTE = TIME_SECOND*60;
}
