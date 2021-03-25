package com.astro.sott.fragments.transactionhistory.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.callBacks.commonCallBacks.PlanSelectedCallback;
import com.astro.sott.databinding.ItemTransactionHistoryBinding;
import com.astro.sott.databinding.SubscriptionPackItemBinding;
import com.astro.sott.fragments.subscription.adapter.SubscriptionAdapter;
import com.astro.sott.fragments.subscription.ui.SubscriptionPacksFragment;
import com.astro.sott.fragments.transactionhistory.ui.TransactionHistory;
import com.astro.sott.usermanagment.modelClasses.getPaymentV2.OrderItem;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.SingleItemHolder> {
    private Fragment fragment;
    private List<OrderItem> orderItems;
    private boolean checkBoxVisible;
    private PlanSelectedCallback planSelectedCallback;

    public TransactionAdapter(TransactionHistory ctx, List<OrderItem> order, boolean checkBoxVisible) {
        this.fragment = ctx;
        orderItems = order;
        this.checkBoxVisible = checkBoxVisible;
        this.planSelectedCallback = (PlanSelectedCallback) ctx;
    }

    @NonNull
    @Override
    public TransactionAdapter.SingleItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionHistoryBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_transaction_history, parent, false);
        return new TransactionAdapter.SingleItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.SingleItemHolder holder, int position) {
        if (orderItems.get(position) != null) {
            if (orderItems.get(position).getOrderProductInfo() != null && orderItems.get(position).getOrderProductInfo().get(0) != null && orderItems.get(position).getOrderProductInfo().get(0).getDisplayName() != null) {
                holder.binding.orderName.setText(orderItems.get(position).getOrderProductInfo().get(0).getDisplayName());
            }

            if (orderItems.get(position).getCurrencyCode() != null) {
                holder.binding.currency.setText(orderItems.get(position).getCurrencyCode() + " 20.00");
            }
            if (orderItems.get(position).getPaymentsInfo() != null && orderItems.get(position).getPaymentsInfo().get(0) != null && orderItems.get(position).getPaymentsInfo().get(0).getCreditCardNumber() != null) {
                holder.binding.creditNo.setText(orderItems.get(position).getPaymentsInfo().get(0).getCreditCardNumber());
            }
        }
        if (checkBoxVisible) {
            holder.binding.checkbox.setVisibility(View.VISIBLE);
        } else {
            holder.binding.checkbox.setVisibility(View.GONE);
        }
        holder.binding.checkbox.setOnClickListener(v -> {
            if (!holder.binding.checkbox.isChecked()) {
                planSelectedCallback.onPlanUnselected(orderItems.get(position).getPaymentsInfo().get(0).getPaymentID());
            } else {
                planSelectedCallback.onPlanSelected(orderItems.get(position).getPaymentsInfo().get(0).getPaymentID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        final ItemTransactionHistoryBinding binding;

        public SingleItemHolder(ItemTransactionHistoryBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}

