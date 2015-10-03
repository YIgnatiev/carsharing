package youdrive.today.listeners;

import youdrive.today.models.Car;
import youdrive.today.models.Check;
import youdrive.today.models.Command;

/**
 * Created by psuhoterin on 26.04.15.
 */
public interface CarActionListener {

    void onCarNotFound(String text);

    void onNotInfo(String text);

    void onNotOrder(String text);

    void onToken(Command command, String token);

    void onPleaseWait();

    void onCommandError();

    void onOpen();

    void onBook(Car car);

    void onError();

    void onAccessDenied(String text);

    void onCommandNotSupported(String text);

    void onClose();

    void onComplete(Check check);

    void onBookingTimeLeft(int bookingTimeLeft);

    void onSessionNotFound(String text);

    void onUnknownError(String text);
}
