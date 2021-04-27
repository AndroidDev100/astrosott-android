package com.astro.sott.activities.profile.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivitySetPasswordBinding;

public class SetPasswordActivity extends BaseBindingActivity<ActivitySetPasswordBinding> {

    @Override
    protected ActivitySetPasswordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySetPasswordBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setClicks();
    }

    private void setClicks() {
        getBinding().backButton.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}