package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class ItemProfileBinding extends android.databinding.ViewDataBinding {
    
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.ivIcon, 1);
        sViewsWithIds.put(R.id.txtTitle, 2);
    }
    // views
    public final android.widget.ImageView ivIcon;
    private final android.widget.LinearLayout mboundView0;
    public final android.widget.TextView txtTitle;
    // variables
    // values
    // listeners
    
    public ItemProfileBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds);
        this.ivIcon = (android.widget.ImageView) bindings[1];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.txtTitle = (android.widget.TextView) bindings[2];
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
    
    public static ItemProfileBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ItemProfileBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ItemProfileBinding>inflate(inflater, youdrive.today.R.layout.item_profile, root, attachToRoot, bindingComponent);
    }
    public static ItemProfileBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ItemProfileBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.item_profile, null, false), bindingComponent);
    }
    public static ItemProfileBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ItemProfileBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/item_profile_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ItemProfileBinding(bindingComponent, view);
    }
}
    /* flag mapping
        flag 0: INVALIDATE ANY
    flag mapping end*/
    //end