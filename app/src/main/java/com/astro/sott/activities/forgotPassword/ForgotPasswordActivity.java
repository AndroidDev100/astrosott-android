package com.astro.sott.activities.forgotPassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends BaseBindingActivity<ActivityForgotPasswordBinding> {

    @Override
    protected ActivityForgotPasswordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityForgotPasswordBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}