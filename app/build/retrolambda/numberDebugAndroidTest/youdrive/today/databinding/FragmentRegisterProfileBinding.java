package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class FragmentRegisterProfileBinding extends android.databinding.ViewDataBinding {
    
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.tvTitle, 10);
        sViewsWithIds.put(R.id.etEmail, 11);
        sViewsWithIds.put(R.id.etPhone, 12);
        sViewsWithIds.put(R.id.etPassword, 13);
        sViewsWithIds.put(R.id.ivPasswordAgainHint, 14);
        sViewsWithIds.put(R.id.etPaswordAgain, 15);
        sViewsWithIds.put(R.id.etPromo, 16);
        sViewsWithIds.put(R.id.etSurname, 17);
        sViewsWithIds.put(R.id.etName, 18);
        sViewsWithIds.put(R.id.etMiddleName, 19);
    }
    // views
    public final com.rengwuxian.materialedittext.MaterialEditText etEmail;
    public final com.rengwuxian.materialedittext.MaterialEditText etMiddleName;
    public final com.rengwuxian.materialedittext.MaterialEditText etName;
    public final com.rengwuxian.materialedittext.MaterialEditText etPassword;
    public final com.rengwuxian.materialedittext.MaterialEditText etPaswordAgain;
    public final com.rengwuxian.materialedittext.MaterialEditText etPhone;
    public final com.rengwuxian.materialedittext.MaterialEditText etPromo;
    public final com.rengwuxian.materialedittext.MaterialEditText etSurname;
    public final android.widget.ImageView ivEmailHint;
    public final android.widget.ImageView ivMidleNameHint;
    public final android.widget.ImageView ivNameHint;
    public final android.widget.ImageView ivPasswordAgainHint;
    public final android.widget.ImageView ivPasswordHind;
    public final android.widget.ImageView ivPhoneHint;
    public final android.widget.ImageView ivPromoHint;
    public final android.widget.ImageView ivSurnameHint;
    private final android.widget.RelativeLayout mboundView0;
    private final android.widget.TextView mboundView9;
    public final android.widget.TextView tvForvard;
    public final android.widget.TextView tvTitle;
    // variables
    private youdrive.today.fragments.RegisterProfileFragment mListener;
    // values
    // listeners
    private OnClickListenerImpl mAndroidViewViewOnCl;
    private OnClickListenerImpl1 mAndroidViewViewOnCl1;
    private OnClickListenerImpl2 mAndroidViewViewOnCl2;
    private OnClickListenerImpl3 mAndroidViewViewOnCl3;
    private OnClickListenerImpl4 mAndroidViewViewOnCl4;
    private OnClickListenerImpl5 mAndroidViewViewOnCl5;
    private OnClickListenerImpl6 mAndroidViewViewOnCl6;
    private OnClickListenerImpl7 mAndroidViewViewOnCl7;
    private OnClickListenerImpl8 mAndroidViewViewOnCl8;
    
    public FragmentRegisterProfileBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 20, sIncludes, sViewsWithIds);
        this.etEmail = (com.rengwuxian.materialedittext.MaterialEditText) bindings[11];
        this.etMiddleName = (com.rengwuxian.materialedittext.MaterialEditText) bindings[19];
        this.etName = (com.rengwuxian.materialedittext.MaterialEditText) bindings[18];
        this.etPassword = (com.rengwuxian.materialedittext.MaterialEditText) bindings[13];
        this.etPaswordAgain = (com.rengwuxian.materialedittext.MaterialEditText) bindings[15];
        this.etPhone = (com.rengwuxian.materialedittext.MaterialEditText) bindings[12];
        this.etPromo = (com.rengwuxian.materialedittext.MaterialEditText) bindings[16];
        this.etSurname = (com.rengwuxian.materialedittext.MaterialEditText) bindings[17];
        this.ivEmailHint = (android.widget.ImageView) bindings[1];
        this.ivEmailHint.setTag(null);
        this.ivMidleNameHint = (android.widget.ImageView) bindings[7];
        this.ivMidleNameHint.setTag(null);
        this.ivNameHint = (android.widget.ImageView) bindings[6];
        this.ivNameHint.setTag(null);
        this.ivPasswordAgainHint = (android.widget.ImageView) bindings[14];
        this.ivPasswordHind = (android.widget.ImageView) bindings[3];
        this.ivPasswordHind.setTag(null);
        this.ivPhoneHint = (android.widget.ImageView) bindings[2];
        this.ivPhoneHint.setTag(null);
        this.ivPromoHint = (android.widget.ImageView) bindings[4];
        this.ivPromoHint.setTag(null);
        this.ivSurnameHint = (android.widget.ImageView) bindings[5];
        this.ivSurnameHint.setTag(null);
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView9 = (android.widget.TextView) bindings[9];
        this.mboundView9.setTag(null);
        this.tvForvard = (android.widget.TextView) bindings[8];
        this.tvForvard.setTag(null);
        this.tvTitle = (android.widget.TextView) bindings[10];
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
                setListener((youdrive.today.fragments.RegisterProfileFragment) variable);
                return true;
        }
        return false;
    }
    
    public void setListener(youdrive.today.fragments.RegisterProfileFragment listener) {
        this.mListener = listener;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        super.requestRebind();
    }
    public youdrive.today.fragments.RegisterProfileFragment getListener() {
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
        youdrive.today.fragments.RegisterProfileFragment listener = mListener;
        android.view.View.OnClickListener androidViewViewOnCli = null;
        android.view.View.OnClickListener androidViewViewOnCli1 = null;
        android.view.View.OnClickListener androidViewViewOnCli2 = null;
        android.view.View.OnClickListener androidViewViewOnCli3 = null;
        android.view.View.OnClickListener androidViewViewOnCli4 = null;
        android.view.View.OnClickListener androidViewViewOnCli5 = null;
        android.view.View.OnClickListener androidViewViewOnCli6 = null;
        android.view.View.OnClickListener androidViewViewOnCli7 = null;
        android.view.View.OnClickListener androidViewViewOnCli8 = null;
    
        if ((dirtyFlags & 0x3L) != 0) {
            // read listener~
            listener = listener;
        
            if (listener != null) {
                // read android.view.View.OnClickListener~listener~~onMiddleName
                androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onBack
                androidViewViewOnCli1 = (((mAndroidViewViewOnCl1 == null) ? (mAndroidViewViewOnCl1 = new OnClickListenerImpl1()) : mAndroidViewViewOnCl1).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onPassword
                androidViewViewOnCli2 = (((mAndroidViewViewOnCl2 == null) ? (mAndroidViewViewOnCl2 = new OnClickListenerImpl2()) : mAndroidViewViewOnCl2).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onEmail
                androidViewViewOnCli3 = (((mAndroidViewViewOnCl3 == null) ? (mAndroidViewViewOnCl3 = new OnClickListenerImpl3()) : mAndroidViewViewOnCl3).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onSurName
                androidViewViewOnCli4 = (((mAndroidViewViewOnCl4 == null) ? (mAndroidViewViewOnCl4 = new OnClickListenerImpl4()) : mAndroidViewViewOnCl4).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onPromo
                androidViewViewOnCli5 = (((mAndroidViewViewOnCl5 == null) ? (mAndroidViewViewOnCl5 = new OnClickListenerImpl5()) : mAndroidViewViewOnCl5).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onName
                androidViewViewOnCli6 = (((mAndroidViewViewOnCl6 == null) ? (mAndroidViewViewOnCl6 = new OnClickListenerImpl6()) : mAndroidViewViewOnCl6).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onForvard
                androidViewViewOnCli7 = (((mAndroidViewViewOnCl7 == null) ? (mAndroidViewViewOnCl7 = new OnClickListenerImpl7()) : mAndroidViewViewOnCl7).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onPhone
                androidViewViewOnCli8 = (((mAndroidViewViewOnCl8 == null) ? (mAndroidViewViewOnCl8 = new OnClickListenerImpl8()) : mAndroidViewViewOnCl8).setValue(listener));
            }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1
            this.ivEmailHint.setOnClickListener(androidViewViewOnCli3);
            this.ivMidleNameHint.setOnClickListener(androidViewViewOnCli);
            this.ivNameHint.setOnClickListener(androidViewViewOnCli6);
            this.ivPasswordHind.setOnClickListener(androidViewViewOnCli2);
            this.ivPhoneHint.setOnClickListener(androidViewViewOnCli8);
            this.ivPromoHint.setOnClickListener(androidViewViewOnCli5);
            this.ivSurnameHint.setOnClickListener(androidViewViewOnCli4);
            this.mboundView9.setOnClickListener(androidViewViewOnCli1);
            this.tvForvard.setOnClickListener(androidViewViewOnCli7);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private youdrive.today.fragments.RegisterProfileFragment value;
        public OnClickListenerImpl setValue(youdrive.today.fragments.RegisterProfileFragment value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onMiddleName(arg0);
        }
    }
    public static class OnClickListenerImpl1 implements android.view.View.OnClickListener{
        private youdrive.today.fragments.RegisterProfileFragment value;
        public OnClickListenerImpl1 setValue(youdrive.today.fragments.RegisterProfileFragment value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onBack(arg0);
        }
    }
    public static class OnClickListenerImpl2 implements android.view.View.OnClickListener{
        private youdrive.today.fragments.RegisterProfileFragment value;
        public OnClickListenerImpl2 setValue(youdrive.today.fragments.RegisterProfileFragment value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onPassword(arg0);
        }
    }
    public static class OnClickListenerImpl3 implements android.view.View.OnClickListener{
        private youdrive.today.fragments.RegisterProfileFragment value;
        public OnClickListenerImpl3 setValue(youdrive.today.fragments.RegisterProfileFragment value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onEmail(arg0);
        }
    }
    public static class OnClickListenerImpl4 implements android.view.View.OnClickListener{
        private youdrive.today.fragments.RegisterProfileFragment value;
        public OnClickListenerImpl4 setValue(youdrive.today.fragments.RegisterProfileFragment value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onSurName(arg0);
        }
    }
    public static class OnClickListenerImpl5 implements android.view.View.OnClickListener{
        private youdrive.today.fragments.RegisterProfileFragment value;
        public OnClickListenerImpl5 setValue(youdrive.today.fragments.RegisterProfileFragment value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onPromo(arg0);
        }
    }
    public static class OnClickListenerImpl6 implements android.view.View.OnClickListener{
        private youdrive.today.fragments.RegisterProfileFragment value;
        public OnClickListenerImpl6 setValue(youdrive.today.fragments.RegisterProfileFragment value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onName(arg0);
        }
    }
    public static class OnClickListenerImpl7 implements android.view.View.OnClickListener{
        private youdrive.today.fragments.RegisterProfileFragment value;
        public OnClickListenerImpl7 setValue(youdrive.today.fragments.RegisterProfileFragment value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onForvard(arg0);
        }
    }
    public static class OnClickListenerImpl8 implements android.view.View.OnClickListener{
        private youdrive.today.fragments.RegisterProfileFragment value;
        public OnClickListenerImpl8 setValue(youdrive.today.fragments.RegisterProfileFragment value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onPhone(arg0);
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    
    public static FragmentRegisterProfileBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static FragmentRegisterProfileBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FragmentRegisterProfileBinding>inflate(inflater, youdrive.today.R.layout.fragment_register_profile, root, attachToRoot, bindingComponent);
    }
    public static FragmentRegisterProfileBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static FragmentRegisterProfileBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.fragment_register_profile, null, false), bindingComponent);
    }
    public static FragmentRegisterProfileBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static FragmentRegisterProfileBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/fragment_register_profile_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FragmentRegisterProfileBinding(bindingComponent, view);
    }
}
    /* flag mapping
        flag 0: listener~
        flag 1: INVALIDATE ANY
    flag mapping end*/
    //end