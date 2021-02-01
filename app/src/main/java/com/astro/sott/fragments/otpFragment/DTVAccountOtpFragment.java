package com.astro.sott.fragments.otpFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.astro.sott.activities.addDTVAccountNumber.UI.addDTVAccountNumberActivity;
import com.astro.sott.activities.loginActivity.viewModel.LoginViewModel;
import com.astro.sott.activities.mbbaccount.ui.AddMBBAccountActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.fragments.Success.UI.Success;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.StringBuilderHolder;
import com.astro.sott.R;
import com.astro.sott.databinding.FragmentOtpBinding;
import com.astro.sott.fragments.dialog.LoginAlertDialogSingleButtonFragment;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.CustomTextWatcher;

import java.lang.ref.WeakReference;


public class DTVAccountOtpFragment extends BaseBindingFragment<FragmentOtpBinding> implements AlertDialogSingleButtonFragment.AlertDialogListener, TextView.OnEditorActionListener {

    private static final String TAG = "DTVAccountOtpFragment";
    private String strPhoneNumber = "";
    private String strFragmentName = "";
    private long lastClickTime = 0;
    private String otp;
    private String strMsisdn = "";
    private String dtvAccountNumber = "";
    private String mbbAccountNumber = "";
    private addDTVAccountNumberActivity addDTVAccountActivity;
    private AddMBBAccountActivity addMBBAccountActivity;
    private Success.OnFragmentInteractionListener mListener;
    private MyCountDownTimer myCountDownTimer;
    private LoginViewModel viewModel;
    private LoginAlertDialogSingleButtonFragment alertDialog;
    private String fragmentName = "";
    private String txnId = null;
    private String fragmentType = "";

    public DTVAccountOtpFragment() {
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

        switch (fragmentType) {

            case AppLevelConstants.DTV_TYPE_FRAGMENT:
                viewModel = ViewModelProviders.of(addDTVAccountActivity).get(LoginViewModel.class);
                break;

            case AppLevelConstants.MBB_TYPE_FRAGMENT:
                viewModel = ViewModelProviders.of(addMBBAccountActivity).get(LoginViewModel.class);
                break;

            default:
                break;
        }
        displayReceivedData();
        startCounter();
      //  getBinding().etPin.addTextChangedListener(new CustomTextWatcher(getBinding().numberEditTextLayout));
        getBinding().etPin.setOnEditorActionListener(this);
    }

    public void startCounter() {

        switch (fragmentType) {

            case AppLevelConstants.DTV_TYPE_FRAGMENT:
                myCountDownTimer = new MyCountDownTimer(getBinding().tvCounter, getBinding().tvResend, addDTVAccountActivity);
                break;

            case AppLevelConstants.MBB_TYPE_FRAGMENT:
                myCountDownTimer = new MyCountDownTimer(getBinding().tvCounter, getBinding().tvResend, addMBBAccountActivity);
                break;

            default:
                break;
        }
        myCountDownTimer.start();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setClicks();
    }

