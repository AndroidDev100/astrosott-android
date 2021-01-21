package com.astro.sott.activities.loginActivity.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.activities.loginActivity.viewModel.LoginViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityAstrLoginBinding;

public class AstrLoginActivity extends BaseBindingActivity<ActivityAstrLoginBinding> {
    private AstroLoginViewModel astroLoginViewModel;

    @Override
    protected ActivityAstrLoginBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityAstrLoginBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelCall();
        setClicks();
    }

    private void modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel.class);

    }

    private void setClicks() {
        getBinding().loginBtn.setOnClickListener(view -> {
            searchAccountv2();
        });
    }

    private void searchAccountv2() {
        astroLoginViewModel.searchAccountV2().observe(this, evergentCommonResponse -> {

            if (evergentCommonResponse.isStatus()) {
                Toast.makeText(this, evergentCommonResponse.getSearchAccountv2Response().getSearchAccountV2ResponseMessage().getMessage(), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
