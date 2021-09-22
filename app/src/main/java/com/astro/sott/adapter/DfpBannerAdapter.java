package com.astro.sott.adapter;

import android.content.Context;
import android.graphics.Color;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.utils.helpers.NavigationItem;
import com.astro.sott.utils.helpers.StringUtils;
import com.astro.sott.BuildConfig;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.databinding.RowDfpBannerBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.enveu.Enum.Layouts;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class DfpBannerAdapter extends RecyclerView.Adapter<DfpBannerAdapter.ViewHolder> {

    private final Context mContext;
    private AssetCommonBean item;
    private String deviceId;
    ArrayList<String> DFP_DETAILS =new ArrayList<String>();
    private PublisherAdRequest adRequest;
    private int bannerType = AppConstants.ADS_BANNER;

    public DfpBannerAdapter(Context context, AssetCommonBean item) {
        this.mContext = context;
        this.item = item;
        if (item.getRailDetail() != null)
            this.bannerType = item.getRailDetail().getWidgetType();
        String android_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceId = md5(android_id).toUpperCase();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RowDfpBannerBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_dfp_banner, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        try {
          DFP_DETAILS   = AppCommonMethods.splitString(item.getFanPlacementId(), "#");
            holder.rowDfpBannerBinding.bannerRoot.setVisibility(View.GONE);
        }catch (NullPointerException e){
            Log.d("NULLEXCEPTION", e+"");

        }
        // Banner ads detail should be in below FORMAT
        //DFP#Android#Phone#Banner#/21805729337/Watcho_Android/Watcho_Aos_app_Prem_ATF
        // /21805729337/Watcho_iOS/Watcho_iOS_app_LiveTV_ATF  /21805729337/Watcho_iOS/Watcho_iOS_app_LiveTV_ATF
        if (NetworkConnectivity.isOnline(mContext)) {
            PrintLogging.printLog("DfpBannerAdapter", "DfpBannerAdapter" + item.getFanPlacementId());
            try {
                if (DFP_DETAILS != null && DFP_DETAILS.size() >= 1 && !StringUtils.isNullOrEmptyOrZero(DFP_DETAILS.get(1))) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (holder).rowDfpBannerBinding.adMobView.getLayoutParams();
                    params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    RelativeLayout adContainer = holder.rowDfpBannerBinding.adMobView;
                    adContainer.setBackgroundColor(Color.parseColor("#00000000"));

                    PublisherAdView adView = new PublisherAdView(mContext);
                    adView.setAdSizes(fetchBannerSize(bannerType));
                    adView.setBackgroundColor(Color.parseColor("#00000000"));
                    //list 5th item is ads id
                    //PrintLogging.printLog("TestingAds",  DFP_DETAILS.get(1));
                    adView.setAdUnitId(DFP_DETAILS.get(1));
                    adView.setLayoutParams(params);
                    adContainer.addView(adView);

                    if (BuildConfig.FLAVOR.equalsIgnoreCase("QA")) {
                        if (NavigationItem.getInstance().getTab() != null) {
                            adRequest = new PublisherAdRequest.Builder().addCustomTargeting("pg", NavigationItem.getInstance().getTab())
                                    .addTestDevice(deviceId).build();
                        } else {
                            adRequest = new PublisherAdRequest.Builder().addTestDevice(deviceId).build();
                        }
                    } else {
                        if (NavigationItem.getInstance().getTab() != null) {
                            adRequest = new PublisherAdRequest.Builder().addCustomTargeting("pg", NavigationItem.getInstance().getTab())
                                    .build();
                        } else {
                            adRequest = new PublisherAdRequest.Builder().addTestDevice(deviceId).build();
                        }
                    }
                    adView.loadAd(adRequest);

                    adView.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            PrintLogging.printLog("", "AdsLoaded-->" + "success");
                            holder.rowDfpBannerBinding.bannerRoot.setVisibility(View.VISIBLE);
                            // Code to be executed when an ad finishes loading.
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Code to be executed when an ad request fails.
                            holder.rowDfpBannerBinding.bannerRoot.setVisibility(View.GONE);
                            PrintLogging.printLog("DfpBannerAdapter", "onAdFailedToLoad" + errorCode);

                        }

                        @Override
                        public void onAdOpened() {
                            // Code to be executed when an ad opens an overlay that
                            // covers the screen.
                        }

                        @Override
                        public void onAdClicked() {
                            // Code to be executed when the user clicks on an ad.
                        }

                        @Override
                        public void onAdLeftApplication() {
                            // Code to be executed when the user has left the app.
                        }

                        @Override
                        public void onAdClosed() {
                            // Code to be executed when the user is about to return
                            // to the app after tapping on an ad.
                        }
                    });

                }

            } catch (Exception e) {

                PrintLogging.printLog("DfpBannerAdapter", "onBindViewHolder" + item.getFanPlacementId().toLowerCase());

            }
        }

    }


    public AdSize fetchBannerSize(int bannerType) {
        //default adSize is Banner
        AdSize adSize;

        if (item.getRailDetail().getLayout().equalsIgnoreCase(Layouts.MRC.name())) {
            adSize = AdSize.MEDIUM_RECTANGLE;
        } else if (item.getRailDetail().getLayout().equalsIgnoreCase(Layouts.CUS.name())) {
            Log.w("heightWidth", item.getRailDetail().getAdWidth() + "   " + item.getRailDetail().getAdHeight());
            adSize = new AdSize(item.getRailDetail().getAdWidth(), item.getRailDetail().getAdHeight());
        } else if (item.getRailDetail().getLayout().equalsIgnoreCase(Layouts.BAN.name())) {
            if (AppCommonMethods.isTablet) {
                float smallestWidth = getTabSize();
                if (smallestWidth > 600) {
                    //Device is a 7" tablet
                    adSize = AdSize.LEADERBOARD;
                } else {
                    adSize = AdSize.FULL_BANNER;
                }
            } else {
                adSize = AdSize.BANNER;
            }

        } else {
            adSize = AdSize.BANNER;
        }


        return adSize;

    }

    private float getTabSize() {
        //  DisplayMetrics metrics = new DisplayMetrics();
        //  AppCommonMethods.windowManager.getDefaultDisplay().getMetrics(metrics);
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();


        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;
        float smallestWidth = Math.min(widthDp, heightDp);

        return smallestWidth;
    }

    private String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final RowDfpBannerBinding rowDfpBannerBinding;

        ViewHolder(RowDfpBannerBinding squareItemBind) {
            super(squareItemBind.getRoot());
            rowDfpBannerBinding = squareItemBind;

        }

    }


}

