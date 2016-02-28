package com.carusselgroup.dwt.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.carusselgroup.dwt.Network;
import com.carusselgroup.dwt.R;
import com.carusselgroup.dwt.model.Car;
import com.carusselgroup.dwt.model.SpinnerPack;
import com.carusselgroup.dwt.rest.CarsNewApi;
import com.carusselgroup.dwt.rest.IResponse;
import com.carusselgroup.dwt.ui.activity.MainActivity;
import com.carusselgroup.dwt.utils.SearchFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class SearchFragment extends Fragment implements OnClickListener {


    public static final String EMPTY_SPINNER_ITEM = " ";
    private static String SEARCH_STATUS = "condition";
    private static String SEARCH_BRAND = "makeId";
    private static String SEARCH_MODEL = "modelGroupId";
    private static String SEARCH_JOB_NO = "jobno";
    private static String SHOW_NO_IMAGES = "noImage";
    private int mCurrentCars;
    private boolean showNoImages = false;
    private HashMap<String, String> mSearchParams = new HashMap<String, String>();
    private EditText jobNumberField;
    private ImageView loadImagesImageView;
    private ArrayList<Car> mBrandCars, mStatusCars, mSModelCars;
    private Map<Integer, Boolean> spinnerListener;
    private SpinnerPack statusPack, brandPack, modelPack, locationPack;


    private TextView searchButton;
    private OnSearchListener mSearchListener;
    private MainActivity mActivity;
    private OnItemSelectedListener mSpinnerListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         jobNumberField.clearFocus();
            if (!spinnerListener.get(parent.getId())) {
                spinnerListener.put(parent.getId(), true);
                return;
            }

            switch (parent.getId()) {
                case R.id.status_Spinner_SearchFragment:
                    findByStatus();
                    break;
                case R.id.brand_Spinner_SearchFragment:
                    findByBrand();
                    break;
                case R.id.model_Spinner_SearchFragment:

                        findByModel();

                    break;
                case R.id.location_Spinner_SearchFragment:
                    //findByLocation();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSearchListener = (OnSearchListener) activity;
        mActivity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_fragment, container, false);
        initView(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSearchParams = new HashMap<>();

        fillSpinner(statusPack, SearchFilter.getStatusSet());
        findByStatus();
    }

    private void initView(View v) {
        statusPack = getEmptySpinnerPack((Spinner) v.findViewById(R.id.status_Spinner_SearchFragment));

        loadImagesImageView = (ImageView) v.findViewById(R.id.search_ImageView_checkBox);
        loadImagesImageView.setOnClickListener(this);

        brandPack = getEmptySpinnerPack((Spinner) v.findViewById(R.id.brand_Spinner_SearchFragment));
        brandPack.disable();

        modelPack = getEmptySpinnerPack((Spinner) v.findViewById(R.id.model_Spinner_SearchFragment));
       // modelPack.disable();


        locationPack = getEmptySpinnerPack((Spinner) v.findViewById(R.id.location_Spinner_SearchFragment));
        locationPack.disable();

        jobNumberField = (EditText) v.findViewById(R.id.job_Number_EditText_SearchFragment);

        searchButton = (TextView)v.findViewById(R.id.search_Button_SearchFragment);
        searchButton.setOnClickListener(this);
        v.findViewById(R.id.logOut_ImageView_SearchCarFragment).setOnClickListener(mActivity.getLogOutListener());
        setSpinnersListener();
    }

    private SpinnerPack getEmptySpinnerPack(Spinner spinner) {
        ArrayList<String> data = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.search_fragment_spinner_item, R.id.Item_TextView_Spinner, data);
        return new SpinnerPack(spinner, adapter, data);
    }

    private void setSpinnersListener() {
        spinnerListener = new HashMap<>();
        spinnerListener.put(R.id.status_Spinner_SearchFragment, false);
        spinnerListener.put(R.id.brand_Spinner_SearchFragment, false);
        spinnerListener.put(R.id.model_Spinner_SearchFragment, false);
        spinnerListener.put(R.id.location_Spinner_SearchFragment, false);

        statusPack.setListener(mSpinnerListener);
        brandPack.setListener(mSpinnerListener);
        modelPack.setListener(mSpinnerListener);
        locationPack.setListener(mSpinnerListener);
    }

    private void getCarsList(String params, IResponse carListResponse) {
        mActivity.progressDialogShow();
        CarsNewApi.getCarsList(0, 100, params, carListResponse);
    }

    private void fillSpinner(SpinnerPack pack, Set<String> newData) {
        if (newData != null) {
            newData.add(SearchFragment.EMPTY_SPINNER_ITEM);
            pack.replaceData(newData);
            pack.setSelection(0);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.search_ImageView_checkBox:
                switchIcons(loadImagesImageView);
                break;
            case R.id.search_Button_SearchFragment:

                if (!isConnected()){
                    showDialog(getString(R.string.sorry_error_get_data));
                    return;
                }
                if (mCurrentCars == 0){
                    showDialog(getString(R.string.no_cars_found_dialog));
                    return;
                }


                if (!jobNumberField.getText().toString().isEmpty()) {
                    if (mSearchParams == null)
                        mSearchParams = new HashMap<>();
                    mSearchParams.put(SEARCH_JOB_NO, jobNumberField.getText().toString());
                }

                mSearchListener.onSearchListener(SearchFilter.paramsToString(mSearchParams));
                break;

        }


    }


    private void showDialog(String _message) {
    View viewGroup = mActivity.getLayoutInflater().inflate(R.layout.warning_dialog_layout, new LinearLayout(mActivity));

    TextView textViewInfo= (TextView) viewGroup.findViewById(R.id.textView_warning_dialog);
    TextView textViewButton= (TextView) viewGroup.findViewById(R.id.textView_warning_dialog_ok);
    textViewInfo.setText(_message);

    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
    builder.setView(viewGroup);

   final AlertDialog adDialog = builder.create();

    //adDialog.setOnShowListener(this);
    textViewButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            adDialog.cancel();
        }
    });
    adDialog.show();
}

    private void findByStatus() {
        String status = statusPack.getSelectedValue();
        if (status.equals(EMPTY_SPINNER_ITEM)) {
            modelPack.enable();

            mSearchParams = new HashMap<>();

            mSearchParams.put(SHOW_NO_IMAGES, String.valueOf(showNoImages));
            mActivity.progressDialogShow();
            mStatusCars = new ArrayList<Car>();
            String params = SearchFilter.paramsToString(mSearchParams);
            getCarsList(SearchFilter.paramsToString(mSearchParams), new IResponse() {
                @Override
                public void onSuccess(Object response) {
                    ArrayList<Car> list = (ArrayList<Car>)response;
                    fillSpinner(brandPack, SearchFilter.getBrandSet(list));
                    mCurrentCars = list.size();
                    searchButton.setText(getString(R.string.search)+ " (" + mCurrentCars+ ")");
                    brandPack.enable();
                    mActivity.progressDialogHide();
                    mStatusCars.addAll(list);
                }

                @Override
                public void onFailure(Object response) {
                    fillSpinner(brandPack, SearchFilter.getBrandSet(new ArrayList<Car>()));
                    brandPack.disable();
                    mActivity.progressDialogHide();
                }
            });
            ;

        } else {
            mSearchParams = new HashMap<>();
            mSearchParams.put(SEARCH_STATUS, SearchFilter.getStatusValue(status));
            mSearchParams.put(SHOW_NO_IMAGES, String.valueOf(showNoImages));
            mActivity.progressDialogShow();
            mStatusCars = new ArrayList<Car>();

            String params = SearchFilter.paramsToString(mSearchParams);
            getCarsList(SearchFilter.paramsToString(mSearchParams), new IResponse() {
                @Override
                public void onSuccess(Object response) {
                    fillSpinner(brandPack, SearchFilter.getBrandSet((ArrayList<Car>) response));
                    brandPack.enable();
                    mActivity.progressDialogHide();
                    mStatusCars.addAll((ArrayList<Car>) response);
                    mCurrentCars = mStatusCars.size();
                    searchButton.setText(getString(R.string.search)+ " (" + mCurrentCars + ")");
                }

                @Override
                public void onFailure(Object response) {
                    fillSpinner(brandPack, SearchFilter.getBrandSet(new ArrayList<Car>()));
                    brandPack.disable();
                    mActivity.progressDialogHide();
                }
            });

        }
       // modelPack.disable();
        locationPack.disable();
    }

    private void findByBrand() {

        String brand = brandPack.getSelectedValue();

        if (brand.equals(EMPTY_SPINNER_ITEM)) {
           // modelPack.disable();
            mSearchParams.put(SEARCH_BRAND, "");
            mSearchParams.put(SEARCH_MODEL, "");
            mSearchParams.put(SHOW_NO_IMAGES, String.valueOf(showNoImages));

        } else {
            for (Car car : mStatusCars) {
                if (car.getBrand().equals(brand)) {
                    mSearchParams = new HashMap<>();
                    mSearchParams.put(SEARCH_STATUS, car.getRawStatus());
                    mSearchParams.put(SEARCH_BRAND, car.getMake().getId() + "");
                    mSearchParams.put(SHOW_NO_IMAGES, String.valueOf(showNoImages));
                    break;
                }
            }

            mActivity.progressDialogShow();
            mBrandCars = new ArrayList<Car>();

            getCarsList(SearchFilter.paramsToString(mSearchParams), new IResponse() {
                @Override
                public void onSuccess(Object response) {
                    fillSpinner(modelPack, SearchFilter.getModelSet((ArrayList<Car>) response));
                    modelPack.enable();
                    mActivity.progressDialogHide();
                    mBrandCars.addAll((ArrayList<Car>) response);
                    mCurrentCars = mBrandCars.size();
                    searchButton.setText(getString(R.string.search)+ " (" + mCurrentCars+ ")");
                }

                @Override
                public void onFailure(Object response) {
                    fillSpinner(modelPack, SearchFilter.getBrandSet(new ArrayList<Car>()));
                   // modelPack.disable();
                    mActivity.progressDialogHide();
                }
            });

        }
        locationPack.disable();
    }

    private void findByModel() {
        String model = modelPack.getSelectedValue();


        if (model.equals(EMPTY_SPINNER_ITEM)) {
            locationPack.disable();
            mSearchParams.put(SEARCH_MODEL, "");
        } else {
            for (Car car : mBrandCars) {
                if (car.getModel().equals(model)) {
                    mSearchParams = new HashMap<>();
                    mSearchParams.put(SEARCH_STATUS, car.getRawStatus());
                    mSearchParams.put(SEARCH_BRAND, car.getMake().getId() + "");
                    mSearchParams.put(SEARCH_MODEL, car.getModelGroup().getId() + "");
                    mSearchParams.put(SHOW_NO_IMAGES, String.valueOf(showNoImages));
                    break;
                }
            }

            mActivity.progressDialogShow();
            mSModelCars = new ArrayList<Car>();

            getCarsList(SearchFilter.paramsToString(mSearchParams), new IResponse() {
                @Override
                public void onSuccess(Object response) {
                    fillSpinner(locationPack, SearchFilter.getLocationSet((ArrayList<Car>) response));
                    locationPack.enable();
                    mActivity.progressDialogHide();
                    mSModelCars.addAll((ArrayList<Car>) response);
                    mCurrentCars = mSModelCars.size();
                    searchButton.setText(getString(R.string.search)+ " (" + mSModelCars.size()+ ")");
                }

                @Override
                public void onFailure(Object response) {
                    fillSpinner(locationPack, SearchFilter.getBrandSet(new ArrayList<Car>()));
                    locationPack.disable();
                    mActivity.progressDialogHide();
                }
            });
        }
    }

    private void findByLocation() {
//        ArrayList<Car> cars;
//        String location = locationPack.getSelectedValue();
//        cars = SearchFilter.getCarsByStatus(mCars, statusPack.getSelectedValue());
//        cars = SearchFilter.getCarsByBrand(cars, brandPack.getSelectedValue());
//        cars = SearchFilter.getCarsByModel(cars, modelPack.getSelectedValue());
//        cars = SearchFilter.getCarsByLocation(cars, location);
//        mVisibleCars = cars;
    }

    private boolean isConnected() {
        boolean connected = false;

        if (Network.isInternetConnectionAvailable(getActivity())) {
            return true;
        } else {
            return false;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSearchListener = null;
    }


    private void switchIcons(ImageView _imageView) {
        if (_imageView.isActivated()) {
            _imageView.setActivated(false);
            _imageView.setImageResource(R.drawable.search_checkbox_checked);

            showNoImages = false;

        } else {
            _imageView.setImageResource(R.drawable.search_checkbox_unchecked);
            _imageView.setActivated(true);
            showNoImages = true;
        }
    }

    public interface OnSearchListener {
        void onSearchListener(String params);
    }
}
