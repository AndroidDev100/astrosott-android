package com.astro.sott.activities.verification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.TextWatcherCallBack;
import com.astro.sott.databinding.ActivityVerificationBinding;
import com.astro.sott.utils.helpers.CustomTextWatcher;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends BaseBindingActivity<ActivityVerificationBinding> {
    private AstroLoginViewModel astroLoginViewModel;
    private String loginType, emailMobile, password;

    @Override
    protected ActivityVerificationBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityVerificationBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        modelCall();
        setClicks();
        countDownTimer();
    }

    private void getIntentData() {
        loginType = getIntent().getExtras().getString("type");
        emailMobile = getIntent().getExtras().getString("emailMobile");
        password = getIntent().getExtras().getString("password");

    }

    private void modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel.class);

    }

    private void countDownTimer() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                getBinding().otpTitle.setVisibility(View.GONE);
                getBinding().otpValid.setVisibility(View.VISIBLE);
                getBinding().resendOtp.setVisibility(View.GONE);
                getBinding().otpValid.setText("OTP valid for " + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds) + "s");
            }

            @Override
            public void onFinish() {
                getBinding().otpTitle.setVisibility(View.VISIBLE);
                getBinding().otpValid.setVisibility(View.GONE);
                getBinding().resendOtp.setVisibility(View.VISIBLE);

            }
        }.start();
    }

    private void setClicks() {
        getBinding().resendOtp.setOnClickListener(view -> {
            createOtp();
        });
        getBinding().verify.setOnClickListener(view -> {
            confirmOtp();
        });

        setTextWatcher();

    }

    private void setTextWatcher() {

        getBinding().pin.addTextChangedListener(new CustomTextWatcher(this, new TextWatcherCallBack() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBinding().invalidOtp.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }));
    }

    private void confirmOtp() {
        String otp = getBinding().pin.getText().toString();
        if (!otp.equalsIgnoreCase("") || otp.length() < 6) {
            astroLoginViewModel.confirmOtp(loginType, emailMobile, otp).observe(this, evergentCommonResponse -> {

                if (evergentCommonResponse.isStatus()) {
                    Toast.makeText(this, evergentCommonResponse.getConfirmOtpResponse().getConfirmOTPResponseMessage().getStatus(), Toast.LENGTH_SHORT).show();
                    // token = evergentCommonResponse.getConfirmOtpResponse().getConfirmOTPResponseMessage().getToken();
                } else {
                    Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            getBinding().invalidOtp.setVisibility(View.VISIBLE);
        }
    }

    private void createOtp() {
        astroLoginViewModel.createOtp(loginType, emailMobile).observe(this, evergentCommonResponse -> {

            if (evergentCommonResponse.isStatus()) {
                countDownTimer();
                Toast.makeText(this, evergentCommonResponse.getCreateOtpResponse().getCreateOTPResponseMessage().getStatus(), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}