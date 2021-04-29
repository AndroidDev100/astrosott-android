package com.astro.sott.activities.isThatYou;

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
import com.astro.sott.activities.forgotPassword.ui.ForgotPasswordActivity;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.TextWatcherCallBack;
import com.astro.sott.databinding.ActivityIsThatYouBinding;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.CustomTextWatcher;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;

public class IsThatYouActivity extends BaseBindingActivity<ActivityIsThatYouBinding> {
    private String emailMobile = "", type = "", socialId = "";
    private boolean passwordVisibilityConfirmPassword = false;

    private AstroLoginViewModel astroLoginViewModel;
    private final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9@$!%*?&]{8,16}$";


    @Override
    protected ActivityIsThatYouBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityIsThatYouBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra(AppLevelConstants.TYPE_KEY);
        socialId = getIntent().getStringExtra(AppLevelConstants.SOCIAL_ID);
        emailMobile = getIntent().getExtras().getString(AppLevelConstants.EMAIL_MOBILE_KEY);
        getBinding().emailEdt.setText(emailMobile);
        setClicks();
        modelCall();
    }

    private void setClicks() {
        getBinding().update.setOnClickListener(v -> {
            String password = getBinding().confirmPasswordEdt.getText().toString();
            if (checkPasswordValidation(password)) {
                login(password);
            } else {
                getBinding().errorPasssword.setVisibility(View.VISIBLE);
            }
        });

        getBinding().confirmPasswordEdt.addTextChangedListener(new CustomTextWatcher(this, new TextWatcherCallBack() {
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
        getBinding().forgotText.setOnClickListener(v -> {
            new ActivityLauncher(this).forgotPasswordActivity(this, ForgotPasswordActivity.class);
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

    private void login(String password) {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        astroLoginViewModel.loginUser("email", emailMobile, password).observe(this, evergentCommonResponse -> {

            if (evergentCommonResponse.isStatus()) {
                UserInfo.getInstance(this).setAccessToken(evergentCommonResponse.getLoginResponse().getGetOAuthAccessTokenv2ResponseMessage().getAccessToken());
                UserInfo.getInstance(this).setRefreshToken(evergentCommonResponse.getLoginResponse().getGetOAuthAccessTokenv2ResponseMessage().getRefreshToken());
                UserInfo.getInstance(this).setExternalSessionToken(evergentCommonResponse.getLoginResponse().getGetOAuthAccessTokenv2ResponseMessage().getExternalSessionToken());
                KsPreferenceKey.getInstance(this).setStartSessionKs(evergentCommonResponse.getLoginResponse().getGetOAuthAccessTokenv2ResponseMessage().getExternalSessionToken());
                astroLoginViewModel.addToken(UserInfo.getInstance(this).getExternalSessionToken());
                getContact();
                updateProfile(socialId, type);

            } else {
                getBinding().progressBar.setVisibility(View.GONE);
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();


            }
        });
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

                // setCleverTap();
            } else {
                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equalsIgnoreCase("111111111")) {
                    EvergentRefreshToken.refreshToken(IsThatYouActivity.this, UserInfo.getInstance(IsThatYouActivity.this).getRefreshToken()).observe(this, evergentCommonResponse1 -> {
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
        });
    }

    private void updateProfile(String name, String type) {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        String acessToken = UserInfo.getInstance(this).getAccessToken();
        astroLoginViewModel.updateProfile(type, name, acessToken).observe(this, updateProfileResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (updateProfileResponse.getResponse() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage().getResponseCode() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage().getResponseCode().equalsIgnoreCase("1")) {
                UserInfo.getInstance(this).setActive(true);
                new ActivityLauncher(IsThatYouActivity.this).profileScreenRedirection(IsThatYouActivity.this, HomeActivity.class);

            } else {
                Toast.makeText(this, updateProfileResponse.getErrorMessage() + "", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean checkPasswordValidation(String password) {
        if (!password.equalsIgnoreCase("") && password.matches(PASSWORD_REGEX)) {
            return true;
        }
        return false;
    }

    private void modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel.class);

    }
}