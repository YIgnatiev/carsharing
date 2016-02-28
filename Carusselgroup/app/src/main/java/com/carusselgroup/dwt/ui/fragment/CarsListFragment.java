package com.carusselgroup.dwt.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carusselgroup.dwt.R;
import com.carusselgroup.dwt.adapter.CarsListAdapter;
import com.carusselgroup.dwt.adapter.OrderListAdapter;
import com.carusselgroup.dwt.model.Car;
import com.carusselgroup.dwt.rest.CarsNewApi;
import com.carusselgroup.dwt.rest.IResponse;
import com.carusselgroup.dwt.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class CarsListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    public static final String LIST_CARS_KEY = "com.carusselgroup.dwt.ui.LIST_CARS_KEY";
    private static String SORTED_ASK = "Asc";
    private static String SORTED_DESC = "Desc";
    private final int CARS_PER_PAGE = 20;
    private final String STATE_ORDER = "pos";
    private final String STATE_DESC = "desc";
    private final String STATE_URL_PARAMS = "url";
    private final String STATE_ELEMENT_SIZE = "ELEMENTS";
    private final String STATE_POSITION = "LIST_POSITION";
    View.OnClickListener mCheckBoxClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sortItems(v, spinnerOrderCar.getSelectedItemPosition());
            switchIcons(sortByImageview);
        }
    };
    private int mRowIndex = 0;
    private boolean mNeedLoad = true;
    private MainActivity mActivity;
    private ListView listViewCars;
    private TextView mTextViewBack;
    private TextView mTextViewEmptyList;
    private TextView ascendingTextView;
    private LinearLayout ascDescLinearLayout;
    private Spinner spinnerOrderCar;
    private ArrayList<Car> mCarItems;
    private String mSearchParams = "";
    private int mCarsCount = -1;
    private int mPosInList = -1;
    private CarsListAdapter mCarListAdapter;
    private AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (!mActivity.isProgressDialogShow()
                    && totalItemCount == firstVisibleItem + visibleItemCount
                    && mNeedLoad
                    && mCarListAdapter != null) {
                if (progressBarLoad.getVisibility() != View.VISIBLE) {
                    addNewElements();
                }
            }
        }
    };
    private OnCarSelectedListener mCarSelectedListener;
    private ProgressBar progressBarLoad;
    private ImageView sortByImageview;
    private String mOrderBy = "&orderBy=";
    private String mExtendedSearch = "";

    public static CarsListFragment newInstance() {
        CarsListFragment fragment = new CarsListFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCarSelectedListener = (OnCarSelectedListener) getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivity.progressDialogHide();
        mRowIndex = 0;
        mPosInList = -1;
        mCarsCount = -1;
        saveState();
    }

    private void saveState() {
        HashMap<String, String> params = mActivity.getmSearchParams();
        if (spinnerOrderCar.getAdapter() != null)
            if (spinnerOrderCar.getAdapter().getCount() != spinnerOrderCar.getSelectedItemPosition()) {
                params.put(STATE_ORDER, String.valueOf(spinnerOrderCar.getSelectedItemPosition()));
            }
        if (sortByImageview.isSelected())
            params.put(STATE_DESC, "1");
        if (!mSearchParams.isEmpty()) {
            params.put(STATE_URL_PARAMS, mSearchParams);
        }
        if (mCarListAdapter != null && mCarListAdapter.getCount() > 0) {
            params.put(STATE_ELEMENT_SIZE, mCarListAdapter.getCount() + "");

            if (listViewCars != null && listViewCars.getFirstVisiblePosition()>0) {
                params.put(STATE_POSITION, listViewCars.getFirstVisiblePosition() + "");
            }
        }
      //  spinnerOrderCar.setAdapter(new ArrayAdapter<String>(getActivity(),0));
        mCarListAdapter = null;
        mActivity.setmSearchParams(params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cars_list_fragment, container, false);
        findViews(rootView);
        initSpinner();
        setListeners();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadState();
    }

    private void loadState() {
        if (mActivity.getmSearchParams() != null) {
             HashMap<String, String> state = mActivity.getmSearchParams();
            if (state.containsKey(STATE_URL_PARAMS))
                mSearchParams = state.get(STATE_URL_PARAMS);
            if (state.containsKey(STATE_DESC))
                sortByImageview.setSelected(true);
            if (state.containsKey(STATE_ORDER)) {
                spinnerOrderCar.setSelection(Integer.parseInt(state.get(STATE_ORDER)));
                sortItems(null, Integer.parseInt(state.get(STATE_ORDER)));
            }

            if (state.containsKey(STATE_ELEMENT_SIZE)) {
                mCarsCount = Integer.parseInt(state.get(STATE_ELEMENT_SIZE));

            }
            if (state.containsKey(STATE_POSITION)) {
                mPosInList = Integer.parseInt(state.get(STATE_POSITION));

            }

            if (!state.containsKey(STATE_ORDER)) {
                initUI();
                spinnerOrderCar.setSelection(spinnerOrderCar.getAdapter().getCount());
            }
        } else {
            initUI();
        }
    }

    private void initUI() {
        mActivity.progressDialogShow();
        int tempIndex = CARS_PER_PAGE;
        if (mCarsCount != -1){
            tempIndex = mCarsCount;
            mCarsCount = -1;
        }
        final int finalTempIndex = tempIndex;
        CarsNewApi.getCarsList(mRowIndex, tempIndex, mSearchParams, new IResponse() {
            @Override
            public void onSuccess(Object response) {
                if (getActivity() != null) {
                    mCarItems = (ArrayList<Car>) response;
                    mNeedLoad = ((ArrayList<Car>) response).size() > 0;
                    if (!mNeedLoad)
                        mTextViewEmptyList.setVisibility(View.VISIBLE);
                    setAdapters();
                    if (mPosInList != -1){
                        listViewCars.setSelection(mPosInList);
                        mPosInList = -1;
                    }
                    mActivity.progressDialogHide();
                    mRowIndex += finalTempIndex;
                }
            }

            @Override
            public void onFailure(Object response) {
                if (getActivity() != null) {
                    mActivity.progressDialogHide();
                    Toast.makeText(getActivity(), "No result", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addNewElements() {
        progressBarLoad.setVisibility(View.VISIBLE);
        CarsNewApi.getCarsList(mRowIndex, CARS_PER_PAGE, mSearchParams + mExtendedSearch, new IResponse() {
            @Override
            public void onSuccess(Object response) {
                if (mCarItems == null)
                    mCarItems = new ArrayList<Car>();
                mNeedLoad = ((ArrayList<Car>) response).size() > 0;
                mCarItems.addAll((ArrayList<Car>) response);
                if (mCarListAdapter != null) {
                    mCarListAdapter.notifyDataSetChanged();
                    mRowIndex += CARS_PER_PAGE;
                }
                progressBarLoad.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Object response) {
                progressBarLoad.setVisibility(View.GONE);
            }
        });
    }

    private void findViews(View _rootView) {
        listViewCars = (ListView) _rootView.findViewById(R.id.listView_carsList_CarListFragment);
        spinnerOrderCar = (Spinner) _rootView.findViewById(R.id.orderBy_Spinner_CarListFragment);
        mTextViewBack = (TextView) _rootView.findViewById(R.id.textView_back_CarListFragment);
        mTextViewBack.setText(Html.fromHtml(getString(R.string.car_list_back)));
        mTextViewEmptyList = (TextView) _rootView.findViewById(R.id.textView_empty_list_CarListFragment);
        progressBarLoad = (ProgressBar) _rootView.findViewById(R.id.progress_ProgressBar_CarListFragment);
        ascDescLinearLayout = (LinearLayout)_rootView.findViewById(R.id.asc_desc_linearlayout_CarListFragment);
        ascendingTextView = (TextView)_rootView.findViewById(R.id.textView_asceding_CarListFragment);
        sortByImageview = (ImageView) _rootView.findViewById(R.id.asc_desc_ImageView_CarListFragment);
        ascDescLinearLayout = (LinearLayout)_rootView.findViewById(R.id.asc_desc_linearlayout_CarListFragment);

       ascDescLinearLayout.setOnClickListener(mCheckBoxClick);
        _rootView.findViewById(R.id.logOut_ImageView_SearchListFragment).setOnClickListener(mActivity.getLogOutListener());
    }

    private void setAdapters() {
        if (mCarItems == null || mCarItems.size() == 0) {
            mTextViewEmptyList.setVisibility(View.VISIBLE);
        } else {
            mTextViewEmptyList.setVisibility(View.GONE);
            if (getActivity() != null) {
                mCarListAdapter = new CarsListAdapter(getActivity(), R.layout.cars_list_item, mCarItems);
                listViewCars.setAdapter(mCarListAdapter);
            }
        }
    }

    private void setListeners() {
        listViewCars.setOnItemClickListener(this);
        spinnerOrderCar.setOnItemSelectedListener(this);
        mTextViewBack.setOnClickListener(this);
        listViewCars.setOnScrollListener(mScrollListener);
        mTextViewBack.setOnClickListener(this);
    }

    private void initSpinner() {
        String[] choiceArray = getResources().getStringArray(R.array.spinner_order_by_choice_array);
//        ArrayAdapter<String> adapter = getAdapter(choiceArray);

        spinnerOrderCar.setAdapter(getAdapter(choiceArray));
        spinnerOrderCar.post(new Runnable() {
            @Override
            public void run() {
                spinnerOrderCar.setSelection(0);
            }
        });
    }

    private BaseAdapter getAdapter(String[] choiceArray) {
        return new OrderListAdapter(mActivity, choiceArray);
    }


    private void sortItems(View view, int position) {
        if (mActivity.isProgressDialogShow()
                || progressBarLoad.getVisibility() == View.VISIBLE) {
            return;
        }
        if (position == spinnerOrderCar.getAdapter().getCount())
            return;
        if (view != null)
            view.setSelected(!view.isSelected());
        String orderingBy = getActivity().getResources().getStringArray(R.array.spinner_order_by_choice_array_value)[position];
        String resOrder = "";
        if (sortByImageview.isSelected())
            resOrder = mOrderBy + orderingBy + SORTED_DESC;
        else
            resOrder = mOrderBy + orderingBy + SORTED_ASK;
        mExtendedSearch = resOrder;
        mRowIndex = 0;
        mActivity.progressDialogShow();
        int tempIndex = CARS_PER_PAGE;
        if (mCarsCount != -1){
            tempIndex = mCarsCount;
            mCarsCount = -1;
        }
        final int finalTempIndex = tempIndex;
        CarsNewApi.getCarsList(mRowIndex, tempIndex, mSearchParams + mExtendedSearch, new IResponse() {
            @Override
            public void onSuccess(Object response) {
                mCarItems = new ArrayList<Car>();
                mNeedLoad = ((ArrayList<Car>) response).size() > 0;
                mCarItems.addAll((ArrayList<Car>) response);
                mCarListAdapter = new CarsListAdapter(getActivity(), R.layout.cars_list_item, mCarItems);
                listViewCars.setAdapter(mCarListAdapter);
                if (mPosInList != -1){
                    listViewCars.setSelection(mPosInList);
                    mPosInList = -1;
                }
                mCarListAdapter.notifyDataSetChanged();
                mRowIndex += finalTempIndex;
                mActivity.progressDialogHide();

            }

            @Override
            public void onFailure(Object response) {
                mActivity.progressDialogHide();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Car selectedCar = mCarItems.get(position);
        mCarSelectedListener.onCarSelected(selectedCar);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (mCarItems != null && mCarListAdapter != null) {
            sortItems(null, position);
            mCarListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void switchIcons(ImageView _imageView){
        if (_imageView.isActivated()){
            _imageView.setActivated(false);
            _imageView.setImageResource(R.drawable.sort_arrow_up_btn);
            ascendingTextView.setText(R.string.ascending);


        }else {
            _imageView.setImageResource(R.drawable.sort_arrow_down_btn);
            _imageView.setActivated(true);
            ascendingTextView.setText(R.string.descending);


        }
    }

    @Override
    public void onClick(View v) {
        getActivity().onBackPressed();
    }

    public interface OnCarSelectedListener {
        void onCarSelected(Car _car);
    }
}
