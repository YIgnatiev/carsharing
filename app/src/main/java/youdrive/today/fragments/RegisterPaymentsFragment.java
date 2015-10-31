package youdrive.today.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import youdrive.today.R;
import youdrive.today.activities.MapsActivity;
import youdrive.today.activities.RegistrationNewActivity;
import youdrive.today.databinding.FragmentRegisterPaymentsBinding;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/25/15.
 */
public class RegisterPaymentsFragment extends BaseFragment<RegistrationNewActivity> {



    private FragmentRegisterPaymentsBinding b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_register_payments, container, false);
        b.setListener(this);
        return b.getRoot();
    }



    public void onBack(View view){
        mActivity.getFragmentManager().popBackStack();

    }

    public void onForvard(View view){
       startActivity(new Intent(mActivity, MapsActivity.class));
        mActivity.finish();
    }

    public void onCardLink(View view){

    }

}
