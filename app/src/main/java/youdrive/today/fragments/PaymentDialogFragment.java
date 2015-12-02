package youdrive.today.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.cloudpayments.sdk.CardFactory;
import ru.cloudpayments.sdk.ICard;
import rx.Observable;
import rx.Subscription;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import youdrive.today.R;
import youdrive.today.activities.RegistrationNewActivity;
import youdrive.today.databinding.FragmentPaymentBinding;
import youdrive.today.listeners.ValueFunction;
import youdrive.today.models.CreditCardModel;
import youdrive.today.models.CreditCardResponse;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 11/2/15.
 */
public class PaymentDialogFragment extends DialogFragment {

    private String mRegId;
    private Subscription mSubscription;
    private ValueFunction<String> mFunction;
    public static final String devId = "pk_348c635ba69b355d6f4dc75a4a20";
    private String mPrice;

    public static final String publicId = "pk_348c635ba69b355d6f4dc75a4a205";

    public static DialogFragment newInstance(String regId, String price, ValueFunction<String> callback) {
        PaymentDialogFragment fragment = new PaymentDialogFragment();
        fragment.setPrice(price);
        fragment.setRegId(regId);
        fragment.setCallback(callback);
        return fragment;
    }

    private void setCallback(ValueFunction<String> callback) {
        this.mFunction = callback;
    }

    private void setRegId(String regId) {
        mRegId = regId;
    }

    private void setPrice(String price) {
        mPrice = price;
    }

    private FragmentPaymentBinding b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        b = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false);

        b.setListener(this);
        b.btnPay.setIdleText(mPrice + " рублей");
        b.btnPay.setEnabled(false);
        checkFields();
        return b.getRoot();
    }

    public void onPay(View view) {
        if (b.btnPay.getProgress() == 0) {
            b.btnPay.setProgress(50);
            setCancelable(false);makePayment();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    private void makePayment() {

        ICard card = CardFactory.create(b.etCardNumber.getText().toString(),
                b.etExpired.getText().toString(),
                b.etCvv.getText().toString());

        if (card.isValidNumber()) {
            //   String hardcodedCrypto ="014925000087170902geD+UKHNLf+SHemZU/ty8spDA/309+In4MgRQXAysNbteN+IIsOuG9iRe4W1rBChqr08GwH+0LYSGKmvTNClxtbC25KzyzP6IEgSrw474k8L33wlmLl1Ifqzri9le+GrOldcBFEf69PjyYezrXR9nAmFFCpz5jh91WJ3A1+V3qljALEI0z5Zf+Xknxoaq4bTVZMa2XwhcLdHqW1SzVyP1kSQkwIr/xIBV+tOETp1w7i31n17yJxo+JLpHqzvZRQmk6fNHwB3pc1lDrpFHuxfy5AWFwFZGsuoZkjdVntlzc6znL5tlxApdZg4jX8uyl5P+qkHpnlnSG4ATBZ/s07ucg==";
            CreditCardModel model = new CreditCardModel(mRegId,
                    mPrice,
                    b.etName.getText().toString().toUpperCase(),
                    card.cardCryptogram(devId));

            mSubscription = ((RegistrationNewActivity) getActivity()).mClient
                    .initCard(model)
                    .subscribe(this::onCreditCardSuccess, this::onFailure);

        } else {
            b.etCardNumber.setError("CardNumber is not valid");
            b.btnPay.setProgress(0);
        }
    }

    private void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void onCreditCardSuccess(CreditCardResponse response) {
        b.btnPay.setProgress(0);
        if(response.getFinish_url() != null)mFunction.apply(response.getFinish_url());
        else if(response.getReq_url() != null)mFunction.apply(response.getReq_url());
        else mFunction.apply("http://www.yandex.ru");
    }

    private void onFailure(Throwable t) {
        b.btnPay.setProgress(0);
    }

    public void checkFields() {
        Observable<Boolean> cardNum = WidgetObservable
                .text(b.etCardNumber)
                .distinctUntilChanged()
                .map(OnTextChangeEvent::text)
                .map(t -> t.length() == 16)
                .doOnNext(bool -> {
                    if (!bool) b.etCardNumber.setError("В номере карты допущены ошибки");
                });
        Observable<Boolean> date = WidgetObservable.text(b.etExpired)
                .distinctUntilChanged()
                .map(OnTextChangeEvent::text)
                .map(t -> t.length() == 4)
                .doOnNext(bool -> {
                    if (!bool) b.etExpired.setError("Ошибка");

                });

        Observable<Boolean> name = WidgetObservable.text(b.etName)
                .distinctUntilChanged()
                .map(OnTextChangeEvent::text)
                .map(t -> t.length() != 0)
                .doOnNext(bool -> {
                    if (!bool) b.etName.setError(getString(R.string.empty));
                });
        Observable<Boolean> cvv = WidgetObservable.text(b.etCvv)
                .distinctUntilChanged()
                .map(OnTextChangeEvent::text)
                .map(t -> t.length() == 3)
                .doOnNext(bool -> {
                    if (!bool) b.etCvv.setError("Ошибка");

                });


        Observable.combineLatest(cardNum, date, name, cvv, (a, b, c, d) -> a && b && c && d)
                .distinctUntilChanged()
                .subscribe(b.btnPay::setEnabled);

    }
}
