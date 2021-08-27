package com.astro.sott.activities.profile.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.activities.verification.VerificationActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.TextWatcherCallBack;
import com.astro.sott.databinding.ActivityEditPasswordBinding;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.CustomTextWatcher;
import com.astro.sott.utils.userInfo.UserInfo;

public class EditPasswordActivity extends BaseBindingActivity<ActivityEditPasswordBinding> {
    private AstroLoginViewModel astroLoginViewModel;
    private final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9@$!%*?&]{8,16}$";

    @Override
    protected ActivityEditPasswordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityEditPasswordBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseEventManager.getFirebaseInstance(this).trackScreenName(FirebaseEventManager.EDIT_PASSWORD);
        setHeader();
        //UiInitialization();
        setClicks();
        modelCall();
    }

    private void modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel.class);
    }

    private void setHeader() {
        if (!UserInfo.getInstance(this).isPasswordExists()) {
            getBinding().title.setText(getResources().getString(R.string.set_password));
            getBinding().layoutExistingpassword.setVisibility(View.GONE);
        } else {
            getBinding().title.setText(getResources().getString(R.string.edit_password));

        }
    }

    private void setClicks() {
        getBinding().backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        getBinding().update.setOnClickListener(v -> {
            String newPassword = getBinding().newPsw.getText().toString();
            if (UserInfo.getInstance(this).isPasswordExists()) {
                String oldPassword = getBinding().existingPsw.getText().toString();

                if (!oldPassword.matches(PASSWORD_REGEX)) {
                    getBinding().existPasswordError.setVisibility(View.VISIBLE);
                } else if (!newPassword.matches(PASSWORD_REGEX)) {
                    getBinding().newPasswordError.setVisibility(View.VISIBLE);

                } else {
                    createOtp();
                }
            } else {
                setPassword(newPassword);
            }
        });
        setTextWatcher();
    }

    private void setPassword(String newPassword) {
        astroLoginViewModel.setPassword(UserInfo.getInstance(this).getAccessToken(), newPassword).observe(this, evergentCommonResponse -> {
            new ActivityLauncher(this).profileActivity(this);
        });
    }

    private void setTextWatcher() {

        getBinding().newPsw.addTextChangedListener(new CustomTextWatcher(this, new TextWatcherCallBack() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBinding().newPasswordError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }));

        getBinding().existingPsw.addTextChangedListener(new CustomTextWatcher(this, new TextWatcherCallBack() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBinding().existPasswordError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }));
    }

    private String email_mobile = "", type = "",num="+91";
    private StringBuilder stringBuilder =new StringBuilder();

    private void createOtp() {
        if (!UserInfo.getInstance(this).getEmail().equalsIgnoreCase("")) {
            type = "email";
            email_mobile = UserInfo.getInstance(this).getEmail();
        } else if (!UserInfo.getInstance(this).getMobileNumber().equalsIgnoreCase("")) {
            type = "mobile";
            email_mobile =UserInfo.getInstance(this).getMobileNumber();
//            email_mobile = num+UserInfo.getInstance(this).getMobileNumber();
            Log.d("mobilenum",email_mobile);
        }

        astroLoginViewModel.createOtp(type, email_mobile).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                // Toast.makeText(this, getResources().getString(R.string.verification_code_Send) + email_mobile, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, VerificationActivity.class);
                intent.putExtra(AppLevelConstants.TYPE_KEY, type);
                intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, email_mobile);
                intent.putExtra(AppLevelConstants.OLD_PASSWORD_KEY, getBinding().existingPsw.getText().toString());
                intent.putExtra(AppLevelConstants.PASSWORD_KEY, getBinding().newPsw.getText().toString());
                intent.putExtra(AppLevelConstants.FROM_KEY, "changePassword");
                startActivity(intent);

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}