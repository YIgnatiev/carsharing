package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class DialogInfo extends android.databinding.ViewDataBinding  {

    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    public final com.dd.CircularProgressButton btnBook;
    private final android.widget.LinearLayout mboundView0;
    private final android.widget.TextView mboundView10;
    private final android.widget.RelativeLayout mboundView9;
    public final android.widget.TextView txtDistance;
    public final android.widget.TextView txtFuel;
    public final android.widget.TextView txtModel;
    public final android.widget.TextView txtNumber;
    public final android.widget.TextView txtTaxDrive;
    public final android.widget.TextView txtTaxPark;
    public final android.widget.TextView txtTimeTo;
    public final android.widget.TextView txtType;
    // variables
    private youdrive.today.models.Car mCar;
    private youdrive.today.activities.MapsActivity mActivity;
    // values
    // listeners
    private OnClickListenerImpl mAndroidViewViewOnCl;
    // Inverse Binding Event Handlers

    public DialogInfo(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 12, sIncludes, sViewsWithIds);
        this.btnBook = (com.dd.CircularProgressButton) bindings[11];
        this.btnBook.setTag(null);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView10 = (android.widget.TextView) bindings[10];
        this.mboundView10.setTag(null);
        this.mboundView9 = (android.widget.RelativeLayout) bindings[9];
        this.mboundView9.setTag(null);
        this.txtDistance = (android.widget.TextView) bindings[2];
        this.txtDistance.setTag(null);
        this.txtFuel = (android.widget.TextView) bindings[8];
        this.txtFuel.setTag(null);
        this.txtModel = (android.widget.TextView) bindings[1];
        this.txtModel.setTag(null);
        this.txtNumber = (android.widget.TextView) bindings[5];
        this.txtNumber.setTag(null);
        this.txtTaxDrive = (android.widget.TextView) bindings[6];
        this.txtTaxDrive.setTag(null);
        this.txtTaxPark = (android.widget.TextView) bindings[7];
        this.txtTaxPark.setTag(null);
        this.txtTimeTo = (android.widget.TextView) bindings[3];
        this.txtTimeTo.setTag(null);
        this.txtType = (android.widget.TextView) bindings[4];
        this.txtType.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    public boolean setVariable(int variableId, Object variable) {
        switch(variableId) {
            case BR.car :
                setCar((youdrive.today.models.Car) variable);
                return true;
            case BR.activity :
                setActivity((youdrive.today.activities.MapsActivity) variable);
                return true;
        }
        return false;
    }

    public void setCar(youdrive.today.models.Car car) {
        this.mCar = car;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.car);
        super.requestRebind();
    }
    public youdrive.today.models.Car getCar() {
        return mCar;
    }
    public void setActivity(youdrive.today.activities.MapsActivity activity) {
        this.mActivity = activity;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.activity);
        super.requestRebind();
    }
    public youdrive.today.activities.MapsActivity getActivity() {
        return mActivity;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        youdrive.today.models.Car car = mCar;
        int parkingTariffCar = 0;
        int carGetDiscountCar = 0;
        long sECONDSTimeUnitToMin = 0L;
        youdrive.today.models.Tariff tariffCar = null;
        java.lang.String transmissionCar = null;
        boolean numberCarObjectnull = false;
        int usageTariffCar = 0;
        boolean carGetDiscountCarInt = false;
        java.lang.String idCar = null;
        java.lang.String SECONDSTimeUnitToMin1 = null;
        java.lang.String appUtilsToKmAppUtils = null;
        int walktimeCar = 0;
        java.lang.Integer fuelCar = null;
        android.view.View.OnClickListener androidViewViewOnCli = null;
        java.lang.String activityConvertRubPe = null;
        youdrive.today.activities.MapsActivity activity = mActivity;
        java.lang.String modelCar = null;
        java.lang.String activityConvertRubPe1 = null;
        java.lang.String stringCarGetDiscount = null;
        java.lang.String fuelCarString = null;
        int CarGetDiscountCarInt1 = 0;
        boolean fuelCarObjectnull = false;
        java.lang.String StringCarGetDiscount1 = null;
        int distanceCar = 0;
        java.lang.String numberCar = null;
        java.lang.String numberCarObjectnullS = null;
        java.lang.String fuelCarObjectnullAnd = null;

        if ((dirtyFlags & 0x7L) != 0) {


            if ((dirtyFlags & 0x5L) != 0) {

                    if (car != null) {
                        // read car.getDiscount()
                        carGetDiscountCar = car.getDiscount();
                        // read car.transmission
                        transmissionCar = car.getTransmission();
                        // read car.id
                        idCar = car.getId();
                        // read car.walktime
                        walktimeCar = car.getWalktime();
                        // read car.fuel
                        fuelCar = car.getFuel();
                        // read car.model
                        modelCar = car.getModel();
                        // read car.distance
                        distanceCar = car.getDistance();
                        // read car.number
                        numberCar = car.getNumber();
                    }


                    // read car.getDiscount() == 0
                    carGetDiscountCarInt = (carGetDiscountCar) == (0);
                    // read ("-") + (car.getDiscount())
                    StringCarGetDiscount1 = ("-") + (carGetDiscountCar);
                    // read TimeUnit.SECONDS.toMinutes(car.walktime)
                    sECONDSTimeUnitToMin = java.util.concurrent.TimeUnit.SECONDS.toMinutes(walktimeCar);
                    // read car.fuel == null
                    fuelCarObjectnull = (fuelCar) == (null);
                    // read AppUtils.toKm(car.distance)
                    appUtilsToKmAppUtils = youdrive.today.helpers.AppUtils.toKm(distanceCar);
                    // read car.number == null
                    numberCarObjectnull = (numberCar) == (null);
                    if((dirtyFlags & 0x5L) != 0) {
                        if (carGetDiscountCarInt) {
                            dirtyFlags |= 0x10L;
                        } else {
                            dirtyFlags |= 0x8L;
                        }}
                    if((dirtyFlags & 0x5L) != 0) {
                        if (fuelCarObjectnull) {
                            dirtyFlags |= 0x100L;
                        } else {
                            dirtyFlags |= 0x80L;
                        }}
                    if((dirtyFlags & 0x5L) != 0) {
                        if (numberCarObjectnull) {
                            dirtyFlags |= 0x40L;
                        } else {
                            dirtyFlags |= 0x20L;
                        }}


                    // read car.getDiscount() == 0 ? View.GONE : View.VISIBLE
                    CarGetDiscountCarInt1 = (carGetDiscountCarInt) ? (android.view.View.GONE) : (android.view.View.VISIBLE);
                    // read (("-") + (car.getDiscount())) + ("%")
                    stringCarGetDiscount = (StringCarGetDiscount1) + ("%");
                    // read (TimeUnit.SECONDS.toMinutes(car.walktime)) + (" min.")
                    SECONDSTimeUnitToMin1 = (sECONDSTimeUnitToMin) + (" min.");
            }

                if (car != null) {
                    // read car.tariff
                    tariffCar = car.getTariff();
                }


                if (tariffCar != null) {
                    // read car.tariff.parking
                    parkingTariffCar = tariffCar.getParking();
                    // read car.tariff.usage
                    usageTariffCar = tariffCar.getUsage();
                }


                if (activity != null) {
                    // read activity.convertRubPerMin(car.tariff.parking)
                    activityConvertRubPe1 = activity.convertRubPerMin(parkingTariffCar);
                    // read activity.convertRubPerMin(car.tariff.usage)
                    activityConvertRubPe = activity.convertRubPerMin(usageTariffCar);
                }
            if ((dirtyFlags & 0x6L) != 0) {

                    if (activity != null) {
                        // read activity::onBookClicked
                        androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(activity));
                    }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x80L) != 0) {

                // read (car.fuel) + (" %")
                fuelCarString = (fuelCar) + (" %");
        }
        if ((dirtyFlags & 0x5L) != 0) {

                // read car.number == null ? "unknown" : car.number
                numberCarObjectnullS = (numberCarObjectnull) ? ("unknown") : (numberCar);
        }

        if ((dirtyFlags & 0x5L) != 0) {

                // read car.fuel == null ? @android:string/unknown : (car.fuel) + (" %")
                fuelCarObjectnullAnd = (fuelCarObjectnull) ? (getRoot().getResources().getString(R.string.unknown)) : (fuelCarString);
        }
        // batch finished
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            this.btnBook.setOnClickListener(androidViewViewOnCli);
        }
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1

            this.btnBook.setTag(idCar);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView10, stringCarGetDiscount);
            this.mboundView9.setVisibility(CarGetDiscountCarInt1);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.txtDistance, appUtilsToKmAppUtils);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.txtFuel, fuelCarObjectnullAnd);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.txtModel, modelCar);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.txtNumber, numberCarObjectnullS);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.txtTimeTo, SECONDSTimeUnitToMin1);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.txtType, transmissionCar);
        }
        if ((dirtyFlags & 0x7L) != 0) {
            // api target 1

            android.databinding.adapters.TextViewBindingAdapter.setText(this.txtTaxDrive, activityConvertRubPe);
            android.databinding.adapters.TextViewBindingAdapter.setText(this.txtTaxPark, activityConvertRubPe1);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private youdrive.today.activities.MapsActivity value;
        public OnClickListenerImpl setValue(youdrive.today.activities.MapsActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onBookClicked(arg0);
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    public static DialogInfo inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static DialogInfo inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<DialogInfo>inflate(inflater, youdrive.today.R.layout.dialog_info_contents, root, attachToRoot, bindingComponent);
    }
    public static DialogInfo inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static DialogInfo inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.dialog_info_contents, null, false), bindingComponent);
    }
    public static DialogInfo bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static DialogInfo bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/dialog_info_contents_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new DialogInfo(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): car
        flag 1 (0x2L): activity
        flag 2 (0x3L): null
        flag 3 (0x4L): car.getDiscount() == 0 ? View.GONE : View.VISIBLE
        flag 4 (0x5L): car.getDiscount() == 0 ? View.GONE : View.VISIBLE
        flag 5 (0x6L): car.number == null ? "unknown" : car.number
        flag 6 (0x7L): car.number == null ? "unknown" : car.number
        flag 7 (0x8L): car.fuel == null ? @android:string/unknown : (car.fuel) + (" %")
        flag 8 (0x9L): car.fuel == null ? @android:string/unknown : (car.fuel) + (" %")
    flag mapping end*/
    //end
}