package com.astro.sott.fragments.manageSubscription.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.activities.manageDevice.adapter.ManageDeviceAdapter;
import com.astro.sott.databinding.ManageSubscriptionItemBinding;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.AccountServiceMessageItem;

import java.util.List;

public class ManageSubscriptionAdapter extends RecyclerView.Adapter<ManageSubscriptionAdapter.SingleItem> {
    private List<AccountServiceMessageItem> accountServiceMessageItems;

    public ManageSubscriptionAdapter(List<AccountServiceMessageItem> accountServiceMessage) {
        accountServiceMessageItems = accountServiceMessage;
    }

    @NonNull
    @Override
    public SingleItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ManageSubscriptionItemBinding manageSubscriptionItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.manage_subscription_item, parent, false);
        return new ManageSubscriptionAdapter.SingleItem(manageSubscriptionItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItem holder, int position) {
        holder.manageSubscriptionItemBinding.planName.setText(accountServiceMessageItems.get(position).getDisplayName());
        holder.manageSubscriptionItemBinding.currency.setText(accountServiceMessageItems.get(position).getCurrencyCode() + " " + accountServiceMessageItems.get(position).getPlanPrice());
        if (accountServiceMessageItems.get(position).getStatus().equalsIgnoreCase("ACTIVE")) {
            holder.manageSubscriptionItemBinding.change.setVisibility(View.VISIBLE);
        } else {
            holder.manageSubscriptionItemBinding.change.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return accountServiceMessageItems.size();
    }

    public class SingleItem extends RecyclerView.ViewHolder {
        ManageSubscriptionItemBinding manageSubscriptionItemBinding;

        public SingleItem(@NonNull ManageSubscriptionItemBinding itemView) {
            super(itemView.getRoot());
            manageSubscriptionItemBinding = itemView;
        }
    }
}
