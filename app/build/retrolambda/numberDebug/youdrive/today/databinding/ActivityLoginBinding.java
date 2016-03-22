package youdrive.today.databinding;
import youdrive.today.R;
import youdrive.today.BR;
import android.view.View;
public class ActivityLoginBinding extends android.databinding.ViewDataBinding {
    
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.ivLogo, 5);
        sViewsWithIds.put(R.id.txtEntrance, 6);
        sViewsWithIds.put(R.id.etLogin, 7);
        sViewsWithIds.put(R.id.etPassword, 8);
        sViewsWithIds.put(R.id.txtQuestion, 9);
    }
    // views
    public final com.dd.CircularProgressButton btnLogin;
    public final com.rengwuxian.materialedittext.MaterialEditText etLogin;
    public final com.rengwuxian.materialedittext.MaterialEditText etPassword;
    public final android.widget.ImageView ivLogo;
    private final android.widget.RelativeLayout mboundView0;
    public final android.widget.TextView txtAbout;
    public final android.widget.TextView txtEntrance;
    public final android.widget.TextView txtQuestion;
    public final android.widget.TextView txtRegistration;
    public final android.widget.TextView txtRestore;
    // variables
    private youdrive.today.activities.LoginActivity mListener;
    // values
    // listeners
    private OnClickListenerImpl mAndroidViewViewOnCl;
    private OnClickListenerImpl1 mAndroidViewViewOnCl1;
    private OnClickListenerImpl2 mAndroidViewViewOnCl2;
    private OnClickListenerImpl3 mAndroidViewViewOnCl3;
    
    public ActivityLoginBinding(android.databinding.DataBindingComponent bindingComponent, View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds);
        this.btnLogin = (com.dd.CircularProgressButton) bindings[1];
        this.btnLogin.setTag(null);
        this.etLogin = (com.rengwuxian.materialedittext.MaterialEditText) bindings[7];
        this.etPassword = (com.rengwuxian.materialedittext.MaterialEditText) bindings[8];
        this.ivLogo = (android.widget.ImageView) bindings[5];
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.txtAbout = (android.widget.TextView) bindings[3];
        this.txtAbout.setTag(null);
        this.txtEntrance = (android.widget.TextView) bindings[6];
        this.txtQuestion = (android.widget.TextView) bindings[9];
        this.txtRegistration = (android.widget.TextView) bindings[4];
        this.txtRegistration.setTag(null);
        this.txtRestore = (android.widget.TextView) bindings[2];
        this.txtRestore.setTag(null);
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
                setListener((youdrive.today.activities.LoginActivity) variable);
                return true;
        }
        return false;
    }
    
    public void setListener(youdrive.today.activities.LoginActivity listener) {
        this.mListener = listener;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        super.requestRebind();
    }
    public youdrive.today.activities.LoginActivity getListener() {
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
        youdrive.today.activities.LoginActivity listener = mListener;
        android.view.View.OnClickListener androidViewViewOnCli = null;
        android.view.View.OnClickListener androidViewViewOnCli1 = null;
        android.view.View.OnClickListener androidViewViewOnCli2 = null;
        android.view.View.OnClickListener androidViewViewOnCli3 = null;
    
        if ((dirtyFlags & 0x3L) != 0) {
            // read listener~
            listener = listener;
        
            if (listener != null) {
                // read android.view.View.OnClickListener~listener~~onLogin
                androidViewViewOnCli = (((mAndroidViewViewOnCl == null) ? (mAndroidViewViewOnCl = new OnClickListenerImpl()) : mAndroidViewViewOnCl).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onAbout
                androidViewViewOnCli1 = (((mAndroidViewViewOnCl1 == null) ? (mAndroidViewViewOnCl1 = new OnClickListenerImpl1()) : mAndroidViewViewOnCl1).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onRegistration
                androidViewViewOnCli2 = (((mAndroidViewViewOnCl2 == null) ? (mAndroidViewViewOnCl2 = new OnClickListenerImpl2()) : mAndroidViewViewOnCl2).setValue(listener));
                // read android.view.View.OnClickListener~listener~~onRestore
                androidViewViewOnCli3 = (((mAndroidViewViewOnCl3 == null) ? (mAndroidViewViewOnCl3 = new OnClickListenerImpl3()) : mAndroidViewViewOnCl3).setValue(listener));
            }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1
            this.btnLogin.setOnClickListener(androidViewViewOnCli);
            this.txtAbout.setOnClickListener(androidViewViewOnCli1);
            this.txtRegistration.setOnClickListener(androidViewViewOnCli2);
            this.txtRestore.setOnClickListener(androidViewViewOnCli3);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private youdrive.today.activities.LoginActivity value;
        public OnClickListenerImpl setValue(youdrive.today.activities.LoginActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onLogin(arg0);
        }
    }
    public static class OnClickListenerImpl1 implements android.view.View.OnClickListener{
        private youdrive.today.activities.LoginActivity value;
        public OnClickListenerImpl1 setValue(youdrive.today.activities.LoginActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onAbout(arg0);
        }
    }
    public static class OnClickListenerImpl2 implements android.view.View.OnClickListener{
        private youdrive.today.activities.LoginActivity value;
        public OnClickListenerImpl2 setValue(youdrive.today.activities.LoginActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onRegistration(arg0);
        }
    }
    public static class OnClickListenerImpl3 implements android.view.View.OnClickListener{
        private youdrive.today.activities.LoginActivity value;
        public OnClickListenerImpl3 setValue(youdrive.today.activities.LoginActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onRestore(arg0);
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    
    public static ActivityLoginBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityLoginBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivityLoginBinding>inflate(inflater, youdrive.today.R.layout.activity_login, root, attachToRoot, bindingComponent);
    }
    public static ActivityLoginBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityLoginBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(youdrive.today.R.layout.activity_login, null, false), bindingComponent);
    }
    public static ActivityLoginBinding bind(android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static ActivityLoginBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_login_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivityLoginBinding(bindingComponent, view);
    }
}
    /* flag mapping
        flag 0: listener~
        flag 1: INVALIDATE ANY
    flag mapping end*/
    //end