package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class ActivityWellcomeBinding extends android.databinding.ViewDataBinding  {

    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.flIndicator, 2);
        sViewsWithIds.put(R.id.cpIndicator, 3);
        sViewsWithIds.put(R.id.viewPager, 4);
    }
    // views
    public final youdrive.today.indicator.viewpagerindicator.CirclePageIndicator cpIndicator;
    public final android.widget.FrameLayout flIndicator;
    private final android.widget.RelativeLayout mboundView0;
    private final android.widget.ImageView mboundView1;
    public final android.support.v4.view.ViewPager viewPager;
    // variables
    private youdrive.today.activities.WellcomeActivity mListener;
    // values
    // listeners
    private OnClickListenerImpl mAndroidViewViewOnCl;
    // Inverse Binding Event Handlers

    public ActivityWellcomeBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds);
        this.cpIndicator = (youdrive.today.indicator.viewpagerindicator.CirclePageIndicator) bindings[3];
        this.flIndicator = (android.widget.FrameLayout) bindings[2];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.ImageView) bindings[1];
        this.mboundView1.setTag(null);
        this.viewPager = (android.support.v4.view.ViewPager) bindings[4];
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
                setListener((youdrive.today.activities.WellcomeActivity) variable);
                return true;
        }
        return false;
    }

    public void setListener(youdrive.today.activities.WellcomeActivity listener) {
        this.mListener = listener;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.listener);
        super.requestRebind();
    }
    public youdrive.today.activities.WellcomeActivity getListener() {
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
        youdrive.today.activities.WellcomeActivity listener = mListener;
        android.view.View.OnClickListener androidViewViewOnCli = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (listener != null) {
                    // read listener::onBack
                    androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(listener));
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.mboundView1.setOnClickListener(androidViewViewOnCli);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private youdrive.today.activities.WellcomeActivity value;
        public OnClickListenerImpl setValue(youdrive.today.activities.WellcomeActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onBack(arg0);
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    public static ActivityWellcomeBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityWellcomeBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivityWellcomeBinding>inflate(inflater, youdrive.today.R.layout.activity_wellcome, root, attachToRoot, bindingComponent);
    }
    public static ActivityWellcomeBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityWellcomeBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.activity_wellcome, null, false), bindingComponent);
    }
    public static ActivityWellcomeBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityWellcomeBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_wellcome_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivityWellcomeBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): listener
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}