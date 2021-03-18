package com.astro.sott.fragments.manageSubscription.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.activities.manageDevice.adapter.ManageDeviceAdapter;
import com.astro.sott.callBacks.commonCallBacks.ChangePlanCallBack;
import com.astro.sott.databinding.ManageSubscriptionItemBinding;
import com.astro.sott.fragments.manageSubscription.ui.ManageSubscriptionFragment;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.AccountServiceMessageItem;
import com.astro.sott.utils.commonMethods.AppCommonMethods;

import java.util.List;

public class ManageSubscriptionAdapter extends RecyclerView.Adapter<ManageSubscriptionAdapter.SingleItem> {
    private List<AccountServiceMessageItem> accountServiceMessageItems;
    private ChangePlanCallBack changePlanCallBack;
    private Context mContext;

    public ManageSubscriptionAdapter(List<AccountServiceMessageItem> accountServiceMessage, Context context, ManageSubscriptionFragment subscriptionFragment) {
        accountServiceMessageItems = accountServiceMessage;
        mContext = context;
        changePlanCallBack = (ChangePlanCallBack) subscriptionFragment;
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
            holder.manageSubscriptionItemBinding.status.setText(accountServiceMessageItems.get(position).getStatus());
            holder.manageSubscriptionItemBinding.status.setBackgroundColor(mContext.getResources().getColor(R.color.green));

        } else if (accountServiceMessageItems.get(position).getStatus().equalsIgnoreCase("PENDING ACTIVE")) {
            holder.manageSubscriptionItemBinding.change.setVisibility(View.GONE);
            holder.manageSubscriptionItemBinding.status.setBackgroundColor(mContext.getResources().getColor(R.color.cancel_red_color));
            holder.manageSubscriptionItemBinding.status.setText("PENDING");
            holder.manageSubscriptionItemBinding.status.setTextColor(mContext.getResources().getColor(R.color.title_color));
            holder.manageSubscriptionItemBinding.cancel.setVisibility(View.GONE);

        } else {
            holder.manageSubscriptionItemBinding.cancel.setVisibility(View.GONE);
            holder.manageSubscriptionItemBinding.status.setTextColor(mContext.getResources().getColor(R.color.title_color));
            holder.manageSubscriptionItemBinding.status.setText(accountServiceMessageItems.get(position).getStatus());
            holder.manageSubscriptionItemBinding.change.setVisibility(View.GONE);
            holder.manageSubscriptionItemBinding.status.setBackgroundColor(mContext.getResources().getColor(R.color.cancel_red_color));
        }
        StringBuilder period = new StringBuilder();
        if (accountServiceMessageItems.get(position).getStartDate() != null)
            period.append("Period: " + AppCommonMethods.getDateFromTimeStamp(accountServiceMessageItems.get(position).getStartDate()));
        if (accountServiceMessageItems.get(position).getValidityTill() != null)
            period.append(" - " + AppCommonMethods.getDateFromTimeStamp(accountServiceMessageItems.get(position).getValidityTill()));

        holder.manageSubscriptionItemBinding.period.setText(period);

        if (accountServiceMessageItems.get(position).isRenewal()) {
            holder.manageSubscriptionItemBinding.renew.setVisibility(View.VISIBLE);
        } else {
            holder.manageSubscriptionItemBinding.renew.setVisibility(View.GONE);

        }
        holder.manageSubscriptionItemBinding.change.setOnClickListener(v -> {
            changePlanCallBack.onClick();
        });
        holder.manageSubscriptionItemBinding.cancel.setOnClickListener(v -> {
            if (accountServiceMessageItems.get(position).getServiceID() != null)
                changePlanCallBack.onCancel();
        });
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
