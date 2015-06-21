package youdrive.today.profile;

public interface ProfileActionListener {

    void onLogout();
    void onError();

    void onSessionNotFound();
    void onInvalidRequest();

    void onUnknownError(String text);
}
