package com.astro.sott.activities.subscription.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.astro.sott.activities.subscription.listeners.DtvAccountActivityListener;
import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentPremiumDtvAccountBinding;

public class PremiumDtvAccountDialogFragment extends BaseBindingFragment<FragmentPremiumDtvAccountBinding> {

    private static final String TAG = "PremiumDtvAccountDialogFragment";

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
    public FragmentPremiumDtvAccountBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentPremiumDtvAccountBinding.inflate(inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btn1.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_premiumDtvAccountDialogFragment_to_premiumOtpDialogFragment,null));

    }
}
