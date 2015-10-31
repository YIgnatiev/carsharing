package youdrive.today.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import youdrive.today.R;
import youdrive.today.activities.RegistrationNewActivity;
import youdrive.today.databinding.FragmentRegisterDocumentsBinding;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/25/15.
 */
public class RegisterDocumentsFragment extends BaseFragment<RegistrationNewActivity> {
    private FragmentRegisterDocumentsBinding b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_register_documents, container, false);
        b.setListener(this);
        return b.getRoot();
    }



    public void onBack(View view){
        mActivity.getFragmentManager().popBackStack();

    }

    public void onForvard(View view){
        mActivity.startFragmentLeft(new RegisterPaymentsFragment());
    }

    public void onLoad(View view){

    }



}
