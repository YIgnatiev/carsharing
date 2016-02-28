package com.carusselgroup.dwt.model;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.carusselgroup.dwt.ui.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.Collection;

public class SpinnerPack {

    private Spinner spinner;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mData;

    public SpinnerPack(Spinner spinner, ArrayAdapter<String> adapter, ArrayList<String> data) {
        this.spinner = spinner;
        mData = data;
        mAdapter = adapter;
        this.spinner.setAdapter(mAdapter);
    }

    public Spinner getSpinner() {
        return spinner;
    }

    public ArrayAdapter<String> getAdapter() {
        return mAdapter;
    }

    public ArrayList<String> getData() {
        return mData;
    }

    public void enable() {
        setSelection(0);
        spinner.setEnabled(true);
    }

    public void disable() {
        spinner.setEnabled(false);
        mData.clear();
        mData.add(SearchFragment.EMPTY_SPINNER_ITEM);
        mAdapter.notifyDataSetChanged();
    }

    public boolean isEnabled() {
        return spinner.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        if (enabled)
            enable();
        else
            disable();
    }

    public void replaceData(Collection<String> data) {
        mData.clear();
        addData(data);
    }

    public void addData(Collection<String> data) {
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    private Object getSelectedItem() {
        return spinner.getSelectedItem();
    }

    public void setSelection(int position) {
        spinner.setSelection(position);
    }

    public void setListener(AdapterView.OnItemSelectedListener listener) {
        spinner.setOnItemSelectedListener(listener);
    }

    public String getSelectedValue() {
        return String.valueOf(getSelectedItem());
    }


    public int getSelectedItemPosition() {
        return spinner.getSelectedItemPosition();
    }
}
