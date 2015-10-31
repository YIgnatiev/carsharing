package youdrive.today.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Observable;
import rx.Subscription;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import youdrive.today.R;
import youdrive.today.activities.RegistrationNewActivity;
import youdrive.today.databinding.FragmentRegisterProfileBinding;
import youdrive.today.models.RegistrationUser;
import youdrive.today.response.RegistrationModel;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/25/15.
 */
public class RegisterProfileFragment extends BaseFragment<RegistrationNewActivity> {

    private FragmentRegisterProfileBinding b;
    private Subscription mSubscription;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_register_profile, container, false);
        b.setListener(this);
        checkFields();
        b.tvForvard.setEnabled(false);
        return b.getRoot();
    }


    public void onBack(View view) {
        mActivity.getFragmentManager().popBackStack();

    }

    public void onForvard(View view) {

        updateUser(mActivity.userId, mActivity.mUser);


    }


    public void updateUser(String userId ,RegistrationUser user ){
        mActivity.showProgress();
        mSubscription = mActivity.mClient
                .updateUser(userId, user)
                .subscribe(this::onUpdateSuccess, mActivity::onCreateFailure);
    }

    public void onUpdateSuccess(RegistrationModel model){
        mActivity.hideProgress();
        mActivity.mUser = model.getData();

        mActivity.startFragmentLeft(new RegisterDocumentsFragment());
    }





    public void checkFields() {


        Observable<Boolean> email = WidgetObservable
                .text(b.etEmail)
                .distinctUntilChanged()
                .map(OnTextChangeEvent::text)
                .map(t -> validateEmail(t.toString()))
                .doOnNext(bool -> {
                    if (!bool) b.etEmail.setError(getString(R.string.email_not_valid));
                    else mActivity.mUser.setEmail(b.etEmail.getText().toString());
                });
            WidgetObservable
                .text(b.etPromo)
                .distinctUntilChanged()
                .map(OnTextChangeEvent::text)
                .doOnNext(text -> mActivity.mUser.setPromocode(text.toString()))
                    .subscribe();


        Observable<Boolean> mobilePhone = WidgetObservable.text(b.etPhone)
                .distinctUntilChanged()
                .map(OnTextChangeEvent::text)
                .map(t -> t.length() > 8)
                .doOnNext(bool -> {
                    if (!bool) b.etPhone.setError(getString(R.string.short_phone));
                    else mActivity.mUser.setPhone(b.etPhone.getText().toString());
                });


        Observable<Boolean> password = WidgetObservable.text(b.etPassword)
                .distinctUntilChanged()
                .map(OnTextChangeEvent::text)
                .map(t -> t.length() != 0)
                .doOnNext(bool -> {
                    if (!bool) b.etPassword.setError(getString(R.string.empty));
                    else mActivity.mUser.setPassword(b.etPassword.getText().toString());
                });

        Observable<Boolean> passwordAgain = WidgetObservable.text(b.etPaswordAgain)
                .distinctUntilChanged()
                .map(OnTextChangeEvent::text)
                .map(t -> t.toString().equals(b.etPassword.getText().toString()))
                .doOnNext(bool -> {
                    if (!bool) b.etPaswordAgain.setError(getString(R.string.password_dont_match));
                    else mActivity.mUser.setPassword_confirm(b.etPaswordAgain.getText().toString());
                });

        Observable<Boolean> name = WidgetObservable.text(b.etName)
                .distinctUntilChanged()
                .map(OnTextChangeEvent::text)
                .map(t -> t.length() != 0)
                .doOnNext(bool -> {
                    if (!bool) b.etName.setError(getString(R.string.empty));
                    else mActivity.mUser.setFirst_name(b.etName.getText().toString());
                });
        Observable<Boolean> surname = WidgetObservable.text(b.etSurname)
                .distinctUntilChanged()
                .map(OnTextChangeEvent::text)
                .map(t -> t.length() != 0)
                .doOnNext(bool -> {
                    if (!bool) b.etSurname.setError(getString(R.string.empty));
                    else mActivity.mUser.setLast_name(b.etSurname.getText().toString());
                });
        Observable<Boolean> middleName = WidgetObservable.text(b.etMiddleName)
                .distinctUntilChanged()
                .map(OnTextChangeEvent::text)
                .map(t -> t.length() != 0)
                .doOnNext(bool -> {
                    if (!bool) b.etMiddleName.setError(getString(R.string.empty));
                    else mActivity.mUser.setMiddle_name(b.etMiddleName.getText().toString());
                });


        Observable.combineLatest(email, mobilePhone, password, passwordAgain, name, surname, middleName, (e, m, p, pa, n, s, mi) -> e && m && p && pa && n && s && mi)
                .distinctUntilChanged()
                .subscribe(b.tvForvard::setEnabled);


    }

    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    @Override
    public void onStop() {
        if(mSubscription != null)mSubscription.unsubscribe();
        super.onStop();
    }
}
