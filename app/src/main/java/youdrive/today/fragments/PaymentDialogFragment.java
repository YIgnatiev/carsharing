package youdrive.today.fragments;

import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.cloudpayments.sdk.CardFactory;
import ru.cloudpayments.sdk.ChargeFactory;
import ru.cloudpayments.sdk.ICard;
import ru.cloudpayments.sdk.ICharge;
import ru.cloudpayments.sdk.business.domain.model.BaseResponse;
import ru.cloudpayments.sdk.view.ChargeTaskListener;
import rx.Observable;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import youdrive.today.R;
import youdrive.today.databinding.FragmentPaymentBinding;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 11/2/15.
 */
public class PaymentDialogFragment extends DialogFragment implements ChargeTaskListener{

    private int mPrice;
    public static final String publicId = "pk_348c635ba69b355d6f4dc75a4a205";
    public static DialogFragment newInstance(String price) {
        PaymentDialogFragment fragment = new PaymentDialogFragment();
        fragment.setPrice(price);
        return fragment;
    }


    private void setPrice(String price) {
        mPrice = Integer.parseInt(price);
    }


    private FragmentPaymentBinding b ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         b = DataBindingUtil.inflate(inflater, R.layout.fragment_payment,container,false);
        b.setListener(this);
        b.tvPrice.setText( mPrice +" рублей");
        b.btnPay.setEnabled(false);
        checkFields();
        return b.getRoot();
    }


    public void onPay(View view){
        if(b.btnPay.getProgress() !=0) {
            b.btnPay.setProgress(50);
            setCancelable(false);
            makePayment();
        }



    }

    private void makePayment(){


        ICard card = CardFactory.create(b.etCardNumber.getText().toString(),
                                        b.etExpired.getText().toString(),
                                        b.etCvv.getText().toString());





        if (card.isValidNumber()) {
            ICharge charge = ChargeFactory.create(getActivity(),
                    publicId,
                    "accId",
                    "invId",
                    card.cardCryptogram(publicId),
                    b.etName.getText().toString(),
                    mPrice,
                    "RUB",
                    b.etEmail.getText().toString(),
                    "http://example.ru");

            charge.run(this);
        } else {
           b.etCardNumber.setError("CardNumber is not valid");
        }
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




        Observable.combineLatest(cardNum,date,name,cvv, (a, b, c,d) -> a && b && c && d )
                .distinctUntilChanged()
                .subscribe(b.btnPay::setEnabled);


    }

    @Override
    public void success(BaseResponse baseResponse) {

    }

    @Override
    public void error(BaseResponse baseResponse) {

    }

    @Override
    public void cancel() {

    }
}
