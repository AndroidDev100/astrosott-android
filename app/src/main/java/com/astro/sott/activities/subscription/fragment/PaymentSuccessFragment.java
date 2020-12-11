package com.astro.sott.activities.subscription.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.astro.sott.activities.subscription.callback.SubscriptionActivityCallBack;
import com.astro.sott.activities.subscription.manager.AllChannelManager;
import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentPaymentSuccessBinding;

public class PaymentSuccessFragment extends BaseBindingFragment<FragmentPaymentSuccessBinding> {

    private SubscriptionActivityCallBack mListener;

    public PaymentSuccessFragment() {
        // Required empty public constructor
    }

    @Override
    public FragmentPaymentSuccessBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentPaymentSuccessBinding.inflate(inflater);
    }

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

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_payment_success, container, false);
//    }

//    @string/payment_successful_description
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListener.showToolBar(false);
        SpannableStringBuilder builder = new SpannableStringBuilder();


        String white = getString(R.string.payment_successful_description);
        SpannableString whiteSpannable= new SpannableString(white);
        whiteSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, white.length(), 0);
        builder.append(whiteSpannable);
        builder.append(" ");

        String red = AllChannelManager.getInstance().getPaymentTitle().concat(".");
        SpannableString redSpannable= new SpannableString(red);
        redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, red.length(), 0);
        builder.append(redSpannable);
        builder.append(" ");


        String white1 = getString(R.string.payment_request_successful_description);
        SpannableString whiteSpannable1= new SpannableString(white1);
        whiteSpannable1.setSpan(new ForegroundColorSpan(Color.WHITE), 0, white1.length(), 0);
        builder.append(whiteSpannable1);


        getBinding().txtPaymentDescription.setText(builder, TextView.BufferType.SPANNABLE);
        getBinding().txtAmmount.setText("Rs. "+AllChannelManager.getInstance().getPrice().concat(" ").concat("+tax"));
        getBinding().txtPaymentMethod.setText(AllChannelManager.getInstance().getPaymentType());
       // getBinding().txtTranscationId.setText(AllChannelManager.getInstance().getTransactionId());
        getBinding().btnSuccess.setOnClickListener(v -> {
            if(getActivity() != null){
                getActivity().finish();
            }
        });
    }
}
