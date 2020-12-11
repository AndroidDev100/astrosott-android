package com.astro.sott.utils.helpers;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astro.sott.activities.moreListing.ui.ContinueWatchingActivity;
import com.astro.sott.activities.moreListing.ui.ListingActivity;
import com.astro.sott.R;
import com.astro.sott.activities.moreListing.ui.DetailListingActivity;
import com.astro.sott.activities.moreListing.ui.ListingActivityNew;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;

public class ToolBarHandler {
    private final Activity activity;
    private long lastClickTime;


    public ToolBarHandler(Activity context) {
        this.activity = context;
    }

    public void setMoreListener(LinearLayout more, final String type, final AssetCommonBean assetCommonBean) {

        more.setOnClickListener(view -> {
            AssetCommonBean assetCommonBean1 = new AssetCommonBean();
            assetCommonBean1.setID(assetCommonBean.getID());
            assetCommonBean1.setTitle(assetCommonBean.getTitle());
            assetCommonBean1.setMoreType(assetCommonBean.getMoreType());
            assetCommonBean1.setAsset(assetCommonBean.getRailAssetList().get(0).getObject());
            assetCommonBean1.setMoreID(assetCommonBean.getMoreID());
            assetCommonBean1.setMoreSeriesID(assetCommonBean.getMoreSeriesID());
            assetCommonBean1.setMoreAssetType(assetCommonBean.getMoreAssetType());
            assetCommonBean1.setMoreGenre(assetCommonBean.getMoreGenre());

            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            new ActivityLauncher(activity).listingActivityNew(activity, ListingActivityNew.class, assetCommonBean.getRailDetail().getCategory().getContentImageType(), assetCommonBean1);
        });
    }

    public void setMoreListener(TextView more, final String type, final AssetCommonBean assetCommonBean) {

        more.setOnClickListener(view -> {
            AssetCommonBean assetCommonBean1 = new AssetCommonBean();
            assetCommonBean1.setID(assetCommonBean.getID());
            assetCommonBean1.setTitle(assetCommonBean.getTitle());
            assetCommonBean1.setMoreType(assetCommonBean.getMoreType());
            assetCommonBean1.setAsset(assetCommonBean.getRailAssetList().get(0).getObject());
            assetCommonBean1.setMoreID(assetCommonBean.getMoreID());
            assetCommonBean1.setMoreSeriesID(assetCommonBean.getMoreSeriesID());
            assetCommonBean1.setMoreAssetType(assetCommonBean.getMoreAssetType());
            assetCommonBean1.setMoreGenre(assetCommonBean.getMoreGenre());

            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            new ActivityLauncher(activity).listingActivityNew(activity, ListingActivityNew.class, assetCommonBean.getRailDetail().getCategory().getContentImageType(), assetCommonBean1);
        });
    }



    public void setMorePromoListener( final String type, final AssetCommonBean assetCommonBean) {

            AssetCommonBean assetCommonBean1 = new AssetCommonBean();
            assetCommonBean1.setID(assetCommonBean.getID());
            assetCommonBean1.setTitle(assetCommonBean.getTitle());
            assetCommonBean1.setMoreType(assetCommonBean.getMoreType());
            assetCommonBean1.setMoreAssetType(assetCommonBean.getMoreAssetType());

            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            new ActivityLauncher(activity).portraitListing(activity, ListingActivity.class, type, assetCommonBean1);
    }
    public void setDetailMoreListener(LinearLayout more, final String type, final AssetCommonBean assetCommonBean) {

        more.setOnClickListener(view -> {
            AssetCommonBean assetCommonBean1 = new AssetCommonBean();
            assetCommonBean1.setID(assetCommonBean.getID());
            assetCommonBean1.setTitle(assetCommonBean.getTitle());
            assetCommonBean1.setMoreType(assetCommonBean.getMoreType());
            assetCommonBean1.setAsset(assetCommonBean.getRailAssetList().get(0).getObject());
            assetCommonBean1.setMoreID(assetCommonBean.getMoreID());
            assetCommonBean1.setMoreSeriesID(assetCommonBean.getMoreSeriesID());
            assetCommonBean1.setMoreAssetType(assetCommonBean.getMoreAssetType());
            assetCommonBean1.setMoreGenre(assetCommonBean.getMoreGenre());

            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            new ActivityLauncher(activity).detailListing(activity, DetailListingActivity.class, type, assetCommonBean1);
        });
    }

