package com.astro.sott.activities.profile.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.TextWatcherCallBack;
import com.astro.sott.databinding.ActivitySetPasswordBinding;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.CustomTextWatcher;
import com.astro.sott.utils.userInfo.UserInfo;

public class SetPasswordActivity extends BaseBindingActivity<ActivitySetPasswordBinding> {
    private final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9@$!%*?&]{8,16}$";
    private boolean passwordVisibilityNewPassword = false;
    private String token = "", newEmail = "", newMobile = "";
    private AstroLoginViewModel astroLoginViewModel;

    @Override
    protected ActivitySetPasswordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySetPasswordBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelCall();
        if (getIntent().getExtras().getString("newEmail") != null)
            newEmail = getIntent().getExtras().getString("newEmail");
        if (getIntent().getExtras().getString("newMobile") != null)
            newMobile = getIntent().getExtras().getString("newMobile");

        token = UserInfo.getInstance(this).getAccessToken();
        setClicks();
    }

    private void setClicks() {
        getBinding().backButton.setOnClickListener(v -> {
            onBackPressed();
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

        getBinding().newPasswordEdt.addTextChangedListener(new CustomTextWatcher(this, new TextWatcherCallBack() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBinding().errorPasssword.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }));
        getBinding().button.setOnClickListener(v -> {
            String password = getBinding().newPasswordEdt.getText().toString();
            if (!password.equalsIgnoreCase("") && password.matches(PASSWORD_REGEX)) {
                resetPassword(password);
            }
        });
    }

    private void modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel.class);

    }

    private void resetPassword(String password) {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        getBinding().errorPasssword.setVisibility(View.GONE);
        astroLoginViewModel.resetPassword(token, password).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                if (!newEmail.equalsIgnoreCase(""))
                    updateProfile(newEmail, "email");
                if (!newMobile.equalsIgnoreCase(""))
                    updateProfile(newMobile, "mobile");
            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfile(String name, String type) {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        String acessToken = UserInfo.getInstance(this).getAccessToken();
        astroLoginViewModel.updateProfile(type, name, acessToken).observe(this, updateProfileResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (updateProfileResponse.getResponse() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage().getResponseCode() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage().getResponseCode().equalsIgnoreCase("1")) {
                new ActivityLauncher(this).profileActivity(this);
                Toast.makeText(this, updateProfileResponse.getResponse().getUpdateProfileResponseMessage().getMessage() + "", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, updateProfileResponse.getErrorMessage() + "", Toast.LENGTH_SHORT).show();

            }
        });
    }
}