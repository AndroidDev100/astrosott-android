package com.dialog.dialoggo.adapter;


import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import android.os.SystemClock;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonImages;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.callBacks.commonCallBacks.ContinueWatchingRemove;
import com.dialog.dialoggo.callBacks.commonCallBacks.DetailRailClick;
import com.dialog.dialoggo.callBacks.commonCallBacks.MediaTypeCallBack;
import com.dialog.dialoggo.databinding.TabContinueWatchingItemBinding;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;
import com.dialog.dialoggo.utils.helpers.ImageHelper;
import com.dialog.dialoggo.utils.helpers.PrintLogging;

import java.util.List;

public class TabsContinueWatchingAdapter extends RecyclerView.Adapter<TabsContinueWatchingAdapter.SingleItemRowHolder> {

    private final int layoutType;
    private final List<RailCommonData> itemsList;
    private final Activity mContext;
    private DetailRailClick detailRailClick;
    private int continueWatchingIndex = -1;
    private long lastClickTime = 0;


    public TabsContinueWatchingAdapter(Activity context, List<RailCommonData> itemsList, int type, ContinueWatchingRemove callBack, int cwIndex, String railName) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        continueWatchingIndex = cwIndex;
        try {
            this.detailRailClick = ((DetailRailClick) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int i) {

        TabContinueWatchingItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.tab_continue_watching_item, parent, false);
        return new SingleItemRowHolder(binding);

    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {
        RailCommonData singleItem = itemsList.get(i);
        try {
            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);

                PrintLogging.printLog(CommonSquareAapter.class,"",assetCommonImages.getImageUrl()+"assetassetaseetss");
                // holder.landscapeItemBinding.setImage(assetCommonImages);
                // Glide.with(mContext).load(assetCommonImages.getImageUrl()).into(holder.landscapeItemBinding.itemImage);
                ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadImageTocontinueWatchingListing(holder.landscapeItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.square1);
            } else {
                ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadImageToPlaceholder(holder.landscapeItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.square1, holder.landscapeItemBinding.itemImage), R.drawable.square1);
            }
            if (singleItem.getProgress() > 0) {
                holder.landscapeItemBinding.progressBar.setVisibility(View.VISIBLE);
                holder.landscapeItemBinding.progressBar.setProgress(singleItem.getPosition());
            } else {
                holder.landscapeItemBinding.progressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }

    }





    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        public final TabContinueWatchingItemBinding landscapeItemBinding;

        private SingleItemRowHolder(TabContinueWatchingItemBinding view) {
            super(view.getRoot());
            this.landscapeItemBinding = view;
            final String name = mContext.getClass().getSimpleName();

            landscapeItemBinding.getRoot().setOnClickListener(view1 -> {

                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                new ActivityLauncher(mContext).railClickCondition("","",name, itemsList.get(getLayoutPosition()),getLayoutPosition(), layoutType,itemsList, new MediaTypeCallBack() {
                    @Override
                    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

                    }
                });

            });
        }


    }


}
