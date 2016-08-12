package youdrive.today.response;

import youdrive.today.models.RegistrationUser;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/31/15.
 */
public class RegistrationModel extends BaseResponse {

    private String id;
    private RegistrationUser data;
    private RegistrationUser validation;
    private String session_id;


    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RegistrationUser getData() {
        return data;
    }

    public void setData(RegistrationUser data) {
        this.data = data;
    }

    public RegistrationUser getValidation() {
        return validation;
    }

    public void setValidation(RegistrationUser validation) {
        this.validation = validation;
    }
}
