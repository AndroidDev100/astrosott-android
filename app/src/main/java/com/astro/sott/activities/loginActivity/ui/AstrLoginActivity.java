package com.astro.sott.activities.loginActivity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.activities.signUp.ui.SignUpActivity;
import com.astro.sott.activities.verification.VerificationActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.TextWatcherCallBack;
import com.astro.sott.databinding.ActivityAstrLoginBinding;
import com.astro.sott.utils.helpers.CustomTextWatcher;


public class AstrLoginActivity extends BaseBindingActivity<ActivityAstrLoginBinding> {
    private AstroLoginViewModel astroLoginViewModel;
    private String email_mobile, type;
    private final String MOBILE_REGEX = "^[0-9]*$";
    private final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


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
            //
            // searchAccountv2();
        });
        getBinding().forgotText.setOnClickListener(view -> {
            checkEmailVaildation();
        });
        getBinding().google.setOnClickListener(view -> {
        });
        getBinding().fb.setOnClickListener(view -> {
            //  confirmOtp();
        });
        getBinding().apple.setOnClickListener(view -> {
            //   resetPassword();
        });
        getBinding().signup.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
        setTextWatcher();
    }

    private void setTextWatcher() {
        getBinding().emailMobileEdt.addTextChangedListener(new CustomTextWatcher(this, new TextWatcherCallBack() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBinding().errorEmail.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }));
    }

    private void searchAccountv2() {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        astroLoginViewModel.searchAccountV2(type, email_mobile).observe(this, evergentCommonResponse -> {

            if (evergentCommonResponse.isStatus()) {
                createOtp();
                // Toast.makeText(this, evergentCommonResponse.getSearchAccountv2Response().getSearchAccountV2ResponseMessage().getMessage(), Toast.LENGTH_SHORT).show();

            } else {
                getBinding().progressBar.setVisibility(View.GONE);

                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void checkEmailVaildation() {
        email_mobile = getBinding().emailMobileEdt.getText().toString();
        if (!email_mobile.equalsIgnoreCase("")) {
            if (email_mobile.matches(MOBILE_REGEX)) {
                if (email_mobile.length() == 10 || email_mobile.length() == 11) {
                    type = "mobile";
                    searchAccountv2();
                } else {
                    getBinding().errorEmail.setVisibility(View.VISIBLE);
                    getBinding().errorEmail.setText(getResources().getString(R.string.mobile_error));

                }
            } else if (email_mobile.matches(EMAIL_REGEX)) {
                type = "email";
                searchAccountv2();
            } else {
                getBinding().errorEmail.setVisibility(View.VISIBLE);
                getBinding().errorEmail.setText(getResources().getString(R.string.email_mobile_error));

            }
        } else {
            getBinding().errorEmail.setVisibility(View.VISIBLE);
            getBinding().errorEmail.setText(getResources().getString(R.string.email_mobile_error));

        }
    }

    private void createOtp() {
        astroLoginViewModel.createOtp(type, email_mobile).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);

            if (evergentCommonResponse.isStatus()) {
                Toast.makeText(this, "Verification code had be sent to " + email_mobile, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, VerificationActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("emailMobile", email_mobile);
                intent.putExtra("password", "password");
                intent.putExtra("from", "signIn");
                startActivity(intent);

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    String token = "";

    private void confirmOtp() {
       /* astroLoginViewModel.confirmOtp(loginType, emailMobile).observe(this, evergentCommonResponse -> {

            if (evergentCommonResponse.isStatus()) {
                Toast.makeText(this, evergentCommonResponse.getConfirmOtpResponse().getConfirmOTPResponseMessage().getStatus(), Toast.LENGTH_SHORT).show();
                token = evergentCommonResponse.getConfirmOtpResponse().getConfirmOTPResponseMessage().getToken();
            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }


   /* private void resetPassword() {
        astroLoginViewModel.resetPassword(token, password).observe(this, evergentCommonResponse -> {

            if (evergentCommonResponse.isStatus()) {
                Toast.makeText(this, evergentCommonResponse.getResetPasswordResponse().getResetPasswordResponseMessage().getStatus(), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/


}
