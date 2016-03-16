package ru.lead2phone.ru.lead2phone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.lead2phone.ru.lead2phone.R;
import ru.lead2phone.ru.lead2phone.adapters.CallerAdapter;
import ru.lead2phone.ru.lead2phone.models.Caller;
import ru.lead2phone.ru.lead2phone.rest.LeadToPhoneApi;
import ru.lead2phone.ru.lead2phone.utils.Preferences;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 8/26/15.
 */
public class ListFragment extends BaseFragment implements Callback<List<Caller>> ,View.OnClickListener {
    private boolean isLaterCalled;
    private List<Caller> mList;
    private CallerAdapter mAdapter;
    @Bind(R.id.lvCallers_FragmentList)ListView lvCallers;
    @Bind(R.id.tvEmpty_FragmentList)TextView tvEmpty;
    @Bind(R.id.tvCallerName_FragmentList)TextView tvCallerName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,null);
        ButterKnife.bind(this, view);
        tvCallerName.setText(Preferences.getUserName());
        getListCallers();
        mActivity.startUpdateService();

        return view;
    }


    @OnClick(R.id.ivBack_FragmentList)
    public void onBackCliced(){

        mActivity.stopUpdateService();
        Preferences.saveIsLogged(false);
        Preferences.setLogin("");
        Preferences.setPassword("");
        mActivity.replaceFragmentWithoutBackStack(new LoginFragment());
    }

    private void getListCallers() {
        String id = Preferences.getLogin();
        String pass = Preferences.getPassword();
        LeadToPhoneApi.getInstance(mActivity).getList(id, pass, this);
    }

    private void getLaterListCallers() {
        isLaterCalled = true;
        String id = Preferences.getLogin();
        String pass = Preferences.getPassword();
        LeadToPhoneApi.getInstance(mActivity).getLaterList(id, pass, this);
    }


    private void setUpListView(){
        if(!mList.isEmpty())tvEmpty.setVisibility(View.INVISIBLE);
        mAdapter= new CallerAdapter(mActivity,mList,this);
        lvCallers.setAdapter(mAdapter);



    }


    @Override
    public void success(List<Caller> callers,Response response) {

        if(!isLaterCalled) getLaterListCallers();
            mList = callers;
            setUpListView();

    }

    @Override
    public void failure(RetrofitError error) {

    }

//    private void saveDeletedId(int id){
//        Set<String> list = Preferences.getDeletedId();
//        if(list != null && list.contains(String.valueOf(id))){
//           return;
//        }
//
//        List<String> arrayList = new ArrayList<>();
//        arrayList.add(String.valueOf(id));
//        if(list != null){
//            for(String str :list)
//                arrayList.add(str);
//        }
//
//        Preferences.saveDeletedIds(arrayList);
//
//
//    }


    public void deleteOnServer(String id){
        LeadToPhoneApi
                .getInstance(mActivity)
                .deleteUser(Preferences.getLogin(), Preferences.getPassword(), String.valueOf(id), new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

    }



    @Override
    public void onClick(View v) {

        int position =( Integer)v.getTag();

        switch (v.getId()){
            case R.id.ivCall_ItemCaller:
                String number = mList.get(position).getNumber();
                mActivity.call(number);
                break;
            case R.id.ivDelete_ItemCaller:
                int callerId = mAdapter.getItem(position).getId();

                deleteOnServer(String.valueOf(callerId));
                mList.remove(position);
                mAdapter.notifyDataSetChanged();

                if(mList.isEmpty())tvEmpty.setVisibility(View.VISIBLE);

                break;
        }
    }
}
