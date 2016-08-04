package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class DialogCloseCar extends android.databinding.ViewDataBinding  {

    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.txtTariff, 4);
        sViewsWithIds.put(R.id.txtPerMin, 5);
        sViewsWithIds.put(R.id.txtTotalUsage, 6);
        sViewsWithIds.put(R.id.txtParking, 7);
        sViewsWithIds.put(R.id.txtTotal, 8);
    }
    // views
    public final com.dd.CircularProgressButton btnCloseOrOpen;
    public final com.dd.CircularProgressButton btnCloseRent;
    public final com.dd.CircularProgressButton btnNavigate;
    private final android.widget.ScrollView mboundView0;
    public final android.widget.TextView txtParking;
    public final android.widget.TextView txtPerMin;
    public final android.widget.TextView txtTariff;
    public final android.widget.TextView txtTotal;
    public final android.widget.TextView txtTotalUsage;
    // variables
    private youdrive.today.activities.MapsActivity mListener;
    // values
    // listeners
    private OnClickListenerImpl mAndroidViewViewOnCl;
    private OnClickListenerImpl1 mAndroidViewViewOnCl1;
    private OnClickListenerImpl2 mAndroidViewViewOnCl2;
    // Inverse Binding Event Handlers

    public DialogCloseCar(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds);
        this.btnCloseOrOpen = (com.dd.CircularProgressButton) bindings[1];
        this.btnCloseOrOpen.setTag(null);
        this.btnCloseRent = (com.dd.CircularProgressButton) bindings[2];
        this.btnCloseRent.setTag(null);
        this.btnNavigate = (com.dd.CircularProgressButton) bindings[3];
        this.btnNavigate.setTag(null);
        this.mboundView0 = (android.widget.ScrollView) bindings[0];
        this.mboundView0.setTag(null);
        this.txtParking = (android.widget.TextView) bindings[7];
        this.txtPerMin = (android.widget.TextView) bindings[5];
        this.txtTariff = (android.widget.TextView) bindings[4];
        this.txtTotal = (android.widget.TextView) bindings[8];
        this.txtTotalUsage = (android.widget.TextView) bindings[6];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
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
            case BR.listener :
                setListener((youdrive.today.activities.MapsActivity) variable);
                return true;
        }
        return false;
    }

    public void setListener(youdrive.today.activities.MapsActivity listener) {
        this.mListener = listener;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.listener);
        super.requestRebind();
    }
    public youdrive.today.activities.MapsActivity getListener() {
        return mListener;
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
        youdrive.today.activities.MapsActivity listener = mListener;
        android.view.View.OnClickListener androidViewViewOnCli = null;
        android.view.View.OnClickListener androidViewViewOnCli1 = null;
        android.view.View.OnClickListener androidViewViewOnCli2 = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (listener != null) {
                    // read listener::onButtonNavigate
                    androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(listener));
                    // read listener::onCloseRent
                    androidViewViewOnCli1 = (((mAndroidViewViewOnCl1 == null) ? (mAndroidViewViewOnCl1 = new OnClickListenerImpl1()) : mAndroidViewViewOnCl1).setValue(listener));
                    // read listener::onCloseOrOpen
                    androidViewViewOnCli2 = (((mAndroidViewViewOnCl2 == null) ? (mAndroidViewViewOnCl2 = new OnClickListenerImpl2()) : mAndroidViewViewOnCl2).setValue(listener));
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.btnCloseOrOpen.setOnClickListener(androidViewViewOnCli2);
            this.btnCloseRent.setOnClickListener(androidViewViewOnCli1);
            this.btnNavigate.setOnClickListener(androidViewViewOnCli);
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
            this.value.onButtonNavigate(arg0);
        }
    }
    public static class OnClickListenerImpl1 implements android.view.View.OnClickListener{
        private youdrive.today.activities.MapsActivity value;
        public OnClickListenerImpl1 setValue(youdrive.today.activities.MapsActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onCloseRent(arg0);
        }
    }
    public static class OnClickListenerImpl2 implements android.view.View.OnClickListener{
        private youdrive.today.activities.MapsActivity value;
        public OnClickListenerImpl2 setValue(youdrive.today.activities.MapsActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onCloseOrOpen(arg0);
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    public static DialogCloseCar inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static DialogCloseCar inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<DialogCloseCar>inflate(inflater, youdrive.today.R.layout.popup_close_car, root, attachToRoot, bindingComponent);
    }
    public static DialogCloseCar inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static DialogCloseCar inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.popup_close_car, null, false), bindingComponent);
    }
    public static DialogCloseCar bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static DialogCloseCar bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/popup_close_car_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new DialogCloseCar(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): listener
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}