    private void setClicks() {
//        getBinding().cancelText.setOnClickListener(view -> addDTVAccountNumberActivity.finish());
//        getBinding().continueText.setOnClickListener(view -> callSignupOrLoginApi());

        getBinding().cancelText.setOnClickListener(view -> {
            if(fragmentType.equalsIgnoreCase(AppLevelConstants.DTV_TYPE_FRAGMENT)){
                if(addDTVAccountActivity == null){
                    return;
                }
                InputMethodManager mgr = (InputMethodManager) addDTVAccountActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (mgr != null)
                    mgr.hideSoftInputFromWindow(getBinding().etPin.getWindowToken(), 0);
            }else if(fragmentType.equalsIgnoreCase(AppLevelConstants.MBB_TYPE_FRAGMENT)){
                if(addMBBAccountActivity == null){
                    return;
                }
                InputMethodManager mgr = (InputMethodManager) addMBBAccountActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (mgr != null)
                    mgr.hideSoftInputFromWindow(getBinding().etPin.getWindowToken(), 0);
            }

            if(fragmentType.equalsIgnoreCase(AppLevelConstants.DTV_TYPE_FRAGMENT)){
                if(addDTVAccountActivity == null){
                    return;
                }
                addDTVAccountActivity.finish();
            }else if(fragmentType.equalsIgnoreCase(AppLevelConstants.MBB_TYPE_FRAGMENT)){
                if(addMBBAccountActivity == null){
                    return;
                }
                addMBBAccountActivity.finish();
            }
        });

        getBinding().continueText.setOnClickListener(view -> {
            callSignupOrLoginApi();
        });

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
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        if (getBinding().etPin.getText() != null) {
            viewModel.verifyPin(strMsisdn, getBinding().etPin.getText().toString().trim(), txnId, true).observe(this, otpModel -> {
                if (otpModel != null) {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    if (otpModel.getResponseCode() == 200) {

                        Log.e("successOTP", "Success");
                        if (fragmentName.equalsIgnoreCase("DeleteFragment")) {
                            deleteDtvAccount();
                        } else {
                            addDtvAccount();
                        }
                    } else {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        showDialog(otpModel.getMessage());
                    }
                } else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    showDialog(getResources().getString(R.string.something_went_wrong));
                }
            });
        }
    }

    private void deleteDtvAccount() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        if (getActivity() != null && NetworkConnectivity.isOnline(getActivity())) {

            viewModel.updateDTVAccountData("0", fragmentType).observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    if (s != null) {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
                        mListener.onFragmentInteraction("DTVAccountOtpFragment", AppLevelConstants.DELETE_SUCCESS, fragmentType, "", 0, "", null);
                    } else {

                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    }
                }
            });

        } else {
            Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();

        }
    }

    private void addDtvAccount() {
        if (getActivity() != null && NetworkConnectivity.isOnline(getActivity())) {

            viewModel.updateDTVAccountData(dtvAccountNumber, fragmentType).observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    if (s != null) {
                        if (fragmentName.equalsIgnoreCase("ChangeFragment")) {
                            mListener.onFragmentInteraction("DTVAccountOtpFragment", AppLevelConstants.CHANGESUCCESSDIALOG, fragmentType, "", 0, "", null);
                        } else {
                            mListener.onFragmentInteraction("DTVAccountOtpFragment", AppLevelConstants.OK, fragmentType, "", 0, "", null);
                        }

                        //TODO Dialog finishing
                        //this.onFinishDialog();
                    } else {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    }
                }
            });

        } else {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                strMsisdn = bundle.getString(AppLevelConstants.USER_NAME);
                otp = bundle.getString(AppLevelConstants.OTP);
                dtvAccountNumber = bundle.getString(AppLevelConstants.ACCOUNT_NUMBER);
                fragmentName = bundle.getString(AppLevelConstants.FRAGMENT_NAME);
                txnId = bundle.getString(AppLevelConstants.TXN_ID);
                fragmentType = bundle.getString(AppLevelConstants.FRAGMENT_TYPE);
            }
            if (fragmentType.equalsIgnoreCase(AppLevelConstants.DTV_TYPE_FRAGMENT)) {
                if (context instanceof addDTVAccountNumberActivity) {
                    addDTVAccountActivity = (addDTVAccountNumberActivity) context;
                    mListener = (Success.OnFragmentInteractionListener) context;
                }
            } else if (fragmentType.equalsIgnoreCase(AppLevelConstants.MBB_TYPE_FRAGMENT)) {
                if (context instanceof AddMBBAccountActivity) {
                    addMBBAccountActivity = (AddMBBAccountActivity) context;
                    mListener = (Success.OnFragmentInteractionListener) context;
                }
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }


        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addDTVAccountActivity = null;
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
    public FragmentOtpBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentOtpBinding.inflate(inflater);
    }

    private void displayReceivedData() {
        strPhoneNumber = strMsisdn;
        strFragmentName = fragmentName;
        otp = otp;
        strPhoneNumber = otp;
        StringBuilderHolder.getInstance().clear();
        StringBuilderHolder.getInstance().append(getResources().getString(R.string.enter_pin) + " ");
        StringBuilderHolder.getInstance().append(strMsisdn);
        getBinding().tvPhoneNo.setText(StringBuilderHolder.getInstance().getText().toString());
    }

    private void showDialog(String message) {
        FragmentManager fm = getFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
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
            String finalMsisdn = strMsisdn;
            if (getActivity() != null && NetworkConnectivity.isOnline(getActivity())) {
                viewModel.getMpin(finalMsisdn).observe(this, otpModel -> {
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String fragment, String event, String fragmentType, String msiddn, int otp, String dtvAccountNum, String txnId);
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
            }
        }
    }

}