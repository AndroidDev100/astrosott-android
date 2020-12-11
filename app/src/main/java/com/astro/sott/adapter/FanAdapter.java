package com.astro.sott.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.callBacks.commonCallBacks.RemoveAdsCallBack;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.databinding.RowFanBannerBinding;
import com.astro.sott.utils.helpers.PrintLogging;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeBannerAd;
import com.facebook.ads.NativeBannerAdView;



public class FanAdapter extends RecyclerView.Adapter<FanAdapter.ViewHolder> {

    private final Context mContext;
    private AssetCommonBean item;
    private RemoveAdsCallBack removeAdsCallBack;
    private int position;


    public FanAdapter(Context context, AssetCommonBean item, RemoveAdsCallBack removeCallBack, int position) {
        this.mContext = context;
        this.item = item;
        this.removeAdsCallBack = removeCallBack;
        this.position = position;
    }

    @NonNull
    @Override
    public FanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RowFanBannerBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_fan_banner, parent, false);
        return new FanAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FanAdapter.ViewHolder holder, int i) {

        NativeBannerAd nativeAd;
        String placementId = item.getFanPlacementId();

        if (placementId != null) {
            if (!placementId.equalsIgnoreCase("")) {
                nativeAd = new NativeBannerAd(mContext, placementId);
                nativeAd.setAdListener(new NativeAdListener() {
                    @Override
                    public void onMediaDownloaded(Ad ad) {

                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        PrintLogging.printLog("", "FBAdsError  " + adError.getErrorMessage());
                        holder.itemBinding.relay.setVisibility(View.GONE);
                        removeAddOnFailure(position);
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {

                        // Check if nativeBannerAd has been loaded successfully
                        // Race condition, load() called again before last ad was displayed
                        if (nativeAd == null || !nativeAd.isAdLoaded()) {
                            return;
                        }
                        PrintLogging.printLog("", "FBAdsError  loaded" + ad.getPlacementId());
                        boolean tabletSize = mContext.getResources().getBoolean(R.bool.isTablet);
                        View adView;
                        if (tabletSize) {

                            NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes()
                                    .setBackgroundColor(mContext.getResources().getColor(R.color.primaryColorDarkBlack))
                                    .setTitleTextColor(Color.WHITE)
                                    .setButtonBorderColor(mContext.getResources().getColor(R.color.sapphire))
                                    .setButtonTextColor(Color.WHITE)
                                    .setButtonColor(mContext.getResources().getColor(R.color.sapphire));

                            adView = NativeBannerAdView.render(mContext, nativeAd, NativeBannerAdView.Type.HEIGHT_100, viewAttributes);
                            // Add the Native Banner Ad View to your ad container
                            holder.itemBinding.relay.addView(adView);
                            holder.itemBinding.relay.setVisibility(View.VISIBLE);

                        } else {

                            NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes()
                                    .setBackgroundColor(mContext.getResources().getColor(R.color.primaryColorDarkBlack))
                                    .setTitleTextColor(Color.WHITE)
                                    .setButtonBorderColor(mContext.getResources().getColor(R.color.sapphire))
                                    .setButtonTextColor(Color.WHITE)
                                    .setButtonColor(mContext.getResources().getColor(R.color.sapphire));

                            adView = NativeBannerAdView.render(mContext, nativeAd, NativeBannerAdView.Type.HEIGHT_50, viewAttributes);
                            // Add the Native Banner Ad View to your ad container
                            holder.itemBinding.relay.addView(adView);
                            holder.itemBinding.relay.setVisibility(View.VISIBLE);
                        }

                        // View adView = NativeAdView.render(MainActivity.this, nativeAd, NativeAdView.Type.HEIGHT_300, viewAttributes);

                    }

                    @Override
                    public void onAdClicked(Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                });

                nativeAd.loadAd();
            }
        }

    }

    private void removeAddOnFailure(int position) {
        removeAdsCallBack.removeAdOnFailure(position);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final RowFanBannerBinding itemBinding;

        ViewHolder(RowFanBannerBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;

        }

    }


}

