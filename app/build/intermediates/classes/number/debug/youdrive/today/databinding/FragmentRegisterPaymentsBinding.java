package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class FragmentRegisterPaymentsBinding extends android.databinding.ViewDataBinding  {

    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.tvPayments, 4);
        sViewsWithIds.put(R.id.wvPayment, 5);
    }
    // views
    public final com.dd.CircularProgressButton btnLogin;
    private final android.widget.RelativeLayout mboundView0;
    private final android.widget.TextView mboundView3;
    public final android.widget.TextView tvPayments;
    public final android.widget.TextView tvReady;
    public final android.webkit.WebView wvPayment;
    // variables
    private youdrive.today.fragments.RegisterPaymentsFragment mListener;
    // values
    // listeners
    private OnClickListenerImpl mAndroidViewViewOnCl;
    private OnClickListenerImpl1 mAndroidViewViewOnCl1;
    private OnClickListenerImpl2 mAndroidViewViewOnCl2;
    // Inverse Binding Event Handlers

    public FragmentRegisterPaymentsBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds);
        this.btnLogin = (com.dd.CircularProgressButton) bindings[1];
        this.btnLogin.setTag(null);
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView3 = (android.widget.TextView) bindings[3];
        this.mboundView3.setTag(null);
        this.tvPayments = (android.widget.TextView) bindings[4];
        this.tvReady = (android.widget.TextView) bindings[2];
        this.tvReady.setTag(null);
        this.wvPayment = (android.webkit.WebView) bindings[5];
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
                setListener((youdrive.today.fragments.RegisterPaymentsFragment) variable);
                return true;
        }
        return false;
    }

    public void setListener(youdrive.today.fragments.RegisterPaymentsFragment listener) {
        this.mListener = listener;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.listener);
        super.requestRebind();
    }
    public youdrive.today.fragments.RegisterPaymentsFragment getListener() {
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
        youdrive.today.fragments.RegisterPaymentsFragment listener = mListener;
        android.view.View.OnClickListener androidViewViewOnCli = null;
        android.view.View.OnClickListener androidViewViewOnCli1 = null;
        android.view.View.OnClickListener androidViewViewOnCli2 = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (listener != null) {
                    // read listener::onBack
                    androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(listener));
                    // read listener::onCardLink
                    androidViewViewOnCli1 = (((mAndroidViewViewOnCl1 == null) ? (mAndroidViewViewOnCl1 = new OnClickListenerImpl1()) : mAndroidViewViewOnCl1).setValue(listener));
                    // read listener::onForvard
                    androidViewViewOnCli2 = (((mAndroidViewViewOnCl2 == null) ? (mAndroidViewViewOnCl2 = new OnClickListenerImpl2()) : mAndroidViewViewOnCl2).setValue(listener));
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.btnLogin.setOnClickListener(androidViewViewOnCli1);
            this.mboundView3.setOnClickListener(androidViewViewOnCli);
            this.tvReady.setOnClickListener(androidViewViewOnCli2);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private youdrive.today.fragments.RegisterPaymentsFragment value;
        public OnClickListenerImpl setValue(youdrive.today.fragments.RegisterPaymentsFragment value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onBack(arg0);
        }
    }
    public static class OnClickListenerImpl1 implements android.view.View.OnClickListener{
        private youdrive.today.fragments.RegisterPaymentsFragment value;
        public OnClickListenerImpl1 setValue(youdrive.today.fragments.RegisterPaymentsFragment value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onCardLink(arg0);
        }
    }
    public static class OnClickListenerImpl2 implements android.view.View.OnClickListener{
        private youdrive.today.fragments.RegisterPaymentsFragment value;
        public OnClickListenerImpl2 setValue(youdrive.today.fragments.RegisterPaymentsFragment value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onForvard(arg0);
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    public static FragmentRegisterPaymentsBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static FragmentRegisterPaymentsBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentRegisterPaymentsBinding>inflate(inflater, youdrive.today.R.layout.fragment_register_payments, root, attachToRoot, bindingComponent);
    }
    public static FragmentRegisterPaymentsBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static FragmentRegisterPaymentsBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.fragment_register_payments, null, false), bindingComponent);
    }
    public static FragmentRegisterPaymentsBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static FragmentRegisterPaymentsBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_register_payments_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentRegisterPaymentsBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): listener
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}