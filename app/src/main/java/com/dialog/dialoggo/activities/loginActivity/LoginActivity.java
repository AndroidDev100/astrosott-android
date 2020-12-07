package com.dialog.dialoggo.activities.loginActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.deviceMangment.ui.DeviceManagementActivity;
import com.dialog.dialoggo.activities.home.HomeActivity;
import com.dialog.dialoggo.activities.loginActivity.viewModel.LoginViewModel;
import com.dialog.dialoggo.callBacks.ParentalSwitchCallback;
import com.dialog.dialoggo.databinding.ActivityLoginBinding;
import com.dialog.dialoggo.fragments.dialog.AlertDialogSingleButtonFragment;
import com.dialog.dialoggo.fragments.signIn.SignInFragment;
import com.dialog.dialoggo.fragments.signUp.SignUpFragment;
import com.dialog.dialoggo.fragments.verification.Verification;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity implements AlertDialogSingleButtonFragment.AlertDialogListener, SignUpFragment.OnFragmentInteractionListener, SignInFragment.OnFragmentInteractionListener {

    private FragmentManager mFragmentManager;
    private LoginViewModel viewModel;
    ActivityLoginBinding activityLoginBinding;
    String activityName = "";

    /*@Override
    public ActivityLoginBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityLoginBinding.inflate(inflater);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        Intent intent = getIntent();
        activityName = intent.getStringExtra("screenName");
        Log.d("ScreenNameIs",activityName);

        mFragmentManager = getSupportFragmentManager();
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        viewPagerIntialization();

    }

    private void viewPagerIntialization() {
        if(activityName.equalsIgnoreCase(AppLevelConstants.ACTICITY_NAME)){

            getMpin();
        } else if (activityName.equalsIgnoreCase(AppLevelConstants.PARENTAL_SWITCH)){
            getMpin();
        }
        else {
            mFragmentManager.beginTransaction().add(R.id.fragment_container, new SignInFragment(), "SignInFragment").commitAllowingStateLoss();
        }

    }


        private void getMpin() {
            activityLoginBinding.progressBar.setVisibility(View.VISIBLE);

                String msisdn = KsPreferenceKey.getInstance(this).getUser().getUsername();
//            if (msisdn.startsWith("0")) {
//                msisdn = msisdn.substring(1);
//                msisdn = AppConstants.SL_COUNTRY_CODE.concat(msisdn);
//            }
                String finalMsisdn = msisdn;
                if (getApplicationContext() != null && NetworkConnectivity.isOnline(LoginActivity.this)) {
                    viewModel.getMpin(finalMsisdn).observe(this, otpModel -> {
                        activityLoginBinding.progressBar.setVisibility(View.GONE);
                        Log.e("OTP MODEL",new Gson().toJson(otpModel));
                        if (otpModel != null) {
                            if (TextUtils.isEmpty(String.valueOf(otpModel.getmPin())) || otpModel.getResponseCode() == 1 || otpModel.getResponseCode() == 2) {
                                showDialog(getResources().getString(R.string.something_went_wrong));
                            }
                            else {
                               // KsPreferenceKey.getInstance(getActivity()).setMsisdn(finalMsisdn);
                               // mListener.onFragmentInteraction(AppLevelConstants.SIGN_IN, AppLevelConstants.CONTINUE, getBinding().etPhoneNo.getText().toString().trim(), "" + otpModel.getmPin(), false, otpModel.getTxnId());


                                Verification verificationFragment = new Verification();
//
                                Bundle verificationBundle = new Bundle();
                                verificationBundle.putString(AppLevelConstants.FRAGMENT_NAME, activityName);
                                verificationBundle.putString(AppLevelConstants.USER_NAME, KsPreferenceKey.getInstance(this).getUser().getUsername());
                                verificationBundle.putString(AppLevelConstants.OTP, String.valueOf(otpModel.getmPin()));
                                verificationBundle.putString(AppLevelConstants.TXN_ID, otpModel.getTxnId());
                                verificationFragment.setArguments(verificationBundle);
                                mFragmentManager.beginTransaction().add(R.id.fragment_container, verificationFragment, "VerificationFragment").commit();

                            }
                        } else {
                            showDialog(getResources().getString(R.string.error_sms_failure));
                        }
                    });
                } else {
                    activityLoginBinding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }



    @Override
    public void onFragmentInteraction(String fragment, String event, String msisdn, String otp, Boolean isAutoMSISDN, String txnId) {
        if (fragment.equalsIgnoreCase(AppLevelConstants.SIGN_UP)) {
            if (isAutoMSISDN) {
                activityLoginBinding.progressBar.setVisibility(View.VISIBLE);
                registerUser(msisdn);
            } else {
                if (event.equalsIgnoreCase(AppLevelConstants.ALREADY_USER)) {
                    mFragmentManager.beginTransaction().add(R.id.fragment_container, new SignInFragment(), "SignInFragment").commitAllowingStateLoss();
                } else if (event.equalsIgnoreCase(AppLevelConstants.CONTINUE)) {
                    goToVerification(fragment, msisdn, otp, txnId);
                }
            }
        } else if (fragment.equalsIgnoreCase(AppLevelConstants.SIGN_IN)) {
            if (isAutoMSISDN) {
                activityLoginBinding.progressBar.setVisibility(View.VISIBLE);
                loginUser(msisdn, false);
            } else {
                if (event.equalsIgnoreCase(AppLevelConstants.CONTINUE)) {
                    goToVerification(fragment, msisdn, otp,txnId);
                } else if (event.equalsIgnoreCase(AppLevelConstants.DONT_HAVE_ACCOUNT)) {
                    mFragmentManager.beginTransaction().add(R.id.fragment_container, new SignUpFragment(), "SignUpFragment").commitAllowingStateLoss();
                }
            }
        }
    }

    private void registerUser(String msisdn) {
        viewModel.registerUser(msisdn).observe(this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    loginUser(msisdn, true);
                } else {
                    showDialog(commonResponse.getMessage());
                }
            } else {
                showDialog(getResources().getString(R.string.something_went_wrong));
            }
        });
    }

    private void loginUser(String msisdn, Boolean viaRegistration) {

        viewModel.loginUser(msisdn, viaRegistration).observe(this, commonResponse -> {
            activityLoginBinding.progressBar.setVisibility(View.GONE);
            String message;
            if (viaRegistration) {
                message = getString(R.string.success_register_user);
            } else {
                message = getString(R.string.success_login_user);
            }
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    KsPreferenceKey.getInstance(this).setUserActive(true);
                    new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                } else {
                    if (commonResponse.getIsDeviceAdded() == 1) {
                        new ActivityLauncher(this).deviceManagementActivity(this, DeviceManagementActivity.class);
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    } else if (commonResponse.getErrorCode() != null && commonResponse.getErrorCode().equals(getString(R.string.error_code_user_not_exist))) {
                        registerUser(msisdn);
                    } else {
                        showDialog(commonResponse.getMessage());
                    }
                }
            } else {
                showDialog(getResources().getString(R.string.something_went_wrong));
            }

        });
    }

    private void showDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        if (fm != null)
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void goToVerification(String fragment, String userName, String otp, String txnId) {

        Verification verificationFragment = new Verification();

        Bundle verificationBundle = new Bundle();
        verificationBundle.putString(AppLevelConstants.FRAGMENT_NAME, fragment);
        verificationBundle.putString(AppLevelConstants.USER_NAME, userName);
        verificationBundle.putString(AppLevelConstants.OTP, otp);
        verificationBundle.putString(AppLevelConstants.TXN_ID, txnId);
        verificationFragment.setArguments(verificationBundle);
        mFragmentManager.beginTransaction().add(R.id.fragment_container, verificationFragment, "VerificationFragment").commit();
    }

    @Override
    public void onFinishDialog() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        InputMethodManager mgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mgr != null)
            mgr.hideSoftInputFromWindow(activityLoginBinding.progressBar.getWindowToken(), 0);
    }


}