package com.astro.sott.activities.forgotPassword.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends BaseBindingActivity<ActivityChangePasswordBinding> {
    private String token = "";
    private AstroLoginViewModel astroLoginViewModel;

    @Override
    protected ActivityChangePasswordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityChangePasswordBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = getIntent().getExtras().getString("token");
        modelCall();
        setCLicks();
    }

    private void setCLicks() {
        getBinding().update.setOnClickListener(view -> {
            resetPassword();
        });
    }

    private void modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel.class);

    }

    private void resetPassword() {
        getBinding().progressBar.setVisibility(View.VISIBLE);

        String password = getBinding().confirmPasswordEdt.getText().toString();
        astroLoginViewModel.resetPassword(token, password).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                Toast.makeText(this, "You have successfully updated your password. All other registered devices will be auto logout for security purposes.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}