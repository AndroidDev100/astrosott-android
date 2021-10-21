package com.astro.sott.activities.loginActivity.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.astro.sott.R;
import com.astro.sott.activities.detailConfirmation.DetailConfirmationActivity;
import com.astro.sott.activities.forgotPassword.ui.ForgotPasswordActivity;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.isThatYou.IsThatYouActivity;
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel;
import com.astro.sott.activities.signUp.ui.SignUpActivity;
import com.astro.sott.activities.verification.VerificationActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.TextWatcherCallBack;
import com.astro.sott.databinding.ActivityAstrLoginBinding;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.thirdParty.CleverTapManager.CleverTapManager;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.AccountServiceMessageItem;
import com.astro.sott.usermanagment.modelClasses.getContact.SocialLoginTypesItem;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.CustomTextWatcher;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;
import com.clevertap.android.sdk.CleverTapAPI;
import com.facebook.AccessToken;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class AstrLoginActivity extends BaseBindingActivity<ActivityAstrLoginBinding> implements View.OnClickListener, AccountBlockedDialog.EditDialogListener {
    private AstroLoginViewModel astroLoginViewModel;
    private static final String EMAIL = "email, public_profile";
    private String email_mobile, type;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private String from;
    private boolean passwordVisibility = false;
    private String passwordError = "";
    private String name = "";
    private final String MOBILE_REGEX = "^[0-9]*$";
    private final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private final String PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=\\S+$).{8,16}$";


    @Override
    protected ActivityAstrLoginBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityAstrLoginBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getStringExtra(AppLevelConstants.FROM_KEY) != null)
            from = getIntent().getStringExtra(AppLevelConstants.FROM_KEY);
        modelCall();
        setClicks();
        CleverTapManager.getInstance().setLoginOrigin(from);
        FirebaseEventManager.getFirebaseInstance(this).trackScreenName(FirebaseEventManager.LOGIN);
        setGoogleSignIn();
        updateWithToken(AccessToken.getCurrentAccessToken());

        setFb();
    }

    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            LoginManager.getInstance().logOut();
        } else {
        }
    }

    private void setFb() {
        callbackManager = CallbackManager.Factory.create();
        getBinding().loginButton.setReadPermissions(Arrays.asList(EMAIL));
        //  LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(EMAIL));
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
                                        String fbname = object.getString("name");
                                        String email = object.getString("email");
                                        String id = object.getString("id");
                                        email_mobile = email;
                                        type = "Facebook";
                                        if (email_mobile != null && !email_mobile.equalsIgnoreCase("") && id != null && !id.equalsIgnoreCase("")) {
                                            name = fbname;
                                            login(id + "");
                                        } else {
                                            Toast.makeText(AstrLoginActivity.this, getResources().getString(R.string.email_unavailable), Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(AstrLoginActivity.this, getResources().getString(R.string.email_unavailable), Toast.LENGTH_SHORT).show();
                                    LoginManager.getInstance().logOut();
                                }
                            } else {
                                Log.w("fb_login", "null" + "");
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
                        getBinding().progressBar.setVisibility(View.GONE);
                        Toast.makeText(AstrLoginActivity.this, getResources().getString(R.string.email_unavailable), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        getBinding().progressBar.setVisibility(View.GONE);
                        Toast.makeText(AstrLoginActivity.this, getResources().getString(R.string.email_unavailable), Toast.LENGTH_SHORT).show();

                    }
                });

        //  getBinding().loginButton.setLoginBehavior(LoginBehavior.WEB_ONLY);
    }

    private void setGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        getBinding().signInButton.setSize(SignInButton.SIZE_STANDARD);
        getBinding().signInButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    }

    private void modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel.class);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getBinding().progressBar.setVisibility(View.GONE);
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
                name = account.getDisplayName();
                email_mobile = account.getEmail();
                login(account.getId());
            } else {
                Toast.makeText(AstrLoginActivity.this, getResources().getString(R.string.email_unavailable), Toast.LENGTH_SHORT).show();

            }
            // Signed in successfully, show authenticated UI.
            //  updateUI(account);
        } catch (ApiException e) {
            getBinding().progressBar.setVisibility(View.GONE);
            Toast.makeText(AstrLoginActivity.this, getResources().getString(R.string.email_unavailable), Toast.LENGTH_SHORT).show();
        }
    }

    private void setClicks() {
        getBinding().backIcon.setOnClickListener(view -> {
            onBackPressed();
        });
        getBinding().term.setOnClickListener(v -> {
            new ActivityLauncher(this).termAndCondition(this);
        });
        getBinding().privacy.setOnClickListener(v -> {
            new ActivityLauncher(this).privacy(this);
        });
        getBinding().loginBtn.setOnClickListener(view -> {
            if (checkEmailVaildation()) {
                String password = getBinding().passwordEdt.getText().toString();
                if (checkPasswordValidation(password)) {
                    getBinding().passwordError.setText(getString(R.string.password_rules));
                    login(password);
                } else {
                    getBinding().passwordError.setTextColor(getResources().getColor(R.color.red_live));
                    getBinding().errorEmail.setText(getResources().getString(R.string.mobile_suggestion));
                    getBinding().passwordError.setText(passwordError);
                    getBinding().passwordError.setVisibility(View.VISIBLE);
                }
            }
        });
        getBinding().passwordEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String password = getBinding().passwordEdt.getText().toString();
                    if (checkPasswordValidation(password)) {
                        getBinding().passwordError.setTextColor(getResources().getColor(R.color.heather));
                        getBinding().passwordError.setText(getString(R.string.password_rules));

                    } else {
                        getBinding().passwordError.setTextColor(getResources().getColor(R.color.red_live));
                        getBinding().errorEmail.setText(getResources().getString(R.string.mobile_suggestion));
                        getBinding().passwordError.setText(passwordError);
                    }
                }
                return false;
            }
        });
        getBinding().passwordEdt.setOnFocusChangeListener((v, hasFocus) -> {
            checkEmailVaildation();
        });
        getBinding().forgotText.setOnClickListener(view -> {
            new ActivityLauncher(this).forgotPasswordActivity(this, ForgotPasswordActivity.class);
        });
        getBinding().google.setOnClickListener(view -> {
            //  getBinding().signInButton.performClick();
            mGoogleSignInClient.signOut();
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 4001);
        });

        getBinding().fb.setOnClickListener(view -> {
            LoginManager.getInstance().logOut();
            getBinding().loginButton.performClick();
            //  confirmOtp();
        });

        getBinding().signup.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            intent.putExtra(AppLevelConstants.FROM_KEY, from);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            // new ActivityLauncher(this).signupActivity(this, SignUpActivity.class, "");

        });
        setTextWatcher();
    }

    private void signIn() {

    }

    private void setTextWatcher() {
        getBinding().eyeIcon.setOnClickListener(view -> {
            if (passwordVisibility) {
                getBinding().eyeIcon.setBackgroundResource(R.drawable.ic_outline_visibility_off_light);
                getBinding().passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordVisibility = false;
            } else {
                passwordVisibility = true;
                getBinding().passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT);
                getBinding().eyeIcon.setBackgroundResource(R.drawable.ic_outline_visibility_light);

            }
            getBinding().passwordEdt.setSelection(getBinding().passwordEdt.getText().length());
        });
        getBinding().emailMobileEdt.addTextChangedListener(new CustomTextWatcher(this, new TextWatcherCallBack() {
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

        getBinding().passwordEdt.addTextChangedListener(new CustomTextWatcher(this, new TextWatcherCallBack() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBinding().passwordError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }));
    }


    private void login(String password) {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        astroLoginViewModel.loginUser(type, email_mobile, password, tabletSize).observe(this, evergentCommonResponse -> {

            if (evergentCommonResponse.isStatus()) {
                UserInfo.getInstance(this).setAccessToken(evergentCommonResponse.getLoginResponse().getGetOAuthAccessTokenv2ResponseMessage().getAccessToken());
                UserInfo.getInstance(this).setRefreshToken(evergentCommonResponse.getLoginResponse().getGetOAuthAccessTokenv2ResponseMessage().getRefreshToken());
                UserInfo.getInstance(this).setExternalSessionToken(evergentCommonResponse.getLoginResponse().getGetOAuthAccessTokenv2ResponseMessage().getExternalSessionToken());
                KsPreferenceKey.getInstance(this).setStartSessionKs(evergentCommonResponse.getLoginResponse().getGetOAuthAccessTokenv2ResponseMessage().getExternalSessionToken());
                getContact();
                try {


                    CleverTapManager.getInstance().setSignInEvent(this, from, type);
                } catch (Exception exception) {

                }
                astroLoginViewModel.addToken(UserInfo.getInstance(this).getExternalSessionToken());
            } else {
                getBinding().progressBar.setVisibility(View.GONE);
                if (type.equalsIgnoreCase("Facebook") || type.equalsIgnoreCase("Google")) {
                    if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2327")) {
                        searchAccountv2(password);
                    } else {
                        Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV4492")) {
                        FragmentManager fm = getSupportFragmentManager();
                        AccountBlockedDialog accountBlockedDialog = AccountBlockedDialog.newInstance(getResources().getString(R.string.create_playlist_name_title), "");
                        accountBlockedDialog.setEditDialogCallBack(this);
                        accountBlockedDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
                    } else {
                        Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private List<SocialLoginTypesItem> socialLoginTypesItem;

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
                if (evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getSocialLoginTypes() != null && evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getSocialLoginTypes().size() > 0) {
                    socialLoginTypesItem = evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getSocialLoginTypes();
                    AppCommonMethods.checkSocailLinking(this, socialLoginTypesItem);
                }
                if (evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getAccountRole() != null)
                    UserInfo.getInstance(this).setAccountRole(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getAccountRole());
                if (type.equalsIgnoreCase("Facebook") || type.equalsIgnoreCase("Google")) {
                    UserInfo.getInstance(this).setSocialLogin(true);
                }
                UserInfo.getInstance(this).setMobileNumber(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getMobileNumber());
                UserInfo.getInstance(this).setPasswordExists(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).isPasswordExists());
                UserInfo.getInstance(this).setEmail(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getContactMessage().get(0).getEmail());
                UserInfo.getInstance(this).setCpCustomerId(evergentCommonResponse.getGetContactResponse().getGetContactResponseMessage().getCpCustomerID());
                AppCommonMethods.setCrashlyticsUserId(this);
                getActiveSubscription();

            } else {
                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equalsIgnoreCase("111111111")) {
                    EvergentRefreshToken.refreshToken(AstrLoginActivity.this, UserInfo.getInstance(AstrLoginActivity.this).getRefreshToken()).observe(this, evergentCommonResponse1 -> {
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

    private String displayName = "", paymentMethod = "";

    private void getActiveSubscription() {
        astroLoginViewModel.getActiveSubscription(UserInfo.getInstance(this).getAccessToken(), "").observe(this, evergentCommonResponse -> {
            if (evergentCommonResponse.isStatus()) {
                if (evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage() != null) {
                    if (evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage() != null && evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage().size() > 0) {
                        for (AccountServiceMessageItem accountServiceMessageItem : evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage()) {
                            if (!accountServiceMessageItem.isFreemium()) {
                                if (accountServiceMessageItem.getDisplayName() != null)
                                    displayName = accountServiceMessageItem.getDisplayName();
                                paymentMethod = accountServiceMessageItem.getPaymentMethod();
                            }
                        }
                        UserInfo.getInstance(this).setMaxis(paymentMethod.equalsIgnoreCase(AppLevelConstants.MAXIS_BILLING));
                        if (!displayName.equalsIgnoreCase("")) {
                            UserInfo.getInstance(this).setVip(true);
                            setActive();
                        } else {
                            UserInfo.getInstance(this).setVip(false);
                            setActive();
                        }
                    } else {
                        UserInfo.getInstance(this).setVip(false);
                        setActive();
                    }
                } else {
                    UserInfo.getInstance(this).setVip(false);
                    setActive();
                }
            } else {
                UserInfo.getInstance(this).setVip(false);
                setActive();
            }
        });
    }

    private void setActive() {
        UserInfo.getInstance(this).setActive(true);
        Toast.makeText(this, getResources().getString(R.string.login_successfull), Toast.LENGTH_SHORT).show();
        AppCommonMethods.setCleverTap(this);
        if (UserInfo.getInstance(this).getCpCustomerId() != null && !UserInfo.getInstance(this).getCpCustomerId().equalsIgnoreCase(""))
            FirebaseEventManager.getFirebaseInstance(this).userLoginEvent(UserInfo.getInstance(this).getCpCustomerId(), UserInfo.getInstance(this).getAccountRole(), type);
      /*  if (from.equalsIgnoreCase("Profile")) {
            new ActivityLauncher(AstrLoginActivity.this).profileScreenRedirection(AstrLoginActivity.this, HomeActivity.class);
        } else {*/
        onBackPressed();
        /*}*/
    }



     /*   CleverTapAPI clevertapDefaultInstance =
                CleverTapAPI.getDefaultInstance(getApplicationContext(),"Custom_CleverTap_ID");

// To create an instance with CleverTapInstanceConfig object and Custom CleverTap ID
        CleverTapAPI instance =
                CleverTapAPI.instanceWithConfig(clevertapAdditionalInstanceConfig,"Custom_CleverTap_ID");*/

// To pass Custom CleverTap ID to onUserLogin method
// Do not call onUserLogin directly in the onCreate() lifecycle method

// each of the below mentioned fields are optional
// with the exception of one of Identity, Email, or FBID

     /*   CleverTapAPI clevertapDefaultInstance =
                CleverTapAPI.getDefaultInstance(getApplicationContext(), "__horizon43503");
        HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
        profileUpdate.put("Name", UserInfo.getInstance(this).getFirstName());    // String
        profileUpdate.put("Identity", UserInfo.getInstance(this).getCpCustomerId());      // String or number
        profileUpdate.put("Email", UserInfo.getInstance(this).getEmail()); // Email address of the user
        profileUpdate.put("Phone", "+14155551234");   // Phone (with the country code, starting with +)
        profileUpdate.put("Gender", "M");             // Can be either M or F
        profileUpdate.put("DOB", new Date());         // Date of Birth. Set the Date object to the appropriate value first

// optional fields. controls whether the user will be sent email, push etc.
        profileUpdate.put("MSG-email", false);        // Disable email notifications
        profileUpdate.put("MSG-push", true);          // Enable push notifications
        profileUpdate.put("MSG-sms", false);          // Disable SMS notifications
        profileUpdate.put("MSG-whatsapp", true);      // Enable WhatsApp notifications

        ArrayList<String> stuff = new ArrayList<String>();
        stuff.add("bag");
        stuff.add("shoes");
        profileUpdate.put("MyStuff", stuff);                        //ArrayList of Strings

        String[] otherStuff = {"Jeans", "Perfume"};
        profileUpdate.put("MyStuff", otherStuff);                   //String Array


        clevertapDefaultInstance.onUserLogin(profileUpdate, "__horizon43503");*/


    private void searchAccountv2(String password) {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        astroLoginViewModel.searchAccountV2("email", email_mobile).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                Intent intent = new Intent(this, IsThatYouActivity.class);
                intent.putExtra(AppLevelConstants.TYPE_KEY, type);
                intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, email_mobile);
                intent.putExtra(AppLevelConstants.SOCIAL_ID, password);
                startActivity(intent);

            } else {
                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2327")) {
                    createUser(password);
                } else {
                    Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createUser(String password) {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        astroLoginViewModel.createUser(type, email_mobile, password, name, tabletSize).observe(this, evergentCommonResponse -> {
            if (evergentCommonResponse.isStatus()) {
                UserInfo.getInstance(this).setAccessToken(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getAccessToken());
                UserInfo.getInstance(this).setRefreshToken(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getRefreshToken());
                UserInfo.getInstance(this).setExternalSessionToken(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getExternalSessionToken());
                KsPreferenceKey.getInstance(this).setStartSessionKs(evergentCommonResponse.getCreateUserResponse().getCreateUserResponseMessage().getExternalSessionToken());
                astroLoginViewModel.addToken(UserInfo.getInstance(this).getExternalSessionToken());
                AppCommonMethods.onUserRegister(this);
                getContact();

            } else {
                Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                getBinding().progressBar.setVisibility(View.GONE);
            }

        });
    }

    private boolean checkPasswordValidation(String password) {
        if (password.equalsIgnoreCase("")) {
            passwordError = getResources().getString(R.string.field_cannot_empty);
            return false;
        } else if (password.matches(PASSWORD_REGEX)) {
            return true;
        }
//        passwordError = getResources().getString(R.string.password_error);
        passwordError = getResources().getString(R.string.password_rules_new);

        return false;
    }

    private boolean checkEmailVaildation() {
        email_mobile = getBinding().emailMobileEdt.getText().toString();
        String password = getBinding().passwordEdt.getText().toString();
        if (!email_mobile.equalsIgnoreCase("")) {
            if (email_mobile.matches(MOBILE_REGEX)) {
                char firstChar = email_mobile.charAt(0);
                if (String.valueOf(firstChar).equalsIgnoreCase("6")) {
                    email_mobile = email_mobile;
                } else {
                    email_mobile = "6" + email_mobile;
                    getBinding().emailMobileEdt.setText(email_mobile);
                }
                if (email_mobile.length() == 11 || email_mobile.length() == 12) {
                    type = "Mobile";
                    getBinding().errorEmail.setTextColor(getResources().getColor(R.color.heather));
                    getBinding().errorEmail.setText(getResources().getString(R.string.mobile_suggestion));
                    return true;

                } else {
                    getBinding().passwordError.setText(getResources().getString(R.string.password_rules));
                    getBinding().errorEmail.setTextColor(getResources().getColor(R.color.red_live));
                    getBinding().errorEmail.setVisibility(View.VISIBLE);
                    getBinding().errorEmail.setText(getResources().getString(R.string.mobile_error));
                    passwordValidaton(password);
                    return false;

                }
            } else if (true) {
                type = "Email";
                getBinding().errorEmail.setTextColor(getResources().getColor(R.color.heather));
                getBinding().errorEmail.setText(getResources().getString(R.string.mobile_suggestion));
                return true;
            } else {

                boolean numeric = true;
                try {
                    double num = Double.parseDouble(email_mobile);
                } catch (NumberFormatException e) {
                    numeric = false;
                }
                if (numeric) {
                    getBinding().errorEmail.setText(getResources().getString(R.string.mobile_error));
                } else {
                    getBinding().errorEmail.setText(getResources().getString(R.string.email_suggestion));
                }
                getBinding().passwordError.setText(getResources().getString(R.string.password_rules));
                getBinding().errorEmail.setVisibility(View.VISIBLE);
                getBinding().errorEmail.setTextColor(getResources().getColor(R.color.red_live));

                passwordValidaton(password);
                return false;

            }
        } else {
            getBinding().passwordError.setText(getResources().getString(R.string.password_rules));
            getBinding().errorEmail.setVisibility(View.VISIBLE);
            getBinding().errorEmail.setTextColor(getResources().getColor(R.color.red_live));
            getBinding().errorEmail.setText(getResources().getString(R.string.field_cannot_empty));
            passwordValidaton(password);
            return false;

        }
    }

    private void passwordValidaton(String password) {
        if (checkPasswordValidation(password)) {
            getBinding().passwordError.setTextColor(getResources().getColor(R.color.heather));
            getBinding().passwordError.setText(getString(R.string.password_rules));
        } else {
            getBinding().passwordError.setTextColor(getResources().getColor(R.color.red_live));
            getBinding().passwordError.setText(passwordError);
            getBinding().passwordError.setVisibility(View.VISIBLE);

        }
    }

    private void createOtp() {
        astroLoginViewModel.createOtp(type, email_mobile).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);

            if (evergentCommonResponse.isStatus()) {
                Intent intent = new Intent(this, IsThatYouActivity.class);
                intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, email_mobile);
                startActivity(intent);

            } else {
                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2327")) {
                    Intent intent = new Intent(this, VerificationActivity.class);
                    intent.putExtra(AppLevelConstants.TYPE_KEY, type);
                    intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, email_mobile);
                    intent.putExtra(AppLevelConstants.PASSWORD_KEY, "password");
                    intent.putExtra(AppLevelConstants.FROM_KEY, "signIn");
                    startActivity(intent);
                } else {
                    Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }

    @Override
    public void onFinishEditDialog() {
        new ActivityLauncher(this).forgotPasswordActivity(this, ForgotPasswordActivity.class);

    }
}
