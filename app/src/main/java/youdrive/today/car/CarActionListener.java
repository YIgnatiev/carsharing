package youdrive.today.car;

import youdrive.today.Car;
import youdrive.today.Check;
import youdrive.today.Command;

/**
 * Created by psuhoterin on 26.04.15.
 */
public interface CarActionListener {

    void onCarNotFound();

    void onNotInfo();

    void onNotOrder();

    void onToken(Command command, String token);

    void onPleaseWait();

    void onCommandError();

    void onOpen();

    void onBook(Car car);

    void onError();

    void onAccessDenied();

    void onCommandNotSupported();

    void onTokenNotFound();

    void onInternalError();

    void onClose();

    void onComplete(Check check);

    void onBookingTimeLeft(int bookingTimeLeft);
}
