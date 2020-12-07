package com.dialog.dialoggo.activities.subscription.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.subscription.listeners.DtvAccountActivityListener;
import com.dialog.dialoggo.activities.subscription.ui.SingleLiveChannelSubscriptionActivity;
import com.dialog.dialoggo.baseModel.BaseBindingFragment;
import com.dialog.dialoggo.databinding.FragmentPremiumSuccessBinding;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;

public class PremiumSuccessDialogFragment extends BaseBindingFragment<FragmentPremiumSuccessBinding> implements View.OnClickListener {


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
    public FragmentPremiumSuccessBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentPremiumSuccessBinding.inflate(inflater);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btn3.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn3:
//                new ActivityLauncher(getActivity()).singleChannelSubscriptionActivity(getActivity(), SingleLiveChannelSubscriptionActivity.class);
                mListener.finishActivity();
                break;
        }
    }
}
