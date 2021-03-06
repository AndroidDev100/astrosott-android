package com.astro.sott.activities.profile.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.activities.verification.VerificationActivity;
import com.astro.sott.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityEditProfileBinding;
import com.astro.sott.fragments.dialog.CommonDialogFragment;
import com.astro.sott.fragments.dialog.MaxisEditRestrictionPop;
import com.astro.sott.fragments.dialog.UnlinkDialogFragment;
import com.astro.sott.fragments.episodeFrament.EpisodeDialogFragment;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.usermanagment.modelClasses.getContact.SocialLoginTypesItem;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.userInfo.UserInfo;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

public class EditProfileActivity extends BaseBindingActivity<ActivityEditProfileBinding> implements MaxisEditRestrictionPop.EditDialogListener, CommonDialogFragment.EditDialogListener, UnlinkDialogFragment.EditDialogListener {
    private AstroLoginViewModel astroLoginViewModel;
    private CallbackManager callbackManager;
    private String email_mobile = "";
    private String type = "";
    private String unlinkType = "";
    private static final String EMAIL = "email, public_profile";

    private List<SocialLoginTypesItem> socialLoginTypesItem;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected ActivityEditProfileBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityEditProfileBinding.inflate(inflater);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseEventManager.getFirebaseInstance(this).trackScreenName(FirebaseEventManager.EDIT_PROFILE);
        AppCommonMethods.setProgressBar(getBinding().progressLay.progressHeart);
        modelCall();
        setClicks();
        setFb();
        setGoogleSignIn();
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

        if (!UserInfo.getInstance(this).isPasswordExists()) {
            if (UserInfo.getInstance(this).isSocialLogin()) {
                getBinding().editemail.setVisibility(View.GONE);
                getBinding().editMobileNo.setVisibility(View.GONE);
            }
        }
        if (UserInfo.getInstance(this).isFbLinked()) {
            getBinding().linkFb.setText(getResources().getString(R.string.unlink));
        } else {
            getBinding().linkFb.setText(getResources().getString(R.string.link));
        }

