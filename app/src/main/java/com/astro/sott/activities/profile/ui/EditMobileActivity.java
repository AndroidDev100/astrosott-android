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
import com.astro.sott.activities.verification.VerificationActivity;
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
    private BillingProcessor billingProcessor;
    private boolean alreadyMobile = false;
    private AstroLoginViewModel astroLoginViewModel;
    private final String MOBILE_REGEX = "^[0-9]*$";

    @Override
    protected ActivityEditMobileBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityEditMobileBinding.inflate(inflater);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeader();
        modelCall();
        setClicks();


        //   billingProcessor.purchase(this, "com.sott.astro.com.my.tvod.1290", "DEVELOPER PAYLOAD HERE");
         /* List<String> purchases = billingProcessor.listOwnedProducts();

            for (String purchase: purchases){
                Log.w("Purchased Item", purchase);
            }
*/
        // billingProcessor.consumePurchase("com.sott.astro.com.my.tvod.1290");
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
            String mobile = getBinding().newMobile.getText().toString();
            if (!mobile.equalsIgnoreCase("") && mobile.matches(MOBILE_REGEX)) {
                if (alreadyMobile && UserInfo.getInstance(this).isPasswordExists()) {
                    Intent intent = new Intent(this, ConfirmPasswordActivity.class);
                    intent.putExtra("newMobile", mobile);
                    startActivity(intent);
                } else if (alreadyMobile && !UserInfo.getInstance(this).isPasswordExists()) {
                    createOtp(mobile);
                } else if (!alreadyMobile && UserInfo.getInstance(this).isPasswordExists()) {
                    Intent intent = new Intent(this, ConfirmPasswordActivity.class);
                    intent.putExtra("newMobile", mobile);
                    startActivity(intent);
                } else if (!alreadyMobile && !UserInfo.getInstance(this).isPasswordExists()) {
                    createOtp(mobile);

                }
            } else {
                getBinding().errorEmail.setVisibility(View.VISIBLE);
                getBinding().errorEmail.setText(getResources().getString(R.string.mobile_error));
            }

        });
        getBinding().newMobile.addTextChangedListener(new CustomTextWatcher(this, new TextWatcherCallBack() {
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
    }

    private void createOtp(String mobile) {
        if (alreadyMobile) {
            astroLoginViewModel.createOtp("mobile", UserInfo.getInstance(this).getMobileNumber()).observe(this, evergentCommonResponse -> {

                if (evergentCommonResponse.isStatus()) {
                    //   Toast.makeText(this, "Verification code had be sent to " + email_mobile, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, VerificationActivity.class);
                    intent.putExtra(AppLevelConstants.TYPE_KEY, "mobile");
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
                    //   Toast.makeText(this, "Verification code had be sent to " + email_mobile, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, VerificationActivity.class);
                    intent.putExtra(AppLevelConstants.TYPE_KEY, "mobile");
                    intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, mobile);
                    intent.putExtra("newMobile", mobile);
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