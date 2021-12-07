package com.astro.sott.activities.forgotPassword.ui;

import androidx.annotation.NonNull;
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
import com.astro.sott.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.TextWatcherCallBack;
import com.astro.sott.databinding.ActivityChangePasswordBinding;
import com.astro.sott.fragments.episodeFrament.EpisodeDialogFragment;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.CustomTextWatcher;

public class ChangePasswordActivity extends BaseBindingActivity<ActivityChangePasswordBinding> implements PasswordChangedDialog.EditDialogListener {
    private String token = "";
    private AstroLoginViewModel astroLoginViewModel;
    private final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9@$!%*?&]{8,16}$";

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

        setTextWather();
        getBinding().backArrow.setOnClickListener(v -> {
            onBackPressed();
        });
        getBinding().update.setOnClickListener(view -> {
            String newPassword = getBinding().newPasswordEdt.getText().toString();
            String confirmPassword = getBinding().confirmPasswordEdt.getText().toString();
            if (!confirmPassword.equals("")) {
                if (confirmPassword.matches(PASSWORD_REGEX)) {
                    if (confirmPassword.equals(newPassword)) {
                        resetPassword();
                    } else {
                        getBinding().errorConfirmPasssword.setText("Confirm password does't match with password");
                        getBinding().errorConfirmPasssword.setVisibility(View.VISIBLE);
                    }
                } else {
                    getBinding().errorConfirmPasssword.setVisibility(View.VISIBLE);
                    getBinding().errorConfirmPasssword.setText(getResources().getString(R.string.password_error));
                    checkPassword();
                }

            } else {
                getBinding().errorConfirmPasssword.setVisibility(View.VISIBLE);
                getBinding().errorConfirmPasssword.setText(getResources().getString(R.string.valid_password));
                checkPassword();
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

    private void setTextWather() {
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

        getBinding().confirmPasswordEdt.addTextChangedListener(new CustomTextWatcher(this, new TextWatcherCallBack() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBinding().errorConfirmPasssword.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }));
    }

    private void checkPassword() {
        String newPassword = getBinding().newPasswordEdt.getText().toString();

        if (!newPassword.equals("")) {
            if (newPassword.matches(PASSWORD_REGEX)) {

            } else {
                getBinding().errorPasssword.setVisibility(View.VISIBLE);
                getBinding().errorPasssword.setText(getResources().getString(R.string.password_error));
            }
        } else {
            getBinding().errorPasssword.setVisibility(View.VISIBLE);
            getBinding().errorPasssword.setText(getResources().getString(R.string.valid_password));
        }
    }

    private void modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel.class);

    }

    private void resetPassword() {
        getBinding().progressBar.progressHeart.setVisibility(View.VISIBLE);
        getBinding().errorPasssword.setVisibility(View.GONE);
        String password = getBinding().confirmPasswordEdt.getText().toString();
        astroLoginViewModel.resetPassword(token, password).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.progressHeart.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                FragmentManager fm = getSupportFragmentManager();
                PasswordChangedDialog cancelDialogFragment = PasswordChangedDialog.newInstance("Detail Page", "");
                cancelDialogFragment.setEditDialogCallBack(ChangePasswordActivity.this);
                cancelDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPasswordChanged() {
        new ActivityLauncher(this).astrLoginActivity(this, AstrLoginActivity.class, "Profile");
    }
}