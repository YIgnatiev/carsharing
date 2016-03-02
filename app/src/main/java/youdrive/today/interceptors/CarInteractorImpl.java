package youdrive.today.interceptors;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import youdrive.today.App;
import youdrive.today.listeners.CarActionListener;
import youdrive.today.models.ApiError;
import youdrive.today.models.Command;
import youdrive.today.models.Result;
import youdrive.today.network.ApiClient;
import youdrive.today.response.CarResponse;
import youdrive.today.response.CommandResponse;

public class CarInteractorImpl implements CarInteractor {

    private final ApiClient mApiClient;
    private CarActionListener mListener;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    public CarInteractorImpl() {
        mApiClient = App.getInstance().getApiClient();
    }

    @Override
    public void booking(final String id, final double lat, final double lon, final CarActionListener listener) {
        mListener = listener;
        Subscription subscription = mApiClient
                .booking(id, lat, lon)
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBookingSuccess, this::handleNetworkError);

        subscriptions.add(subscription);

    }


    public void onBookingSuccess(CarResponse response) {
        if (response.isSuccess()) {
            if (response.getCar() != null) {
                mListener.onBook(response.getCar());
            }
            mListener.onBookingTimeLeft(response.getBookingTimeLeft());
        } else {
            handlingError(new ApiError(response.getCode(), response.getText()), mListener);
        }


    }


    @Override
    public void command(final Command command, final CarActionListener listener) {
        mListener = listener;
        Subscription subscription = mApiClient
                .command(command)
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onCommandSuccess(command, response), this::handleNetworkError);

        subscriptions.add(subscription);
    }


    private void onCommandSuccess(Command command, CommandResponse response) {
        if (response.isSuccess()) {
            mListener.onToken(command, response.getResultToken());
            result(command, response.getResultToken(), mListener);
        } else {
            handlingError(new ApiError(response.getCode(), response.getText()),
                    mListener);
        }
    }


    @Override
    public void complete(final Command command, final CarActionListener listener) {

        mListener = listener;
       Subscription subscription = mApiClient
                .complete()
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onCompleteSuccess(command, response), this::handleNetworkError);

        subscriptions.add(subscription);
    }

    private void onCompleteSuccess(Command command, CommandResponse response) {

        if (response.isSuccess()) {
            mListener.onToken(command, response.getResultToken());
            result(command, response.getResultToken(), mListener);
        } else {
            handlingError(new ApiError(response.getCode(), response.getText()), mListener);
        }
    }


    @Override
    public void result(final Command command, final String token, final CarActionListener listener) {

        mListener = listener;
        Subscription subscription = mApiClient
                .result(token)
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onResultSuccess(command, response), this::handleNetworkError);
        subscriptions.add(subscription);
    }


    private void onResultSuccess(Command command, CommandResponse response) {

        if (response.isSuccess()) {
            Result result = Result.fromString(response.getStatus());
            if (result != null) {
                if (result.equals(Result.NEW) || result.equals(Result.PROCESSING)) {
                    mListener.onPleaseWait();
                } else if (result.equals(Result.ERROR)) {
                    mListener.onCommandError();
                } else {


                    if (command.equals(Command.OPEN)) mListener.onOpen();
                    else if (command.equals(Command.CLOSE)) mListener.onClose();
                    else mListener.onComplete(response.getCheck());

                }
            } else {
                mListener.onError();
            }

        } else {
            handlingError(new ApiError(response.getCode(), response.getText()),
                    mListener);
        }

    }


    public void handleNetworkError(Throwable t) {
        mListener.onError();
    }

    private void handlingError(ApiError error, CarActionListener listener) {
        if (error.getCode() == ApiError.CAR_NOT_FOUND) {
            listener.onCarNotFound(error.getText());
        } else if (error.getCode() == ApiError.NOT_INFO) {
            listener.onNotInfo(error.getText());
        } else if (error.getCode() == ApiError.NOT_ORDER) {
            listener.onNotOrder(error.getText());
        } else if (error.getCode() == ApiError.ACCESS_DENIED) {
            listener.onAccessDenied(error.getText());
        } else if (error.getCode() == ApiError.COMMAND_NOT_SUPPORTED) {
            listener.onCommandNotSupported(error.getText());
        } else if (error.getCode() == ApiError.SESSION_NOT_FOUND) {
            listener.onSessionNotFound(error.getText());
        } else if (error.getText() != null) {
            listener.onUnknownError(error.getText());
        } else {
            listener.onError();
        }
    }

    public Subscription getSubscription() {
        return subscriptions;
    }
}
