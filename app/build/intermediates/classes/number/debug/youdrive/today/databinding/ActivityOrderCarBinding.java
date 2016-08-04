package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class ActivityOrderCarBinding extends android.databinding.ViewDataBinding  {

    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar, 2);
        sViewsWithIds.put(R.id.imgCar, 3);
        sViewsWithIds.put(R.id.txtModel, 4);
        sViewsWithIds.put(R.id.txtNumber, 5);
        sViewsWithIds.put(R.id.txtColor, 6);
    }
    // views
    public final com.dd.CircularProgressButton btnShowOnMap;
    public final android.widget.ImageView imgCar;
    private final android.widget.RelativeLayout mboundView0;
    public final android.support.v7.widget.Toolbar toolbar;
    public final android.widget.TextView txtColor;
    public final android.widget.TextView txtModel;
    public final android.widget.TextView txtNumber;
    // variables
    private youdrive.today.activities.BookCarActivity mListener;
    // values
    // listeners
    private OnClickListenerImpl mAndroidViewViewOnCl;
    // Inverse Binding Event Handlers

    public ActivityOrderCarBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds);
        this.btnShowOnMap = (com.dd.CircularProgressButton) bindings[1];
        this.btnShowOnMap.setTag(null);
        this.imgCar = (android.widget.ImageView) bindings[3];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.toolbar = (android.support.v7.widget.Toolbar) bindings[2];
        this.txtColor = (android.widget.TextView) bindings[6];
        this.txtModel = (android.widget.TextView) bindings[4];
        this.txtNumber = (android.widget.TextView) bindings[5];
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
                setListener((youdrive.today.activities.BookCarActivity) variable);
                return true;
        }
        return false;
    }

    public void setListener(youdrive.today.activities.BookCarActivity listener) {
        this.mListener = listener;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.listener);
        super.requestRebind();
    }
    public youdrive.today.activities.BookCarActivity getListener() {
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
        youdrive.today.activities.BookCarActivity listener = mListener;
        android.view.View.OnClickListener androidViewViewOnCli = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (listener != null) {
                    // read listener::onShow
                    androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(listener));
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.btnShowOnMap.setOnClickListener(androidViewViewOnCli);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private youdrive.today.activities.BookCarActivity value;
        public OnClickListenerImpl setValue(youdrive.today.activities.BookCarActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onShow(arg0);
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    public static ActivityOrderCarBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityOrderCarBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivityOrderCarBinding>inflate(inflater, youdrive.today.R.layout.activity_order_car, root, attachToRoot, bindingComponent);
    }
    public static ActivityOrderCarBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityOrderCarBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.activity_order_car, null, false), bindingComponent);
    }
    public static ActivityOrderCarBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityOrderCarBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_order_car_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivityOrderCarBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): listener
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}