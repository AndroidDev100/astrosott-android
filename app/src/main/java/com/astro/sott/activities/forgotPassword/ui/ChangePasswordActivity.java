package com.astro.sott.activities.forgotPassword.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends BaseBindingActivity<ActivityChangePasswordBinding> {
    @Override
    protected ActivityChangePasswordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityChangePasswordBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}