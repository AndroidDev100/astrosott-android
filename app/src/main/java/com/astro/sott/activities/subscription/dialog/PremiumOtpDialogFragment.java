package com.astro.sott.activities.subscription.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.astro.sott.R;
import com.astro.sott.activities.subscription.listeners.DtvAccountActivityListener;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentPremiumOtpBinding;

public class PremiumOtpDialogFragment extends BaseBindingFragment<FragmentPremiumOtpBinding> {

    private static final String TAG = "PremiumOtpDialogFragment";

    private DtvAccountActivityListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DtvAccountActivityListener) {
            mListener = (DtvAccountActivityListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public FragmentPremiumOtpBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentPremiumOtpBinding.inflate(inflater);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_premium_dtv_account,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btn2.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_premiumOtpDialogFragment_to_premiumSuccessDialogFragment,null));

    }
}
