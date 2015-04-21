package youdrive.today;

/**
 * Created by psuhoterin on 22.04.15.
 */
public class Error {

    public static int NOT_FOUND = 10101;
    public static int EMPTY = 10102;

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
