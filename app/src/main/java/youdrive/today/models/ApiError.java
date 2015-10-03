package youdrive.today.models;

/**
 * Created by psuhoterin on 22.04.15.
 */
public class ApiError {

    public static final int USER_NOT_FOUND = 113;
    public static final int FIELD_IS_EMPTY = 114;
    public static final int SESSION_NOT_FOUND = 115;
    public static final int INVALID_REQUEST = 102;
    public static final int FORBIDDEN = 101;
    public static final int TARIFF_NOT_FOUND = 116;
    public static final int USER_ALREADY_EXISTS = 110;
    public static final int REGION_NOT_FOUND = 109;
    public static final int CAR_NOT_FOUND = 103;
    public static final int NOT_INFO = 104;
    public static final int NOT_ORDER = 107;
    public static final int ACCESS_DENIED = 101;
    public static final int COMMAND_NOT_SUPPORTED = 111;

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
