package pro.theboard.models;

/**
 * Created by Oleh Makhobey on 16.07.2015.
 * tajcig@ya.ru
 */
public class BaseResponse<T> {
    private boolean success;
    private String error;
    private T data;
    private Settings settings;
    private Style style;

    public Style getStyle(){
        return style;
    }
    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public T getData() {
        return data;
    }

    public Settings getSettings() {
        return settings;
    }
}

class Settings {
    private boolean notifications;
    private boolean location;

    public boolean isNotifications() {
        return notifications;
    }

    public boolean isLocation() {
        return location;
    }
}
class Style{
    private String bg_color;
    private String font_size;
    private String font_color;

    public String getBg_color() {
        return bg_color;
    }

    public String getFont_size() {
        return font_size;
    }

    public String getFont_color() {
        return font_color;
    }
}
