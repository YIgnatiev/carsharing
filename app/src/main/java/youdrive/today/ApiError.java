package youdrive.today;

/**
 * Created by psuhoterin on 22.04.15.
 */
public class ApiError {

    public static final int USER_NOT_FOUND = 113;
    public static final int FIELD_IS_EMPTY = 114;
    public static final int SESSION_NOT_FOUND = 11101;
    public static final int INVALID_REQUEST = 11102;
    public static final int FORBIDDEN = 15101;
    public static final int TARIFF_NOT_FOUND = 20101;
    public static final int UNKNOWN_ERROR = 13101;
    public static final int USER_ALREADY_EXISTS = 110;
    public static final int REGION_NOT_FOUND = 109;
    public static final int CAR_NOT_FOUND = 19101;
    public static final int NOT_INFO = 19102;
    public static final int NOT_ORDER = 19103;
    public static final int ACCESS_DENIED = 21101;
    public static final int COMMAND_NOT_SUPPORTED = 21102;
    public static final int TOKEN_NOT_FOUND = 21103;
    public static final int INTERNAL_ERROR = 21104;


    int code;
    String text;

    public ApiError(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
