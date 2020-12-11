package com.astro.sott.fragments.signIn;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.astro.sott.BuildConfig;
import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.LoginActivity;
import com.astro.sott.activities.loginActivity.viewModel.LoginViewModel;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentSignInBinding;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.CustomTextviewWatcher;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.google.gson.Gson;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SignInFragment extends BaseBindingFragment<FragmentSignInBinding> implements TextView.OnEditorActionListener, AlertDialogSingleButtonFragment.AlertDialogListener {
    private OnFragmentInteractionListener mListener;
    private LoginActivity loginActivity;
    private LoginViewModel viewModel;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public FragmentSignInBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentSignInBinding.inflate(inflater);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
       // getAutoMsisdn();
        setClicks();

        getBinding().etPhoneNo.addTextChangedListener(new CustomTextviewWatcher(getBinding().errorText, getBinding().etPhoneNo));
        getBinding().etPhoneNo.setOnEditorActionListener(this);
        getBinding().etPhoneNo.requestFocus();
        // String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IklmQkFXQ001UDNWa0htZ3habU9RbTdCZ0IxUkRtOFJhWjktMnROZElDR3MifQ.eyJuYW1lIjoic2FpZnVsbGFoIEtoYW4iLCJhdWQiOiJ1cm46ZXhhbXBsZTpjbGllbnRfaWQiLCJpc3MiOiJodHRwczovL29wLmV4YW1wbGUuY29tIiwiaWF0IjoxNTczMzY0ODcwLCJleHAiOjE1NzMzNjU3NzB9.GIIStHh5KlCdqYYUw0iBOYRxv1qcmJl0-KsP1snUFWxNSkDPLXyMjzp6nsG_Xwizu7EOhrl1ofe9YpU39A3Geg";

    }

    private void getAutoMsisdn() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        viewModel.getMsisdn().observe(this, otpModel -> {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            if (otpModel != null) {
                switch (otpModel.getResponseCode()) {
                    case 200: {
                        if (!otpModel.getMsisdn().equals("")) {
                            String msisdn = otpModel.getMsisdn();
                            getBinding().etPhoneNo.append(msisdn);
//                            if (msisdn.startsWith(AppConstants.SL_COUNTRY_CODE)) {
//                                msisdn = msisdn.substring(2);
//                                msisdn = "0".concat(msisdn);
//                                getBinding().etPhoneNo.append(msisdn);
//                            }
                            signin(true);
                        }
                    }
                    break;
                }
            }
        });

    }

    private void setClicks() {
        getBinding().dontAccountText.setOnClickListener(view -> mListener.onFragmentInteraction(AppLevelConstants.SIGN_IN, AppLevelConstants.DONT_HAVE_ACCOUNT, "", "", false, null));
        getBinding().cancelText.setOnClickListener(view -> {
            InputMethodManager mgr = (InputMethodManager) loginActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mgr != null)
                mgr.hideSoftInputFromWindow(getBinding().etPhoneNo.getWindowToken(), 0);
            loginActivity.finish();
        });
        getBinding().continueText.setOnClickListener(view -> signin(false));
    }

    private void signin(Boolean isAutoMsisdn) {
        if (isAutoMsisdn) {
            String msisdn = getBinding().etPhoneNo.getText().toString().trim();
//            if (msisdn.startsWith("0")) {
//                msisdn = msisdn.substring(1);
//                msisdn = AppConstants.SL_COUNTRY_CODE.concat(msisdn);
//            }
            String finalMsisdn = msisdn;
            mListener.onFragmentInteraction(AppLevelConstants.SIGN_IN, AppLevelConstants.CONTINUE, finalMsisdn, "", true, null);
        } else {
            if (getBinding().etPhoneNo.getText() != null) {
                if (TextUtils.isEmpty(getBinding().etPhoneNo.getText().toString().trim())) {
                    getBinding().errorText.setVisibility(View.VISIBLE);
                    getBinding().errorText.setText(getResources().getString(R.string.phone_no_required));
                    getBinding().etPhoneNo.getBackground().setColorFilter(getActivity().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    AppCommonMethods.requestFocus(loginActivity, getBinding().etPhoneNo);
                } else if (getBinding().etPhoneNo.getText().toString().trim().length() < 9) {
                    getBinding().errorText.setVisibility(View.VISIBLE);
                    getBinding().errorText.setText(getResources().getString(R.string.incorrect_phone_number));
                    getBinding().etPhoneNo.getBackground().setColorFilter(getActivity().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    AppCommonMethods.requestFocus(loginActivity, getBinding().etPhoneNo);
                }
//                else if (getBinding().etPhoneNo.getText().toString().startsWith("0") && getBinding().etPhoneNo.getText().toString().trim().length() == 9) {
//                    getBinding().errorText.setText(getResources().getString(R.string.incorrect_phone_number));
//                    AppCommonMethods.requestFocus(loginActivity, getBinding().etPhoneNo);
                // }
                else {
                    getBinding().etPhoneNo.getBackground().clearColorFilter();
                    getBinding().errorText.setVisibility(View.GONE);
                    getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
                    getMpin();
                }
            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context != null) {
            if (context instanceof LoginActivity) {
                loginActivity = (LoginActivity) context;
                mListener = (OnFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            signin(false);
            return true;
        }
        return false;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String fragment, String event, String userName, String otp, Boolean isAutoMsisdn, String txnId);
    }

    private void getMpin() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        if (getBinding().etPhoneNo.getText() != null) {
            String msisdn = getBinding().etPhoneNo.getText().toString().trim();
//            if (msisdn.startsWith("0")) {
//                msisdn = msisdn.substring(1);
//                msisdn = AppConstants.SL_COUNTRY_CODE.concat(msisdn);
//            }
            String finalMsisdn = msisdn;
            if (getActivity() != null && NetworkConnectivity.isOnline(getActivity())) {

                if (BuildConfig.FLAVOR.equalsIgnoreCase("qa")) {
                    if (finalMsisdn.equalsIgnoreCase("763742892") || finalMsisdn.equalsIgnoreCase("768468718") || finalMsisdn.equalsIgnoreCase("767413749") ||
                            finalMsisdn.equalsIgnoreCase("774098897") || finalMsisdn.equalsIgnoreCase("777301080") || finalMsisdn.equalsIgnoreCase("765111864")
                            || finalMsisdn.equalsIgnoreCase("765111924") || finalMsisdn.equalsIgnoreCase("777338767")) {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        KsPreferenceKey.getInstance(getActivity()).setMsisdn(finalMsisdn);
                        mListener.onFragmentInteraction(AppLevelConstants.SIGN_IN, AppLevelConstants.CONTINUE, getBinding().etPhoneNo.getText().toString().trim(), "" + "123456", false, "");

                    }else {
                        viewModel.getMpin(finalMsisdn).observe(this, otpModel -> {
                            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                            Log.e("OTP MODEL", new Gson().toJson(otpModel));
                            if (otpModel != null) {
                                if (TextUtils.isEmpty(String.valueOf(otpModel.getmPin())) || otpModel.getResponseCode() == 1 || otpModel.getResponseCode() == 2) {
                                    showDialog(getActivity().getResources().getString(R.string.something_went_wrong));
                                } else {
                                    KsPreferenceKey.getInstance(getActivity()).setMsisdn(finalMsisdn);
                                    mListener.onFragmentInteraction(AppLevelConstants.SIGN_IN, AppLevelConstants.CONTINUE, getBinding().etPhoneNo.getText().toString().trim(), "" + otpModel.getmPin(), false, otpModel.getTxnId());
                                }
                            } else {
                                showDialog(getResources().getString(R.string.error_sms_failure));
                            }
                        });
                    }
                }

                else {
                    viewModel.getMpin(finalMsisdn).observe(this, otpModel -> {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        Log.e("OTP MODEL", new Gson().toJson(otpModel));
                        if (otpModel != null) {
                            if (TextUtils.isEmpty(String.valueOf(otpModel.getmPin())) || otpModel.getResponseCode() == 1 || otpModel.getResponseCode() == 2) {
                                showDialog(getActivity().getResources().getString(R.string.something_went_wrong));
                            } else {
                                KsPreferenceKey.getInstance(getActivity()).setMsisdn(finalMsisdn);
                                mListener.onFragmentInteraction(AppLevelConstants.SIGN_IN, AppLevelConstants.CONTINUE, getBinding().etPhoneNo.getText().toString().trim(), "" + otpModel.getmPin(), false, otpModel.getTxnId());
                            }
                        } else {
                            showDialog(getResources().getString(R.string.error_sms_failure));
                        }
                    });
                }
            } else {
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void test() {
        Log.e("VERIFIACATION", "TEST");
        KeySpec keySpecPublic = new X509EncodedKeySpec(Base64.decode(AppConstants.JWT_PUBLIC_KEY, Base64.DEFAULT));
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IklmQkFXQ001UDNWa0htZ3habU9RbTdCZ0IxUkRtOFJhWjktMnROZElDR3MifQ.eyJuYW1lIjoic2FpZnVsbGFoIEtoYW4iLCJhdWQiOiJ1cm46ZXhhbXBsZTpjbGllbnRfaWQiLCJpc3MiOiJodHRwczovL29wLmV4YW1wbGUuY29tIiwiaWF0IjoxNTczMjk5OTY0LCJleHAiOjE1NzMzMDM1NjR9.FY9w3apWjoDqNNrNi01VXnS2HPjgogplzOkd5wEvO0w1W_Jg1bF9uS0mTubRSSDkveMfcNp6mv3gbxCvlJa8Bg";
        try {
            RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpecPublic);
            Algorithm publicKeyAlgo = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(publicKeyAlgo)
                    .withIssuer("https://op.example.com")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            Log.e("VERIFICATION HO GYA", jwt.getToken());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
//        loginActivity.finish();
    }
}
