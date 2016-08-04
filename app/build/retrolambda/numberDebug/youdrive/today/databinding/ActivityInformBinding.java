package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class ActivityInformBinding extends android.databinding.ViewDataBinding  {

    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.ivLogo, 2);
        sViewsWithIds.put(R.id.txtThanks, 3);
        sViewsWithIds.put(R.id.txtMessage, 4);
    }
    // views
    public final com.dd.CircularProgressButton btnOk;
    public final android.widget.ImageView ivLogo;
    private final android.widget.RelativeLayout mboundView0;
    public final android.widget.TextView txtMessage;
    public final android.widget.TextView txtThanks;
    // variables
    private youdrive.today.activities.InformActivity mListener;
    // values
    // listeners
    private OnClickListenerImpl mAndroidViewViewOnCl;
    // Inverse Binding Event Handlers

    public ActivityInformBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds);
        this.btnOk = (com.dd.CircularProgressButton) bindings[1];
        this.btnOk.setTag(null);
        this.ivLogo = (android.widget.ImageView) bindings[2];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.txtMessage = (android.widget.TextView) bindings[4];
        this.txtThanks = (android.widget.TextView) bindings[3];
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
                setListener((youdrive.today.activities.InformActivity) variable);
                return true;
        }
        return false;
    }

    public void setListener(youdrive.today.activities.InformActivity listener) {
        this.mListener = listener;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.listener);
        super.requestRebind();
    }
    public youdrive.today.activities.InformActivity getListener() {
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
        youdrive.today.activities.InformActivity listener = mListener;
        android.view.View.OnClickListener androidViewViewOnCli = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (listener != null) {
                    // read listener::auth
                    androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(listener));
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.btnOk.setOnClickListener(androidViewViewOnCli);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private youdrive.today.activities.InformActivity value;
        public OnClickListenerImpl setValue(youdrive.today.activities.InformActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.auth(arg0);
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    public static ActivityInformBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityInformBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivityInformBinding>inflate(inflater, youdrive.today.R.layout.activity_inform, root, attachToRoot, bindingComponent);
    }
    public static ActivityInformBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityInformBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.activity_inform, null, false), bindingComponent);
    }
    public static ActivityInformBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityInformBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_inform_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivityInformBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): listener
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}