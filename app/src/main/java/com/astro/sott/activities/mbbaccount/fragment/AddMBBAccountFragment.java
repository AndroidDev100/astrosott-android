package com.astro.sott.activities.mbbaccount.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.astro.sott.activities.loginActivity.viewModel.LoginViewModel;
import com.astro.sott.activities.mbbaccount.ui.AddMBBAccountActivity;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentAddMbbAccountBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.CustomTextWatcher;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.google.gson.Gson;


public class AddMBBAccountFragment extends BaseBindingFragment<FragmentAddMbbAccountBinding> implements TextView.OnEditorActionListener, AlertDialogSingleButtonFragment.AlertDialogListener {
    private AddMBBAccountActivity addMBBAccountActivity;
    private OnFragmentInteraction mListener;
   // private String msindn = "";
    private LoginViewModel viewModel;
    private String number = "";
    private String strFragmentName = "";

    public AddMBBAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public FragmentAddMbbAccountBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentAddMbbAccountBinding.inflate(inflater);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        setClicks();

        getBinding().etPhoneNo.addTextChangedListener(new CustomTextWatcher(getBinding().inputLayoutPhoneNo));
        getBinding().etPhoneNo.setOnEditorActionListener(this);
        getBinding().etPhoneNo.requestFocus();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            strFragmentName = bundle.getString(AppLevelConstants.SCREENFROM);
        }
    }

    private void setClicks() {
        getBinding().cancelText.setOnClickListener(view -> {
            InputMethodManager mgr = (InputMethodManager) addMBBAccountActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mgr != null)
                mgr.hideSoftInputFromWindow(getBinding().etPhoneNo.getWindowToken(), 0);
            addMBBAccountActivity.finish();
        });
        getBinding().continueText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getBinding().etPhoneNo.getText() != null) {
                    if (TextUtils.isEmpty(getBinding().etPhoneNo.getText().toString().trim())) {
                        getBinding().inputLayoutPhoneNo.setError(getResources().getString(R.string.mobile_no_required));
                        AppCommonMethods.requestFocus(addMBBAccountActivity, getBinding().inputLayoutPhoneNo);
                    } else if (getBinding().etPhoneNo.getText().toString().trim().length() < 9) {
                        getBinding().inputLayoutPhoneNo.setError(getResources().getString(R.string.incorrect_mobile_number));
                        AppCommonMethods.requestFocus(addMBBAccountActivity, getBinding().inputLayoutPhoneNo);
                    }  else {
                        addMBBAccount(getBinding().etPhoneNo.getText().toString().trim());
                    }
                }
            }
        });
    }

    private void addMBBAccount(String dtvAccountNumber) {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
//        getDtvAccountDetails(getBinding().etPhoneNo.getText().toString());
        generatemMsisdn(getBinding().etPhoneNo.getText().toString());

    }

/*
    private void getDtvAccountDetails(String dtvAccountNumber) {
     //  getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        if (getActivity() != null && NetworkConnectivity.isOnline(getActivity())) {

            viewModel.getDtvAccountDetail(dtvAccountNumber).observe(this, dtvContactInfoModel -> {
                if(dtvContactInfoModel!= null){
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    if(!TextUtils.isEmpty(dtvContactInfoModel.getSmsNotifyNo()) && (dtvContactInfoModel.getSmsNotifyNo().matches("[0-9]+"))) {
                        number = dtvContactInfoModel.getSmsNotifyNo();
                        generatemMsisdn(number);
                    } else if(!TextUtils.isEmpty(dtvContactInfoModel.getMobileNo())&& (dtvContactInfoModel.getMobileNo().matches("[0-9]+"))) {
                        number = dtvContactInfoModel.getMobileNo();
                        generatemMsisdn(number);
                    }else if(!TextUtils.isEmpty(dtvContactInfoModel.getContactNo())&& (dtvContactInfoModel.getContactNo().matches("[0-9]+"))){
                        number = dtvContactInfoModel.getContactNo();
                        generatemMsisdn(number);

                    } else {
                        showDialog(dtvContactInfoModel.getResultDesc());
                    }

                }else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    showDialog(dtvContactInfoModel.getResultDesc());
                    }

            });

        }else {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }

    }
*/

    private void generatemMsisdn(String msisdn) {
        getMpin(msisdn);
    }

    private void getMpin(String msisdn) {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);

          //  Log.d("finalResponseIs",msindn);
            if (getActivity() != null && NetworkConnectivity.isOnline(getActivity())) {
                viewModel.getMpin(msisdn).observe(this, otpModel -> {
                    Gson gson = new Gson();
                    Log.e("SihnIn",gson.toJson(otpModel));

                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);


                    if (otpModel != null) {
                        if (TextUtils.isEmpty(String.valueOf(otpModel.getmPin())) || otpModel.getResponseCode() == 1 || otpModel.getResponseCode() == 2) {
                            showDialog(getActivity().getResources().getString(R.string.something_went_wrong));
                        }else {
                            if(strFragmentName.equalsIgnoreCase(AppLevelConstants.CHANGE_FRAGMENT)){
                                if(mListener != null){
                                    mListener.onFragmentInteraction(AppLevelConstants.MBB_FRAGMENT, AppLevelConstants.CHANGESUCCESS,AppLevelConstants.MBB_TYPE_FRAGMENT,msisdn,otpModel.getmPin(),getBinding().etPhoneNo.getText().toString(),otpModel.getTxnId());
                                }
                            }else {
                                if(mListener != null){
                                    mListener.onFragmentInteraction(AppLevelConstants.MBB_FRAGMENT, AppLevelConstants.CONTINUE,AppLevelConstants.MBB_TYPE_FRAGMENT,msisdn,otpModel.getmPin(),getBinding().etPhoneNo.getText().toString(),otpModel.getTxnId());
                                }
                            }
                        }
                    }

                    else {
                        showDialog(getResources().getString(R.string.error_sms_failure));
                    }
                });
            } else {
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            }

    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }



    @Override
    public void onFinishDialog() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context != null) {
            if (context instanceof AddMBBAccountActivity) {
                addMBBAccountActivity = (AddMBBAccountActivity) context;
                mListener = (OnFragmentInteraction) context;
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

    public interface OnFragmentInteraction {
        void onFragmentInteraction(String fragment, String event,String fragmentType, String msindn, int otp, String dtvAccountNum, String txnId);
    }

    private void showDialog(String message) {
        FragmentManager fm = getFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        if (fm != null)
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

}