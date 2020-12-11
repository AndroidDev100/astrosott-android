package com.astro.sott.fragments.dtvAccount;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.astro.sott.activities.loginActivity.viewModel.LoginViewModel;
import com.astro.sott.R;
import com.astro.sott.activities.addDTVAccountNumber.UI.addDTVAccountNumberActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentDtvBinding;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.CustomTextWatcher;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.google.gson.Gson;


public class dtvFragment extends BaseBindingFragment<FragmentDtvBinding> implements TextView.OnEditorActionListener, AlertDialogSingleButtonFragment.AlertDialogListener{
    private addDTVAccountNumberActivity addDTVAccountNumber;
    private OnFragmentInteractionListener mListener;
   // private String msindn = "";
    private LoginViewModel viewModel;
    private String number = "";
    private String strFragmentName = "";

    public dtvFragment() {
        // Required empty public constructor
    }


    @Override
    public FragmentDtvBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentDtvBinding.inflate(inflater);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

      //  viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
      //  getAutoMsisdn();

      //   msindn = KsPreferenceKey.getInstance(getActivity()).getMsisdn();
      //  Log.d("MsisdnOtp",msindn);

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
            InputMethodManager mgr = (InputMethodManager) addDTVAccountNumber.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mgr != null)
                mgr.hideSoftInputFromWindow(getBinding().etPhoneNo.getWindowToken(), 0);
            addDTVAccountNumber.finish();
        });
        getBinding().continueText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getBinding().etPhoneNo.getText() != null) {
                    if (TextUtils.isEmpty(getBinding().etPhoneNo.getText().toString().trim())) {
                        getBinding().inputLayoutPhoneNo.setError(getResources().getString(R.string.account_no_required));
                        AppCommonMethods.requestFocus(addDTVAccountNumber, getBinding().inputLayoutPhoneNo);
                    } else if (getBinding().etPhoneNo.getText().toString().trim().length() < 8) {
                        getBinding().inputLayoutPhoneNo.setError(getResources().getString(R.string.incorrect_account_number));
                        AppCommonMethods.requestFocus(addDTVAccountNumber, getBinding().inputLayoutPhoneNo);
                    }  else {

                        addDtvAccount(getBinding().etPhoneNo.getText().toString().trim());





                       // getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);

                    }
                }




            }
        });
    }

    private void addDtvAccount(String dtvAccountNumber) {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        getDtvAccountDetails(getBinding().etPhoneNo.getText().toString());

    }

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

    private void generatemMsisdn(String msisdn) {
//        if (msisdn.startsWith("0")) {
//            msisdn = msisdn.substring(1);
//            msisdn = AppConstants.SL_COUNTRY_CODE.concat(msisdn);
//            getMpin(msisdn);
//        } else {
//            msisdn = AppConstants.SL_COUNTRY_CODE.concat(msisdn);
//            getMpin(msisdn);
//        }

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
                            if(strFragmentName.equalsIgnoreCase("ChangeFragment")){
                                mListener.onFragmentInteraction("dtvFragment", AppLevelConstants.CHANGESUCCESS,AppLevelConstants.DTV_TYPE_FRAGMENT,msisdn,otpModel.getmPin(),getBinding().etPhoneNo.getText().toString(),otpModel.getTxnId());
                            }else {
                                mListener.onFragmentInteraction("dtvFragment", AppLevelConstants.CONTINUE,AppLevelConstants.DTV_TYPE_FRAGMENT,msisdn,otpModel.getmPin(),getBinding().etPhoneNo.getText().toString(),otpModel.getTxnId());
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
            if (context instanceof addDTVAccountNumberActivity) {
                addDTVAccountNumber = (addDTVAccountNumberActivity) context;
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String fragment, String event,String fragmentType, String msindn, int otp,String dtvAccountNum,String txnId);
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