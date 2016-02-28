package shedule.budivnictvo.com.shedule.objects;

/**
 * Created by Администратор on 06.12.2014.
 */
public class User {
    private int userType ;
    private int userId;
    private String password;


    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
