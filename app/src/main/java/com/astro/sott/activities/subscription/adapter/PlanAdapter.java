package com.astro.sott.activities.subscription.adapter;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.activities.subscription.manager.AllChannelManager;
import com.astro.sott.beanModel.subscriptionmodel.SubscriptionModel;
import com.astro.sott.R;
import com.astro.sott.databinding.LayoutPackageSelectionItemBinding;

import java.text.DecimalFormat;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.SingleItemRowHolder> {

    private static final String TAG = "PlanAdapter";
    private List<SubscriptionModel> mPlanModelList;
    private int mCurrentSelectionPosition = -1;
    private final int DELAY_IN_MILLI_SEC_60 = 60;
    private PlanAdapterListener mListener;
    private long lastClickTime;


    public PlanAdapter(List<SubscriptionModel> planModelList, PlanAdapterListener listener) {
        this.mListener = listener;
        this.mPlanModelList = planModelList;
    }

    @NonNull
    @Override
    public PlanAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutPackageSelectionItemBinding itemBinding;
        itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_package_selection_item, parent, false);
        return new PlanAdapter.SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.SingleItemRowHolder holder, int position) {
        SubscriptionModel subscriptionModel = mPlanModelList.get(position);

        holder.itemBinding.txtPackageTitle.setText(subscriptionModel.getSubscription().getName());
        holder.itemBinding.txtPackageDescription.setText(subscriptionModel.getSubscription().getDescription());
        String currencySign = subscriptionModel.getSubscription().getPrice().getPrice().getCurrencySign();
        String finalresult = new DecimalFormat("#").format(subscriptionModel.getSubscription().getPrice().getPrice().getAmount());
        holder.itemBinding.txtPackageCost.setText(currencySign+" "+finalresult);


        if(subscriptionModel.isSelected()){
            mCurrentSelectionPosition = position;
            AllChannelManager.getInstance().setProductId(subscriptionModel.getSubscription().getId());
            AllChannelManager.getInstance().setCurrency(subscriptionModel.getSubscription().getPrice().getPrice().getCurrency());
            AllChannelManager.getInstance().setPrice(new DecimalFormat("#").format(subscriptionModel.getSubscription().getPrice().getPrice().getAmount()));
            AllChannelManager.getInstance().setPaymentTitle(subscriptionModel.getSubscription().getName());
            holder.itemBinding.rlPackageDescription.setVisibility(View.VISIBLE);
            holder.itemBinding.imvSelectedPackage.setVisibility(View.VISIBLE);
            holder.itemBinding.liPlanMainLayout.setBackgroundResource(R.drawable.plan_selected_bg);
        }else {
            holder.itemBinding.rlPackageDescription.setVisibility(View.GONE);
            holder.itemBinding.imvSelectedPackage.setVisibility(View.GONE);
            holder.itemBinding.liPlanMainLayout.setBackgroundResource(R.drawable.plan_un_selected_bg);
        }


        holder.itemBinding.rlPackageTitle.setOnClickListener(v -> {

            if(mCurrentSelectionPosition == position) {
                return;
            }

            if (SystemClock.elapsedRealtime() - lastClickTime < 600) {
                return;
            }

            lastClickTime = SystemClock.elapsedRealtime();



            new Handler().postDelayed(() -> {
                if(mCurrentSelectionPosition != -1){
                    mPlanModelList.get(mCurrentSelectionPosition).setSelected(false);
                    notifyItemChanged(mCurrentSelectionPosition);
                }
            }, DELAY_IN_MILLI_SEC_60);

            new Handler().postDelayed(() -> {
                mPlanModelList.get(position).setSelected(true);
                mCurrentSelectionPosition = position;
                notifyItemChanged(position);
            }, DELAY_IN_MILLI_SEC_60);


        });

        holder.itemBinding.txtViewAllChannel.setOnClickListener(v -> {
            mListener.openBottomSheet(true);
        });

        holder.itemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if(mPlanModelList != null) {
            return mPlanModelList.size();
        }
        return 0;
    }

    public static class SingleItemRowHolder extends RecyclerView.ViewHolder {
        final LayoutPackageSelectionItemBinding itemBinding;

        public SingleItemRowHolder(@NonNull LayoutPackageSelectionItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    public interface PlanAdapterListener {
        void openBottomSheet( boolean open);
    }
}
