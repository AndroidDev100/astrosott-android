package com.astro.sott.fragments.episodeFrament;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.EpisodeClickListener;
import com.astro.sott.databinding.EpisodeItemBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.Asset;


import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.SingleItemRowHolder> {
    private final List<RailCommonData> railList;
    private final Activity mContext;
    private int episodeNumber = 0;
    EpisodeClickListener itemClickListener;
    KsPreferenceKey ksPreferenceKeys;
    private long lastClickTime = 0;
    boolean isLoggedIn = false;

    public EpisodeAdapter(Activity activity, List<RailCommonData> itemsList, int episodeNumber, EpisodeClickListener episodeClickListener) {
        this.railList = itemsList;
        this.mContext = activity;
        this.itemClickListener = episodeClickListener;
        ksPreferenceKeys = new KsPreferenceKey(mContext);
        isLoggedIn = ksPreferenceKeys.getUserActive();
        this.episodeNumber = episodeNumber;
    }

    @NonNull
    @Override
    public EpisodeAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        EpisodeItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.episode_item, viewGroup, false);

        return new EpisodeAdapter.SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull EpisodeAdapter.SingleItemRowHolder viewHolder, final int position) {
        try {
            RailCommonData singleItem = railList.get(position);
            Asset asset = singleItem.getObject();
            PrintLogging.printLog("", asset.getType() + "valuesOfListType");

            if (singleItem.getImages() != null && singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                ImageHelper.getInstance(viewHolder.watchlistItemBinding.itemImage.getContext()).loadImageTo(viewHolder.watchlistItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.landscape);
            }
            viewHolder.watchlistItemBinding.tvTitle.setText(singleItem.getName());
            viewHolder.watchlistItemBinding.tvDescription.setText(asset.getDescription());
            int value = AppCommonMethods.getEpisodeNumber(asset.getMetas());
            if (value == episodeNumber) {
                RailCommonData singleItems = railList.get(position);
                if (singleItems.getExpended()) {
                    singleItems.setExpended(false);
                    railList.remove(position);
                    railList.add(position, singleItems);
                    //AppCommonMethods.collapse(viewHolder.watchlistItemBinding.clRoot,1000,200);
                    viewHolder.watchlistItemBinding.clRoot.setVisibility(View.GONE);
                } else {
                    singleItems.setExpended(true);
                    railList.remove(position);
                    railList.add(position, singleItems);
                    viewHolder.watchlistItemBinding.clRoot.setVisibility(View.VISIBLE);
                    // itemClickListener.expendedView(position);
                    //AppCommonMethods.expand(viewHolder.watchlistItemBinding.clRoot,1000,200);
                }
            }
            viewHolder.watchlistItemBinding.episodeNumber.setText("Episode " + value + "");
            if (singleItem.getImages() != null && singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                ImageHelper.getInstance(viewHolder.watchlistItemBinding.landscapeImage.getContext()).loadImageTo(viewHolder.watchlistItemBinding.landscapeImage, assetCommonImages.getImageUrl(), R.drawable.landscape);
            }
           /* if (singleItem.getExpended()) {
                viewHolder.watchlistItemBinding.clRoot.setVisibility(View.VISIBLE);
            } else {
                viewHolder.watchlistItemBinding.clRoot.setVisibility(View.GONE);
            }*/


            viewHolder.watchlistItemBinding.downloadIcon.setVisibility(View.VISIBLE);


            viewHolder.watchlistItemBinding.mainLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RailCommonData singleItem = railList.get(position);
                    if (singleItem.getExpended()) {
                        singleItem.setExpended(false);
                        railList.remove(position);
                        railList.add(position, singleItem);
                        //AppCommonMethods.collapse(viewHolder.watchlistItemBinding.clRoot,1000,200);
                        viewHolder.watchlistItemBinding.clRoot.setVisibility(View.GONE);
                        //itemClickListener.onClick(position);
                    } else {
                        singleItem.setExpended(true);
                        railList.remove(position);
                        railList.add(position, singleItem);
                        viewHolder.watchlistItemBinding.clRoot.setVisibility(View.VISIBLE);
                        //AppCommonMethods.expand(viewHolder.watchlistItemBinding.clRoot,1000,200);
                    }

                }
            });
            viewHolder.watchlistItemBinding.playLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.moveToPlay(position, railList.get(position), 0);
                }
            });

        } catch (Exception e) {
            Log.w("FSEFESFSEFESFESFESFSEF", e + "");
        }
    }


    @Override
    public int getItemCount() {
        return railList.size();
    }

    int itemAdded;
    //typeofdownload 1=single download 0=series download
    int typeOfDownload = 100;

    public void updateIcons(int i, int typeofDownload) {
        this.itemAdded = i;
        this.typeOfDownload = typeofDownload;
        PrintLogging.printLog("", "episodeAdapterValues" + itemAdded + "<---->" + typeOfDownload);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final EpisodeItemBinding watchlistItemBinding;

        SingleItemRowHolder(EpisodeItemBinding binding) {
            super(binding.getRoot());
            this.watchlistItemBinding = binding;
            final String name = mContext.getClass().getSimpleName();
            // int layoutType= AppCommonMethods.getRailTypeAccToMedia(getLayoutPosition(),railList,i);
           /* watchlistItemBinding.getRoot().setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                GAManager.getInstance().setEvent(GAManager.ENAGAGEMENT, GAManager.CLICK_VIDEO_WATCHLIST_PAGE, GAManager.ENGAGE_WATCHLIST, GAManager.TWENTY);
                new ActivityLauncher(mContext).railClickCondition("", "", name, railList.get(getLayoutPosition()), getLayoutPosition(), AppConstants.Rail5, (_url, position, type, commonData) -> {
                    //detailRailClick.detailItemClicked(_url,position,type,commonData);
                });


            });*/

        }

    }


}

