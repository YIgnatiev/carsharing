package youdrive.today.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;

import rx.Subscription;
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
public class RegisterOffertFragment extends BaseFragment<RegistrationNewActivity> implements CompoundButton.OnCheckedChangeListener,  ViewTreeObserver.OnScrollChangedListener{


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
        b.checkbox.setEnabled(false);
        b.checkbox.setOnCheckedChangeListener(this);
        b.svAgreement.getViewTreeObserver().addOnScrollChangedListener(this);

    }

    public void onBack(View view){
        startActivity(new Intent(mActivity, WellcomeActivity.class));
        mActivity.finish();

    }

    public void onForvard(View view){

        if (b.etPrice.getText().length() == 0) b.etPrice.setError(getString(R.string.enter_price));

        else if(Integer.parseInt(b.etPrice.getText().toString())< 3000) b.etPrice.setError(getString(R.string.summ_bigger_than_3000));

        else {
            mActivity.price = b.etPrice.getText().toString();
            createUser();
        }
    }

    public void createUser() {
        mActivity.showProgress();
        mSubscription = mActivity.mClient
                        .createUser()
                        .subscribe(this::onCreateSucccess, mActivity::onCreateFailure);

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

    @Override
    public void onScrollChanged() {
        int scrollY = b.svAgreement.getScrollY() + b.svAgreement.getHeight();
        if(scrollY == b.svAgreement.getChildAt(0).getHeight())b.checkbox.setEnabled(true);
    }
}
