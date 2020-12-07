package com.dialog.dialoggo.activities.myPlans.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.myPlans.models.SubscriptionPlanPackageModel;
import com.dialog.dialoggo.base.BaseViewHolder;

import java.util.List;

public class MyPlansAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mcontext;
    private List<SubscriptionPlanPackageModel> mPackageModelList;
    private long lastClickTime = 0;
    private int total_types;

    public MyPlansAdapter(Context context, List<SubscriptionPlanPackageModel> packageModelList) {
        this.mcontext = context;
        this.mPackageModelList = packageModelList;
        total_types = mPackageModelList.size();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case SubscriptionPlanPackageModel.SUBSCRIPTION_TITLE_TYPE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_payment_item, parent, false);
                return new TitleTypeViewHolder(view);
            case SubscriptionPlanPackageModel.SUBSCRIPTION_DESC_TYPE:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcription_package_list_item, parent, false);
                return new DescTypeViewHolder(view1);
            case SubscriptionPlanPackageModel.SUBSCRIPTION_CHANNEL_TYPE:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_package_channel_list_item, parent, false);
                return new ChannelTypeViewHolder(view2);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        SubscriptionPlanPackageModel subscriptionPlanPackageModel = mPackageModelList.get(position);
        switch (subscriptionPlanPackageModel.type) {
            case SubscriptionPlanPackageModel.SUBSCRIPTION_TITLE_TYPE:
                holder.onBind(position);
                break;
            case SubscriptionPlanPackageModel.SUBSCRIPTION_DESC_TYPE:
                holder.onBind(position);
                break;
            case SubscriptionPlanPackageModel.SUBSCRIPTION_CHANNEL_TYPE:
                holder.onBind(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mPackageModelList != null) {
            return mPackageModelList.size();
        }
        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        switch (mPackageModelList.get(position).type) {
            case 0:
                return SubscriptionPlanPackageModel.SUBSCRIPTION_TITLE_TYPE;
            case 1:
                return SubscriptionPlanPackageModel.SUBSCRIPTION_DESC_TYPE;
            case 2:
                return SubscriptionPlanPackageModel.SUBSCRIPTION_CHANNEL_TYPE;
            default:
                return -1;
        }
    }

    public class TitleTypeViewHolder extends BaseViewHolder {

        TextView txtTitle;

        public TitleTypeViewHolder(View itemView) {
            super(itemView);
            this.txtTitle = itemView.findViewById(R.id.paymentModeTitle);
        }

        @Override
        public void onBind(int position) {
            txtTitle.setText(mPackageModelList.get(position).getTitle());
        }
    }

    public class DescTypeViewHolder extends BaseViewHolder {

        TextView txtTitle, txtCharge,txtCharge1,txtPackageDescription,txtSubscriptionDate,txtExpiryDate;

        public DescTypeViewHolder(View itemView) {
            super(itemView);

            this.txtTitle = itemView.findViewById(R.id.txtTitle);
            this.txtCharge = itemView.findViewById(R.id.txtCharge);
            this.txtCharge1 = itemView.findViewById(R.id.txtCharge1);
            this.txtPackageDescription = itemView.findViewById(R.id.txtPackageDescription);
//            this.txtSubscriptionDate = itemView.findViewById(R.id.txtSubscriptionDate);
            this.txtExpiryDate = itemView.findViewById(R.id.txtExpiryDate);
        }

        @Override
        public void onBind(int position) {
            txtTitle.setText(mPackageModelList.get(position).getPackageTitle());
            txtCharge.setText(mPackageModelList.get(position).getCurrency());
            txtCharge1.setText(mPackageModelList.get(position).getPackageCharge());
            txtPackageDescription.setText(mPackageModelList.get(position).getPackageDesc());
            txtSubscriptionDate.setText(mPackageModelList.get(position).getPackagestartDate());
            txtExpiryDate.setText(mPackageModelList.get(position).getPackageEndDate());
        }
    }

    public class ChannelTypeViewHolder extends BaseViewHolder {

        TextView txtChannelTitle, txtChannelDesc;

        public ChannelTypeViewHolder(View itemView) {
            super(itemView);

            this.txtChannelTitle = itemView.findViewById(R.id.txtChannelTitle);
            this.txtChannelDesc = itemView.findViewById(R.id.txtChannelDesc);
        }

        @Override
        public void onBind(int position) {
            txtChannelTitle.setText(mPackageModelList.get(position).getChannelTitle());
            txtChannelDesc.setText(mPackageModelList.get(position).getChannelDesc());
        }
    }
}