        if (UserInfo.getInstance(this).isGoogleLinked()) {
            getBinding().linkGoogle.setText(getResources().getString(R.string.unlink));
        } else {
            getBinding().linkGoogle.setText(getResources().getString(R.string.link));
        }
        getBinding().backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        getBinding().editname.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), EditNameActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });
        getBinding().editemail.setOnClickListener(view -> {
            if (UserInfo.getInstance(this).isMaxis()) {
                FragmentManager fm = getSupportFragmentManager();
                MaxisEditRestrictionPop cancelDialogFragment = MaxisEditRestrictionPop.newInstance(getResources().getString(R.string.maxis_edit_restriction_title), getResources().getString(R.string.maxis_edit_description), getResources().getString(R.string.ok_understand));
                cancelDialogFragment.setEditDialogCallBack(EditProfileActivity.this);
                cancelDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
            } else {
                Intent i = new Intent(getApplicationContext(), EditEmailActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        getBinding().editMobileNo.setOnClickListener(view -> {
            if (UserInfo.getInstance(this).isMaxis()) {
                FragmentManager fm = getSupportFragmentManager();
                MaxisEditRestrictionPop cancelDialogFragment = MaxisEditRestrictionPop.newInstance(getResources().getString(R.string.maxis_edit_restriction_title), getResources().getString(R.string.maxis_edit_description), getResources().getString(R.string.ok_understand));
                cancelDialogFragment.setEditDialogCallBack(EditProfileActivity.this);
                cancelDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
            } else {
                Intent i = new Intent(getApplicationContext(), EditMobileActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        getBinding().editpassword.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), EditPasswordActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            //   checkForPassword();

        });

        getBinding().linkFb.setOnClickListener(v -> {
            if (getBinding().linkFb.getText().toString().equalsIgnoreCase("Link")) {
                if (!UserInfo.getInstance(this).getEmail().equalsIgnoreCase("")) {
                    LoginManager.getInstance().logOut();
                    getBinding().loginButton.performClick();
                } else {
                    ToastHandler.show(getResources().getString(R.string.acount_mismatch), EditProfileActivity.this);

                }
            } else {
                unlinkType = "Facebook";
                unlinkDialog(getResources().getString(R.string.unlink_from) + " " + unlinkType + "?", getResources().getString(R.string.unlink_desc), getResources().getString(R.string.YES), getResources().getString(R.string.cancel));
            }

        });
        getBinding().linkGoogle.setOnClickListener(v -> {
            if (getBinding().linkGoogle.getText().toString().equalsIgnoreCase("Link")) {
                if (!UserInfo.getInstance(this).getEmail().equalsIgnoreCase("")) {
                    mGoogleSignInClient.signOut();
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, 4001);
                } else {
                    ToastHandler.show(getResources().getString(R.string.acount_mismatch), EditProfileActivity.this);


                }
            } else {
                unlinkType = "Google";
                unlinkDialog(getResources().getString(R.string.unlink_from) + " " + unlinkType + "?", getResources().getString(R.string.unlink_desc), getResources().getString(R.string.YES), getResources().getString(R.string.cancel));
            }
        });

    }

    private void unlinkDialog(String tiltle, String description, String actionBtn, String negativeActn) {
        FragmentManager fm = getSupportFragmentManager();
        UnlinkDialogFragment commonDialogFragment = UnlinkDialogFragment.newInstance(tiltle, description, actionBtn, negativeActn);
        commonDialogFragment.setEditDialogCallBack(this);
        commonDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void commonDialog(String tiltle, String description, String actionBtn) {
        FragmentManager fm = getSupportFragmentManager();
        CommonDialogFragment commonDialogFragment = CommonDialogFragment.newInstance(tiltle, description, actionBtn);
        commonDialogFragment.setEditDialogCallBack(this);
        commonDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void checkForPassword() {
        if (!UserInfo.getInstance(this).isPasswordExists()) {

        } else {
            createOtp();
        }
    }

    private void createOtp() {
        if (!UserInfo.getInstance(this).getEmail().equalsIgnoreCase("")) {
            type = "email";
            email_mobile = UserInfo.getInstance(this).getEmail();
        } else if (!UserInfo.getInstance(this).getMobileNumber().equalsIgnoreCase("")) {
            type = "mobile";
            email_mobile = UserInfo.getInstance(this).getMobileNumber();
//            email_mobile = num+UserInfo.getInstance(this).getMobileNumber();
            Log.d("mobilenum", email_mobile);
        }

        astroLoginViewModel.createOtp(type, email_mobile).observe(this, evergentCommonResponse -> {
            getBinding().progressLay.progressHeart.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                Intent intent = new Intent(this, VerificationActivity.class);
                intent.putExtra(AppLevelConstants.TYPE_KEY, type);
                intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, email_mobile);
                intent.putExtra(AppLevelConstants.FROM_KEY, "changePassword");
                startActivity(intent);

            } else {
                ToastHandler.show(evergentCommonResponse.getErrorMessage(), EditProfileActivity.this);
            }
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
            getBinding().progressLay.progressHeart.setVisibility(View.GONE);

            if (evergentCommonResponse.isStatus() && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage() != null && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage() != null && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().size() > 0) {
                UserInfo.getInstance(this).setFirstName(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getFirstName());
                UserInfo.getInstance(this).setLastName(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getLastName());
                if (evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getUserName() != null && !evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getUserName().equalsIgnoreCase("")) {
                    UserInfo.getInstance(this).setUserName(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getUserName());
                } else if (evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getAlternateUserName() != null && !evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getAlternateUserName().equalsIgnoreCase("")) {
                    UserInfo.getInstance(this).setAlternateUserName(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getAlternateUserName());
                }
                if (evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getSocialLoginTypes() != null && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getSocialLoginTypes().size() > 0) {
                    socialLoginTypesItem = evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getSocialLoginTypes();
                    AppCommonMethods.checkSocailLinking(this, socialLoginTypesItem);
                }

                UserInfo.getInstance(this).setMobileNumber(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getMobileNumber());
                UserInfo.getInstance(this).setPasswordExists(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).isPasswordExists());
                UserInfo.getInstance(this).setEmail(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getEmail());
                UserInfo.getInstance(this).setCpCustomerId(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getCpCustomerID());
                AppCommonMethods.setCrashlyticsUserId(this);
                UserInfo.getInstance(this).setActive(true);
            } else {
                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equalsIgnoreCase("111111111")) {
                    EvergentRefreshToken.refreshToken(EditProfileActivity.this, UserInfo.getInstance(EditProfileActivity.this).getRefreshToken()).observe(this, evergentCommonResponse1 -> {
                        if (evergentCommonResponse1.isStatus()) {
                            getContact();
                        } else {
                            AppCommonMethods.removeUserPrerences(this);
                            onBackPressed();

                        }
                    });
                } else {
                    ToastHandler.show(evergentCommonResponse.getErrorMessage(), EditProfileActivity.this);

                }

            }

            try {
                if (UserInfo.getInstance(this).isPasswordExists()) {
                    getBinding().psw.setText(getResources().getString(R.string.pswd_asterik));
                }
                String masked = AppCommonMethods.maskedEmail(EditProfileActivity.this);
                getBinding().email.setText(masked);
                if (!UserInfo.getInstance(this).getFirstName().equalsIgnoreCase("")) {
                    getBinding().name.setText(UserInfo.getInstance(this).getFirstName());
                } else {
                    getBinding().name.setText(getResources().getString(R.string.sooka_superstar));

                }
                getBinding().mobileNo.setText(AppCommonMethods.maskedMobile(EditProfileActivity.this));
            } catch (Exception ignored) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 4001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            type = "Google";
            if (account.getId() != null && !account.getId().equalsIgnoreCase("") && account.getEmail() != null && !account.getEmail().equalsIgnoreCase("")) {
                email_mobile = account.getEmail();
                if (email_mobile.equalsIgnoreCase(UserInfo.getInstance(this).getEmail())) {
                    updateProfile(account.getId(), type, true);
                } else {
                    commonDialog(type + " " + getResources().getString(R.string.linked_failed), getResources().getString(R.string.unable_link_social), getResources().getString(R.string.ok_single_exlamation));
                    //Toast.makeText(this, getResources().getString(R.string.acount_mismatch), Toast.LENGTH_SHORT).show();
                }
            } else {
                ToastHandler.show(getResources().getString(R.string.email_unavailable), EditProfileActivity.this);

            }
            // Signed in successfully, show authenticated UI.
            //  updateUI(account);
        } catch (ApiException e) {
            ToastHandler.show(getResources().getString(R.string.email_unavailable), EditProfileActivity.this);

        }
    }

    private void setFb() {
        callbackManager = CallbackManager.Factory.create();
        getBinding().loginButton.setReadPermissions(Arrays.asList(EMAIL));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code

                        Log.w("fb_login", loginResult + "");
                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                            if (object != null) {
                                if (object.has("email")) {
                                    try {
                                        String id = object.getString("id");
                                        email_mobile = object.getString("email");
                                        type = "Facebook";
                                        if (email_mobile != null && !email_mobile.equalsIgnoreCase("") && id != null && !id.equalsIgnoreCase("")) {
                                            if (email_mobile.equalsIgnoreCase(UserInfo.getInstance(EditProfileActivity.this).getEmail())) {
                                                updateProfile(id, type, true);
                                            } else {
                                                commonDialog(type + " " + getResources().getString(R.string.linked_failed), getResources().getString(R.string.unable_link_social), getResources().getString(R.string.ok_single_exlamation));
                                                //  Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.acount_mismatch), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                        ToastHandler.show(getResources().getString(R.string.email_unavailable),EditProfileActivity.this);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    ToastHandler.show(getResources().getString(R.string.email_unavailable), EditProfileActivity.this);

                                    LoginManager.getInstance().logOut();
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields",
                                "id,name,email");
                        graphRequest.setParameters(parameters);
                        graphRequest.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        //    getBinding().loginButton.setLoginBehavior(LoginBehavior.WEB_ONLY);
    }

    private void updateProfile(String name, String type, boolean isLinking) {
        getBinding().progressLay.progressHeart.setVisibility(View.VISIBLE);
        String acessToken = UserInfo.getInstance(this).getAccessToken();
        astroLoginViewModel.updateProfile(type, name, acessToken, "").observe(this, updateProfileResponse -> {
            getBinding().progressLay.progressHeart.setVisibility(View.GONE);
            if (updateProfileResponse.getResponse() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage().getResponseCode() != null && updateProfileResponse.getResponse().getUpdateProfileResponseMessage().getResponseCode().equalsIgnoreCase("1")) {
                if (type.equalsIgnoreCase("Facebook")) {
                    if (isLinking) {
                        UserInfo.getInstance(this).setFbLinked(true);
                        getBinding().linkFb.setText(getResources().getString(R.string.unlink));
                        ToastHandler.show(type + " " + getResources().getString(R.string.link_success),
                                EditProfileActivity.this);
                    } else {
                        UserInfo.getInstance(this).setFbLinked(false);
                        getBinding().linkFb.setText(getResources().getString(R.string.link));
                        ToastHandler.show(type + " " + getResources().getString(R.string.unlink_success),
                                EditProfileActivity.this);
                    }
                } else if (type.equalsIgnoreCase("Google")) {
                    if (isLinking) {
                        ToastHandler.show(type + " " + getResources().getString(R.string.link_success),
                                EditProfileActivity.this);
                        UserInfo.getInstance(this).setGoogleLinked(true);
                        getBinding().linkGoogle.setText(getResources().getString(R.string.unlink));
                    } else {
                        UserInfo.getInstance(this).setGoogleLinked(false);
                        getBinding().linkGoogle.setText(getResources().getString(R.string.link));
                        ToastHandler.show(type + " " + getResources().getString(R.string.unlink_success), EditProfileActivity.this);
                    }
                }

            } else {
                ToastHandler.show(updateProfileResponse.getErrorMessage() + "", EditProfileActivity.this);


            }
        });
    }

    private void setGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    public void onFinishEditDialog() {

    }

    @Override
    public void onActionBtnClicked() {

    }

    @Override
    public void unlinkBtnClicked() {
        updateProfile("null", unlinkType, false);
    }
}