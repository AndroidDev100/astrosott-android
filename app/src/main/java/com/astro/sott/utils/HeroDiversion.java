package com.astro.sott.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.liveChannel.ui.LiveChannel;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.activities.moreListing.ui.GridListingActivity;
import com.astro.sott.activities.moreListing.ui.ListingActivity;
import com.astro.sott.activities.movieDescription.ui.MovieDescriptionActivity;
import com.astro.sott.activities.search.ui.ActivitySearch;
import com.astro.sott.activities.signUp.ui.SignUpActivity;
import com.astro.sott.activities.splash.ui.SplashActivity;
import com.astro.sott.activities.splash.viewModel.SplashViewModel;
import com.astro.sott.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.astro.sott.activities.webview.ui.WebViewActivity;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;
import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory;
import com.enveu.Enum.LandingPageType;
import com.enveu.Enum.ListingLayoutType;
import com.enveu.Enum.PDFTarget;
import com.enveu.Enum.PredefinePlaylistType;
import com.kaltura.client.types.Asset;


public class HeroDiversion {

    private RailCommonData railCommonData;
    private AssetCommonBean commonData;
    private Context context;
    private Activity activity;
    private SplashViewModel homeViewModel;
    private VIUChannel category;

    public HeroDiversion(RailCommonData railCommonData, AssetCommonBean commonData, Context context, Activity activity, SplashViewModel homeViewModel) {
        this.railCommonData = railCommonData;
        this.commonData = commonData;
        this.context = context;
        this.activity = activity;
        this.homeViewModel = homeViewModel;
        this.category = railCommonData.getRailDetail();
    }


    public void heroClickRedirection() {
        String landingPageType = category.getLandingPageType();
        if (landingPageType != null) {
            if (landingPageType.equals(LandingPageType.DEF.name())) {
                Long videoId = 0l;
                progressCall();
                checkRedirectionForDef(category);
                /*
                if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(AppConstants.ContentType.EPISODE.name())) {
                   //TODO DetailScreen
                } else if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(AppConstants.ContentType.SERIES.name())) {
                    //TODO DetailScreen
                } else {
                    //TODO DetailScreen
                }*/
            } else if (landingPageType.equals(LandingPageType.AST.name())) {
                progressCall();
                checkRedirection(category);
            } else if (landingPageType.equals(LandingPageType.HTM.name())) {
                Intent webViewIntent = new Intent(activity, WebViewActivity.class);
                webViewIntent.putExtra(AppLevelConstants.WEB_VIEW_HEADING, category.getLandingPageTitle());
                webViewIntent.putExtra(AppLevelConstants.WEB_VIEW_URL, category.getHtmlLink());
                context.startActivity(webViewIntent);
            } else if (landingPageType.equals(LandingPageType.PDF.name())) {
                if (category.getLandingPagetarget() != null) {
                    if (category.getLandingPagetarget().equals(PDFTarget.LGN.name())) {
                        //TODO Open login page
                        new ActivityLauncher(activity).signupActivity(activity, SignUpActivity.class, "");
                    } else if (category.getLandingPagetarget().equals(PDFTarget.SRH.name())) {

                        //TODO Open search page
                        new ActivityLauncher(activity).searchActivity(activity, ActivitySearch.class);
                    } else if (category.getLandingPagetarget().equals(PDFTarget.RCG.name())) {
                    } else if (category.getLandingPagetarget().equals(PDFTarget.CLT.name())) {
                    } else if (category.getLandingPagetarget().equals(PDFTarget.CDT.name())) {
                    } else if (category.getLandingPagetarget().equals(PDFTarget.IFP.name())) {

                    }
                }

            } else if (landingPageType.equals(LandingPageType.PLT.name())) {
                moreRailClick(category, commonData);
            }
        }
    }

