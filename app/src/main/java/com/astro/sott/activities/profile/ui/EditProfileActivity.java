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
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityEditProfileBinding;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.userInfo.UserInfo;

public class EditProfileActivity extends BaseBindingActivity<ActivityEditProfileBinding> {
    private AstroLoginViewModel astroLoginViewModel;

    @Override
    protected ActivityEditProfileBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityEditProfileBinding.inflate(inflater);
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

    @Override
    protected void onResume() {
        super.onResume();
        getContact();
    }

    private void setClicks() {

        getBinding().backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        getBinding().editname.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), EditNameActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });
        getBinding().editemail.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), EditEmailActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });
        getBinding().editMobileNo.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), EditMobileActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });
        getBinding().editpassword.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), EditPasswordActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

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
                UserInfo.getInstance(this).setMobileNumber(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getMobileNumber());
                UserInfo.getInstance(this).setPasswordExists(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).isPasswordExists());
                UserInfo.getInstance(this).setEmail(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getEmail());
                UserInfo.getInstance(this).setCpCustomerId(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getCpCustomerID());
                UserInfo.getInstance(this).setActive(true);
            } else {
                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equalsIgnoreCase("111111111")) {
                    EvergentRefreshToken.refreshToken(EditProfileActivity.this, UserInfo.getInstance(EditProfileActivity.this).getRefreshToken()).observe(this, evergentCommonResponse1 -> {
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

            try {
                String masked = AppCommonMethods.maskedEmail(EditProfileActivity.this);
                getBinding().email.setText(masked);
                getBinding().name.setText(UserInfo.getInstance(this).getFirstName());
                getBinding().psw.setText("********");
                getBinding().mobileNo.setText(AppCommonMethods.maskedMobile(EditProfileActivity.this));
            } catch (Exception ignored) {

            }
        });
    }
}