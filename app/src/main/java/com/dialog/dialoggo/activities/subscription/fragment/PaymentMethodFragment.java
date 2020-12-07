package com.dialog.dialoggo.activities.subscription.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.subscription.callback.SubscriptionActivityCallBack;
import com.dialog.dialoggo.baseModel.BaseBindingFragment;
import com.dialog.dialoggo.databinding.FragmentPaymentMethodBinding;


public class PaymentMethodFragment extends BaseBindingFragment<FragmentPaymentMethodBinding> {

    private SubscriptionActivityCallBack mListener;

    public PaymentMethodFragment() {
        // Required empty public constructor
    }

    @Override
    public FragmentPaymentMethodBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentPaymentMethodBinding.inflate(inflater);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_payment_method, container, false);
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SubscriptionActivityCallBack) {
            mListener = (SubscriptionActivityCallBack) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SubscriptionActivityCallBack");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListener.setToolBarTitle(getString(R.string.payments));
        mListener.showToolBar(true);
        getBinding().btnDialogConnection.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_paymentMethodFragment_to_billPaymentFragment,null));

    }

}
