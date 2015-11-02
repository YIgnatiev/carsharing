package youdrive.today.response;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 11/1/15.
 */
public class UploadGroupResponse {
    private String id;
    private String datetime_created;
    private String datetime_stored;
    private String files_count;
    private String cdn_url;
    private String url;


    public String getId() {
        return id;
    }

    public String getDatetime_created() {
        return datetime_created;
    }

    public String getDatetime_stored() {
        return datetime_stored;
    }

    public String getFiles_count() {
        return files_count;
    }

    public String getCdn_url() {
        return cdn_url;
    }

    public String getUrl() {
        return url;
    }
}
