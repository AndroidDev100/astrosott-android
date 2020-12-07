package com.dialog.dialoggo.baseModel;

import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;

public abstract class BaseBindingActivity<B extends ViewDataBinding> extends BaseActivity {

    private B mBinding;

    protected abstract B inflateBindingLayout(@NonNull LayoutInflater inflater);


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding = setupBinding(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }



    public B getBinding()
    {
        return mBinding;
    }


    private B setupBinding(@NonNull LayoutInflater inflater)
    {
        return inflateBindingLayout(inflater);
    }
}
