package youdrive.today.listeners;

public interface ProfileActionListener {

    void onLogout();

    void onError();

    void onSessionNotFound();

    void onInvalidRequest();

    void onUnknownError(String text);
}
