package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class FragmentPaymentBinding extends android.databinding.ViewDataBinding {
    
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.etCardNumber, 2);
        sViewsWithIds.put(R.id.etName, 3);
        sViewsWithIds.put(R.id.etExpired, 4);
        sViewsWithIds.put(R.id.etCvv, 5);
    }
    // views
    public final com.dd.CircularProgressButton btnPay;
    public final com.rengwuxian.materialedittext.MaterialEditText etCardNumber;
    public final com.rengwuxian.materialedittext.MaterialEditText etCvv;
    public final com.rengwuxian.materialedittext.MaterialEditText etExpired;
    public final com.rengwuxian.materialedittext.MaterialEditText etName;
    private final android.widget.LinearLayout mboundView0;
    // variables
    private youdrive.today.fragments.PaymentDialogFragment mListener;
    // values
    // listeners
    private OnClickListenerImpl mAndroidViewViewOnCl;
    
    public FragmentPaymentBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds);
        this.btnPay = (com.dd.CircularProgressButton) bindings[1];
        this.btnPay.setTag(null);
        this.etCardNumber = (com.rengwuxian.materialedittext.MaterialEditText) bindings[2];
        this.etCvv = (com.rengwuxian.materialedittext.MaterialEditText) bindings[5];
        this.etExpired = (com.rengwuxian.materialedittext.MaterialEditText) bindings[4];
        this.etName = (com.rengwuxian.materialedittext.MaterialEditText) bindings[3];
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
                setListener((youdrive.today.fragments.PaymentDialogFragment) variable);
                return true;
        }
        return false;
    }
    
    public void setListener(youdrive.today.fragments.PaymentDialogFragment listener) {
        this.mListener = listener;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        super.requestRebind();
    }
    public youdrive.today.fragments.PaymentDialogFragment getListener() {
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
        youdrive.today.fragments.PaymentDialogFragment listener = mListener;
        android.view.View.OnClickListener androidViewViewOnCli = null;
    
        if ((dirtyFlags & 0x3L) != 0) {
            // read listener~
            listener = listener;
        
            if (listener != null) {
                // read android.view.View.OnClickListener~listener~~onPay
                androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(listener));
            }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1
            this.btnPay.setOnClickListener(androidViewViewOnCli);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private youdrive.today.fragments.PaymentDialogFragment value;
        public OnClickListenerImpl setValue(youdrive.today.fragments.PaymentDialogFragment value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onPay(arg0);
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    
    public static FragmentPaymentBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static FragmentPaymentBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentPaymentBinding>inflate(inflater, youdrive.today.R.layout.fragment_payment, root, attachToRoot, bindingComponent);
    }
    public static FragmentPaymentBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static FragmentPaymentBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.fragment_payment, null, false), bindingComponent);
    }
    public static FragmentPaymentBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static FragmentPaymentBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_payment_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentPaymentBinding(bindingComponent, view);
    }
}
    /* flag mapping
        flag 0: listener~
        flag 1: INVALIDATE ANY
    flag mapping end*/
    //end