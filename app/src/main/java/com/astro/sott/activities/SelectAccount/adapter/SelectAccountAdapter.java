package com.astro.sott.activities.SelectAccount.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.activities.SelectAccount.SelectAccountModel.BillingAccountTypeModel;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.R;
import com.astro.sott.callBacks.DTVItemClickListner;
import com.astro.sott.databinding.ItemHeaderBinding;

import java.util.List;

public class SelectAccountAdapter extends RecyclerView.Adapter<SelectAccountAdapter.SingleItemHolder> {

    private Activity activity;
    private AccountItemAdapter childAdapter;
    private List<BillingAccountTypeModel> channelList;
    private DTVItemClickListner dtvItemClickListner;


    public SelectAccountAdapter(Activity activity, List<BillingAccountTypeModel> data, DTVItemClickListner clickListner) {
        this.activity = activity;
        this.channelList = data;
        this.dtvItemClickListner = clickListner;

    }

    @NonNull
    @Override
    public SelectAccountAdapter.SingleItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ItemHeaderBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.item_header, viewGroup, false);

        return new SelectAccountAdapter.SingleItemHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemHolder holder, int position) {


//        if (channelList.get(position).getViewType().equalsIgnoreCase(AppLevelConstants.BROAD_BAND_HBB_ACCOUNT)) {
//            holder.accountItemBinding.headerTitle.setText(channelList.get(position).getViewType());
//            holder.accountItemBinding.moreText.setText(activity.getResources().getString(R.string.hbb_channel_text));
//        }else
        if(channelList.get(position).getViewType().equalsIgnoreCase(AppLevelConstants.MOBILE_ACCOUNT)){
            holder.accountItemBinding.headerTitle.setText(channelList.get(position).getViewType());
            holder.accountItemBinding.moreText.setText(activity.getResources().getString(R.string.mbb_channel_text));
        } else if(channelList.get(position).getViewType().equalsIgnoreCase(AppLevelConstants.DIALOG_TV)){
            holder.accountItemBinding.headerTitle.setText(channelList.get(position).getViewType());
            holder.accountItemBinding.moreText.setText(activity.getResources().getString(R.string.dtv_channel_text));
        }

        holder.accountItemBinding.myRecyclerView.setHasFixedSize(true);
        holder.accountItemBinding.myRecyclerView.setNestedScrollingEnabled(false);
        holder.accountItemBinding.myRecyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));

        childAdapter = new AccountItemAdapter(activity, channelList.get(position).getDetailListItems(), dtvItemClickListner);
        holder.accountItemBinding.myRecyclerView.setAdapter(childAdapter);


    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        final ItemHeaderBinding accountItemBinding;
        RelativeLayout linearLayout;

        SingleItemHolder(ItemHeaderBinding binding) {
            super(binding.getRoot());
            this.accountItemBinding = binding;


            //linearLayout = view.findViewById(R.id.item);

        }
    }
}
