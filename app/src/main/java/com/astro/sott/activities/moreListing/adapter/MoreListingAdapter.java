package com.astro.sott.activities.moreListing.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.ApplicationMain;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.ItemClickListener;
import com.astro.sott.databinding.MoreListingItemBinding;
import com.astro.sott.modelClasses.dmsResponse.MediaTypes;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.PrintLogging;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enveu.Enum.ImageType;
import com.kaltura.client.types.Asset;

import java.util.List;
import java.util.Random;

public class MoreListingAdapter extends RecyclerView.Adapter<MoreListingAdapter.SingleItemRowHolder> {
    private final List<RailCommonData> railList;
    private final Activity mContext;
    private long lastClickTime = 0;
    private String layout;
    private DisplayMetrics displaymetrics;
    private int num = 3;
    private int itemWidth;
    private int itemHeight;
    private boolean isTablet;

    private ResponseDmsModel responseDmsModel;
    private MediaTypes mediaTypes;

    public MoreListingAdapter(Activity activity, List<RailCommonData> itemsList, String layout, ItemClickListener listener) {
        this.railList = itemsList;
        this.mContext = activity;
        this.layout = layout;
        responseDmsModel = AppCommonMethods.callpreference(mContext);
        mediaTypes = responseDmsModel.getParams().getMediaTypes();
        displaymetrics = new DisplayMetrics();
        (mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        isTablet = mContext.getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            num = 8;
        }
        setImageSize();
    }

