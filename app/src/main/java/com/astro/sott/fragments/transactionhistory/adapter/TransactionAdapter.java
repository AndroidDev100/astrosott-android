package com.astro.sott.fragments.transactionhistory.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.databinding.ItemTransactionHistoryBinding;
import com.astro.sott.databinding.SubscriptionPackItemBinding;
import com.astro.sott.fragments.subscription.adapter.SubscriptionAdapter;
import com.astro.sott.fragments.subscription.ui.SubscriptionPacksFragment;
import com.astro.sott.fragments.transactionhistory.ui.TransactionHistory;

public class TransactionAdapter  extends RecyclerView.Adapter<TransactionAdapter.SingleItemHolder>{
    Fragment fragment;
    public TransactionAdapter(TransactionHistory ctx) {
        this.fragment=ctx;
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

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        final ItemTransactionHistoryBinding  binding;

        public SingleItemHolder(ItemTransactionHistoryBinding  itemBinding) {
            super(itemBinding.getRoot());
            this.binding=itemBinding;
        }
    }
}

