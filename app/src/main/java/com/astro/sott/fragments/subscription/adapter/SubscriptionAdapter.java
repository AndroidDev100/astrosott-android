package com.astro.sott.fragments.subscription.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.activities.search.adapter.LanguageAdapter;
import com.astro.sott.callBacks.commonCallBacks.CardCLickedCallBack;
import com.astro.sott.databinding.LanguagePreferenceItemBinding;
import com.astro.sott.databinding.SubcriptionPackageListItemBinding;
import com.astro.sott.databinding.SubscriptionPackItemBinding;
import com.astro.sott.fragments.subscription.ui.SubscriptionPacksFragment;
import com.astro.sott.usermanagment.modelClasses.getProducts.ProductsResponseMessageItem;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SingleItemHolder> {
    Fragment fragment;
    private List<ProductsResponseMessageItem> productsResponseMessageItems;
    private CardCLickedCallBack cardCLickedCallBack;

    public SubscriptionAdapter(SubscriptionPacksFragment ctx, List<ProductsResponseMessageItem> productsResponseMessage) {
        this.fragment = ctx;
        productsResponseMessageItems = productsResponseMessage;
        cardCLickedCallBack = (CardCLickedCallBack) ctx;
    }

    @NonNull
    @Override
    public SingleItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SubscriptionPackItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.subscription_pack_item, parent, false);
        return new SingleItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemHolder holder, int position) {
        holder.binding.packName.setText(productsResponseMessageItems.get(position).getProductName());
        StringBuilder description = new StringBuilder();
        if (productsResponseMessageItems.get(position).getDuration() != null && productsResponseMessageItems.get(position).getPeriod() != null) {
            description.append(productsResponseMessageItems.get(position).getDuration() + " " + productsResponseMessageItems.get(position).getPeriod());
        }
        if (productsResponseMessageItems.get(position).getRenewable() != null) {
            description.append(" recurring subscription");
        }
        holder.binding.packDescription.setText(description);
        holder.binding.cardLayout.setOnClickListener(v -> {
            cardCLickedCallBack.onCardClicked();
        });
    }

    @Override
    public int getItemCount() {
        return productsResponseMessageItems.size();
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        final SubscriptionPackItemBinding binding;

        public SingleItemHolder(SubscriptionPackItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
