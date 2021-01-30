package com.astro.sott.activities.profile.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityEditPasswordBinding;

public class EditPasswordActivity extends BaseBindingActivity<ActivityEditPasswordBinding> {

    @Override
    protected ActivityEditPasswordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityEditPasswordBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}