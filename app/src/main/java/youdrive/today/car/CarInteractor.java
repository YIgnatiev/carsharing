package youdrive.today.car;

import youdrive.today.Command;

/**
 * Created by psuhoterin on 26.04.15.
 */
public interface CarInteractor {

    void booking(String id, double lat, double lon, CarActionListener listener);

    void command(Command command, CarActionListener listener);

    void result(Command command, String token, final CarActionListener listener);

    void complete(Command command, CarActionListener listener);
}
