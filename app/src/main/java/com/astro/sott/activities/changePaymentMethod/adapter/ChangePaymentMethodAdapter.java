package com.astro.sott.activities.changePaymentMethod.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.activities.SelectAccount.SelectAccountModel.DetailListItem;
import com.astro.sott.callBacks.commonCallBacks.ChangePaymentMethodCallBack;
import com.astro.sott.R;
import com.astro.sott.callBacks.DTVItemClickListner;
import com.astro.sott.databinding.PaymentItemBinding;
import com.kaltura.client.types.HouseholdPaymentMethod;

import java.util.List;

public class ChangePaymentMethodAdapter extends RecyclerView.Adapter<ChangePaymentMethodAdapter.SingleItemHolder> {
    private Activity activity;
    private List<DetailListItem> data;
    private DTVItemClickListner dtvItemClickListner;
    private int mSelectedPosition = 0;
    private List<HouseholdPaymentMethod> mPaymentMethodList;
    private ChangePaymentMethodCallBack changePaymentMethodCallBack;
    private long lastClickTime = 0;

    public ChangePaymentMethodAdapter(Activity ctx, List<HouseholdPaymentMethod> paymentMethodList, ChangePaymentMethodCallBack callBack) {
        activity = ctx;
        this.mPaymentMethodList = paymentMethodList;
        this.changePaymentMethodCallBack = callBack;
    }

    private View view;

    @NonNull
    @Override
    public ChangePaymentMethodAdapter.SingleItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        PaymentItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.payment_item, viewGroup, false);

        return new ChangePaymentMethodAdapter.SingleItemHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangePaymentMethodAdapter.SingleItemHolder singleItemHolder, int position) {
        singleItemHolder.paymentItemBinding.tvAccount.setText(activity.getResources().getString(R.string.add_to_my)+" "+String.valueOf(mPaymentMethodList.get(position).getExternalId()));

//        singleItemHolder.paymentItemBinding.tvChange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
//                    return;
//                }
//                lastClickTime = SystemClock.elapsedRealtime();
//
//                changePaymentMethodCallBack.callBack(mPaymentMethodList.get(position).getId(),mPaymentMethodList.get(position).getExternalId());
//            }
//        });
    } 


    @Override
    public int getItemCount() {
        return mPaymentMethodList.size();
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        RelativeLayout linearLayout;
        final PaymentItemBinding paymentItemBinding;

        SingleItemHolder(PaymentItemBinding binding) {
            super(binding.getRoot());
            this.paymentItemBinding = binding;


            //linearLayout = view.findViewById(R.id.item);

        }
    }
}
