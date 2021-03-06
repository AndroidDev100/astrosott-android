package com.astro.sott.activities.verification;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.activities.forgotPassword.ui.ChangePasswordActivity;
import com.astro.sott.activities.forgotPassword.ui.PasswordChangedDialog;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.activities.loginActivity.ui.AccountBlockedDialog;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.activities.profile.ui.EditPasswordActivity;
import com.astro.sott.activities.profile.ui.SetPasswordActivity;
import com.astro.sott.activities.subscriptionActivity.ui.ProfileSubscriptionActivity;
import com.astro.sott.activities.subscriptionActivity.ui.SubscriptionDetailActivity;
import com.astro.sott.activities.verification.dialog.MaximumLimitDialog;
import com.astro.sott.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.TextWatcherCallBack;
import com.astro.sott.databinding.ActivityVerificationBinding;
import com.astro.sott.fragments.dialog.CommonDialogFragment;
import com.astro.sott.fragments.episodeFrament.EpisodeDialogFragment;
import com.astro.sott.fragments.verification.Verification;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.thirdParty.CleverTapManager.CleverTapManager;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.AccountServiceMessageItem;
import com.astro.sott.usermanagment.modelClasses.getContact.SocialLoginTypesItem;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.CustomTextWatcher;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;

import java.util.List;

public class VerificationActivity extends BaseBindingActivity<ActivityVerificationBinding> implements MaximumLimitDialog.EditDialogListener, PasswordChangedDialog.EditDialogListener, CommonDialogFragment.EditDialogListener {
    private AstroLoginViewModel astroLoginViewModel;
    private String loginType, emailMobile, password, oldPassword = "", from, token = "", newEmail = "", newMobile = "", origin = "";
    private CountDownTimer countDownTimer;
    private String num = "+91";
    private StringBuilder stringBuilder = new StringBuilder();
    ;
    private List<SocialLoginTypesItem> socialLoginTypesItem;

    @Override
    protected ActivityVerificationBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityVerificationBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCommonMethods.setProgressBar(getBinding().progressLay.progressHeart);
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
        if (getIntent().getExtras().getString("newEmail") != null)
            newEmail = getIntent().getExtras().getString("newEmail");
        if (getIntent().getExtras().getString("newMobile") != null)
            newMobile = getIntent().getExtras().getString("newMobile");
        Log.d("eMOBILENUMBER", newMobile + "");


