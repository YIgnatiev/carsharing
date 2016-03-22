package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class ActivityMapsBinding extends android.databinding.ViewDataBinding {
    
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar, 4);
        sViewsWithIds.put(R.id.ltMap, 5);
        sViewsWithIds.put(R.id.ltInfo, 6);
        sViewsWithIds.put(R.id.ltContainer, 7);
    }
    // views
    public final android.widget.ImageButton btnZoomIn;
    public final android.widget.ImageButton btnZoomOut;
    public final android.support.v4.widget.DrawerLayout drawer;
    public final android.widget.FrameLayout ltContainer;
    public final android.widget.FrameLayout ltInfo;
    public final android.widget.FrameLayout ltMap;
    public final android.widget.ListView lvProfile;
    public final android.support.v7.widget.Toolbar toolbar;
    // variables
    private youdrive.today.activities.MapsActivity mListener;
    // values
    // listeners
    private OnClickListenerImpl mAndroidViewViewOnCl;
    private OnItemClickListenerI mAndroidWidgetAdapte;
    private OnClickListenerImpl1 mAndroidViewViewOnCl1;
    
    public ActivityMapsBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 8, sIncludes, sViewsWithIds);
        this.btnZoomIn = (android.widget.ImageButton) bindings[1];
        this.btnZoomIn.setTag(null);
        this.btnZoomOut = (android.widget.ImageButton) bindings[2];
        this.btnZoomOut.setTag(null);
        this.drawer = (android.support.v4.widget.DrawerLayout) bindings[0];
        this.drawer.setTag(null);
        this.ltContainer = (android.widget.FrameLayout) bindings[7];
        this.ltInfo = (android.widget.FrameLayout) bindings[6];
        this.ltMap = (android.widget.FrameLayout) bindings[5];
        this.lvProfile = (android.widget.ListView) bindings[3];
        this.lvProfile.setTag(null);
        this.toolbar = (android.support.v7.widget.Toolbar) bindings[4];
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
        android.widget.AdapterView.OnItemClickListener androidWidgetAdapter = null;
        android.view.View.OnClickListener androidViewViewOnCli1 = null;
    
        if ((dirtyFlags & 0x3L) != 0) {
            // read listener~
            listener = listener;
        
            if (listener != null) {
                // read android.view.View.OnClickListener~listener~~onZoomIn
                androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(listener));
                // read android.widget.AdapterView.OnItemClickListener~listener~~onItemSelected
                androidWidgetAdapter = (((mAndroidWidgetAdapte == null) ? (mAndroidWidgetAdapte = new OnItemClickListenerI()) : mAndroidWidgetAdapte).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onZoomOut
                androidViewViewOnCli1 = (((mAndroidViewViewOnCl1 == null) ? (mAndroidViewViewOnCl1 = new OnClickListenerImpl1()) : mAndroidViewViewOnCl1).setValue(listener));
            }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1
            this.btnZoomIn.setOnClickListener(androidViewViewOnCli);
            this.btnZoomOut.setOnClickListener(androidViewViewOnCli1);
            this.lvProfile.setOnItemClickListener(androidWidgetAdapter);
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
            this.value.onZoomIn(arg0);
        }
    }
    public static class OnItemClickListenerI implements android.widget.AdapterView.OnItemClickListener{
        private youdrive.today.activities.MapsActivity value;
        public OnItemClickListenerI setValue(youdrive.today.activities.MapsActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onItemClick(android.widget.AdapterView arg0, android.view.View arg1, int arg2, long arg3) {
            this.value.onItemSelected(arg0, arg1, arg2, arg3);
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
            this.value.onZoomOut(arg0);
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    
    public static ActivityMapsBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityMapsBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivityMapsBinding>inflate(inflater, youdrive.today.R.layout.activity_maps, root, attachToRoot, bindingComponent);
    }
    public static ActivityMapsBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityMapsBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.activity_maps, null, false), bindingComponent);
    }
    public static ActivityMapsBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityMapsBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_maps_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivityMapsBinding(bindingComponent, view);
    }
}
    /* flag mapping
        flag 0: listener~
        flag 1: INVALIDATE ANY
    flag mapping end*/
    //end