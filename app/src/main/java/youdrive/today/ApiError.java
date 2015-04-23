package youdrive.today;

/**
 * Created by psuhoterin on 22.04.15.
 */
public class ApiError {

    public static int USER_NOT_FOUND = 10101;
    public static int FIELD_IS_EMPTY = 10102;
    public static int SESSION_NOT_FOUND = 11101;
    public static int INVALID_REQUEST = 11102;
    public static int FORBIDDEN = 15101;
    public static int TARIFF_NOT_FOUND = 20101;
    public static int UNKNOWN_ERROR = 13101;
    public static final int USER_ALREADY_EXISTS = 14102;
    public static final int REGION_NOT_FOUND = 14101;

    int code;
    String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Error{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
