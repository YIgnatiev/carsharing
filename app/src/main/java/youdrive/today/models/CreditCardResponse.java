package youdrive.today.models;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 12/2/15.
 */
public class CreditCardResponse {
    private boolean secure_check_needed;
    private String req_url;
    private String finish_url;

    public boolean isSecure_check_needed() {
        return secure_check_needed;
    }

    public String getReq_url() {
        return req_url;
    }

    public String getFinish_url() {
        return finish_url;
    }
}