    public void moreRailClick(VIUChannel railCommonData, final AssetCommonBean assetCommonBean) {
        BaseCategory data = assetCommonBean.getRailDetail().getCategory();
        if (data != null) {

            if (data.getName() != null && data.getReferenceName() != null && (data.getReferenceName().equalsIgnoreCase(PredefinePlaylistType.CON_W.name()) || data.getReferenceName().equalsIgnoreCase("special_playlist"))) {
                // TODO Continue watching
                //  new ActivityLauncher(activity).continueWatchingListing(activity, ContinueWatchingActivity.class, AppConstants.TYPE5, assetCommonBean);
            } else if (data.getName() != null && data.getReferenceName() != null && (data.getReferenceName().equalsIgnoreCase(PredefinePlaylistType.MY_W.name()))) {
                // TODO Watchlist page
                boolean isActive = UserInfo.getInstance(activity).isActive();
                if (!isActive) {
                    new ActivityLauncher(activity).signupActivity(activity, SignUpActivity.class, "");
                } else {
                    //  new ActivityLauncher(activity).myWatchlist(activity, MyWatchlistActivity.class);
                }

            } else {
                if (data.getContentListinglayout() != null && !data.getContentListinglayout().equalsIgnoreCase("") && data.getContentListinglayout().equalsIgnoreCase(ListingLayoutType.LST.name())) {
                    try {
                        Log.e("getRailData", "LST");
                        String className = new KsPreferenceKey(context).getClassName();
                        AssetCommonBean assetCommonBean1 = new AssetCommonBean();
                        assetCommonBean1.setID(Long.parseLong(railCommonData.getLandingPagePlayListId()));
                        assetCommonBean1.setTitle(railCommonData.getLandingPageTitle());
                        assetCommonBean1.setMoreType(assetCommonBean.getMoreType());
                        assetCommonBean1.setAsset(assetCommonBean.getRailAssetList().get(0).getObject());
                        assetCommonBean1.setMoreID(assetCommonBean.getMoreID());
                        assetCommonBean1.setMoreSeriesID(assetCommonBean.getMoreSeriesID());
                        assetCommonBean1.setMoreAssetType(assetCommonBean.getMoreAssetType());
                        assetCommonBean1.setMoreGenre(assetCommonBean.getMoreGenre());
                        assetCommonBean1.setRailDetail(assetCommonBean.getRailDetail());
                        PrintLogging.printLog("", "className--" + className);
                        new ActivityLauncher(activity).gridListing(activity, GridListingActivity.class, data.getContentImageType(), assetCommonBean1);

                    } catch (Exception e) {

                    }


                } else if (data.getContentListinglayout() != null && !data.getContentListinglayout().equalsIgnoreCase("") && data.getContentListinglayout().equalsIgnoreCase(ListingLayoutType.GRD.name())) {
                    String className = new KsPreferenceKey(context).getClassName();
                    AssetCommonBean assetCommonBean1 = new AssetCommonBean();
                    assetCommonBean1.setID(assetCommonBean.getID());
                    assetCommonBean1.setTitle(assetCommonBean.getTitle());
                    assetCommonBean1.setMoreType(assetCommonBean.getMoreType());
                    assetCommonBean1.setAsset(assetCommonBean.getRailAssetList().get(0).getObject());
                    assetCommonBean1.setMoreID(assetCommonBean.getMoreID());
                    assetCommonBean1.setMoreSeriesID(assetCommonBean.getMoreSeriesID());
                    assetCommonBean1.setMoreAssetType(assetCommonBean.getMoreAssetType());
                    assetCommonBean1.setMoreGenre(assetCommonBean.getMoreGenre());
                    assetCommonBean1.setRailDetail(assetCommonBean.getRailDetail());
                    PrintLogging.printLog("", "className--" + className);
                    // dont get confused with name as potrait its for grid
                    new ActivityLauncher(activity).portraitListing(activity, ListingActivity.class, data.getContentImageType(), assetCommonBean1, assetCommonBean.getRailDetail().getCategory());
                } else {
                    try {
                        Log.e("getRailData", "PDF");
                        String className = new KsPreferenceKey(context).getClassName();
                        AssetCommonBean assetCommonBean1 = new AssetCommonBean();
                        assetCommonBean1.setID(Long.parseLong(railCommonData.getLandingPagePlayListId()));
                        assetCommonBean1.setTitle(railCommonData.getLandingPageTitle());
                        assetCommonBean1.setMoreType(100);
                        assetCommonBean1.setRailDetail(assetCommonBean.getRailDetail());
                        PrintLogging.printLog("", "className--" + className);
                        // dont get confused with name as potrait its for grid
                        new ActivityLauncher(activity).portraitListing(activity, ListingActivity.class, data.getContentImageType(), assetCommonBean1, assetCommonBean.getRailDetail().getCategory());

                    } catch (Exception ignored) {

                    }

                }
            }
        }

    }

