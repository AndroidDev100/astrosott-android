package com.astro.sott.activities.forgotPassword.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.R;
import com.astro.sott.activities.search.ui.QuickSearchGenre;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityDetailsConfirmationBinding;

public class DetailsConfirmationActivity extends BaseBindingActivity<ActivityDetailsConfirmationBinding> {

    @Override
    protected ActivityDetailsConfirmationBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityDetailsConfirmationBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setClicks();
    }

    private void setClicks() {
        getBinding().backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}