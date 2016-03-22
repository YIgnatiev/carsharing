package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class OpenCarDialog extends android.databinding.ViewDataBinding {
    
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    public final com.dd.CircularProgressButton btnCancel;
    public final com.dd.CircularProgressButton btnNavigate;
    public final com.dd.CircularProgressButton btnOpen;
    private final android.widget.LinearLayout mboundView0;
    // variables
    private youdrive.today.activities.MapsActivity mListener;
    // values
    // listeners
    private OnClickListenerImpl mAndroidViewViewOnCl;
    private OnClickListenerImpl1 mAndroidViewViewOnCl1;
    private OnClickListenerImpl2 mAndroidViewViewOnCl2;
    
    public OpenCarDialog(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds);
        this.btnCancel = (com.dd.CircularProgressButton) bindings[1];
        this.btnCancel.setTag(null);
        this.btnNavigate = (com.dd.CircularProgressButton) bindings[3];
        this.btnNavigate.setTag(null);
        this.btnOpen = (com.dd.CircularProgressButton) bindings[2];
        this.btnOpen.setTag(null);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
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
            // read listener~
            listener = listener;
        
            if (listener != null) {
                // read android.view.View.OnClickListener~listener~~onButtonNavigate
                androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onButtonOpen
                androidViewViewOnCli1 = (((mAndroidViewViewOnCl1 == null) ? (mAndroidViewViewOnCl1 = new OnClickListenerImpl1()) : mAndroidViewViewOnCl1).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onButtonCancel
                androidViewViewOnCli2 = (((mAndroidViewViewOnCl2 == null) ? (mAndroidViewViewOnCl2 = new OnClickListenerImpl2()) : mAndroidViewViewOnCl2).setValue(listener));
            }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1
            this.btnCancel.setOnClickListener(androidViewViewOnCli2);
            this.btnNavigate.setOnClickListener(androidViewViewOnCli);
            this.btnOpen.setOnClickListener(androidViewViewOnCli1);
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
            this.value.onButtonOpen(arg0);
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
            this.value.onButtonCancel(arg0);
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    
    public static OpenCarDialog inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static OpenCarDialog inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<OpenCarDialog>inflate(inflater, youdrive.today.R.layout.popup_open_car, root, attachToRoot, bindingComponent);
    }
    public static OpenCarDialog inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static OpenCarDialog inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.popup_open_car, null, false), bindingComponent);
    }
    public static OpenCarDialog bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static OpenCarDialog bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/popup_open_car_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new OpenCarDialog(bindingComponent, view);
    }
}
    /* flag mapping
        flag 0: listener~
        flag 1: INVALIDATE ANY
    flag mapping end*/
    //end