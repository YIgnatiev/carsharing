package youdrive.today.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.gms.analytics.HitBuilders;
import com.yandex.metrica.YandexMetrica;

import rx.Subscription;
import youdrive.today.App;
import youdrive.today.R;
import youdrive.today.activities.RegistrationNewActivity;
import youdrive.today.activities.WellcomeActivity;
import youdrive.today.databinding.FragmentRegisterOffertBinding;
import youdrive.today.response.RegistrationModel;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/25/15.
 */
public class RegisterOffertFragment extends BaseFragment<RegistrationNewActivity> implements CompoundButton.OnCheckedChangeListener {


    private FragmentRegisterOffertBinding b;
    private Subscription mSubscription;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_register_offert,container,false);
        b.setListener(this);
        setData();
        return b.getRoot();
    }

    private void setData(){
        b.tvDogovor.setText(Html.fromHtml(getString(R.string.dogovor)));
        b.checkbox.setOnCheckedChangeListener(this);
        b.tvForvard.setEnabled(false);
    }

    public void onBack(View view){
        startActivity(new Intent(mActivity, WellcomeActivity.class));
        mActivity.finish();
    }

    public void onForvard(View view){
            createUser();

    }

    public void createUser() {
        mActivity.showProgress();
        mSubscription = mActivity.mClient
                        .createUser()
                        .subscribe(this::onCreateSucccess, mActivity::onCreateFailure);

    }

    @Override
    public void onStart() {
        super.onStart();
        YandexMetrica.reportEvent("registration_1_0");
        App.tracker().setScreenName("registration_1_0");
        App.tracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void onCreateSucccess(RegistrationModel model){
        mActivity.hideProgress();
        mActivity.userId = model.getId();
        mActivity.mUser = model.getData();
        mActivity.startFragmentLeft(new RegisterProfileFragment());

    }

    @Override
    public void onStop() {
        if(mSubscription != null)
        mSubscription.unsubscribe();
        super.onStop();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
       b.tvForvard.setEnabled(isChecked);
    }
}