    public void setContinueWatchingListener(LinearLayout moreText, final String type, final AssetCommonBean assetCommonBean) {

        moreText.setOnClickListener(view -> new ActivityLauncher(activity).continueWatchingListing(activity, ContinueWatchingActivity.class, type, assetCommonBean));
    }

    public void setContinueWatchingListener(TextView moreText, final String type, final AssetCommonBean assetCommonBean) {

        moreText.setOnClickListener(view -> new ActivityLauncher(activity).continueWatchingListing(activity, ContinueWatchingActivity.class, type, assetCommonBean));
    }

    public void setAction(View toolbar, final String currentActivity) {
        if (currentActivity.equalsIgnoreCase("listing")) {
            ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
            ImageView homeIcon = toolbar.findViewById(R.id.home_icon);
//            ImageView notification_icon = toolbar.findViewById(R.id.notification_icon);
//            notification_icon.setVisibility(View.GONE);
            searchIcon.setVisibility(View.GONE);
            homeIcon.setBackgroundResource(R.drawable.ic_arrow_back_black_24dp);
            homeIcon.setOnClickListener(view -> activity.onBackPressed());


        }

    }


    public void setNotificationAction(View toolbar) {
        ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
        ImageView homeIcon = toolbar.findViewById(R.id.home_icon);
       // ImageView notification_icon = toolbar.findViewById(R.id.notification_icon);
        TextView title = toolbar.findViewById(R.id.toolbar_text);
        // title.setText("Notifications");
       // notification_icon.setVisibility(View.INVISIBLE);
        searchIcon.setVisibility(View.INVISIBLE);
        homeIcon.setBackgroundResource(R.drawable.ic_arrow_back_black_24dp);
        homeIcon.setOnClickListener(view -> activity.onBackPressed());
    }

    public void setAppSettingAction(Context context, View toolbar) {


        ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
        ImageView homeIcon = toolbar.findViewById(R.id.home_icon);
       // ImageView notification_icon = toolbar.findViewById(R.id.notification_icon);
        TextView title = toolbar.findViewById(R.id.toolbar_text);
        title.setText(context.getResources().getString(R.string.app_settings));
     //   notification_icon.setVisibility(View.INVISIBLE);
        searchIcon.setVisibility(View.INVISIBLE);
        homeIcon.setBackgroundResource(R.drawable.ic_arrow_back_black_24dp);
        homeIcon.setOnClickListener(view -> activity.onBackPressed());
    }

    public void setVideoQuality(Context context, View toolbar) {
        ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
        ImageView homeIcon = toolbar.findViewById(R.id.home_icon);
      //  ImageView notification_icon = toolbar.findViewById(R.id.notification_icon);
        TextView title = toolbar.findViewById(R.id.toolbar_text);
        title.setText(context.getResources().getString(R.string.video_quality));
     //   notification_icon.setVisibility(View.INVISIBLE);
        searchIcon.setVisibility(View.INVISIBLE);
        homeIcon.setBackgroundResource(R.drawable.ic_arrow_back_black_24dp);
        homeIcon.setOnClickListener(view -> activity.onBackPressed());
    }


    public void setWebViewSetting(Context context, View toolbar, String toolbarTitle) {


        ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
        ImageView homeIcon = toolbar.findViewById(R.id.home_icon);
      //  ImageView notification_icon = toolbar.findViewById(R.id.notification_icon);
        TextView title = toolbar.findViewById(R.id.toolbar_text);
        title.setText(toolbarTitle);
       // notification_icon.setVisibility(View.INVISIBLE);
        searchIcon.setVisibility(View.INVISIBLE);
        homeIcon.setBackgroundResource(R.drawable.ic_arrow_back_black_24dp);
        homeIcon.setOnClickListener(view -> activity.onBackPressed());
    }


    public void setAction(LinearLayout toolbar) {
        ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
        ImageView homeIcon = toolbar.findViewById(R.id.home_icon);
      //  ImageView notification_icon = toolbar.findViewById(R.id.notification_icon);
        TextView title = toolbar.findViewById(R.id.toolbar_text);
         title.setText("DTV Account");
      //  notification_icon.setVisibility(View.INVISIBLE);
        searchIcon.setVisibility(View.INVISIBLE);
        homeIcon.setBackgroundResource(R.drawable.ic_arrow_back_black_24dp);
        homeIcon.setOnClickListener(view -> activity.onBackPressed());
    }
}
