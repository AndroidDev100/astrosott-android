package com.astro.sott.activities.profile.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
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
import com.astro.sott.activities.subscriptionActivity.ui.ProfileSubscriptionActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.TextWatcherCallBack;
import com.astro.sott.databinding.ActivitySetPasswordBinding;
import com.astro.sott.fragments.dialog.CommonDialogFragment;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.CustomTextWatcher;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.userInfo.UserInfo;

public class SetPasswordActivity extends BaseBindingActivity<ActivitySetPasswordBinding>  implements CommonDialogFragment.EditDialogListener{
    private final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9@$!%*?&]{8,16}$";
    private boolean passwordVisibilityNewPassword = false;
    private String accessToken = "", newEmail = "", newMobile = "", token = "";


    private AstroLoginViewModel astroLoginViewModel;

    @Override
    protected ActivitySetPasswordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySetPasswordBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelCall();
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getString("newEmail") != null)
            newEmail = getIntent().getExtras().getString("newEmail");
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getString("newMobile") != null)
            newMobile = getIntent().getExtras().getString("newMobile");

        accessToken = UserInfo.getInstance(this).getAccessToken();
        token = getIntent().getExtras().getString("token");

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
        astroLoginViewModel.setPassword(accessToken, password).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                if (!newEmail.equalsIgnoreCase(""))
                    updateProfile(newEmail, "email");
                if (!newMobile.equalsIgnoreCase(""))
                    updateProfile(newMobile, "mobile");
            } else {
                ToastHandler.show(evergentCommonResponse.getErrorMessage(),SetPasswordActivity.this);

            }
        });
    }

    private void updateProfile(String name, String type) {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        astroLoginViewModel.updateProfile(type, name, accessToken, "").observe(this, updateProfileResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (updateProfileResponse.getResponse() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage().getResponseCode() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage().getResponseCode().equalsIgnoreCase("1")) {
                if (type.equalsIgnoreCase("email")) {
                    AppCommonMethods.emailPushCleverTap(this, name);
                    commonDialog(getResources().getString(R.string.email_updated), getResources().getString(R.string.email_updated_Description), getResources().getString(R.string.ok_single_exlamation));
                } else {
                    AppCommonMethods.mobilePushCleverTap(this, name);
                    commonDialog(getResources().getString(R.string.mobile_updated), getResources().getString(R.string.mobile_updated_description), getResources().getString(R.string.ok_single_exlamation));

                }
            } else {
                ToastHandler.show(updateProfileResponse.getErrorMessage() + "", SetPasswordActivity.this);

            }
        });
    }

    private void commonDialog(String tiltle, String description, String actionBtn) {
        FragmentManager fm = getSupportFragmentManager();
        CommonDialogFragment commonDialogFragment = CommonDialogFragment.newInstance(tiltle, description, actionBtn);
        commonDialogFragment.setEditDialogCallBack(this);
        commonDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    @Override
    public void onActionBtnClicked() {
        new ActivityLauncher(this).profileActivity(this);
    }
}