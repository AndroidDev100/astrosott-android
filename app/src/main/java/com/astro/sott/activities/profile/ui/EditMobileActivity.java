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
import com.astro.sott.activities.confirmPassword.ui.ConfirmPasswordActivity;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.TextWatcherCallBack;
import com.astro.sott.databinding.ActivityEditEmailBinding;
import com.astro.sott.databinding.ActivityEditMobileBinding;
import com.astro.sott.utils.billing.BillingProcessor;
import com.astro.sott.utils.billing.TransactionDetails;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.CustomTextWatcher;
import com.astro.sott.utils.userInfo.UserInfo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class EditMobileActivity extends BaseBindingActivity<ActivityEditMobileBinding> {
    private boolean alreadyMobile = false;
    private AstroLoginViewModel astroLoginViewModel;
    private final String MOBILE_REGEX = "^[0-9]*$";
    private String email_mobile = "", type = "";

    private String password, newMobile;
    private final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9@$!%*?&]{8,16}$";

    @Override
    protected ActivityEditMobileBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityEditMobileBinding.inflate(inflater);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCommonMethods.setProgressBar(getBinding().progressLay.progressHeart);
        setHeader();
        modelCall();
        setClicks();
    }

    private void setHeader() {
        if (UserInfo.getInstance(this).getMobileNumber().equalsIgnoreCase("")) {
            getBinding().title.setText(getResources().getString(R.string.set_Mobile));
        } else {
            getBinding().layoutEmail.setVisibility(View.VISIBLE);
            getBinding().title.setText(getResources().getString(R.string.edit_Mobile));
            alreadyMobile = true;
            getBinding().email.setText(AppCommonMethods.maskedMobile(this));

        }
    }

    private void setClicks() {
        getBinding().backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        getBinding().button.setOnClickListener(v -> {
            if (checkMobileValidation()) {
                if (alreadyMobile && UserInfo.getInstance(this).isPasswordExists()) {
                    if (checkPasswordValidation()) {
                        checkCredential();
                    }
                } else if (alreadyMobile && !UserInfo.getInstance(this).isPasswordExists()) {
                    createOtp(newMobile);
                } else if (!alreadyMobile && UserInfo.getInstance(this).isPasswordExists()) {
                    if (checkPasswordValidation()) {
                        checkCredential();
                    }
                } else if (!alreadyMobile && !UserInfo.getInstance(this).isPasswordExists()) {
                    createOtp(newMobile);
                }
            }

        });
        getBinding().newMobile.setOnFocusChangeListener((v, hasFocus) -> {
            checkMobileValidation();
            checkPasswordValidation();

        });
        getBinding().confirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            checkMobileValidation();
            checkPasswordValidation();

        });
        getBinding().newMobile.addTextChangedListener(new

                CustomTextWatcher(this, new TextWatcherCallBack() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBinding().errorEmail.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }));

        getBinding().confirmPassword.addTextChangedListener(new

                CustomTextWatcher(this, new TextWatcherCallBack() {
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
    }

    private boolean checkMobileValidation() {
        newMobile = getBinding().newMobile.getText().toString();
        if (!newMobile.equalsIgnoreCase("")) {
            if (newMobile.matches(MOBILE_REGEX)) {
                char firstChar = newMobile.charAt(0);
                if (String.valueOf(firstChar).equalsIgnoreCase("6")) {
                    newMobile = newMobile;
                } else {
                    newMobile = "6" + newMobile;
                    getBinding().newMobile.setText(newMobile);
                }
                if (newMobile.length() == 11 || newMobile.length() == 12) {
                    return true;
                } else {
                    getBinding().errorEmail.setVisibility(View.VISIBLE);
                    getBinding().errorEmail.setText(getResources().getString(R.string.mobile_error));
                    return false;

                }
            } else {
                getBinding().errorEmail.setVisibility(View.VISIBLE);
                getBinding().errorEmail.setText(getResources().getString(R.string.mobile_error));
                return false;
            }
        } else {
            getBinding().errorEmail.setVisibility(View.VISIBLE);
            getBinding().errorEmail.setText(getResources().getString(R.string.field_cannot_empty));
            return false;
        }
    }

    private boolean checkPasswordValidation() {
        password = getBinding().confirmPassword.getText().toString();
        if (!password.equalsIgnoreCase("")) {
            if (password.matches(PASSWORD_REGEX)) {
                getBinding().errorPasssword.setVisibility(View.GONE);
                return true;
            } else {
                getBinding().errorPasssword.setVisibility(View.VISIBLE);
                getBinding().errorPasssword.setText(getResources().getString(R.string.password_rules));
                return false;
            }
        } else {
            getBinding().errorPasssword.setText(getResources().getString(R.string.field_cannot_empty));
            getBinding().errorPasssword.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private void checkCredential() {
        if (UserInfo.getInstance(this).getEmail().equalsIgnoreCase("")) {
            type = "Mobile";
            email_mobile = UserInfo.getInstance(this).getMobileNumber();
        } else {
            type = "Email";
            email_mobile = UserInfo.getInstance(this).getEmail();
        }
        getBinding().progressLay.progressHeart.setVisibility(View.VISIBLE);
        astroLoginViewModel.checkCredential(password, email_mobile, type).observe(this, checkCredentialResponse -> {
            if (checkCredentialResponse != null && checkCredentialResponse.getResponse() != null && checkCredentialResponse.getResponse().getCheckCredentialsResponseMessage() != null && checkCredentialResponse.getResponse().getCheckCredentialsResponseMessage().getResponseCode().equalsIgnoreCase("1")) {
                createOtp();
            } else {
                getBinding().progressLay.progressHeart.setVisibility(View.GONE);
                Toast.makeText(this, checkCredentialResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createOtp() {
        type = "mobile";
        astroLoginViewModel.createOtp(type, newMobile).observe(this, evergentCommonResponse -> {
            if (evergentCommonResponse.isStatus()) {
                getBinding().progressLay.progressHeart.setVisibility(View.GONE);
                Intent intent = new Intent(this, EditVerificationActivity.class);
                intent.putExtra(AppLevelConstants.TYPE_KEY, type);
                intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, newMobile);
                intent.putExtra(AppLevelConstants.SCREEN_FROM,  getBinding().title.getText().toString());
                if (!newMobile.equalsIgnoreCase(""))
                    intent.putExtra("newMobile", newMobile);
                intent.putExtra(AppLevelConstants.PASSWORD_KEY, password);
                intent.putExtra(AppLevelConstants.FROM_KEY, AppLevelConstants.CONFIRM_PASSWORD);
                startActivity(intent);
            } else {
                getBinding().progressLay.progressHeart.setVisibility(View.GONE);
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createOtp(String mobile) {
        if (alreadyMobile) {
            astroLoginViewModel.createOtp("mobile", UserInfo.getInstance(this).getMobileNumber()).observe(this, evergentCommonResponse -> {

                if (evergentCommonResponse.isStatus()) {
                    //   Toast.makeText(this, "Verification code had be sent to " + email_mobile, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, EditVerificationActivity.class);
                    intent.putExtra(AppLevelConstants.TYPE_KEY, "mobile");
                    intent.putExtra(AppLevelConstants.SCREEN_FROM,  getBinding().title.getText().toString());
                    intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, UserInfo.getInstance(this).getEmail());
                    intent.putExtra("newMobile", mobile);
                    intent.putExtra(AppLevelConstants.PASSWORD_KEY, "");
                    intent.putExtra(AppLevelConstants.FROM_KEY, AppLevelConstants.CONFIRM_PASSWORD_WITHOUT_PASSWORD);
                    startActivity(intent);


                } else {
                    Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            astroLoginViewModel.createOtp("mobile", mobile).observe(this, evergentCommonResponse -> {

                if (evergentCommonResponse.isStatus()) {
                    Intent intent = new Intent(this, EditVerificationActivity.class);
                    intent.putExtra(AppLevelConstants.TYPE_KEY, "mobile");
                    intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, mobile);
                    intent.putExtra("newMobile", mobile);
                    intent.putExtra(AppLevelConstants.SCREEN_FROM, getBinding().title.getText().toString());
                    intent.putExtra(AppLevelConstants.PASSWORD_KEY, "");
                    intent.putExtra(AppLevelConstants.FROM_KEY, AppLevelConstants.CONFIRM_PASSWORD_WITHOUT_PASSWORD);
                    startActivity(intent);


                } else {
                    Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel.class);

    }
}