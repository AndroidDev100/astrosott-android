package com.astro.sott.fragments.verification;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.astro.sott.activities.SelectAccount.UI.SelectDtvAccountActivity;
import com.astro.sott.activities.deviceMangment.ui.DeviceManagementActivity;
import com.astro.sott.activities.loginActivity.viewModel.LoginViewModel;
import com.astro.sott.activities.parentalControl.ui.ViewingRestrictionActivity;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.BuildConfig;
import com.astro.sott.R;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.loginActivity.LoginActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.callBacks.ParentalSwitchCallback;
import com.astro.sott.databinding.FragmentVerificationBinding;
import com.astro.sott.fragments.dialog.LoginAlertDialogSingleButtonFragment;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.CustomTextWatcher;
import com.astro.sott.utils.helpers.DialogHelper;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.StringBuilderHolder;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

public class Verification extends BaseBindingFragment<FragmentVerificationBinding> implements LoginAlertDialogSingleButtonFragment.AlertDialogListener, TextView.OnEditorActionListener {


    private String strPhoneNumber = "";
    private String strFragmentName = "";
    private LoginActivity loginActivity;
    private long lastClickTime = 0;
    private String otp;
    private String strMsisdn = "";

    private MyCountDownTimer myCountDownTimer;
    private LoginViewModel viewModel;
    private LoginAlertDialogSingleButtonFragment alertDialog;
    private String txnId = null;
    private ParentalSwitchCallback parentalSwitchCallback;

    public Verification() {
        // Required empty public constructor
    }

    public static void resetCounter(Context context, TextView tvCounter, TextView tvResend) {
        tvCounter.setText("");
        tvResend.setEnabled(true);
        tvResend.setTextColor(ContextCompat.getColor(context, R.color.primary_blue));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(loginActivity).get(LoginViewModel.class);
        startCounter();
        getBinding().etPin.addTextChangedListener(new CustomTextWatcher(getBinding().numberEditTextLayout));
        getBinding().etPin.setOnEditorActionListener(this);
    }

