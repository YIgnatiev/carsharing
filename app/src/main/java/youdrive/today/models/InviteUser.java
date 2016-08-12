package youdrive.today.models;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/3/15.
 */
public class InviteUser {

    private Long phone;
    private String region;
    private boolean readyToUse;
    private String email;

    public InviteUser(String email, long phone, String region, boolean readyToUse) {
        this.email = email;
        this.phone = phone;
        this.region = region;
        this.readyToUse = readyToUse;
    }
}
