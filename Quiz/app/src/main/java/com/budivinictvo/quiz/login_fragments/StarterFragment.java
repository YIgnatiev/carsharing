package com.budivinictvo.quiz.login_fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.budivinictvo.quiz.R;
import com.budivinictvo.quiz.activity.LoginActivity;
import com.budivinictvo.quiz.login_fragments.LoginFragment;
import com.budivinictvo.quiz.login_fragments.RegisterFragment;

/**
 * Created by Администратор on 06.01.2015.
 */
public class StarterFragment extends Fragment implements View.OnClickListener {
    private TextView textViewLogin;
    private TextView textViewRegister;
    private LoginActivity mActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.starter_fragment_layout, container, false);
        findViews(rootView);
        setListeners();
        setActionBar();
        return rootView;
    }
    private void findViews(View _rootView){
        textViewLogin = (TextView)_rootView.findViewById(R.id.textView_login_StarterFragment);
        textViewRegister = (TextView)_rootView.findViewById(R.id.textView_register_StarterFragment);
        mActivity = (LoginActivity)getActivity();
    }
    private void setListeners(){
        textViewLogin.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);
    }
    private void setActionBar(){
        mActivity.setActionBarTitle(getString(R.string.app_name));
        mActivity.setBackButtonVisible(false);
        mActivity.hideBar();
    }

    @Override
    public void onClick(View _view) {
        switch (_view.getId()){
            case R.id.textView_login_StarterFragment:
                mActivity.replaceFragmentWithBackStack(new LoginFragment());
                break;
            case R.id.textView_register_StarterFragment:
                mActivity.replaceFragmentWithBackStack(new RegisterFragment());
                break;
        }
    }
}
