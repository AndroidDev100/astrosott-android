package com.astro.sott.fragments.parental;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentChangePinBinding;

public class ChangePinFragment extends BaseBindingFragment<FragmentChangePinBinding> implements View.OnClickListener {


    private BaseActivity baseActivity;
    private OnValidatePinInteraction mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getBinding().validatePin.setText(R.string.validate_pin);
    }

    @Override
    protected FragmentChangePinBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentChangePinBinding.inflate(inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBinding().validatePin.setText(R.string.validate_pin);
        getBinding().btnOk.setOnClickListener(this);
        getBinding().cancel.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                mListener.onValidateOkClick("");
                break;
            case R.id.cancel:
                mListener.onValidateCancelClick("");
                break;
        }
    }

    public interface OnValidatePinInteraction {
        void onValidateOkClick(String name);

        void onValidateCancelClick(String name);
    }
}
