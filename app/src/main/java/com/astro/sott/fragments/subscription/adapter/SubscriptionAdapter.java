package com.astro.sott.fragments.subscription.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.callBacks.commonCallBacks.CardCLickedCallBack;
import com.astro.sott.databinding.SubscriptionPackItemBinding;
import com.astro.sott.modelClasses.InApp.PackDetail;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.userInfo.UserInfo;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SingleItemHolder> {
    Activity fragment;
    private List<PackDetail> packDetailList;
    private CardCLickedCallBack cardCLickedCallBack;
    private List<String> productList;

    public SubscriptionAdapter(Activity ctx, List<PackDetail> productsResponseMessage, List<String> productList) {
        this.fragment = ctx;
        packDetailList = productsResponseMessage;
        cardCLickedCallBack = (CardCLickedCallBack) ctx;
        this.productList = productList;
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
        holder.binding.packName.setText(packDetailList.get(position).getProductsResponseMessageItem().getDisplayName());
        StringBuilder description = new StringBuilder();
        if (packDetailList.get(position).getProductsResponseMessageItem().getDuration() != null && packDetailList.get(position).getProductsResponseMessageItem().getPeriod() != null) {
            description.append(packDetailList.get(position).getProductsResponseMessageItem().getDuration() + " " + packDetailList.get(position).getProductsResponseMessageItem().getPeriod());
        }
        if (packDetailList.get(position).getProductsResponseMessageItem().getRenewable() != null && packDetailList.get(position).getProductsResponseMessageItem().getRenewable()) {
            description.append(" recurring subscription");
        }

        if (packDetailList.get(position).getSkuDetails().getIntroductoryPricePeriod() != null && !packDetailList.get(position).getSkuDetails().getIntroductoryPricePeriod().equalsIgnoreCase("")) {
            holder.binding.introductoryPrice.setText("BUY @ " + packDetailList.get(position).getSkuDetails().getPriceCurrencyCode() + " " + packDetailList.get(position).getSkuDetails().getIntroductoryPrice());
            holder.binding.actualPrice.setText(packDetailList.get(position).getSkuDetails().getPriceCurrencyCode() + " " + packDetailList.get(position).getSkuDetails().getPrice());
            holder.binding.actualPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.binding.actualPrice.setVisibility(View.VISIBLE);
            if (packDetailList.get(position).getProductsResponseMessageItem().getPromotions() != null && packDetailList.get(position).getProductsResponseMessageItem().getPromotions().size() > 0 && packDetailList.get(position).getProductsResponseMessageItem().getPromotions().get(0) != null && packDetailList.get(position).getProductsResponseMessageItem().getPromotions().get(0).getPromoDescrip() != null) {
                holder.binding.specialText.setText(packDetailList.get(position).getProductsResponseMessageItem().getPromotions().get(0).getPromoDescrip());
                holder.binding.specialLay.setVisibility(View.VISIBLE);
            }
        } else {
            holder.binding.actualPrice.setVisibility(View.GONE);
            if (packDetailList.get(position).getProductsResponseMessageItem().getSkuORQuickCode() != null && productList != null) {
                if (checkActiveOrNot(packDetailList.get(position).getProductsResponseMessageItem().getSkuORQuickCode())) {
                    holder.binding.introductoryPrice.setText("SUBSCRIBED");
                } else {
                    holder.binding.introductoryPrice.setText("BUY @ " + packDetailList.get(position).getSkuDetails().getPriceCurrencyCode() + " " + packDetailList.get(position).getSkuDetails().getPrice());
                }
            } else {
                holder.binding.introductoryPrice.setText("BUY @ " + packDetailList.get(position).getSkuDetails().getPriceCurrencyCode() + " " + packDetailList.get(position).getSkuDetails().getPrice());

            }
        }

        holder.binding.packDescription.setText(description);
        holder.binding.btnBuy.setOnClickListener(v -> {
            if (UserInfo.getInstance(fragment).isActive()) {
                if (!holder.binding.actualPrice.getText().toString().equalsIgnoreCase("subscribed"))
                    cardCLickedCallBack.onCardClicked(packDetailList.get(position).getProductsResponseMessageItem().getAppChannels().get(0).getAppID(), packDetailList.get(position).getProductsResponseMessageItem().getServiceType(), null);
            } else {
                new ActivityLauncher(fragment).astrLoginActivity(fragment, AstrLoginActivity.class, "");
            }
        });


    }

    private boolean checkActiveOrNot(String skuORQuickCode) {
        boolean matched = false;
        for (String productId : productList) {
            matched = productId.equalsIgnoreCase(skuORQuickCode);
        }
        return matched;
    }

    @Override
    public int getItemCount() {
        return packDetailList.size();
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        final SubscriptionPackItemBinding binding;

        public SingleItemHolder(SubscriptionPackItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
