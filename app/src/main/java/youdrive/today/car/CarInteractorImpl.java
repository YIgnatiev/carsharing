package youdrive.today.car;

import java.io.IOException;

import rx.Subscription;
import rx.subscriptions.Subscriptions;
import youdrive.today.ApiError;
import youdrive.today.App;
import youdrive.today.BaseObservable;
import youdrive.today.Command;
import youdrive.today.Result;
import youdrive.today.data.network.ApiClient;
import youdrive.today.response.CarResponse;
import youdrive.today.response.CommandResponse;

public class CarInteractorImpl implements CarInteractor {

    private final ApiClient mApiClient;
    private Subscription subscription = Subscriptions.empty();

    public CarInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
    }

    @Override
    public void booking(final String id, final double lat, final double lon, final CarActionListener listener) {
        subscription = BaseObservable.ApiCall(
                () -> {
                    try {
                        return mApiClient.booking(id, lat, lon);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return new CarResponse();
                    }
                }).doOnNext(baseResponse -> {
                    CarResponse response = (CarResponse) baseResponse;
                    if (response.isSuccess()) {
                        if (response.getCar() != null) {
                            listener.onBook(response.getCar());
                        }
                        listener.onBookingTimeLeft(response.getBookingTimeLeft());
                    } else {
                        handlingError(new ApiError(response.getCode(), response.getText()), listener);
                    }
                }).subscribe();
    }

    @Override
    public void command(final Command command, final CarActionListener listener) {
        subscription = BaseObservable.ApiCall(() -> {
            try {
                return mApiClient.command(command);
            } catch (IOException e) {
                e.printStackTrace();
                return new CommandResponse();
            }
        }).doOnNext(baseResponse -> {
            CommandResponse response = (CommandResponse) baseResponse;
            if (response.isSuccess()){
                listener.onToken(command, response.getResultToken());
                result(command, response.getResultToken(), listener);
            } else {
                handlingError(new ApiError(response.getCode(), response.getText()),
                        listener);
            }
        }).subscribe();
    }

    @Override
    public void complete(final Command command, final CarActionListener listener) {
        subscription = BaseObservable.ApiCall(() -> {
            try {
                return mApiClient.complete();
            } catch (IOException e) {
                e.printStackTrace();
                return new CommandResponse();
            }
        }).doOnNext(baseResponse -> {
            CommandResponse response = (CommandResponse) baseResponse;
            if (response.isSuccess()){
                listener.onToken(command, response.getResultToken());
                result(command, response.getResultToken(), listener);
            } else {
                handlingError(new ApiError(response.getCode(), response.getText()),
                        listener);
            }
        }).subscribe();
    }

    @Override
    public void result(final Command command, final String token, final CarActionListener listener) {
        subscription = BaseObservable.ApiCall(() -> {
            try {
                return mApiClient.result(token);
            } catch (IOException e) {
                e.printStackTrace();
                return new CommandResponse();
            }
        }).doOnNext(baseResponse -> {
            CommandResponse response = (CommandResponse) baseResponse;
            if (response.isSuccess()) {
                Result result = Result.fromString(response.getStatus());
                if (result != null) {
                    if (result.equals(Result.NEW)
                            || result.equals(Result.PROCESSING)) {
                        listener.onPleaseWait();
                    } else if (result.equals(Result.ERROR)) {
                        listener.onCommandError();
                    } else {
                        if (command.equals(Command.OPEN)) {
                            listener.onOpen();
                        } else if (command.equals(Command.CLOSE)) {
                            listener.onClose();
                        } else {
                            listener.onComplete(response.getCheck());
                        }
                    }
                } else {
                    listener.onError();
                }

            } else {
                handlingError(new ApiError(response.getCode(), response.getText()),
                        listener);
            }
        }).subscribe();
    }

    private void handlingError(ApiError error, CarActionListener listener) {
        if (error.getCode() == ApiError.CAR_NOT_FOUND){
            listener.onCarNotFound(error.getText());
        } else if (error.getCode() == ApiError.NOT_INFO){
            listener.onNotInfo(error.getText());
        } else if(error.getCode() == ApiError.NOT_ORDER){
            listener.onNotOrder(error.getText());
        } else if(error.getCode() == ApiError.ACCESS_DENIED){
            listener.onAccessDenied(error.getText());
        } else if(error.getCode() == ApiError.COMMAND_NOT_SUPPORTED){
            listener.onCommandNotSupported(error.getText());
        } else if(error.getCode() == ApiError.SESSION_NOT_FOUND){
            listener.onSessionNotFound(error.getText());
        } else if (error.getText() != null){
            listener.onUnknownError(error.getText());
        } else {
            listener.onError();
        }
    }

    public Subscription getSubscription() {
        return subscription;
    }
}
