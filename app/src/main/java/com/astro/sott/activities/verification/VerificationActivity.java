package com.astro.sott.activities.verification;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
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
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.TextWatcherCallBack;
import com.astro.sott.databinding.ActivityVerificationBinding;
import com.astro.sott.fragments.verification.Verification;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.CustomTextWatcher;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;

public class VerificationActivity extends BaseBindingActivity<ActivityVerificationBinding> {
    private AstroLoginViewModel astroLoginViewModel;
    private String loginType, emailMobile, password, oldPassword = "", from, token = "";
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
        loginType = getIntent().getExtras().getString(AppLevelConstants.TYPE_KEY);
        emailMobile = getIntent().getExtras().getString(AppLevelConstants.EMAIL_MOBILE_KEY);
        if (getIntent().getExtras().getString(AppLevelConstants.OLD_PASSWORD_KEY) != null)
            oldPassword = getIntent().getExtras().getString(AppLevelConstants.OLD_PASSWORD_KEY);

        password = getIntent().getExtras().getString(AppLevelConstants.PASSWORD_KEY);
        from = getIntent().getExtras().getString(AppLevelConstants.FROM_KEY);
        if (emailMobile != null && !emailMobile.equalsIgnoreCase("")) {
            getBinding().descriptionTxt.setText(getResources().getString(R.string.onetime_pass_code_text) + "\n" + emailMobile);
        }
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

                getBinding().otpValid.setVisibility(View.VISIBLE);
                getBinding().otpValid.setText("OTP valid for " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "s");
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
            getBinding().pin.setText("");
            createOtp();
        });
        getBinding().verify.setOnClickListener(view -> {
            confirmOtp();
        });

        getBinding().verifyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
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
                        if (token != null && !token.equalsIgnoreCase("")) {
                            Intent intent = new Intent(this, ChangePasswordActivity.class);
                            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("token", token);
                            startActivity(intent);
                        }
                    } else if (from.equalsIgnoreCase("signUp")) {
                        createUser();

                    } else if (from.equalsIgnoreCase("changePassword")) {
                        changePassword();
                    }

                } else {
                    Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
//                    getBinding().pin.setLineColor(Color.parseColor("#f42d5b"));
                    getBinding().errorLine.setVisibility(View.VISIBLE);
                    getBinding().progressBar.setVisibility(View.GONE);
                    getBinding().invalidOtp.setVisibility(View.VISIBLE);


                }
            });
        } else {
            getBinding().progressBar.setVisibility(View.GONE);

            getBinding().invalidOtp.setVisibility(View.VISIBLE);
        }
    }

    private void changePassword() {
        astroLoginViewModel.changePassword(UserInfo.getInstance(this).getAccessToken(), oldPassword, password).observe(this, changePasswordResponse -> {
            if (changePasswordResponse.isStatus() && changePasswordResponse.getResponse().getChangePasswordResponseMessage() != null) {
                Toast.makeText(this, getResources().getString(R.string.password_changed), Toast.LENGTH_SHORT).show();
                new ActivityLauncher(VerificationActivity.this).profileScreenRedirection(VerificationActivity.this, HomeActivity.class);

            } else {
                Toast.makeText(this, changePasswordResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    private void createOtp() {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        astroLoginViewModel.createOtp(loginType, emailMobile).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);

            if (evergentCommonResponse.isStatus()) {
                countDownTimer();

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
//                getBinding().pin.setLineColor(Color.parseColor("#f42d5b"));
                getBinding().errorLine.setVisibility(View.VISIBLE);

                getBinding().progressBar.setVisibility(View.GONE);


            }
        });
    }

    private void createUser() {
        astroLoginViewModel.createUser(loginType, emailMobile, password, "").observe(this, evergentCommonResponse -> {
            if (evergentCommonResponse.isStatus()) {
                UserInfo.getInstance(this).setAccessToken(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getAccessToken());
                UserInfo.getInstance(this).setRefreshToken(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getRefreshToken());
                UserInfo.getInstance(this).setExternalSessionToken(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getExternalSessionToken());
                KsPreferenceKey.getInstance(this).setStartSessionKs(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getExternalSessionToken());
                astroLoginViewModel.addToken(UserInfo.getInstance(this).getExternalSessionToken());
                getContact();
                Toast.makeText(this, getResources().getString(R.string.registered_success), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
//                getBinding().pin.setLineColor(Color.parseColor("#f42d5b"));
                getBinding().errorLine.setVisibility(View.VISIBLE);

                getBinding().progressBar.setVisibility(View.GONE);
            }

        });
    }

    private void login(String loginType, String emailMobile, String password) {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        astroLoginViewModel.loginUser(loginType, emailMobile, password).observe(this, evergentCommonResponse -> {

            if (evergentCommonResponse.isStatus()) {

            } else {
                getBinding().progressBar.setVisibility(View.GONE);
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getContact() {
        astroLoginViewModel.getContact(UserInfo.getInstance(this).getAccessToken()).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);

            if (evergentCommonResponse.isStatus() && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage() != null && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage() != null && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().size() > 0) {
                UserInfo.getInstance(this).setFirstName(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getFirstName());
                UserInfo.getInstance(this).setLastName(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getLastName());
                if (evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getUserName() != null && !evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getUserName().equalsIgnoreCase("")) {
                    UserInfo.getInstance(this).setUserName(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getUserName());
                } else if (evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getAlternateUserName() != null && !evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getAlternateUserName().equalsIgnoreCase("")) {
                    UserInfo.getInstance(this).setAlternateUserName(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getAlternateUserName());
                }
                UserInfo.getInstance(this).setPasswordExists(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).isPasswordExists());
                UserInfo.getInstance(this).setEmail(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getEmail());
                UserInfo.getInstance(this).setActive(true);
                new ActivityLauncher(VerificationActivity.this).profileScreenRedirection(VerificationActivity.this, HomeActivity.class);
                Toast.makeText(this, getResources().getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();

            } else {
                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equalsIgnoreCase("111111111")) {
                    EvergentRefreshToken.refreshToken(VerificationActivity.this, UserInfo.getInstance(VerificationActivity.this).getRefreshToken()).observe(this, evergentCommonResponse1 -> {
                        if (evergentCommonResponse.isStatus()) {
                            getContact();
                        } else {
                            AppCommonMethods.removeUserPrerences(this);
                            onBackPressed();

                        }
                    });
                } else {
                    Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}