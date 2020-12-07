package com.dialog.dialoggo.activities.changePaymentMethod.adapter;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.DetailListItem;
import com.dialog.dialoggo.callBacks.DTVItemClickListner;
import com.dialog.dialoggo.callBacks.commonCallBacks.ChangePaymentMethodCallBack;
import com.dialog.dialoggo.callBacks.commonCallBacks.UpdateCallBack;
import com.dialog.dialoggo.databinding.PaymentItemBinding;
import com.dialog.dialoggo.databinding.UpdateItemBinding;
import com.kaltura.client.types.HouseholdPaymentMethod;

import java.util.List;

public class UpdatePaymentMethodAdapter extends RecyclerView.Adapter<UpdatePaymentMethodAdapter.SingleItemHolder> {
    private Activity activity;
    private List<DetailListItem> data;
    private DTVItemClickListner dtvItemClickListner;
    private int mSelectedPosition = 0;
    private List<HouseholdPaymentMethod> mPaymentMethodList;
    private UpdateCallBack changePaymentMethodCallBack;
    private long lastClickTime = 0;

    public UpdatePaymentMethodAdapter(Activity ctx, List<HouseholdPaymentMethod> paymentMethodList, UpdateCallBack callBack) {
        activity = ctx;
        this.mPaymentMethodList = paymentMethodList;
        this.changePaymentMethodCallBack = callBack;
    }

    private View view;

    @NonNull
    @Override
    public UpdatePaymentMethodAdapter.SingleItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        UpdateItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.update_item, viewGroup, false);

        return new UpdatePaymentMethodAdapter.SingleItemHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdatePaymentMethodAdapter.SingleItemHolder singleItemHolder, int position) {
        singleItemHolder.paymentItemBinding.tvAccount.setText(mPaymentMethodList.get(position).getExternalId());

        singleItemHolder.paymentItemBinding.tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                changePaymentMethodCallBack.callBack(mPaymentMethodList.get(position).getId(),mPaymentMethodList.get(position).getExternalId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mPaymentMethodList.size();
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        RelativeLayout linearLayout;
        final UpdateItemBinding paymentItemBinding;

        SingleItemHolder(UpdateItemBinding binding) {
            super(binding.getRoot());
            this.paymentItemBinding = binding;


            //linearLayout = view.findViewById(R.id.item);

        }
    }
}

