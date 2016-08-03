package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class DialogInfo extends android.databinding.ViewDataBinding {
    
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
        long SECONDSTimeUnitToMin = 0L;
        java.lang.String FuelCarObjectnullAnd = null;
        youdrive.today.models.Tariff tariffCar = null;
        java.lang.String transmissionCar = null;
        boolean NumberCarObjectnull = false;
        int usageTariffCar = 0;
        boolean CarGetDiscountCarInt = false;
        java.lang.String idCar = null;
        java.lang.String SECONDSTimeUnitToMin1 = null;
        java.lang.String AppUtilsToKmAppUtils = null;
        int walktimeCar = 0;
        java.lang.Integer fuelCar = null;
        android.view.View.OnClickListener androidViewViewOnCli = null;
        java.lang.String activityConvertRubPe = null;
        java.lang.String fuelCarStringLitr = null;
        youdrive.today.activities.MapsActivity activity = mActivity;
        java.lang.String modelCar = null;
        java.lang.String activityConvertRubPe1 = null;
        java.lang.String StringCarGetDiscount = null;
        int CarGetDiscountCarInt1 = 0;
        boolean FuelCarObjectnull = false;
        java.lang.String StringCarGetDiscount1 = null;
        int distanceCar = 0;
        java.lang.String numberCar = null;
        java.lang.String NumberCarObjectnullS = null;
    
        if ((dirtyFlags & 0x7L) != 0) {
            // read car~
            car = car;
            // read activity~
            activity = activity;
        
            if ((dirtyFlags & 0x5L) != 0) {
                if (car != null) {
                    // read car~~getDiscount~car~
                    carGetDiscountCar = car.getDiscount();
                    // read transmission~.~car~
                    transmissionCar = car.getTransmission();
                    // read id~.~car~
                    idCar = car.getId();
                    // read walktime~.~car~
                    walktimeCar = car.getWalktime();
                    // read fuel~.~car~
                    fuelCar = car.getFuel();
                    // read model~.~car~
                    modelCar = car.getModel();
                    // read distance~.~car~
                    distanceCar = car.getDistance();
                    // read number~.~car~
                    numberCar = car.getNumber();
                }
            
                // read ==~car~~getDiscount~car~~int0
                CarGetDiscountCarInt = carGetDiscountCar==0;
                // read String"-"~+~car~~getDiscount~car~
                StringCarGetDiscount1 = "-"+carGetDiscountCar;
                // read SECONDS~.~TimeUnit~~toMinutes~SECONDS~.~TimeUnit~~walktime~.~car~
                SECONDSTimeUnitToMin = java.util.concurrent.TimeUnit.SECONDS.toMinutes(walktimeCar);
                // read ==~fuel~.~car~~Objectnull
                FuelCarObjectnull = fuelCar==null;
                // read AppUtils~~toKm~AppUtils~~distance~.~car~
                AppUtilsToKmAppUtils = youdrive.today.helpers.AppUtils.toKm(distanceCar);
                // read ==~number~.~car~~Objectnull
                NumberCarObjectnull = numberCar==null;
                if((dirtyFlags & 0x5L) != 0) {
                    if (CarGetDiscountCarInt) {
                        dirtyFlags |= 0x40L;
                    } else {
                        dirtyFlags |= 0x20L;
                    }}
                if((dirtyFlags & 0x5L) != 0) {
                    if (FuelCarObjectnull) {
                        dirtyFlags |= 0x10L;
                    } else {
                        dirtyFlags |= 0x8L;
                    }}
                if((dirtyFlags & 0x5L) != 0) {
                    if (NumberCarObjectnull) {
                        dirtyFlags |= 0x100L;
                    } else {
                        dirtyFlags |= 0x80L;
                    }}
            
                // read ?:==~car~~getDiscount~car~~int0~GONE~.~View~~VISIBLE~.~View~
                CarGetDiscountCarInt1 = CarGetDiscountCarInt ? android.view.View.GONE : android.view.View.VISIBLE;
                // read String"-"~+~car~~getDiscount~car~~+~String"%"
                StringCarGetDiscount = StringCarGetDiscount1+"%";
                // read SECONDS~.~TimeUnit~~toMinutes~SECONDS~.~TimeUnit~~walktime~.~car~~+~String" min."
                SECONDSTimeUnitToMin1 = SECONDSTimeUnitToMin+" min.";
            }
            if (car != null) {
                // read tariff~.~car~
                tariffCar = car.getTariff();
            }
        
            if (tariffCar != null) {
                // read parking~.~tariff~.~car~
                parkingTariffCar = tariffCar.getParking();
                // read usage~.~tariff~.~car~
                usageTariffCar = tariffCar.getUsage();
            }
        
            if (activity != null) {
                // read activity~~convertRubPerMin~activity~~parking~.~tariff~.~car~
                activityConvertRubPe1 = activity.convertRubPerMin(parkingTariffCar);
                // read activity~~convertRubPerMin~activity~~usage~.~tariff~.~car~
                activityConvertRubPe = activity.convertRubPerMin(usageTariffCar);
            }
            if ((dirtyFlags & 0x6L) != 0) {
                if (activity != null) {
                    // read android.view.View.OnClickListener~activity~~onBookClicked
                    androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(activity));
                }
            }
        }
        // batch finished
    
        if ((dirtyFlags & 0x8L) != 0) {
            // read fuel~.~car~~+~String" litr"
            fuelCarStringLitr = fuelCar+" litr";
        }
        if ((dirtyFlags & 0x5L) != 0) {
            // read ?:==~number~.~car~~Objectnull~String"unknown"~number~.~car~
            NumberCarObjectnullS = NumberCarObjectnull ? "unknown" : numberCar;
        }
    
        if ((dirtyFlags & 0x5L) != 0) {
            // read ?:==~fuel~.~car~~Objectnull~@android:string/unknown~~fuel~.~car~~+~String" litr"
            FuelCarObjectnullAnd = FuelCarObjectnull ? getRoot().getResources().getString(R.string.unknown) : fuelCarStringLitr;
        }
        // batch finished
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1
            this.btnBook.setOnClickListener(androidViewViewOnCli);
        }
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1
            this.btnBook.setTag(idCar);
            this.mboundView10.setText(StringCarGetDiscount);
            this.mboundView9.setVisibility(CarGetDiscountCarInt1);
            this.txtDistance.setText(AppUtilsToKmAppUtils);
            this.txtFuel.setText(FuelCarObjectnullAnd);
            this.txtModel.setText(modelCar);
            this.txtNumber.setText(NumberCarObjectnullS);
            this.txtTimeTo.setText(SECONDSTimeUnitToMin1);
            this.txtType.setText(transmissionCar);
        }
        if ((dirtyFlags & 0x7L) != 0) {
            // api target 1
            this.txtTaxDrive.setText(activityConvertRubPe);
            this.txtTaxPark.setText(activityConvertRubPe1);
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
}
    /* flag mapping
        flag 0: car~
        flag 1: activity~
        flag 2: INVALIDATE ANY
        flag 3: ?:==~fuel~.~car~~Objectnull~@android:string/unknown~~fuel~.~car~~+~String" litr"== false
        flag 4: ?:==~fuel~.~car~~Objectnull~@android:string/unknown~~fuel~.~car~~+~String" litr"== true
        flag 5: ?:==~car~~getDiscount~car~~int0~GONE~.~View~~VISIBLE~.~View~== false
        flag 6: ?:==~car~~getDiscount~car~~int0~GONE~.~View~~VISIBLE~.~View~== true
        flag 7: ?:==~number~.~car~~Objectnull~String"unknown"~number~.~car~== false
        flag 8: ?:==~number~.~car~~Objectnull~String"unknown"~number~.~car~== true
    flag mapping end*/
    //end