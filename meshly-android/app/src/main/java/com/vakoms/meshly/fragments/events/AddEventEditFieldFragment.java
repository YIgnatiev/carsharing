package com.vakoms.meshly.fragments.events;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.utils.KeyboardUtil;
import com.vakoms.meshly.utils.P;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.EditFieldBinding;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;

/**
 * Created by taras.melko on 12.09.2014.
 */
public class AddEventEditFieldFragment extends BaseFragment<MainActivity> {

    private String mFieldName ;
    private int maxSymbolCount = 140;


    private  EditFieldBinding b;

    public static AddEventEditFieldFragment newInstance(String fieldName) {
        AddEventEditFieldFragment fragment = new AddEventEditFieldFragment();
        fragment.setFieldName(fieldName);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(b == null) {
            b = DataBindingUtil.inflate(inflater, R.layout.fragment_add_event_edit_field, null, false);

            setListeners();
            setupView();
            setTextObserver();
        }
        return b.getRoot();
    }

    protected void setListeners(){
        b.setListener(this);
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(maxSymbolCount);
        b.etDescription.setFilters(filterArray);
    }

    private void setupView() {

        KeyboardUtil.showKeyBoard(b.etDescription, getActivity());

        b.tvTitle.setText(mFieldName);

        if (mFieldName.equals("Address")) {
            b.etDescription.setText(P.getAddEventsAddress());
        } else if (mFieldName.equals("Description")) {
            b.etDescription.setText(P.getAddEventsDescription());
        }

        b.tvSymbols.setText(String.valueOf (maxSymbolCount - b.etDescription.getText().length()));
    }

    public void setFieldName(String _fieldName){
        this.mFieldName = _fieldName;
    }

    private void saveData() {
        if (mFieldName.equals("Address")) {
            P.saveAddEventsAdress(b.etDescription.getText().toString());
        } else if (mFieldName.equals("Description")) {
            P.saveAddEventsDescription(b.etDescription.getText().toString());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        KeyboardUtil.hideKeyBoard(b.etDescription, getActivity());
    }




    private void  setTextObserver(){

        WidgetObservable.text(b.etDescription)
                .map(OnTextChangeEvent::text)
                .map(CharSequence::toString)
                .subscribe(this::onTextChanged);


    }

    @Override
    protected void handleError(Throwable throwable) {
        mActivity.handleError(throwable);
    }

    public void onTextChanged(String s) {
        Editable editable = b.etDescription.getText();
        int len = editable.length();

        if (len > maxSymbolCount) {
            int selEndIndex = Selection.getSelectionEnd(editable);
            String str = editable.toString();
            //
            String newStr = str.substring(0, maxSymbolCount);
            b.etDescription.setText(newStr);
            editable = b.etDescription.getText();

            //
            int newLen = editable.length();
            //
            if (selEndIndex > newLen) {
                selEndIndex = editable.length();
            }
            //
            Selection.setSelection(editable, selEndIndex);

        }
        b.tvSymbols.setText(String.valueOf(maxSymbolCount - s.length()));
    }

    public void onSave(View view){
        saveData();
        mActivity.onBackPressed();
    }


    public void onBack(View view){
        mActivity.onBackPressed();
    }

}
