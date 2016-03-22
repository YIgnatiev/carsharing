package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class ActivityConfirmationBinding extends android.databinding.ViewDataBinding {
    
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.ivLogo, 3);
        sViewsWithIds.put(R.id.txtAlarm, 4);
        sViewsWithIds.put(R.id.txtLocationService, 5);
    }
    // views
    public final com.dd.CircularProgressButton btnCancel;
    public final com.dd.CircularProgressButton btnOk;
    public final android.widget.ImageView ivLogo;
    private final android.widget.RelativeLayout mboundView0;
    public final android.widget.TextView txtAlarm;
    public final android.widget.TextView txtLocationService;
    // variables
    private youdrive.today.activities.ConfirmationActivity mListener;
    // values
    // listeners
    private OnClickListenerImpl mAndroidViewViewOnCl;
    private OnClickListenerImpl1 mAndroidViewViewOnCl1;
    
    public ActivityConfirmationBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds);
        this.btnCancel = (com.dd.CircularProgressButton) bindings[2];
        this.btnCancel.setTag(null);
        this.btnOk = (com.dd.CircularProgressButton) bindings[1];
        this.btnOk.setTag(null);
        this.ivLogo = (android.widget.ImageView) bindings[3];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.txtAlarm = (android.widget.TextView) bindings[4];
        this.txtLocationService = (android.widget.TextView) bindings[5];
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
                setListener((youdrive.today.activities.ConfirmationActivity) variable);
                return true;
        }
        return false;
    }
    
    public void setListener(youdrive.today.activities.ConfirmationActivity listener) {
        this.mListener = listener;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        super.requestRebind();
    }
    public youdrive.today.activities.ConfirmationActivity getListener() {
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
        youdrive.today.activities.ConfirmationActivity listener = mListener;
        android.view.View.OnClickListener androidViewViewOnCli = null;
        android.view.View.OnClickListener androidViewViewOnCli1 = null;
    
        if ((dirtyFlags & 0x3L) != 0) {
            // read listener~
            listener = listener;
        
            if (listener != null) {
                // read android.view.View.OnClickListener~listener~~onCancel
                androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onOk
                androidViewViewOnCli1 = (((mAndroidViewViewOnCl1 == null) ? (mAndroidViewViewOnCl1 = new OnClickListenerImpl1()) : mAndroidViewViewOnCl1).setValue(listener));
            }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1
            this.btnCancel.setOnClickListener(androidViewViewOnCli);
            this.btnOk.setOnClickListener(androidViewViewOnCli1);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private youdrive.today.activities.ConfirmationActivity value;
        public OnClickListenerImpl setValue(youdrive.today.activities.ConfirmationActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onCancel(arg0);
        }
    }
    public static class OnClickListenerImpl1 implements android.view.View.OnClickListener{
        private youdrive.today.activities.ConfirmationActivity value;
        public OnClickListenerImpl1 setValue(youdrive.today.activities.ConfirmationActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onOk(arg0);
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    
    public static ActivityConfirmationBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityConfirmationBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivityConfirmationBinding>inflate(inflater, youdrive.today.R.layout.activity_confirmation, root, attachToRoot, bindingComponent);
    }
    public static ActivityConfirmationBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityConfirmationBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.activity_confirmation, null, false), bindingComponent);
    }
    public static ActivityConfirmationBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityConfirmationBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_confirmation_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivityConfirmationBinding(bindingComponent, view);
    }
}
    /* flag mapping
        flag 0: listener~
        flag 1: INVALIDATE ANY
    flag mapping end*/
    //end