        password = getIntent().getExtras().getString(AppLevelConstants.PASSWORD_KEY);
        from = getIntent().getExtras().getString(AppLevelConstants.FROM_KEY);
        if (from.equalsIgnoreCase(AppLevelConstants.CONFIRM_PASSWORD) || from.equalsIgnoreCase(AppLevelConstants.CONFIRM_PASSWORD_WITHOUT_PASSWORD)) {
            if (loginType.equalsIgnoreCase("Email")) {
                emailMobile = newEmail;
            } else if (loginType.equalsIgnoreCase("Mobile")) {
                emailMobile = newMobile;
                stringBuilder = stringBuilder.append(emailMobile);
                Log.d("eMOBILENUMBER", stringBuilder + "");
            }
        }
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
        getBinding().progressLay.progressHeart.setVisibility(View.VISIBLE);
        String otp = getBinding().pin.getText().toString();
        if (!otp.equalsIgnoreCase("") && otp.length() == 6) {

            astroLoginViewModel.confirmOtp(loginType, emailMobile, otp).observe(this, evergentCommonResponse -> {
                if (evergentCommonResponse.isStatus()) {
                    token = evergentCommonResponse.getConfirmOtpResponse().getConfirmOTPResponseMessage().getToken();

                    // Toast.makeText(this, evergentCommonResponse.getConfirmOtpResponse().getConfirmOTPResponseMessage().getStatus(), Toast.LENGTH_SHORT).show();
                    if (from.equalsIgnoreCase("signIn")) {
                        getBinding().progressLay.progressHeart.setVisibility(View.GONE);

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
                    } else if (from.equalsIgnoreCase(AppLevelConstants.CONFIRM_PASSWORD)) {
                        if (!newEmail.equalsIgnoreCase(""))
                            updateProfile(newEmail, "email");
                        if (!newMobile.equalsIgnoreCase(""))
                            updateProfile(newMobile, "mobile");
                    } else if (from.equalsIgnoreCase(AppLevelConstants.CONFIRM_PASSWORD_WITHOUT_PASSWORD)) {
                        setPassword();
                    }

                } else {
                    ToastHandler.show(evergentCommonResponse.getErrorMessage() + "", VerificationActivity.this);

//                    getBinding().pin.setLineColor(Color.parseColor("#f42d5b"));
                    getBinding().errorLine.setVisibility(View.VISIBLE);
                    getBinding().progressLay.progressHeart.setVisibility(View.GONE);
                    getBinding().invalidOtp.setVisibility(View.VISIBLE);


                }
            });
        } else {
            getBinding().progressLay.progressHeart.setVisibility(View.GONE);

            getBinding().invalidOtp.setVisibility(View.VISIBLE);
        }
    }

    private void setPassword() {
        getBinding().progressLay.progressHeart.setVisibility(View.GONE);
        new ActivityLauncher(this).setPasswordActivity(this, token, newEmail, newMobile);
    }

    private void updateProfile(String name, String type) {
        getBinding().progressLay.progressHeart.setVisibility(View.VISIBLE);
        String acessToken = UserInfo.getInstance(this).getAccessToken();
        astroLoginViewModel.updateProfile(type, name, acessToken, "").observe(this, updateProfileResponse -> {
            getBinding().progressLay.progressHeart.setVisibility(View.GONE);
            if (updateProfileResponse.getResponse() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage().getResponseCode() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage().getResponseCode().equalsIgnoreCase("1")) {
                if (type.equalsIgnoreCase("email")) {
                    AppCommonMethods.emailPushCleverTap(this, name);
                    commonDialog(getResources().getString(R.string.email_updated), getResources().getString(R.string.email_updated_Description), getResources().getString(R.string.ok_single_exlamation));
                } else {
                    AppCommonMethods.mobilePushCleverTap(this, name);
                    commonDialog(getResources().getString(R.string.mobile_updated), getResources().getString(R.string.mobile_updated_description), getResources().getString(R.string.ok_single_exlamation));

                }
            } else {
                if (updateProfileResponse.getErrorCode().equalsIgnoreCase("eV1062")) {
                    if (type.equalsIgnoreCase("email")) {
                        commonDialog(getResources().getString(R.string.email_updated_failed), getResources().getString(R.string.email_updated_failed_desc), getResources().getString(R.string.ok_double_exlamation));
                    } else {
                        commonDialog(getResources().getString(R.string.mobile_updated_failed), getResources().getString(R.string.mobile_updated_description_failed), getResources().getString(R.string.ok_double_exlamation));
                    }
                } else {
                    ToastHandler.show(updateProfileResponse.getErrorMessage(),
                            VerificationActivity.this);
                }
            }
        });
    }

    private void commonDialog(String tiltle, String description, String actionBtn) {
        FragmentManager fm = getSupportFragmentManager();
        CommonDialogFragment commonDialogFragment = CommonDialogFragment.newInstance(tiltle, description, actionBtn);
        commonDialogFragment.setEditDialogCallBack(this);
        commonDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void changePassword() {
        Intent i = new Intent(getApplicationContext(), EditPasswordActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    private void createOtp() {
        getBinding().progressLay.progressHeart.setVisibility(View.VISIBLE);
        astroLoginViewModel.createOtp(loginType, emailMobile).observe(this, evergentCommonResponse -> {
            getBinding().progressLay.progressHeart.setVisibility(View.GONE);

            if (evergentCommonResponse.isStatus()) {
                ToastHandler.show("Verification code resend " + (evergentCommonResponse.getCreateOtpResponse().getCreateOTPResponseMessage().getCurrentOTPCount() - 1) + " of " + (evergentCommonResponse.getCreateOtpResponse().getCreateOTPResponseMessage().getMaxOTPCount() - 1),
                        VerificationActivity.this);
                countDownTimer();
            } else {
                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2846")) {
                    FragmentManager fm = getSupportFragmentManager();
                    MaximumLimitDialog cancelDialogFragment = MaximumLimitDialog.newInstance("Detail Page", "");
                    cancelDialogFragment.setEditDialogCallBack(VerificationActivity.this);
                    cancelDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
                } else {
                    ToastHandler.show(evergentCommonResponse.getErrorMessage(),
                            VerificationActivity.this);
                }
//                getBinding().pin.setLineColor(Color.parseColor("#f42d5b"));
                getBinding().errorLine.setVisibility(View.VISIBLE);

                getBinding().progressLay.progressHeart.setVisibility(View.GONE);


            }
        });
    }

    private void createUser() {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        astroLoginViewModel.createUser(loginType, emailMobile, password, "", tabletSize).observe(this, evergentCommonResponse -> {
            if (evergentCommonResponse.isStatus()) {
                UserInfo.getInstance(this).setAccessToken(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getAccessToken());
                UserInfo.getInstance(this).setRefreshToken(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getRefreshToken());
                UserInfo.getInstance(this).setExternalSessionToken(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getExternalSessionToken());
                KsPreferenceKey.getInstance(this).setStartSessionKs(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getExternalSessionToken());
                astroLoginViewModel.addToken(UserInfo.getInstance(this).getExternalSessionToken());
                getContact();
                try {
                    origin = CleverTapManager.getInstance().getLoginOrigin();
                    AppCommonMethods.onUserRegister(this);
                    CleverTapManager.getInstance().setSignInEvent(this, origin, loginType);
                } catch (Exception ex) {
                }
            } else {
                ToastHandler.show( evergentCommonResponse.getErrorMessage(),
                        VerificationActivity.this);
//                getBinding().pin.setLineColor(Color.parseColor("#f42d5b"));
                getBinding().errorLine.setVisibility(View.VISIBLE);

                getBinding().progressLay.progressHeart.setVisibility(View.GONE);
            }
        });
    }

    private void login(String loginType, String emailMobile, String password) {
        getBinding().progressLay.progressHeart.setVisibility(View.VISIBLE);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        astroLoginViewModel.loginUser(loginType, emailMobile, password, tabletSize).observe(this, evergentCommonResponse -> {

            if (evergentCommonResponse.isStatus()) {

            } else {
                getBinding().progressLay.progressHeart.setVisibility(View.GONE);
                ToastHandler.show( evergentCommonResponse.getErrorMessage(),
                        VerificationActivity.this);
            }
        });
    }

    private void getContact() {
        astroLoginViewModel.getContact(UserInfo.getInstance(this).getAccessToken()).observe(this, evergentCommonResponse -> {

            if (evergentCommonResponse.isStatus() && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage() != null && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage() != null && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().size() > 0) {
                UserInfo.getInstance(this).setFirstName(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getFirstName());
                UserInfo.getInstance(this).setLastName(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getLastName());
                if (evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getUserName() != null && !evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getUserName().equalsIgnoreCase("")) {
                    UserInfo.getInstance(this).setUserName(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getUserName());
                } else if (evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getAlternateUserName() != null && !evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getAlternateUserName().equalsIgnoreCase("")) {
                    UserInfo.getInstance(this).setAlternateUserName(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getAlternateUserName());
                }
                if (evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getSocialLoginTypes() != null && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getSocialLoginTypes().size() > 0) {
                    socialLoginTypesItem = evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getSocialLoginTypes();
                    AppCommonMethods.checkSocailLinking(this, socialLoginTypesItem);
                }
                if (evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getAccountRole() != null)
                    UserInfo.getInstance(this).setAccountRole(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getAccountRole());
                UserInfo.getInstance(this).setCpCustomerId(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getCpCustomerID());
                UserInfo.getInstance(this).setMobileNumber(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getMobileNumber());
                UserInfo.getInstance(this).setPasswordExists(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).isPasswordExists());
                UserInfo.getInstance(this).setEmail(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getEmail());
                AppCommonMethods.setCrashlyticsUserId(this);
                getActiveSubscription();
            } else {
                getBinding().progressLay.progressHeart.setVisibility(View.GONE);
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
                    ToastHandler.show( evergentCommonResponse.getErrorMessage(),
                            VerificationActivity.this);

                }

            }
        });
    }

    private String displayName = "";

    private void getActiveSubscription() {
        astroLoginViewModel.getActiveSubscription(UserInfo.getInstance(this).getAccessToken(), "").observe(this, evergentCommonResponse -> {
            getBinding().progressLay.progressHeart.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                if (evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage() != null) {
                    if (evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage() != null && evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage().size() > 0) {
                        for (AccountServiceMessageItem accountServiceMessageItem : evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage()) {
                            if (!accountServiceMessageItem.isFreemium()) {
                                if (accountServiceMessageItem.getDisplayName() != null)
                                    displayName = accountServiceMessageItem.getDisplayName();
                            }
                        }
                        if (!displayName.equalsIgnoreCase("")) {
                            UserInfo.getInstance(this).setVip(true);
                            setActive();
                        } else {
                            UserInfo.getInstance(this).setVip(false);
                            setActive();
                        }
                    } else {
                        UserInfo.getInstance(this).setVip(false);
                        setActive();
                    }
                } else {
                    UserInfo.getInstance(this).setVip(false);
                    setActive();
                }
            } else {
                UserInfo.getInstance(this).setVip(false);
                setActive();
            }
        });
    }

    private void setActive() {
        FirebaseEventManager.getFirebaseInstance(this).userLoginEvent(UserInfo.getInstance(this).getCpCustomerId(), UserInfo.getInstance(this).getAccountRole(), "Evergent");
        UserInfo.getInstance(this).setActive(true);
        AppCommonMethods.setCleverTap(this);
        // new ActivityLauncher(VerificationActivity.this).profileScreenRedirection(VerificationActivity.this, HomeActivity.class);
        ToastHandler.show(  getResources().getString(R.string.registered_success),
                VerificationActivity.this);
        onBackPressed();

        // Toast.makeText(this, getResources().getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishEditDialog() {

    }

    @Override
    public void onPasswordChanged() {
        new ActivityLauncher(VerificationActivity.this).profileScreenRedirection(VerificationActivity.this, HomeActivity.class);
    }

    @Override
    public void onActionBtnClicked() {
        new ActivityLauncher(this).profileActivity(this);
    }
}