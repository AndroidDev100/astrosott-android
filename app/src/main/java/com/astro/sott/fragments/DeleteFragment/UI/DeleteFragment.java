package com.astro.sott.fragments.DeleteFragment.UI;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.astro.sott.activities.addDTVAccountNumber.UI.addDTVAccountNumberActivity;
import com.astro.sott.activities.loginActivity.viewModel.LoginViewModel;
import com.astro.sott.activities.mbbaccount.ui.AddMBBAccountActivity;
import com.astro.sott.activities.verification.VerificationActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.R;
import com.astro.sott.databinding.FragmentDeleteBinding;
import com.astro.sott.utils.helpers.ToastHandler;
import com.google.gson.Gson;


public class DeleteFragment extends BaseBindingFragment<FragmentDeleteBinding> implements TextView.OnEditorActionListener, AlertDialogSingleButtonFragment.AlertDialogListener {
    private addDTVAccountNumberActivity addDTVAccountActivity;
    private AddMBBAccountActivity addMBBAccountActivity;
    private OnFragmentInteractionListener mListener;
    private LoginViewModel viewModel;
    private String accountNumber = "";
    private String number = "";
    private String fragmentType = "";

    public DeleteFragment() {
        // Required empty public constructor
    }


    @Override
    public FragmentDeleteBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentDeleteBinding.inflate(inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context != null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                accountNumber = bundle.getString(AppLevelConstants.DTV_ACCOUNT_NUM);
                fragmentType = bundle.getString(AppLevelConstants.FRAGMENT_TYPE);
            }

            if(fragmentType.equalsIgnoreCase(AppLevelConstants.DTV_TYPE_FRAGMENT)){
                if (context instanceof addDTVAccountNumberActivity) {
                    addDTVAccountActivity = (addDTVAccountNumberActivity) context;
                    mListener = (OnFragmentInteractionListener) context;
                }
            }else if(fragmentType.equalsIgnoreCase(AppLevelConstants.MBB_TYPE_FRAGMENT)){
                if (context instanceof AddMBBAccountActivity) {
                    addMBBAccountActivity = (AddMBBAccountActivity) context;
                    mListener = (OnFragmentInteractionListener) context;
                }
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        callViewModel();
        setUiData();
        setClicks();

    }

    private void setUiData() {
        switch (fragmentType){

            case AppLevelConstants.DTV_TYPE_FRAGMENT:
                getBinding().validatePin.setText(getActivity().getResources().getString(R.string.remove_dtv_number));
                getBinding().resetPin.setText(getActivity().getResources().getString(R.string.remove_dtv_number_message));
                break;

            case AppLevelConstants.MBB_TYPE_FRAGMENT:
                getBinding().validatePin.setText(getActivity().getResources().getString(R.string.remove_mbb_number));
                getBinding().resetPin.setText(getActivity().getResources().getString(R.string.remove_mbb_number_message));
                break;

            default:
                getBinding().validatePin.setText("");
                getBinding().resetPin.setText("");
                break;
        }

    }

    private void callViewModel() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    private void setClicks() {

        getBinding().no.setOnClickListener(view -> {

            switch (fragmentType){

                case AppLevelConstants.DTV_TYPE_FRAGMENT:
                    addDTVAccountActivity.finish();
                    break;

                case AppLevelConstants.MBB_TYPE_FRAGMENT:
                    addMBBAccountActivity.finish();
                    break;
            }
        });

        getBinding().yes.setOnClickListener(view -> {

            switch (fragmentType){

                case AppLevelConstants.DTV_TYPE_FRAGMENT:
                    getDtvAccountDetails(accountNumber);
                    break;

                case AppLevelConstants.MBB_TYPE_FRAGMENT:
                    generatemMsisdn(accountNumber);
                    break;
            }

        });

    }

    private void getDtvAccountDetails(String accountNumber) {
        if (getActivity() != null && NetworkConnectivity.isOnline(getActivity())) {

            viewModel.getDtvAccountDetail(accountNumber).observe(this, dtvContactInfoModel -> {
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
            ToastHandler.show(getString(R.string.no_internet_connection) + "", requireActivity());
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

    private void generatemMsisdn(String msisdn) {
        getMpin(msisdn);
    }

    private void getMpin(String msisdn) {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        if (getActivity() != null && NetworkConnectivity.isOnline(getActivity())) {
            viewModel.getMpin(msisdn).observe(this, otpModel -> {
                Gson gson = new Gson();
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);

                if (otpModel != null) {
                    if (TextUtils.isEmpty(String.valueOf(otpModel.getmPin())) || otpModel.getResponseCode() == 1 || otpModel.getResponseCode() == 2) {
                        showDialog(getActivity().getResources().getString(R.string.something_went_wrong));
                    }else {
                        mListener.onFragmentInteraction(AppLevelConstants.DELETE_FRAGMENT, AppLevelConstants.YES,fragmentType,msisdn,otpModel.getmPin(), accountNumber,otpModel.getTxnId());
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String fragment, String event,String fragmentType, String msiddn, int otp, String dtvAccoutNum,String txnId);
    }


}