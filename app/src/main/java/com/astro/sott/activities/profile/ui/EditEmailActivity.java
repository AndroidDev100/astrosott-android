package com.astro.sott.activities.profile.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityEditEmailBinding;

public class EditEmailActivity extends BaseBindingActivity<ActivityEditEmailBinding> {

    @Override
    protected ActivityEditEmailBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityEditEmailBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}