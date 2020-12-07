package com.dialog.dialoggo.activities.subscription.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonImages;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.databinding.ViewAllChannelBottomSheetItemBinding;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.ImageHelper;

import java.util.List;

public class AllChannelGridAdapter extends RecyclerView.Adapter<AllChannelGridAdapter.SingleItemRowHolder> {

    private List<RailCommonData> mAllChannelList;

    public AllChannelGridAdapter(List<RailCommonData> allChannelList) {
        this.mAllChannelList = allChannelList;
    }

    @NonNull
    @Override
    public AllChannelGridAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewAllChannelBottomSheetItemBinding itemBinding;
        itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_all_channel_bottom_sheet_item, parent, false);
        return new AllChannelGridAdapter.SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllChannelGridAdapter.SingleItemRowHolder holder, int position) {

        RailCommonData singleItem = mAllChannelList.get(position);
        try {
            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                ImageHelper.getInstance(holder.itemBinding.imvChannelLogo.getContext()).loadImageTo(holder.itemBinding.imvChannelLogo, assetCommonImages.getImageUrl(), R.drawable.square1);
            } else {
                ImageHelper.getInstance(holder.itemBinding.imvChannelLogo.getContext()).loadImageTo(holder.itemBinding.imvChannelLogo, AppCommonMethods.getImageURI(R.drawable.square1, holder.itemBinding.imvChannelLogo), R.drawable.square1);
            }

        } catch (Exception e) {
        }

    }

    @Override
    public int getItemCount() {
        if(mAllChannelList != null) {
            return mAllChannelList.size();
        }
        return 0;
    }

    public static class SingleItemRowHolder extends RecyclerView.ViewHolder {
        final ViewAllChannelBottomSheetItemBinding itemBinding;

        public SingleItemRowHolder(@NonNull ViewAllChannelBottomSheetItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

}
