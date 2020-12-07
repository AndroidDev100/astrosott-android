package com.dialog.dialoggo.activities.subscription.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.subscription.model.BillPaymentMethodModel;
import com.dialog.dialoggo.activities.subscription.model.BillPaymentModel;
import com.dialog.dialoggo.callBacks.commonCallBacks.BillPaymentMethodCallBack;
import com.dialog.dialoggo.databinding.BillPaymentItemBinding;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;

import java.util.List;

public class BillPaymentMethodAdapter extends RecyclerView.Adapter {

    private static final int PAYMENT_TITLE_TYPE = 0;
    private static final int PAYMENT_DESC_TYPE = 1;
    private Context mcontext;
    private List<BillPaymentModel> mPackageModelList;
    private long lastClickTime = 0;
    private int total_types;
    private BillPaymentMethodCallBack mListener;
    private int mSelectedItem = -1;

    public BillPaymentMethodAdapter(Context context, List<BillPaymentModel> packageModelList, BillPaymentMethodCallBack mListener) {
        this.mListener = mListener;
        this.mcontext = context;
        this.mPackageModelList = packageModelList;
        total_types = mPackageModelList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case PAYMENT_TITLE_TYPE:

                BillPaymentItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.bill_payment_item, parent, false);
                PaymentItemHolder paymentItemHolder = new PaymentItemHolder(binding);
                setRecyclerProperties(paymentItemHolder.potraitRecyclerItemBinding.recyclerview);
                return paymentItemHolder;

            case PAYMENT_DESC_TYPE:

//                BillPaymentItemBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.bill_payment_item, parent, false);
//                PaymentItemHolder paymentItemHolder1 = new PaymentItemHolder(binding1);
//                setRecyclerProperties(paymentItemHolder1.potraitRecyclerItemBinding.recyclerview);
//                return paymentItemHolder1;

            default:
                return null;
        }

    }

    private void setRecyclerProperties(RecyclerView recyclerview) {
        recyclerview.hasFixedSize();
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(mcontext, RecyclerView.VERTICAL, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PaymentItemHolder) {
            try {
                paymentDataLogic(((PaymentItemHolder) holder), mPackageModelList, position);
            } catch (ClassCastException e) {

            }
        }

    }

    private void paymentDataLogic(PaymentItemHolder holder, List<BillPaymentModel> mPackageModelList, int position) {
        PaymentChildAdapter landscape = new PaymentChildAdapter(mPackageModelList.get(position).getBillPaymentDetails(), mcontext, mListener);
        holder.potraitRecyclerItemBinding.recyclerview.setAdapter(landscape);
        holder.potraitRecyclerItemBinding.headerTitle.setText(mPackageModelList.get(position).getHeaderTitle());

    }

    @Override
    public int getItemCount() {
        return mPackageModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (mPackageModelList.get(position).getHeaderTitle()) {
            case AppLevelConstants.ADD_TO_BILL:
                return BillPaymentMethodModel.PAYMENT_TITLE_TYPE;
            case AppLevelConstants.DEDUCT_FROM_BILL:
                return BillPaymentMethodModel.PAYMENT_DESC_TYPE;
            default:
                return -1;

        }

    }


//    public static class SingleItemRowHolder extends RecyclerView.ViewHolder {
//        final BillPaymentItemBinding itemBinding;
//
//        public SingleItemRowHolder(@NonNull BillPaymentItemBinding binding) {
//            super(binding.getRoot());
//            this.itemBinding = binding;
//        }
//    }


    public class PaymentItemHolder extends RecyclerView.ViewHolder {
        final BillPaymentItemBinding potraitRecyclerItemBinding;

        private PaymentItemHolder(BillPaymentItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            potraitRecyclerItemBinding = flightItemLayoutBinding;

        }
    }
}
