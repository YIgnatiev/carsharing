package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class ActivityCompleteBinding extends android.databinding.ViewDataBinding  {

    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar, 2);
        sViewsWithIds.put(R.id.ivLogo, 3);
        sViewsWithIds.put(R.id.txtTotalUsage, 4);
        sViewsWithIds.put(R.id.txtParking, 5);
        sViewsWithIds.put(R.id.llDiscount, 6);
        sViewsWithIds.put(R.id.txtTitleDiscount, 7);
        sViewsWithIds.put(R.id.txtDiscount, 8);
        sViewsWithIds.put(R.id.txtTotal, 9);
    }
    // views
    public final com.dd.CircularProgressButton btnClose;
    public final android.widget.ImageView ivLogo;
    public final android.widget.LinearLayout llDiscount;
    private final android.widget.LinearLayout mboundView0;
    public final android.support.v7.widget.Toolbar toolbar;
    public final android.widget.TextView txtDiscount;
    public final android.widget.TextView txtParking;
    public final android.widget.TextView txtTitleDiscount;
    public final android.widget.TextView txtTotal;
    public final android.widget.TextView txtTotalUsage;
    // variables
    private youdrive.today.activities.CompleteActivity mListener;
    // values
    // listeners
    private OnClickListenerImpl mAndroidViewViewOnCl;
    // Inverse Binding Event Handlers

    public ActivityCompleteBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds);
        this.btnClose = (com.dd.CircularProgressButton) bindings[1];
        this.btnClose.setTag(null);
        this.ivLogo = (android.widget.ImageView) bindings[3];
        this.llDiscount = (android.widget.LinearLayout) bindings[6];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.toolbar = (android.support.v7.widget.Toolbar) bindings[2];
        this.txtDiscount = (android.widget.TextView) bindings[8];
        this.txtParking = (android.widget.TextView) bindings[5];
        this.txtTitleDiscount = (android.widget.TextView) bindings[7];
        this.txtTotal = (android.widget.TextView) bindings[9];
        this.txtTotalUsage = (android.widget.TextView) bindings[4];
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
                setListener((youdrive.today.activities.CompleteActivity) variable);
                return true;
        }
        return false;
    }

    public void setListener(youdrive.today.activities.CompleteActivity listener) {
        this.mListener = listener;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.listener);
        super.requestRebind();
    }
    public youdrive.today.activities.CompleteActivity getListener() {
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
        youdrive.today.activities.CompleteActivity listener = mListener;
        android.view.View.OnClickListener androidViewViewOnCli = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (listener != null) {
                    // read listener::onClose
                    androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(listener));
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.btnClose.setOnClickListener(androidViewViewOnCli);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private youdrive.today.activities.CompleteActivity value;
        public OnClickListenerImpl setValue(youdrive.today.activities.CompleteActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onClose(arg0);
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    public static ActivityCompleteBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityCompleteBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivityCompleteBinding>inflate(inflater, youdrive.today.R.layout.activity_complete, root, attachToRoot, bindingComponent);
    }
    public static ActivityCompleteBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityCompleteBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.activity_complete, null, false), bindingComponent);
    }
    public static ActivityCompleteBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityCompleteBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_complete_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivityCompleteBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): listener
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}