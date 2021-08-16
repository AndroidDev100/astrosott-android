package com.astro.sott.activities.detailConfirmation;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityDetailConfirmationBinding;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.AccountServiceMessageItem;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;

public class DetailConfirmationActivity extends BaseBindingActivity<ActivityDetailConfirmationBinding> {
    private AstroLoginViewModel astroLoginViewModel;
    private String type = "", email_mobile = "", password = "", name = "";

    @Override
    protected ActivityDetailConfirmationBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityDetailConfirmationBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentValues();
        setClicks();
        modelCall();
    }

    private void setClicks() {

        getBinding().terms.setOnClickListener(v -> {
            new ActivityLauncher(this).termAndCondition(this);

        });
        getBinding().privacy.setOnClickListener(v -> {
            new ActivityLauncher(this).privacy(this);

        });
        getBinding().backIcon.setOnClickListener(v -> {
            onBackPressed();
        });
        getBinding().nameEdt.setText(name + "");
        getBinding().emailMobileEdt.setText(email_mobile + "");
        getBinding().submit.setOnClickListener(v -> {
            if (getBinding().checkbox.isChecked()) {
                createUser(password);
            } else {
                getBinding().errorCheckbox.setVisibility(View.VISIBLE);
            }
        });
    }

    private void modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel.class);

    }

    private void getIntentValues() {
        type = getIntent().getStringExtra(AppLevelConstants.TYPE_KEY);
        email_mobile = getIntent().getStringExtra(AppLevelConstants.EMAIL_MOBILE_KEY);
        password = getIntent().getStringExtra(AppLevelConstants.PASSWORD_KEY);
        name = getIntent().getStringExtra("name");

    }


    private void createUser(String password) {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        astroLoginViewModel.createUser(type, email_mobile, password, name, tabletSize).observe(this, evergentCommonResponse -> {
            if (evergentCommonResponse.isStatus()) {
                UserInfo.getInstance(this).setAccessToken(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getAccessToken());
                UserInfo.getInstance(this).setRefreshToken(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getRefreshToken());
                UserInfo.getInstance(this).setExternalSessionToken(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getExternalSessionToken());
                KsPreferenceKey.getInstance(this).setStartSessionKs(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getExternalSessionToken());
                astroLoginViewModel.addToken(UserInfo.getInstance(this).getExternalSessionToken());
                AppCommonMethods.onUserRegister(this);
                getContact();

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                getBinding().progressBar.setVisibility(View.GONE);
            }

        });
    }

    private void getContact() {
        astroLoginViewModel.getContact(UserInfo.getInstance(this).getAccessToken()).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);

            if (evergentCommonResponse.isStatus() && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage() != null && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage() != null && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().size() > 0) {
                UserInfo.getInstance(this).setFirstName(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getFirstName());
                UserInfo.getInstance(this).setLastName(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getLastName());
                UserInfo.getInstance(this).setEmail(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getEmail());
                UserInfo.getInstance(this).setCpCustomerId(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getCpCustomerID());
                if (evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getAccountRole() != null)
                    UserInfo.getInstance(this).setAccountRole(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getAccountRole());
                AppCommonMethods.setCrashlyticsUserId(this);
                getActiveSubscription();

            } else {
                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equalsIgnoreCase("111111111")) {
                    EvergentRefreshToken.refreshToken(DetailConfirmationActivity.this, UserInfo.getInstance(DetailConfirmationActivity.this).getRefreshToken()).observe(this, evergentCommonResponse1 -> {
                        if (evergentCommonResponse.isStatus()) {
                            getContact();
                        } else {
                            AppCommonMethods.removeUserPrerences(this);
                            new ActivityLauncher(DetailConfirmationActivity.this).homeScreen(DetailConfirmationActivity.this, HomeActivity.class);


                        }
                    });
                } else {
                    Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private String displayName = "";

    private void getActiveSubscription() {
        astroLoginViewModel.getActiveSubscription(UserInfo.getInstance(this).getAccessToken(), "").observe(this, evergentCommonResponse -> {
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
                    setActive();
                }
            } else {
                setActive();
            }
        });
    }

    private void setActive() {
        FirebaseEventManager.getFirebaseInstance(this).userLoginEvent(UserInfo.getInstance(this).getCpCustomerId(), UserInfo.getInstance(this).getAccountRole(), type);
        UserInfo.getInstance(this).setActive(true);
        UserInfo.getInstance(this).setSocialLogin(true);
        AppCommonMethods.setCleverTap(this);
        Toast.makeText(this, "User Logged in successfully.", Toast.LENGTH_SHORT).show();
        new ActivityLauncher(DetailConfirmationActivity.this).homeScreen(DetailConfirmationActivity.this, HomeActivity.class);

    }
}