    public void startCounter() {
        myCountDownTimer = new MyCountDownTimer(getBinding().tvCounter, getBinding().tvResend, loginActivity);
        myCountDownTimer.start();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            strMsisdn = bundle.getString(AppLevelConstants.USER_NAME);
            otp = bundle.getString(AppLevelConstants.OTP);
            txnId = bundle.getString(AppLevelConstants.TXN_ID);
            displayReceivedData(bundle.getString(AppLevelConstants.FRAGMENT_NAME), strMsisdn, otp);

            setClicks();
        }
    }

    private void setClicks() {
        getBinding().cancelText.setOnClickListener(view -> loginActivity.finish());
        getBinding().continueText.setOnClickListener(view -> callSignupOrLoginApi());

        getBinding().tvResend.setOnClickListener(view -> {
            getBinding().tvResend.setEnabled(false);
            getBinding().tvResend.setTextColor(ContextCompat.getColor(getActivity(), R.color.cancel_text));
            getMpin();
            startCounter();
        });

    }

    private void callSignupOrLoginApi() {
        if (strMsisdn != null) {

            if (TextUtils.isEmpty(getBinding().etPin.getText().toString().trim())) {
                getBinding().numberEditTextLayout.setError(getResources().getString(R.string.pin_required));
                AppCommonMethods.requestFocus(getActivity(), getBinding().etPin);
                return;
            }

            if (getBinding().etPin.getText().toString().trim().length() != 6) {

                getBinding().numberEditTextLayout.setError(getResources().getString(R.string.incorrect_pin));
                AppCommonMethods.requestFocus(getActivity(), getBinding().etPin);
                return;
            }
            getBinding().numberEditTextLayout.setErrorEnabled(false);

            if (SystemClock.elapsedRealtime() - lastClickTime < 3000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            if (NetworkConnectivity.isOnline(getActivity()))
                verifyPin();
            else
                Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    private void verifyPin() {
        if (strFragmentName.equalsIgnoreCase(AppLevelConstants.SIGN_IN)) {
            DialogHelper.showProgressDialog(getActivity(), getString(R.string.loggin_you_in));
        } else if (strFragmentName.equalsIgnoreCase(AppLevelConstants.SIGN_UP)) {
            DialogHelper.showProgressDialog(getActivity(), getString(R.string.signing_you_up));
        } else if(strFragmentName.equalsIgnoreCase(AppLevelConstants.ACTICITY_NAME)){
            DialogHelper.showProgressDialog(getActivity(), getString(R.string.verifying));
        }else if(strFragmentName.equalsIgnoreCase(AppLevelConstants.PARENTAL_SWITCH)){
            DialogHelper.showProgressDialog(getActivity(), getString(R.string.verifying));
        }
        if (getBinding().etPin.getText() != null) {

            if (BuildConfig.FLAVOR.equalsIgnoreCase("qa")) {

            if(!strFragmentName.equalsIgnoreCase(AppLevelConstants.ACTICITY_NAME) && !strFragmentName.equalsIgnoreCase(AppLevelConstants.PARENTAL_SWITCH)) {
                if (strMsisdn.equalsIgnoreCase("763742892") || strMsisdn.equalsIgnoreCase("768468718") || strMsisdn.equalsIgnoreCase("767413749") ||
                        strMsisdn.equalsIgnoreCase("774098897") || strMsisdn.equalsIgnoreCase("777301080") || strMsisdn.equalsIgnoreCase("765111864")
                        || strMsisdn.equalsIgnoreCase("765111924") || strMsisdn.equalsIgnoreCase("777338767")) {


                    viewModel.loginUser(strMsisdn, false).observe(this, commonResponse -> {

                        if (commonResponse != null) {
                            if (commonResponse.getStatus()) {
                                String message = getString(R.string.success_login_user);
                                ToastHandler.show(message, getActivity());
                                KsPreferenceKey.getInstance(getActivity()).setUserActive(true);
                                KsPreferenceKey.getInstance(getActivity()).setParentalActive(true);
                                new ActivityLauncher(getActivity()).homeScreen(getActivity(), HomeActivity.class);

                            } else {
                                if (commonResponse.getIsDeviceAdded() == 1) {
                                    new ActivityLauncher(getActivity()).deviceManagementActivity(getActivity(), DeviceManagementActivity.class);

                                } else if (commonResponse.getErrorCode() != null && commonResponse.getErrorCode().equals(getString(R.string.error_code_user_not_exist))) {
                                    showDialog(commonResponse.getMessage());
                                    registerForHardcodedNumber(strMsisdn);
                                } else {
                                    DialogHelper.hideProgressDialog();
                                    showDialog(commonResponse.getMessage());
                                }
                            }
                        } else {
                            DialogHelper.hideProgressDialog();
                            showDialog(getResources().getString(R.string.something_went_wrong));
                        }

                    });

                }else {
                    viewModel.verifyPin(strMsisdn, getBinding().etPin.getText().toString().trim(), txnId, false).observe(loginActivity, otpModel -> {
                        if (otpModel != null) {
                            if (otpModel.getResponseCode() == 200) {
                                if (strFragmentName.equalsIgnoreCase(AppLevelConstants.SIGN_IN)) {
                                    loginUser(false);
                                } else if (strFragmentName.equalsIgnoreCase(AppLevelConstants.SIGN_UP)) {
                                    loginUser(true);
                                } else if (strFragmentName.equalsIgnoreCase(AppLevelConstants.ACTICITY_NAME)) {
                                    getActivity().onBackPressed();
                                    new ActivityLauncher(getActivity()).viewRestrictionActivity(getActivity(), ViewingRestrictionActivity.class);
                                } else if (strFragmentName.equalsIgnoreCase(AppLevelConstants.PARENTAL_SWITCH)) {
                                    getActivity().onBackPressed();
                                    if (KsPreferenceKey.getInstance(getActivity()).getParentalActive()) {
                                        KsPreferenceKey.getInstance(getActivity()).setParentalActive(false);
                                    } else {
                                        KsPreferenceKey.getInstance(getActivity()).setParentalActive(true);
                                    }
//                            Intent intent = new Intent(getActivity(), ParentalControl.class);
//                            startActivity(intent);

                                }
                            } else {
                                DialogHelper.hideProgressDialog();
                                showDialog(otpModel.getMessage());

                            }
                        } else {
                            DialogHelper.hideProgressDialog();
                            showDialog(getResources().getString(R.string.something_went_wrong));
                        }
                    });
                }
            }
                else {
                    viewModel.verifyPin(strMsisdn, getBinding().etPin.getText().toString().trim(), txnId, false).observe(loginActivity, otpModel -> {
                        if (otpModel != null) {
                            if (otpModel.getResponseCode() == 200) {
                                if (strFragmentName.equalsIgnoreCase(AppLevelConstants.SIGN_IN)) {
                                    loginUser(false);
                                } else if (strFragmentName.equalsIgnoreCase(AppLevelConstants.SIGN_UP)) {
                                    loginUser(true);
                                } else if (strFragmentName.equalsIgnoreCase(AppLevelConstants.ACTICITY_NAME)) {
                                    getActivity().onBackPressed();
                                    new ActivityLauncher(getActivity()).viewRestrictionActivity(getActivity(), ViewingRestrictionActivity.class);
                                } else if (strFragmentName.equalsIgnoreCase(AppLevelConstants.PARENTAL_SWITCH)) {
                                    getActivity().onBackPressed();
                                    if (KsPreferenceKey.getInstance(getActivity()).getParentalActive()) {
                                        KsPreferenceKey.getInstance(getActivity()).setParentalActive(false);
                                    } else {
                                        KsPreferenceKey.getInstance(getActivity()).setParentalActive(true);
                                    }
//                            Intent intent = new Intent(getActivity(), ParentalControl.class);
//                            startActivity(intent);

                                }
                            } else {
                                DialogHelper.hideProgressDialog();
                                showDialog(otpModel.getMessage());

                            }
                        } else {
                            DialogHelper.hideProgressDialog();
                            showDialog(getResources().getString(R.string.something_went_wrong));
                        }
                    });
                }
            }else {
                viewModel.verifyPin(strMsisdn, getBinding().etPin.getText().toString().trim(), txnId, false).observe(loginActivity, otpModel -> {
                    if (otpModel != null) {
                        if (otpModel.getResponseCode() == 200) {
                            if (strFragmentName.equalsIgnoreCase(AppLevelConstants.SIGN_IN)) {
                                loginUser(false);
                            } else if (strFragmentName.equalsIgnoreCase(AppLevelConstants.SIGN_UP)) {
                                loginUser(true);
                            } else if (strFragmentName.equalsIgnoreCase(AppLevelConstants.ACTICITY_NAME)) {
                                getActivity().onBackPressed();
                                new ActivityLauncher(getActivity()).viewRestrictionActivity(getActivity(), ViewingRestrictionActivity.class);
                            } else if (strFragmentName.equalsIgnoreCase(AppLevelConstants.PARENTAL_SWITCH)) {
                                getActivity().onBackPressed();
                                if (KsPreferenceKey.getInstance(getActivity()).getParentalActive()) {
                                    KsPreferenceKey.getInstance(getActivity()).setParentalActive(false);
                                } else {
                                    KsPreferenceKey.getInstance(getActivity()).setParentalActive(true);
                                }
//                            Intent intent = new Intent(getActivity(), ParentalControl.class);
//                            startActivity(intent);

                            }
                        } else {
                            DialogHelper.hideProgressDialog();
                            showDialog(otpModel.getMessage());

                        }
                    } else {
                        DialogHelper.hideProgressDialog();
                        showDialog(getResources().getString(R.string.something_went_wrong));
                    }
                });
            }
        }
    }

    private void registerForHardcodedNumber(String strMsisdn) {
        viewModel.registerUser(strMsisdn).observe(this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    verifyPin();
                } else {
                    showDialog(commonResponse.getMessage());
                }
            } else {
                showDialog(getResources().getString(R.string.something_went_wrong));
            }
        });
    }

    private void loginUser(boolean viaRegistration) {
        viewModel.addToken(KsPreferenceKey.getInstance(getActivity()).getUserRegistered()).observe(loginActivity, commonResponse -> {
            String message;
            if (viaRegistration) {
                message = getString(R.string.success_register_user);
            } else {
                message = getString(R.string.success_login_user);
            }
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {

                    viewModel.checkUserPreference();

                    if (strFragmentName.equalsIgnoreCase(AppLevelConstants.SIGN_UP)) {
                        getActivity().onBackPressed();
                        if (viaRegistration) {
                            new ActivityLauncher(getActivity()).selectAccount(getActivity(), SelectDtvAccountActivity.class, strPhoneNumber);
                        } else {
                            KsPreferenceKey.getInstance(getActivity()).setUserActive(true);
                            KsPreferenceKey.getInstance(getActivity()).setParentalActive(true);
                            new ActivityLauncher(getActivity()).homeScreen(getActivity(), HomeActivity.class);
                            DialogHelper.hideProgressDialog();
                            ToastHandler.show(message, getActivity());
                        }
                    } else {
                        KsPreferenceKey.getInstance(getActivity()).setUserActive(true);
                        KsPreferenceKey.getInstance(getActivity()).setParentalActive(true);
                        new ActivityLauncher(getActivity()).homeScreen(getActivity(), HomeActivity.class);
                        DialogHelper.hideProgressDialog();
                        ToastHandler.show(message, getActivity());
                    }

                } else {
                    if (commonResponse.getIsDeviceAdded() == 1) {
                        new ActivityLauncher(getActivity()).deviceManagementActivity(getActivity(), DeviceManagementActivity.class);
                        DialogHelper.hideProgressDialog();
                        ToastHandler.show(message, getActivity());

                    } else if (commonResponse.getErrorCode() != null && (commonResponse.getErrorCode().equals(getString(R.string.error_code_user_not_exist)) || (commonResponse.getErrorCode().equals(getString(R.string.error_code_user_not_exist_))))) {
                        registerUser();
                    } else {
                        DialogHelper.hideProgressDialog();
                        showDialog(commonResponse.getMessage());
                    }
                }
            } else {
                DialogHelper.hideProgressDialog();
                showDialog(getResources().getString(R.string.something_went_wrong));
            }

        });
    }

    private void registerUser() {
        viewModel.registerUser(strPhoneNumber).observe(loginActivity, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    loginUser(true);
                } else {
                    if (commonResponse.getErrorCode().equalsIgnoreCase("2014")) {
                        loginUser(false);
                    } else {
                        DialogHelper.hideProgressDialog();
                        showDialog(commonResponse.getMessage());

                    }
                }
            } else {
                DialogHelper.hideProgressDialog();
                showDialog(getResources().getString(R.string.something_went_wrong));
            }

        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginActivity = (LoginActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        loginActivity = null;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            callSignupOrLoginApi();
            return true;
        }
        return false;

    }

    @Override
    public FragmentVerificationBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentVerificationBinding.inflate(inflater);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void displayReceivedData(String fragmentName, String userName, String otpPin) {
        strPhoneNumber = userName;
        strFragmentName = fragmentName;
        otp = otpPin;
        strMsisdn = userName;
        String msisdn = strPhoneNumber;
//        if (msisdn.startsWith("0")) {
//            msisdn = msisdn.substring(1);
//            msisdn = AppConstants.SL_COUNTRY_CODE.concat(msisdn);
//        }
        strPhoneNumber = msisdn;
        StringBuilderHolder.getInstance().clear();
        StringBuilderHolder.getInstance().append(getResources().getString(R.string.enter_pin) + " ");
        StringBuilderHolder.getInstance().append(strMsisdn);
        getBinding().tvPhoneNo.setText(StringBuilderHolder.getInstance().getText().toString());

        if(strFragmentName.equals(AppLevelConstants.ACTICITY_NAME) || strFragmentName.equals(AppLevelConstants.PARENTAL_SWITCH)){
            getBinding().imgNotieEye.setVisibility(View.VISIBLE);
            getBinding().imgNotieEye.setOnClickListener(v -> {
                if (getBinding().etPin.getTransformationMethod() == null) {
                    getBinding().imgNotieEye.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    getBinding().etPin.setTransformationMethod(new PasswordTransformationMethod());
                    getBinding().etPin.setSelection(getBinding().etPin.getText().length());

                } else {
                    getBinding().imgNotieEye.setImageResource(R.drawable.ic_visibility_black_24dp);
                    getBinding().etPin.setTransformationMethod(null);
                    getBinding().etPin.setSelection(getBinding().etPin.getText().length());

                }
            });
        } else{
            getBinding().imgNotieEye.setVisibility(View.GONE);
        }
    }

    private void showDialog(String message) {
        FragmentManager fm = getFragmentManager();
        alertDialog = LoginAlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        if (fm != null)
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    @Override
    public void onFinishDialog() {
        if (alertDialog != null && alertDialog.isAdded()) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
            myCountDownTimer = null;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getMpin() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        if (strMsisdn != null) {
//            if (strMsisdn.startsWith("0")) {
//                strMsisdn = strMsisdn.substring(1);
//                strMsisdn = AppConstants.SL_COUNTRY_CODE.concat(strMsisdn);
//            }
            String finalMsisdn = strMsisdn;
            if (getActivity() != null && NetworkConnectivity.isOnline(getActivity())) {
                viewModel.getMpin(finalMsisdn).observe(this, otpModel -> {
                    Gson gson = new Gson();
                    Log.e("verification", gson.toJson(otpModel));
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    if (otpModel != null) {
                        if (TextUtils.isEmpty(String.valueOf(otpModel.getmPin())) || otpModel.getResponseCode() == 1 || otpModel.getResponseCode() == 2) {
                                showDialog(getActivity().getResources().getString(R.string.something_went_wrong));
                        }
                    } else {
                        showDialog(getResources().getString(R.string.error_sms_failure));
                        resetCounter(getActivity(), getBinding().tvCounter, getBinding().tvResend);
                        if (myCountDownTimer != null) {
                            myCountDownTimer.cancel();
                            myCountDownTimer = null;
                        }
                    }
                });
            } else {
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            }
        }
    }

    private static class MyCountDownTimer extends CountDownTimer {

        private final WeakReference<TextView> tvCounterReference;
        private final WeakReference<TextView> tvResendReference;
        private final WeakReference<Context> contextWeakReference;

        MyCountDownTimer(TextView tvCounter, TextView tvResend, Context context) {
            super(AppLevelConstants.RESEND_PIN_DELAY, AppLevelConstants.COUNT_DOWN_TIMER);
            // Use a WeakReference to ensure the TextView can be garbage collected
            tvCounterReference = new WeakReference<>(tvCounter);
            tvResendReference = new WeakReference<>(tvResend);
            contextWeakReference = new WeakReference<>(context);
        }


        @Override
        public void onTick(long millisUntilFinished) {

            TextView tvCounter = tvCounterReference.get();
            Context context = contextWeakReference.get();
            if (tvCounter != null && context != null) {

                if (millisUntilFinished / 1000 >= 10) {
                    tvCounter.setText("(" + millisUntilFinished / 1000 + "s)");
                } else if (millisUntilFinished / 1000 >= 0 && millisUntilFinished / 1000 < 10) {

                    tvCounter.setText("(" + millisUntilFinished / 1000 + "s)");
                }
            }

        }

        @Override
        public void onFinish() {
            TextView tvResend = tvResendReference.get();
            Context context = contextWeakReference.get();
            TextView tvCounter = tvCounterReference.get();
            if (tvResend != null && context != null) {
                resetCounter(context, tvCounter, tvResend);
                //    tvCounter.setText("");
                //   tvResend.setEnabled(true);
                //    tvResend.setTextColor(ContextCompat.getColor(context, R.color.primary_blue));
            }
        }
    }

}