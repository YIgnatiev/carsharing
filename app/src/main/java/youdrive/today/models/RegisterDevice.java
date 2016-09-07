package youdrive.today.models;

/**
 * Created by Sergio on 06/09/16.
 */
public class RegisterDevice {
    String os;
    String token;
    float os_version;
    String device_type;
    public RegisterDevice(String os, String token, float os_version, String device_type) {
        this.os = os;
        this.token = token;
        this.os_version = os_version;
        this.device_type = device_type;
    }

}
