package com.astro.sott.fragments.Success.UI;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.astro.sott.R;
import com.astro.sott.activities.addDTVAccountNumber.UI.addDTVAccountNumberActivity;
import com.astro.sott.activities.mbbaccount.ui.AddMBBAccountActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentSuccessBinding;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.utils.helpers.AppLevelConstants;


public class Success extends BaseBindingFragment<FragmentSuccessBinding> implements TextView.OnEditorActionListener, AlertDialogSingleButtonFragment.AlertDialogListener {

    private static final String TAG = "Success";

    private addDTVAccountNumberActivity addDTVAccountNumber;
    private OnFragmentInteractionListener mListener;
    private AddMBBAccountActivity addMBBAccountActivity;
    private String screenFrom = "";
    private String fragmentType = "";

    public Success() {
        // Required empty public constructor
    }


    @Override
    public FragmentSuccessBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentSuccessBinding.inflate(inflater);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setClicks();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        switch (fragmentType){

            case AppLevelConstants.DTV_TYPE_FRAGMENT:
                if(screenFrom.equalsIgnoreCase(AppLevelConstants.ADDSUCCESS)){
                    getBinding().successMessage.setText(getActivity().getResources().getString(R.string.dtv_number_added_successfully));

                }else if(screenFrom.equalsIgnoreCase(AppLevelConstants.DELETE_SUCCESS)){
                    getBinding().successMessage.setText(getActivity().getResources().getString(R.string.dtv_number_removed_successfully));
                }else {
                    getBinding().successMessage.setText(getActivity().getResources().getString(R.string.dtv_number_changed_successfully));
                }                break;

            case AppLevelConstants.MBB_TYPE_FRAGMENT:
                if(screenFrom.equalsIgnoreCase(AppLevelConstants.ADDSUCCESS)){
                    getBinding().successMessage.setText(getActivity().getResources().getString(R.string.mbb_number_added_successfully));

                }else if(screenFrom.equalsIgnoreCase(AppLevelConstants.DELETE_SUCCESS)){
                    getBinding().successMessage.setText(getActivity().getResources().getString(R.string.mbb_number_removed_successfully));
                }else {
                    getBinding().successMessage.setText(getActivity().getResources().getString(R.string.mbb_number_changed_successfully));
                }                break;

            default:
                if(screenFrom.equalsIgnoreCase(AppLevelConstants.ADDSUCCESS)){
                    getBinding().successMessage.setText("");

                }else if(screenFrom.equalsIgnoreCase(AppLevelConstants.DELETE_SUCCESS)){
                    getBinding().successMessage.setText("");
                }else {
                    getBinding().successMessage.setText("");
                }
                break;
        }





    }

    private void setClicks() {
        getBinding().continueText.setOnClickListener(view -> {
            if(fragmentType.equalsIgnoreCase(AppLevelConstants.DTV_TYPE_FRAGMENT)){
                addDTVAccountNumber.finish();
            }else if(fragmentType.equalsIgnoreCase(AppLevelConstants.MBB_TYPE_FRAGMENT)){
                addMBBAccountActivity.finish();
            }
        });
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
            Bundle bundle = getArguments();
            if (bundle != null) {
                screenFrom = bundle.getString(AppLevelConstants.SCREENFROM);
                fragmentType = bundle.getString(AppLevelConstants.FRAGMENT_TYPE);
            }
            if(fragmentType.equalsIgnoreCase(AppLevelConstants.DTV_TYPE_FRAGMENT)){
                if (context instanceof addDTVAccountNumberActivity) {
                    addDTVAccountNumber = (addDTVAccountNumberActivity) context;
                    mListener = (OnFragmentInteractionListener) context;                }
            }else if(fragmentType.equalsIgnoreCase(AppLevelConstants.MBB_TYPE_FRAGMENT)){
                if (context instanceof AddMBBAccountActivity) {
                    addMBBAccountActivity = (AddMBBAccountActivity) context;
                    mListener = (OnFragmentInteractionListener) context;                }
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
//        void onFragmentInteraction(String fragment, String event,String fragmentType, String msiddn, int otp, String s,String txnId);

        void onFragmentInteraction(String fragment, String event, String fragmentType, String msisdn, int otp, String s, String txnId);
    }

}