    private void progressCall() {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });

        } catch (Exception ignored) {

        }
    }

    private void checkRedirectionForDef(VIUChannel railCommonDataa) {
        boolean prog = railCommonDataa.isProgram();
        if (prog) {
            progressCall();
            homeViewModel.getLiveSpecificAsset(activity, railCommonDataa.getManualImageAssetId()).observe((LifecycleOwner) activity, railCommonData -> {
                if (railCommonData.getStatus()) {
                    new ActivityLauncher(activity).checkCurrentProgram(railCommonData.getObject());
                }
            });
        } else {
            homeViewModel.getSpecificAsset(activity, railCommonDataa.getManualImageAssetId()).observe((LifecycleOwner) activity, asset -> {
                progressCall();
                if (asset.getStatus()) {
                    redirectionOnMediaType(asset, asset.getObject().getType());
                } else {
                }
            });
        }
    }


    private void checkRedirection(VIUChannel railCommonDataa) {
        boolean prog = railCommonDataa.isProgram();
        if (prog) {
            homeViewModel.getLiveSpecificAsset(activity, railCommonDataa.getLandingPageAssetId()).observe((LifecycleOwner) activity, railCommonData -> {
                if (railCommonData != null && railCommonData.getStatus()) {
                    new ActivityLauncher(activity).checkCurrentProgram(railCommonData.getObject());
                } else {
                    new ActivityLauncher(activity).homeActivity(activity, HomeActivity.class);
                }
            });
        } else {
            homeViewModel.getSpecificAsset(activity, railCommonDataa.getLandingPageAssetId()).observe((LifecycleOwner) activity, asset -> {
                if (asset.getStatus()) {
                    redirectionOnMediaType(asset, asset.getObject().getType());
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            });
        }

    }

    private void getProgramRailCommonData(Asset currentProgram, String program_videoItemClicked) {
        railCommonData = new RailCommonData();
        railCommonData.setObject(currentProgram);
    }

    private void redirectionOnMediaType(RailCommonData asset, int mediaType) {
        if (mediaType == MediaTypeConstant.getMovie(context)) {
            try {
                progressCall();
            } catch (Exception e) {
            }
            new ActivityLauncher(activity).detailActivity(activity, MovieDescriptionActivity.class, asset, AppConstants.Rail3);
        } else if (mediaType == MediaTypeConstant.getEpisode(context)) {
            try {
                progressCall();
            } catch (Exception e) {
            }
            new ActivityLauncher(activity).webDetailRedirection(asset, AppConstants.Rail5);


        } else if (mediaType == MediaTypeConstant.getSeries(context)) {
            try {
                progressCall();
            } catch (Exception e) {
            }
            new ActivityLauncher(activity).webSeriesActivity(activity, WebSeriesDescriptionActivity.class, asset, AppConstants.Rail5);
        } else if (mediaType == MediaTypeConstant.getTrailer(context)) {
           /* try {
                progressCall();
            } catch (Exception e) {
            }
            new ActivityLauncher(activity).detailActivity(activity, MovieDescriptionActivity.class, asset, AppConstants.Rail5);
        */
        } else if (mediaType == MediaTypeConstant.getLinear(context)) {
            try {
                progressCall();
            } catch (Exception e) {
            }
            new ActivityLauncher(activity).liveChannelActivity(activity, LiveChannel.class, asset);
        } else if (mediaType == MediaTypeConstant.getProgram(context)) {
            new ActivityLauncher(activity).checkCurrentProgram(asset.getObject());
        }

    }

    private void checkCurrentProgram(Asset object) {

    }

}
