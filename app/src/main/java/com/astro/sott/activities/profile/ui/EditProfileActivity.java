package com.astro.sott.activities.profile.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityEditProfileBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.userInfo.UserInfo;

public class EditProfileActivity extends BaseBindingActivity<ActivityEditProfileBinding> {

    @Override
    protected ActivityEditProfileBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityEditProfileBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setClicks();
    }

    private void setClicks() {
        try {
            String masked = AppCommonMethods.maskedEmail(EditProfileActivity.this);
            getBinding().email.setText(masked);
            getBinding().name.setText(UserInfo.getInstance(this).getFirstName());
            getBinding().mobileNo.setText(AppCommonMethods.maskedMobile(EditProfileActivity.this));
        } catch (Exception ignored) {

        }
        getBinding().backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        getBinding().editemail.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), EditEmailActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });
        getBinding().editpassword.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), EditPasswordActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });

    }
}