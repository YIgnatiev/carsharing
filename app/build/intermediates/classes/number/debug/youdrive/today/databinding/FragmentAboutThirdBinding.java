package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class FragmentAboutThirdBinding extends android.databinding.ViewDataBinding  {

    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    private final android.widget.LinearLayout mboundView0;
    public final android.widget.TextView tvFarRides;
    public final android.widget.TextView tvFreeFuel;
    public final android.widget.TextView tvFreeNight;
    public final android.widget.TextView tvFreeParking;
    public final android.widget.TextView tvMinuteTarification;
    // variables
    private youdrive.today.fragments.AboutThird mListener;
    // values
    // listeners
    private OnClickListenerImpl mAndroidViewViewOnCl;
    private OnClickListenerImpl1 mAndroidViewViewOnCl1;
    private OnClickListenerImpl2 mAndroidViewViewOnCl2;
    private OnClickListenerImpl3 mAndroidViewViewOnCl3;
    private OnClickListenerImpl4 mAndroidViewViewOnCl4;
    // Inverse Binding Event Handlers

    public FragmentAboutThirdBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tvFarRides = (android.widget.TextView) bindings[5];
        this.tvFarRides.setTag(null);
        this.tvFreeFuel = (android.widget.TextView) bindings[3];
        this.tvFreeFuel.setTag(null);
        this.tvFreeNight = (android.widget.TextView) bindings[4];
        this.tvFreeNight.setTag(null);
        this.tvFreeParking = (android.widget.TextView) bindings[2];
        this.tvFreeParking.setTag(null);
        this.tvMinuteTarification = (android.widget.TextView) bindings[1];
        this.tvMinuteTarification.setTag(null);
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
                setListener((youdrive.today.fragments.AboutThird) variable);
                return true;
        }
        return false;
    }

    public void setListener(youdrive.today.fragments.AboutThird listener) {
        this.mListener = listener;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.listener);
        super.requestRebind();
    }
    public youdrive.today.fragments.AboutThird getListener() {
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
        youdrive.today.fragments.AboutThird listener = mListener;
        android.view.View.OnClickListener androidViewViewOnCli = null;
        android.view.View.OnClickListener androidViewViewOnCli1 = null;
        android.view.View.OnClickListener androidViewViewOnCli2 = null;
        android.view.View.OnClickListener androidViewViewOnCli3 = null;
        android.view.View.OnClickListener androidViewViewOnCli4 = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (listener != null) {
                    // read listener::onFuelIsPayed
                    androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(listener));
                    // read listener::onFreeNightParking
                    androidViewViewOnCli1 = (((mAndroidViewViewOnCl1 == null) ? (mAndroidViewViewOnCl1 = new OnClickListenerImpl1()) : mAndroidViewViewOnCl1).setValue(listener));
                    // read listener::onMinuteTarrification
                    androidViewViewOnCli2 = (((mAndroidViewViewOnCl2 == null) ? (mAndroidViewViewOnCl2 = new OnClickListenerImpl2()) : mAndroidViewViewOnCl2).setValue(listener));
                    // read listener::onFarRides
                    androidViewViewOnCli3 = (((mAndroidViewViewOnCl3 == null) ? (mAndroidViewViewOnCl3 = new OnClickListenerImpl3()) : mAndroidViewViewOnCl3).setValue(listener));
                    // read listener::onFreeParking
                    androidViewViewOnCli4 = (((mAndroidViewViewOnCl4 == null) ? (mAndroidViewViewOnCl4 = new OnClickListenerImpl4()) : mAndroidViewViewOnCl4).setValue(listener));
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.tvFarRides.setOnClickListener(androidViewViewOnCli3);
            this.tvFreeFuel.setOnClickListener(androidViewViewOnCli);
            this.tvFreeNight.setOnClickListener(androidViewViewOnCli1);
            this.tvFreeParking.setOnClickListener(androidViewViewOnCli4);
            this.tvMinuteTarification.setOnClickListener(androidViewViewOnCli2);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private youdrive.today.fragments.AboutThird value;
        public OnClickListenerImpl setValue(youdrive.today.fragments.AboutThird value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onFuelIsPayed(arg0);
        }
    }
    public static class OnClickListenerImpl1 implements android.view.View.OnClickListener{
        private youdrive.today.fragments.AboutThird value;
        public OnClickListenerImpl1 setValue(youdrive.today.fragments.AboutThird value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onFreeNightParking(arg0);
        }
    }
    public static class OnClickListenerImpl2 implements android.view.View.OnClickListener{
        private youdrive.today.fragments.AboutThird value;
        public OnClickListenerImpl2 setValue(youdrive.today.fragments.AboutThird value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onMinuteTarrification(arg0);
        }
    }
    public static class OnClickListenerImpl3 implements android.view.View.OnClickListener{
        private youdrive.today.fragments.AboutThird value;
        public OnClickListenerImpl3 setValue(youdrive.today.fragments.AboutThird value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onFarRides(arg0);
        }
    }
    public static class OnClickListenerImpl4 implements android.view.View.OnClickListener{
        private youdrive.today.fragments.AboutThird value;
        public OnClickListenerImpl4 setValue(youdrive.today.fragments.AboutThird value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onFreeParking(arg0);
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    public static FragmentAboutThirdBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static FragmentAboutThirdBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentAboutThirdBinding>inflate(inflater, youdrive.today.R.layout.fragment_about_third, root, attachToRoot, bindingComponent);
    }
    public static FragmentAboutThirdBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static FragmentAboutThirdBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.fragment_about_third, null, false), bindingComponent);
    }
    public static FragmentAboutThirdBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static FragmentAboutThirdBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_about_third_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentAboutThirdBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): listener
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}