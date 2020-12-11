package com.astro.sott.fragments.ChangeFragment.UI;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.astro.sott.activities.addDTVAccountNumber.UI.addDTVAccountNumberActivity;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.R;
import com.astro.sott.activities.mbbaccount.ui.AddMBBAccountActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentChangeBinding;
import com.astro.sott.utils.helpers.AppLevelConstants;


public class ChangeFragment extends BaseBindingFragment<FragmentChangeBinding> implements TextView.OnEditorActionListener, AlertDialogSingleButtonFragment.AlertDialogListener {

    private addDTVAccountNumberActivity addDTVAccountActivity;
    private AddMBBAccountActivity addMBBAccountActivity;
    private OnFragmentInteractionListener mListener;
    private String accountNumber = "";
    private String fragmentType = "";

    public ChangeFragment() {
        // Required empty public constructor
    }


    @Override
    public FragmentChangeBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentChangeBinding.inflate(inflater);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setClicks();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (fragmentType){

            case AppLevelConstants.DTV_TYPE_FRAGMENT:
                getBinding().validatePin.setText(getActivity().getResources().getString(R.string.dtv_account_).concat(" ").concat(accountNumber));
                getBinding().resetPin.setText(getActivity().getResources().getString(R.string.dtv_change_account_number));
                break;

            case AppLevelConstants.MBB_TYPE_FRAGMENT:
                getBinding().validatePin.setText(getActivity().getResources().getString(R.string.mbb_account_).concat(" ").concat(accountNumber));
                getBinding().resetPin.setText(getActivity().getResources().getString(R.string.mbb_change_account_number));
                break;

            default:
                getBinding().validatePin.setText("");
                getBinding().resetPin.setText("");
                break;

        }


    }

    private void setClicks() {

        getBinding().remove.setOnClickListener(view -> {

            switch (fragmentType){

                case AppLevelConstants.DTV_TYPE_FRAGMENT:
                    addDTVAccountActivity.finish();
                    break;

                case AppLevelConstants.MBB_TYPE_FRAGMENT:
                    addMBBAccountActivity.finish();
                    break;
            }

        });

        getBinding().change.setOnClickListener(view -> {

            switch (fragmentType){

                case AppLevelConstants.DTV_TYPE_FRAGMENT:
                    mListener.onFragmentInteraction("ChangeFragment", AppLevelConstants.CHANGE,fragmentType,"",0,"",null);
                    break;

                case AppLevelConstants.MBB_TYPE_FRAGMENT:
                    mListener.onFragmentInteraction("ChangeFragment", AppLevelConstants.CHANGE,fragmentType,"",0,"",null);
                    break;
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String fragment, String event,String fragmentType,String msiddn, int otp, String dtvAccountNum,String txnId);
    }

}