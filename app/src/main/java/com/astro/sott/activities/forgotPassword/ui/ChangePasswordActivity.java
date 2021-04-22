package com.astro.sott.activities.forgotPassword.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityChangePasswordBinding;
import com.astro.sott.utils.helpers.ActivityLauncher;

public class ChangePasswordActivity extends BaseBindingActivity<ActivityChangePasswordBinding> {
    private String token = "";
    private AstroLoginViewModel astroLoginViewModel;
    private boolean passwordVisibilityNewPassword = false;
    private boolean passwordVisibilityConfirmPassword = false;

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
            String newPassword = getBinding().newPasswordEdt.getText().toString();
            String confirmPassword = getBinding().confirmPasswordEdt.getText().toString();
            if (!confirmPassword.equals("")) {
                if (confirmPassword.equals(newPassword)) {
                    resetPassword();
                } else {
                    getBinding().errorPasssword.setText("Confirm password does't match with password");
                    getBinding().errorPasssword.setVisibility(View.VISIBLE);
                }
            } else {
                getBinding().errorPasssword.setVisibility(View.VISIBLE);
                getBinding().errorPasssword.setText("Please enter valid Password ");
            }
        });


        getBinding().eyeIconNewPassword.setOnClickListener(view -> {
            if (passwordVisibilityNewPassword) {
                getBinding().eyeIconNewPassword.setBackgroundResource(R.drawable.ic_outline_visibility_off_light);
                getBinding().newPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordVisibilityNewPassword = false;
            } else {
                passwordVisibilityNewPassword = true;
                getBinding().newPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT);
                getBinding().eyeIconNewPassword.setBackgroundResource(R.drawable.ic_outline_visibility_light);

            }
            getBinding().newPasswordEdt.setSelection(getBinding().newPasswordEdt.getText().length());
        });

        getBinding().eyeIconConfirmPassword.setOnClickListener(view -> {
            if (passwordVisibilityConfirmPassword) {
                getBinding().eyeIconConfirmPassword.setBackgroundResource(R.drawable.ic_outline_visibility_off_light);
                getBinding().confirmPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordVisibilityConfirmPassword = false;
            } else {
                passwordVisibilityConfirmPassword = true;
                getBinding().confirmPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT);
                getBinding().eyeIconConfirmPassword.setBackgroundResource(R.drawable.ic_outline_visibility_light);

            }
            getBinding().confirmPasswordEdt.setSelection(getBinding().confirmPasswordEdt.getText().length());
        });


    }

    private void modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel.class);

    }

    private void resetPassword() {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        getBinding().errorPasssword.setVisibility(View.GONE);
        String password = getBinding().confirmPasswordEdt.getText().toString();
        astroLoginViewModel.resetPassword(token, password).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                Toast.makeText(this, "You have successfully updated your password. All other registered devices will be auto logout for security purposes.", Toast.LENGTH_SHORT).show();
                new ActivityLauncher(this).astrLoginActivity(this, AstrLoginActivity.class, "profile");

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}