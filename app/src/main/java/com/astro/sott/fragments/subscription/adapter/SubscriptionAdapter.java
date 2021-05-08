package com.astro.sott.fragments.subscription.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.activities.movieDescription.ui.MovieDescriptionActivity;
import com.astro.sott.activities.search.adapter.LanguageAdapter;
import com.astro.sott.callBacks.commonCallBacks.CardCLickedCallBack;
import com.astro.sott.databinding.LanguagePreferenceItemBinding;
import com.astro.sott.databinding.SubcriptionPackageListItemBinding;
import com.astro.sott.databinding.SubscriptionPackItemBinding;
import com.astro.sott.fragments.subscription.ui.SubscriptionPacksFragment;
import com.astro.sott.modelClasses.InApp.PackDetail;
import com.astro.sott.usermanagment.modelClasses.getProducts.ProductsResponseMessageItem;
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
        if (packDetailList.get(position).getProductsResponseMessageItem().getSkuORQuickCode() != null && productList != null) {
            if (checkActiveOrNot(packDetailList.get(position).getProductsResponseMessageItem().getSkuORQuickCode())) {
                holder.binding.btnBuy.setText("SUBSCRIBED");
            } else {
                holder.binding.btnBuy.setText("BUY @ " + packDetailList.get(position).getSkuDetails().getPriceCurrencyCode() + " " + packDetailList.get(position).getSkuDetails().getPrice());
            }
        } else {
            holder.binding.btnBuy.setText("BUY @ " + packDetailList.get(position).getSkuDetails().getPriceCurrencyCode() + " " + packDetailList.get(position).getSkuDetails().getPrice());

        }

        holder.binding.packDescription.setText(description);
        holder.binding.btnBuy.setOnClickListener(v -> {
            if (UserInfo.getInstance(fragment).isActive()) {
                if (!holder.binding.btnBuy.getText().toString().equalsIgnoreCase("subscribed"))
                    cardCLickedCallBack.onCardClicked(packDetailList.get(position).getProductsResponseMessageItem().getAppChannels().get(0).getAppID(), packDetailList.get(position).getProductsResponseMessageItem().getServiceType());
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
