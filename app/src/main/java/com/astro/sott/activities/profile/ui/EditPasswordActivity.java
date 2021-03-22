package com.astro.sott.activities.profile.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.activities.verification.VerificationActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityEditPasswordBinding;
import com.astro.sott.utils.userInfo.UserInfo;

public class EditPasswordActivity extends BaseBindingActivity<ActivityEditPasswordBinding> {
    private AstroLoginViewModel astroLoginViewModel;

    @Override
    protected ActivityEditPasswordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityEditPasswordBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setClicks();
        modelCall();
    }

    private void modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel.class);
    }

    private void setClicks() {
        getBinding().backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        getBinding().update.setOnClickListener(v -> {
            createOtp();
        });

    }

    private void createOtp() {
        String email_mobile = UserInfo.getInstance(this).getEmail();
        astroLoginViewModel.createOtp("email", UserInfo.getInstance(this).getEmail()).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);

            if (evergentCommonResponse.isStatus()) {
                Toast.makeText(this, getResources().getString(R.string.verification_code_Send) + email_mobile, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, VerificationActivity.class);
                intent.putExtra("type", "email");
                intent.putExtra("emailMobile", email_mobile);
                intent.putExtra("password", getBinding().newPsw.getText().toString());
                intent.putExtra("from", "changePassword");
                startActivity(intent);

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}