    @NonNull
    @Override
    public MoreListingAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        MoreListingItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.more_listing_item, viewGroup, false);

        return new MoreListingAdapter.SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull MoreListingAdapter.SingleItemRowHolder viewHolder, final int position) {
        try {
            RailCommonData singleItem = railList.get(position);
            Asset asset = singleItem.getObject();
            PrintLogging.printLog("", asset.getType() + "valuesOfListType");

            viewHolder.watchlistItemBinding.itemImageCircular.getLayoutParams().width = itemWidth;
            viewHolder.watchlistItemBinding.itemImageCircular.getLayoutParams().height = itemHeight;

            viewHolder.watchlistItemBinding.itemImage.getLayoutParams().width = itemWidth;
            viewHolder.watchlistItemBinding.itemImage.getLayoutParams().height = itemHeight;
            setImageDimension(viewHolder.watchlistItemBinding.itemImageCircular, viewHolder.watchlistItemBinding.itemImage);
            String imageURl = "";
            if (singleItem.getImages() != null && singleItem.getImages().size() > 0) {
                imageURl = setImageUrl(asset, layout);
                ImageHelper.getInstance(viewHolder.watchlistItemBinding.itemImage.getContext()).loadImageTo(viewHolder.watchlistItemBinding.itemImage, imageURl,R.drawable.landscape);
                viewHolder.watchlistItemBinding.setMUrl(imageURl);

            }


            if (layout.equalsIgnoreCase(ImageType.CIR.name())) {
                viewHolder.watchlistItemBinding.itemImageCircular.setVisibility(View.VISIBLE);
                viewHolder.watchlistItemBinding.itemImage.setVisibility(View.GONE);
                viewHolder.watchlistItemBinding.creatorLay.setVisibility(View.GONE);
                viewHolder.watchlistItemBinding.itemImageCircular.setVisibility(View.VISIBLE);
                viewHolder.watchlistItemBinding.itemImage.setVisibility(View.GONE);


                setCircularImage(viewHolder.watchlistItemBinding.itemImageCircular, imageURl);

            } else {
                viewHolder.watchlistItemBinding.creatorLay.setVisibility(View.GONE);
                viewHolder.watchlistItemBinding.itemImageCircular.setVisibility(View.GONE);
                viewHolder.watchlistItemBinding.itemImage.setVisibility(View.VISIBLE);
            }


            viewHolder.watchlistItemBinding.setTitle(asset);
            viewHolder.watchlistItemBinding.tvEpisode.setText(AssetContent.getWatchListGenre(asset.getTags()));
            try {
                mediaTypeCondition(position, viewHolder.watchlistItemBinding);
            } catch (Exception e) {

            }


        } catch (Exception ignored) {

        }
    }

    private void mediaTypeCondition(int position, MoreListingItemBinding potraitItemBinding) {
        if (Integer.parseInt(mediaTypes.getMovie()) == railList.get(0).getType() || Integer.parseInt(mediaTypes.getSeries()) == railList.get(0).getType() || Integer.parseInt(mediaTypes.getCollection()) == railList.get(0).getType()) {
            potraitItemBinding.tvName.setText(railList.get(position).getName());

        }
        else if (Integer.parseInt(mediaTypes.getEpisode()) == railList.get(0).getType()) {
            potraitItemBinding.tvName.setText("E" + 1 + " | " + railList.get(position).getName());

        }
    }


    public void setCircularImage(ImageView imageView, String imageURl) {
        Glide.with(mContext)
                .load(imageURl)
                .apply(new RequestOptions().placeholder(R.drawable.landscape).error(R.drawable.landscape))
                .into(imageView);

    }


    public String setImageUrl(Asset asset, String type) {
        String landscapeUrl = "";
        String potraitUrl = "";
        String squareUrl = "";
        if (asset.getImages().size() > 0) {
            for (int i = 0; i < asset.getImages().size(); i++) {
                if (type.equalsIgnoreCase("LDS")){
                    if (asset.getImages().get(i).getRatio().equals("16x9")) {
                        String image_url =asset.getImages().get(i).getUrl();
                        landscapeUrl = image_url + AppConstants.WIDTH + (int) mContext.getResources().getDimension(R.dimen.landscape_image_width) + AppConstants.HEIGHT + (int) mContext.getResources().getDimension(R.dimen.landscape_image_height) + AppConstants.QUALITY;
                    }
                }
                if (type.equalsIgnoreCase("PR2")){
                    if (asset.getImages().get(i).getRatio().equals("2x3")) {
                        String image_url =asset.getImages().get(i).getUrl();
                        potraitUrl = image_url;//image_url + AppConstants.WIDTH + (int) ApplicationMain.getAppContext().getResources().getDimension(R.dimen.landscape_image_width) + AppConstants.HEIGHT + (int) ApplicationMain.getAppContext().getResources().getDimension(R.dimen.landscape_image_height) + AppConstants.QUALITY;
                    }
                }

            }
        }


        switch (type) {
            case "PR1":
            case "PR2":
                return potraitUrl;
            case "LDS":
                return landscapeUrl;
            case "CIR":
            case "SQR":
                return squareUrl;
            default:
                return landscapeUrl;
        }


    }


    public void setImageDimension(ImageView circular, ImageView other) {
        circular.getLayoutParams().width = itemWidth;
        circular.getLayoutParams().height = itemHeight;

        other.getLayoutParams().width = itemWidth;
        other.getLayoutParams().height = itemHeight;


    }

    public void setImageSize() {
         if (layout.equalsIgnoreCase(ImageType.LDS.name())) {
            if (isTablet)
                num = 5;
            itemWidth = (displaymetrics.widthPixels - (int) mContext.getResources().getDimension(R.dimen.extraSpacingLDS)) / num;
            itemHeight = (int) (itemWidth / 16) * 9;
        }
        if (layout.equalsIgnoreCase(ImageType.PR2.name())) {

            itemWidth = (displaymetrics.widthPixels - (int) mContext.getResources().getDimension(R.dimen.extraSpacingLDS)) / num;
            itemHeight = (int) (itemWidth / 2) * 3;
        }
    }



    @Override
    public int getItemCount() {
        return railList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final MoreListingItemBinding watchlistItemBinding;

        SingleItemRowHolder(MoreListingItemBinding binding) {
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
