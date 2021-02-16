package com.astro.sott.activities.verification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.activities.forgotPassword.ui.ChangePasswordActivity;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.activities.splash.ui.SplashActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.TextWatcherCallBack;
import com.astro.sott.databinding.ActivityVerificationBinding;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.CustomTextWatcher;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends BaseBindingActivity<ActivityVerificationBinding> {
    private AstroLoginViewModel astroLoginViewModel;
    private String loginType, emailMobile, password, from, token = "";
    private CountDownTimer countDownTimer;

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
        from = getIntent().getExtras().getString("from");

    }

    private void modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel.class);

    }

    private void countDownTimer() {

        if (countDownTimer != null)
            countDownTimer.cancel();

        countDownTimer = new CountDownTimer(300000, 1000) {
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
                if (String.format("%02d", minutes).equalsIgnoreCase("03")) {
                    getBinding().otpTitle.setVisibility(View.VISIBLE);
                    getBinding().resendOtp.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFinish() {
                getBinding().otpValid.setVisibility(View.GONE);

            }
        };
        countDownTimer.start();
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
        getBinding().progressBar.setVisibility(View.VISIBLE);
        String otp = getBinding().pin.getText().toString();
        if (!otp.equalsIgnoreCase("") && otp.length() == 6) {
            astroLoginViewModel.confirmOtp(loginType, emailMobile, otp).observe(this, evergentCommonResponse -> {

                if (evergentCommonResponse.isStatus()) {
                    // Toast.makeText(this, evergentCommonResponse.getConfirmOtpResponse().getConfirmOTPResponseMessage().getStatus(), Toast.LENGTH_SHORT).show();
                    if (from.equalsIgnoreCase("signIn")) {
                        getBinding().progressBar.setVisibility(View.GONE);

                        token = evergentCommonResponse.getConfirmOtpResponse().getConfirmOTPResponseMessage().getToken();
                        if (!token.equalsIgnoreCase("")) {
                            Intent intent = new Intent(this, ChangePasswordActivity.class);
                            intent.putExtra("token", token);
                            startActivity(intent);
                        }
                    } else if (from.equalsIgnoreCase("signUp")) {
                        createUser();

                    }

                } else {
                    Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    getBinding().pin.setLineColor(Color.parseColor("#f42d5b"));
                    getBinding().progressBar.setVisibility(View.GONE);
                    getBinding().invalidOtp.setVisibility(View.VISIBLE);


                }
            });
        } else {
            getBinding().invalidOtp.setVisibility(View.VISIBLE);
        }
    }

    private void createOtp() {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        astroLoginViewModel.createOtp(loginType, emailMobile).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);

            if (evergentCommonResponse.isStatus()) {
                countDownTimer();
                Toast.makeText(this, "Verification code had be sent to " + emailMobile, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                getBinding().pin.setLineColor(Color.parseColor("#f42d5b"));
                getBinding().progressBar.setVisibility(View.GONE);


            }
        });
    }

    private void createUser() {
        astroLoginViewModel.createUser(loginType, emailMobile, password).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {

                Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                new ActivityLauncher(VerificationActivity.this).homeScreen(VerificationActivity.this, HomeActivity.class);

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                getBinding().pin.setLineColor(Color.parseColor("#f42d5b"));
                getBinding().progressBar.setVisibility(View.GONE);


            }
        });
    }


}