package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class MarkerInfo extends android.databinding.ViewDataBinding {
    
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.rlDiscount, 1);
        sViewsWithIds.put(R.id.tvDiscount, 2);
        sViewsWithIds.put(R.id.txtModel, 3);
        sViewsWithIds.put(R.id.txtNumber, 4);
        sViewsWithIds.put(R.id.txtColor, 5);
        sViewsWithIds.put(R.id.txtStartUsage, 6);
    }
    // views
    private final android.widget.LinearLayout mboundView0;
    public final android.widget.RelativeLayout rlDiscount;
    public final android.widget.TextView tvDiscount;
    public final android.widget.TextView txtColor;
    public final android.widget.TextView txtModel;
    public final android.widget.TextView txtNumber;
    public final android.widget.TextView txtStartUsage;
    // variables
    // values
    // listeners
    
    public MarkerInfo(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.rlDiscount = (android.widget.RelativeLayout) bindings[1];
        this.tvDiscount = (android.widget.TextView) bindings[2];
        this.txtColor = (android.widget.TextView) bindings[5];
        this.txtModel = (android.widget.TextView) bindings[3];
        this.txtNumber = (android.widget.TextView) bindings[4];
        this.txtStartUsage = (android.widget.TextView) bindings[6];
        setRootTag(root);
        invalidateAll();
    }
    
    @Override
    public void invalidateAll() {
        synchronized(this) {
            mDirtyFlags = 0x1L;
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
        }
        return false;
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
        // batch finished
    }
    // Listener Stub Implementations
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    
    public static MarkerInfo inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static MarkerInfo inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<MarkerInfo>inflate(inflater, youdrive.today.R.layout.marker_info, root, attachToRoot, bindingComponent);
    }
    public static MarkerInfo inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static MarkerInfo inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.marker_info, null, false), bindingComponent);
    }
    public static MarkerInfo bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static MarkerInfo bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/marker_info_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new MarkerInfo(bindingComponent, view);
    }
}
    /* flag mapping
        flag 0: INVALIDATE ANY
    flag mapping end*/
    //end