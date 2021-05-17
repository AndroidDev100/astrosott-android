package com.astro.sott.activities.myList.adapter;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.astro.sott.R;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.ItemClickListener;
import com.astro.sott.databinding.LandscapeItemBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.PrintLogging;
import com.kaltura.client.types.Asset;

import java.util.List;

public class MyWatchlistAdapter extends RecyclerView.Adapter<MyWatchlistAdapter.SingleItemRowHolder> {
    private final List<RailCommonData> railList;
    private final Activity mContext;
    private long lastClickTime = 0;
    VIUChannel channel;
    public MyWatchlistAdapter(Activity activity, List<RailCommonData> itemsList, VIUChannel channel) {
        this.railList = itemsList;
        this.mContext = activity;
        this.channel=channel;
    }

    @NonNull
    @Override
    public MyWatchlistAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LandscapeItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.landscape_item, viewGroup, false);

        return new SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyWatchlistAdapter.SingleItemRowHolder viewHolder, final int position) {
        try {
            RailCommonData singleItem = railList.get(position);
            Asset asset = singleItem.getObject();
            PrintLogging.printLog("", asset.getType() + "valuesOfListType");

            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                // holder.landscapeItemBinding.setImage(assetCommonImages);
                // Glide.with(mContext).load(assetCommonImages.getImageUrl()).into(holder.landscapeItemBinding.itemImage);
                ImageHelper.getInstance(viewHolder.watchlistItemBinding.itemImage.getContext()).loadImageToLandscape(viewHolder.watchlistItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.ic_landscape_placeholder);

            } else {
              //  ImageHelper.getInstance(landscapeItemBinding.itemImage.getContext()).loadImageToPlaceholder(landscapeItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.ic_landscape_placeholder, landscapeItemBinding.itemImage), R.drawable.ic_landscape_placeholder);

            }
          //  viewHolder.watchlistItemBinding.setTitle(singleItem);
          //  viewHolder.watchlistItemBinding.tvEpisode.setText(AssetContent.getWatchListGenre(asset.getTags()));

        } catch (Exception ignored) {

        }
    }


    @Override
    public int getItemCount() {
        return railList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final LandscapeItemBinding watchlistItemBinding;

        SingleItemRowHolder(LandscapeItemBinding binding) {
            super(binding.getRoot());
            this.watchlistItemBinding = binding;
            final String name = mContext.getClass().getSimpleName();
            // int layoutType= AppCommonMethods.getRailTypeAccToMedia(getLayoutPosition(),railList,i);
            watchlistItemBinding.getRoot().setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();

                new ActivityLauncher(mContext).railClickCondition("", "", name, railList.get(getLayoutPosition()), getLayoutPosition(), AppConstants.Rail5,railList, (_url, position, type, commonData) -> {
                    //detailRailClick.detailItemClicked(_url,position,type,commonData);
                });


            });

        }

    }


}

