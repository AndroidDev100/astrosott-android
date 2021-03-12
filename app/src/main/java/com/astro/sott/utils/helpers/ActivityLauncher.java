package com.astro.sott.utils.helpers;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.astro.sott.activities.boxSet.ui.BoxSetDetailActivity;
import com.astro.sott.activities.catchUpRails.ui.CatchupActivity;
import com.astro.sott.activities.forgotPassword.ui.ForgotPasswordActivity;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.activities.moreListing.ui.GridListingActivity;
import com.astro.sott.activities.myplaylist.ui.MyPlaylist;
import com.astro.sott.R;
import com.astro.sott.activities.SelectAccount.UI.SelectDtvAccountActivity;
import com.astro.sott.activities.accountSettings.ui.AccountSettingsActivity;
import com.astro.sott.activities.addDTVAccountNumber.UI.addDTVAccountNumberActivity;
import com.astro.sott.activities.changePaymentMethod.ui.ChangePaymentMethodActivity;
import com.astro.sott.activities.changePaymentMethod.ui.UpdatePaymentMethod;
import com.astro.sott.activities.crWebViewActivity.CrWebViewActivity;
import com.astro.sott.activities.deviceMangment.ui.DeviceManagementActivity;
import com.astro.sott.activities.dtvActivity.UI.dtvActivity;
import com.astro.sott.activities.forwardEPG.ForwardedEPGActivity;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.liveChannel.liveChannelManager.LiveChannelManager;
import com.astro.sott.activities.liveChannel.ui.LiveChannel;
import com.astro.sott.activities.loginActivity.LoginActivity;
import com.astro.sott.activities.mbbaccount.ui.AddMBBAccountActivity;
import com.astro.sott.activities.mbbaccount.ui.MBBAccountActivity;
import com.astro.sott.activities.moreListing.ui.ContinueWatchingActivity;
import com.astro.sott.activities.moreListing.ui.DetailListingActivity;
import com.astro.sott.activities.moreListing.ui.ListingActivity;
import com.astro.sott.activities.moreListing.ui.ListingActivityNew;
import com.astro.sott.activities.movieDescription.ui.MovieDescriptionActivity;
import com.astro.sott.activities.myPlans.ui.MyPlansActivity;
import com.astro.sott.activities.myplaylist.ui.MultiplePlaylistActivity;
import com.astro.sott.activities.notification.ui.NotificationActivity;
import com.astro.sott.activities.parentalControl.ui.ViewingRestrictionActivity;
import com.astro.sott.activities.search.ui.ActivitySearch;
import com.astro.sott.activities.search.ui.ResultActivity;
import com.astro.sott.activities.signUp.ui.SignUpActivity;
import com.astro.sott.activities.splash.viewModel.SplashViewModel;
import com.astro.sott.activities.subscription.ui.SingleLiveChannelSubscriptionActivity;
import com.astro.sott.activities.subscription.ui.SubscriptionActivity;
import com.astro.sott.activities.subscriptionActivity.ui.SubscriptionAndMyPlanActivity;
import com.astro.sott.activities.webEpisodeDescription.ui.WebEpisodeDescriptionActivity;
import com.astro.sott.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.astro.sott.beanModel.commonBeanModel.SearchModel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.MediaTypeCallBack;
import com.astro.sott.repositories.liveChannel.LinearProgramDataLayer;
import com.astro.sott.repositories.trailerFragment.TrailerHighlightsDataLayer;
import com.astro.sott.repositories.webSeriesDescription.SeriesDataLayer;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MediaAsset;
import com.kaltura.client.types.ProgramAsset;

import java.util.ArrayList;
import java.util.List;


public class ActivityLauncher {
    private static String key = "";
    private static String value = "";
    private final Activity activity;
    private MediaTypeCallBack detailRailClick;
    private RailCommonData railCommonData;
    private SplashViewModel myViewModel;
    private int railType;
    private String layoutRailType;

    public ActivityLauncher(Activity context) {
        this.activity = context;
        callViewModel();
    }

    private void callViewModel() {
        myViewModel = ViewModelProviders.of((FragmentActivity) activity).get(SplashViewModel.class);
    }

    public void gridListing(Activity source, Class<GridListingActivity> destination, String type, AssetCommonBean assetCommonBean) {
        Intent intent = new Intent(source, GridListingActivity.class);
        intent.putExtra("layouttype", type);
        intent.putExtra("assetCommonBean", assetCommonBean);
        intent.putExtra("baseCategory", assetCommonBean.getRailDetail());
        boolean hasFilter = false;
        if (assetCommonBean.getRailDetail() != null && assetCommonBean.getRailDetail().getFilter() != null) {
            if (((ArrayList) assetCommonBean.getRailDetail().getFilter()).size() > 0)
                hasFilter = true;
        }
        intent.putExtra("hasFilter", hasFilter);
        activity.startActivity(intent);
    }

    public void listListing(Activity source, Class<ListingActivityNew> destination, String type, AssetCommonBean assetCommonBean) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("layouttype", type);
        intent.putExtra("assetCommonBean", assetCommonBean);
        intent.putExtra("baseCategory", assetCommonBean.getRailDetail());
        boolean hasFilter = false;
        if (assetCommonBean.getRailDetail() != null && assetCommonBean.getRailDetail().getFilter() != null) {
            if (((ArrayList) assetCommonBean.getRailDetail().getFilter()).size() > 0)
                hasFilter = true;
        }
        intent.putExtra("hasFilter", hasFilter);
        activity.startActivity(intent);
    }

    public void astrLoginActivity(Activity source, Class<AstrLoginActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }


    public void signupActivity(Activity source, Class<SignUpActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void forgotPasswordActivity(Activity source, Class<ForgotPasswordActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void continueWatchingListing(Activity source, Class<ContinueWatchingActivity> destination, String type, AssetCommonBean assetCommonBean) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("layouttype", type);
        intent.putExtra("title", assetCommonBean.getTitle());
        // intent.putExtra("assetCommonBean", assetCommonBean);
        activity.startActivity(intent);
    }

    public void notification(Activity source, Class<NotificationActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void subscriptionActivity(Activity source, Class<SubscriptionActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void addDTVAccountNumberActivity(Activity source, Class<addDTVAccountNumberActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void singleChannelSubscriptionActivity(Activity source, Class<SingleLiveChannelSubscriptionActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void addDtvActivity(Activity source, Class<addDTVAccountNumberActivity> destination, String fragmentName, String dtvAccountNumber) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppLevelConstants.FRAGMENTTYPE, fragmentName);
        intent.putExtra(AppLevelConstants.DTV_ACCOUNT_NUM, dtvAccountNumber);
        activity.startActivity(intent);
    }

    public void addMBBAccountActivity(Activity source, Class<AddMBBAccountActivity> destination, String fragmentName, String mbbAccountNumber) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppLevelConstants.FRAGMENTTYPE, fragmentName);
        intent.putExtra(AppLevelConstants.MBB_ACCOUNT_NUM, mbbAccountNumber);
        activity.startActivity(intent);
    }


    public void portraitListing(Activity source, Class<ListingActivity> destination, String type, AssetCommonBean assetCommonBean) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppLevelConstants.LAYOUT_TYPE, type);
        intent.putExtra(AppLevelConstants.ASSET_COMMON_BEAN, assetCommonBean);
        activity.startActivity(intent);
    }

    public void listingActivityNew(Activity source, Class<ListingActivityNew> destination, String type, AssetCommonBean assetCommonBean) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppLevelConstants.LAYOUT_TYPE, type);
        intent.putExtra(AppLevelConstants.ASSET_COMMON_BEAN, assetCommonBean);
        activity.startActivity(intent);
    }

    private void webEpisodeScreenCheck(String name, RailCommonData railCommonData, int layoutPosition, int layoutType, MediaTypeCallBack detailRailClick) {
        if (railCommonData.getObject().getType() == MediaTypeConstant.getWebEpisode(activity)) {
            detailRailClick.detailItemClicked(AssetContent.getTileVideoURL(railCommonData.getObject(), AssetContent.getVideoResol(railCommonData.getObject().getTags())), layoutPosition, 1, railCommonData);
        } else if (railCommonData.getObject().getType() == MediaTypeConstant.getPromo(activity)) {
            promoCondition(railCommonData, layoutType);
        } else {
            clearPlayerStack(name, railCommonData, layoutPosition, layoutType, detailRailClick);

        }
    }

    public void searchActivity(Activity source, Class<ActivitySearch> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void dtvActivity(Activity source, Class<dtvActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void mbbActivity(Activity source, Class<MBBAccountActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void viewRestrictionActivity(Activity source, Class<ViewingRestrictionActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void planActivity(Activity source, Class<SubscriptionAndMyPlanActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void catchUpActivity(Activity source, Class<CatchupActivity> destination, RailCommonData commonData) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, commonData);
        activity.startActivity(intent);
    }

    public void railClickCondition(String menuNavigationName, String railName, String name,
                                   RailCommonData railCommonData,
                                   int layoutPosition, int layoutType, List<RailCommonData> railList,
                                   MediaTypeCallBack mediaTypeCallBack) {
        this.detailRailClick = mediaTypeCallBack;
        switch (name) {
            case AppLevelConstants.MOVIE_DESCRIPTION_ACTIVITY:
                movieScreenCheck(name, railCommonData, layoutPosition, layoutType, detailRailClick);
                break;
            case AppLevelConstants.WEB_EPISODE_DESCRIPTION_ACTIVITY:
                webEpisodeScreenCheck(name, railCommonData, layoutPosition, layoutType, detailRailClick);
                break;
            case AppLevelConstants.LIVE_CHANNEL:
                liveplayerScreenCheck(name, railCommonData, layoutPosition, layoutType, detailRailClick);
                break;
            case AppLevelConstants.SHORT_FILM_ACTIVITY:
                break;
            case AppLevelConstants.WEB_SERIES_DESCCRIPTION_ACTIVITY:
                dramaScreenCheck(name, railCommonData, layoutPosition, layoutType, detailRailClick);
                break;
            default:
                mediaTypeCheck(railCommonData, layoutType);
                break;
        }
    }

    private void dramaScreenCheck(String name, RailCommonData railCommonData, int layoutPosition, int layoutType, MediaTypeCallBack detailRailClick) {
        if (railCommonData.getObject().getType() == MediaTypeConstant.getDrama(activity)) {
            new ActivityLauncher(activity).webSeriesActivity(activity, WebSeriesDescriptionActivity.class, railCommonData, layoutType);
        } else if (railCommonData.getObject().getType() == MediaTypeConstant.getPromo(activity)) {
            promoCondition(railCommonData, layoutType);
        } else {
            clearPlayerStack(name, railCommonData, layoutPosition, layoutType, detailRailClick);
        }
    }

    private void movieScreenCheck(String name, RailCommonData railCommonData, int layoutPosition, int layoutType, MediaTypeCallBack detailRailClick) {
        if (railCommonData.getObject().getType() == MediaTypeConstant.getMovie(activity)) {
            detailRailClick.detailItemClicked(AssetContent.getTileVideoURL(railCommonData.getObject(), AssetContent.getVideoResol(railCommonData.getObject().getTags())), layoutPosition, 1, railCommonData);
        } else if (railCommonData.getObject().getType() == MediaTypeConstant.getPromo(activity)) {
            promoCondition(railCommonData, layoutType);
        } else {
            clearPlayerStack(name, railCommonData, layoutPosition, layoutType, detailRailClick);
        }
    }

    private void getPromoRedirection(String name, int layoutType, List<PromoPojo> mediaInformation) {
        if (mediaInformation != null) {

            for (int i = 0; i < mediaInformation.size(); i++) {


                if (mediaInformation.get(i).getKeyName().equalsIgnoreCase(AppLevelConstants.KEY_EXTERNAL_URL)) {
                    //Will handle Webview
                    Intent intent = new Intent(activity, CrWebViewActivity.class);
                    intent.putExtra(AppLevelConstants.WEBVIEW, mediaInformation.get(i).getKeyValue());
                    intent.putExtra(AppLevelConstants.PROMO_ASSET_NAME, name);
                    activity.startActivity(intent);
                    return;

                } else if (mediaInformation.get(i).getKeyName().equalsIgnoreCase(AppLevelConstants.KEY_PROMO_MEDIA_ID)) {
                    callSpecficAssetApi(mediaInformation.get(i).getKeyValue());
                    return;
                } else if (mediaInformation.get(i).getKeyName().equalsIgnoreCase(AppLevelConstants.KEY_PROGRAM_ID)) {
                    callLiveSpecificAsset(mediaInformation.get(i).getKeyValue());
                    return;
                } else if (mediaInformation.get(i).getKeyName().equalsIgnoreCase(AppLevelConstants.KEY_BASE_ID)) {
                    getLayoutType(layoutType);
                    AssetCommonBean assetCommonBean = new AssetCommonBean();
                    assetCommonBean.setID(Long.parseLong(mediaInformation.get(i).getKeyValue()));
                    assetCommonBean.setMoreType(AppLevelConstants.PROMO);
                    assetCommonBean.setMoreAssetType(railType);
                    assetCommonBean.setTitle(name);
                    new ToolBarHandler(activity).setMorePromoListener(layoutRailType, assetCommonBean);
                    return;
                }
            }


        }
    }

    private void getLayoutType(int layoutType) {

        if (layoutType == 2) {
            railType = AppLevelConstants.Rail3;
            layoutRailType = AppLevelConstants.TYPE3;

        } else if (layoutType == 3) {
            railType = AppLevelConstants.Rail4;
            layoutRailType = AppLevelConstants.TYPE4;
        } else if (layoutType == 4) {
            railType = AppLevelConstants.Rail5;
            layoutRailType = AppLevelConstants.TYPE5;
        } else {
            railType = AppLevelConstants.Rail4;
            layoutRailType = AppLevelConstants.TYPE4;
        }
    }

    private void callLiveSpecificAsset(String value) {
        myViewModel.getLiveSpecificAsset(activity, value).observe((LifecycleOwner) activity, new Observer<RailCommonData>() {
            @Override
            public void onChanged(@Nullable RailCommonData railCommonData) {
                if (railCommonData != null && railCommonData.getStatus()) {
                    liveManger(railCommonData);
                } else {
                    ToastHandler.display(activity.getString(R.string.asset_not_found), activity);
                }
            }
        });
    }

    private void callSpecficAssetApi(String value) {
        myViewModel.getSpecificAsset(activity, value).observe((LifecycleOwner) activity, asset -> {
            if (asset != null && asset.getStatus()) {
                PrintLogging.printLog("MediaTypeIs", "", "MediaTypeIs--" + asset.getObject().getType());
                redirectionOnMediaType(asset, asset.getObject().getType().toString());
            } else {
                ToastHandler.display(activity.getString(R.string.asset_not_found), activity);
            }
        });
    }

    private void redirectionOnMediaType(RailCommonData asset, String mediaType) {

        PrintLogging.printLog(this.getClass(), "", "mediaTypeDeepLink" + mediaType);
        if (Integer.parseInt(mediaType) == MediaTypeConstant.getMovie(activity)) {

            new ActivityLauncher(activity).detailActivity(activity, MovieDescriptionActivity.class, asset, AppLevelConstants.Rail3);
        } else if (Integer.parseInt(mediaType) == MediaTypeConstant.getShortFilm(activity)) {

            new ActivityLauncher(activity).detailActivity(activity, MovieDescriptionActivity.class, asset, AppLevelConstants.Rail5);
        } else if (Integer.parseInt(mediaType) == MediaTypeConstant.getWebEpisode(activity)) {
            webDetailRedirection(asset.getObject(), AppLevelConstants.Rail5);
            //   new ActivityLauncher(activity).webEpisodeActivity(activity, WebEpisodeDescriptionActivity.class, asset, AppLevelConstants.Rail5);
        } else if (Integer.parseInt(mediaType) == MediaTypeConstant.getTrailer(activity)) {

            new ActivityLauncher(activity).detailActivity(activity, MovieDescriptionActivity.class, asset, AppLevelConstants.Rail5);
        } else if (Integer.parseInt(mediaType) == MediaTypeConstant.getDrama(activity)) {

            new ActivityLauncher(activity).webSeriesActivity(activity, WebSeriesDescriptionActivity.class, asset, AppLevelConstants.Rail5);
        } else if (Integer.parseInt(mediaType) == MediaTypeConstant.getLinear(activity)) {

            new ActivityLauncher(activity).liveChannelActivity(activity, LiveChannel.class, asset);
        }

    }

    private void finishWebEpisodeActivity(Activity source, Class<WebEpisodeDescriptionActivity> destination, RailCommonData railData, int layoutType) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppLevelConstants.LAYOUT_TYPE, layoutType);
        intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        activity.startActivity(intent);
        activity.finish();
    }

    private void finishMovieActivity(Activity source, Class<MovieDescriptionActivity> destination, RailCommonData railData, int layoutType) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppLevelConstants.LAYOUT_TYPE, layoutType);
        intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        activity.startActivity(intent);
        activity.finish();
    }

    private void clearPlayerStack(String name, RailCommonData railCommonData, int layoutPosition, int layoutType, MediaTypeCallBack detailRailClick) {
        if (railCommonData.getObject().getType() == MediaTypeConstant.getWebEpisode(activity)) {
            new ActivityLauncher(activity).finishWebEpisodeActivity(activity, WebEpisodeDescriptionActivity.class, railCommonData, layoutType);
        } else if (railCommonData.getObject().getType() == MediaTypeConstant.getLinear(activity)) {
            new ActivityLauncher(activity).finishLiveChannelActivity(activity, LiveChannel.class, railCommonData);
        } else if (railCommonData.getObject().getType() == MediaTypeConstant.getMovie(activity)) {
            new ActivityLauncher(activity).finishMovieActivity(activity, MovieDescriptionActivity.class, railCommonData, layoutType);
        } else {
            mediaTypeCheck(railCommonData, layoutType);
        }
    }

    private void finishLiveChannelActivity(Activity source,
                                           Class<LiveChannel> destination, RailCommonData commonData) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, commonData);
        activity.startActivity(intent);
        activity.finish();
    }

    private void mediaTypeCheck(RailCommonData itemsList, int layoutType) {
        if (itemsList.getObject().getType() == MediaTypeConstant.getSeries(activity) || itemsList.getObject().getType() == MediaTypeConstant.getCollection(activity)) {
            new ActivityLauncher(activity).webSeriesActivity(activity, WebSeriesDescriptionActivity.class, itemsList, layoutType);
        } else if (itemsList.getObject().getType() == MediaTypeConstant.getMovie(activity)) {
            new ActivityLauncher(activity).detailActivity(activity, MovieDescriptionActivity.class, itemsList, layoutType);
        } else if (itemsList.getObject().getType() == MediaTypeConstant.getShortFilm(activity)) {
            new ActivityLauncher(activity).detailActivity(activity, MovieDescriptionActivity.class, itemsList, layoutType);
        } else if (itemsList.getObject().getType() == MediaTypeConstant.getLinear(activity)) {
            new ActivityLauncher(activity).liveChannelActivity(activity, LiveChannel.class, itemsList);
        } else if (itemsList.getObject().getType() == MediaTypeConstant.getEpisode(activity)) {
            webDetailRedirection(itemsList.getObject(), layoutType);
        } else if (itemsList.getObject().getType() == MediaTypeConstant.getTrailer(activity)) {
            trailerDirection(itemsList.getObject(), layoutType);
        } else if (itemsList.getObject().getType() == MediaTypeConstant.getHighlight(activity)) {
            trailerDirection(itemsList.getObject(), layoutType);
        } else if (itemsList.getObject().getType() == MediaTypeConstant.getClip()) {
            //new ActivityLauncher(activity).webEpisodeActivity(activity, WebEpisodeDescriptionActivity.class, itemsList, layoutType);
        } else if (itemsList.getObject().getType() == MediaTypeConstant.getProgram(activity)) {
            checkCurrentProgram(itemsList.getObject());
        } else if (itemsList.getObject().getType() == MediaTypeConstant.getPromo(activity)) {
            promoCondition(itemsList, layoutType);
        }
    }

    public void trailerDirection(Asset itemsList, int layoutType) {
        if (SystemClock.elapsedRealtime() - episodeClickTime < 1000) {
            return;
        }
        episodeClickTime = SystemClock.elapsedRealtime();
        String parentRefId = AssetContent.getParentRefId(itemsList.getTags());
        if (!parentRefId.equalsIgnoreCase("")) {
            TrailerHighlightsDataLayer.geAssetFromTrailer(activity, parentRefId).observe((LifecycleOwner) activity, asset -> {
                if (asset != null) {
                    RailCommonData railCommonData = new RailCommonData();
                    railCommonData.setObject(asset);
                    if (asset.getType() == MediaTypeConstant.getMovie(activity)) {
                        new ActivityLauncher(activity).detailActivity(activity, MovieDescriptionActivity.class, railCommonData, layoutType);

                    } else if (asset.getType() == MediaTypeConstant.getSeries(activity)) {
                        new ActivityLauncher(activity).webSeriesActivity(activity, WebSeriesDescriptionActivity.class, railCommonData, layoutType);

                    }
                } else {
                    Toast.makeText(activity, "Asset not Found", Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            Toast.makeText(activity, "Asset not Found", Toast.LENGTH_SHORT).show();

        }

    }

    private long episodeClickTime = 0;

    public void webDetailRedirection(Asset asset, int layoutType) {
        if (SystemClock.elapsedRealtime() - episodeClickTime < 1000) {
            return;
        }
        episodeClickTime = SystemClock.elapsedRealtime();
        String seriesId = AssetContent.getSeriesId(asset.getMetas());
        if (!seriesId.equalsIgnoreCase("")) {
            SeriesDataLayer.getSeries(activity, asset.getType(), seriesId).observe((LifecycleOwner) activity, asset1 -> {
                if (asset1 != null) {
                    RailCommonData railCommonData = new RailCommonData();
                    railCommonData.setObject(asset1);
                    new ActivityLauncher(activity).webSeriesActivity(activity, WebSeriesDescriptionActivity.class, railCommonData, layoutType);

                } else {
                    Toast.makeText(activity, "Asset not Found", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "Asset not Found", Toast.LENGTH_SHORT).show();

        }

    }

    private void promoCondition(RailCommonData itemsList, int layoutType) {
        Long today = Long.parseLong(AppCommonMethods.getCurrentTimeStamp());
        if (itemsList != null && itemsList.getObject() != null) {
            if (itemsList.getObject().getStartDate() < today && today < itemsList.getObject().getEndDate()) {
                getPromoRedirection(itemsList.getObject().getName(), layoutType, AssetContent.getMediaInformation(itemsList.getObject().getMetas()));


            }
        }
    }

    private void liveplayerScreenCheck(String name, RailCommonData itemsList, int layoutPosition, int layoutType, MediaTypeCallBack detailRailClick) {
        if (itemsList.getObject().getType() == MediaTypeConstant.getLinear(activity)) {
            detailRailClick.detailItemClicked(AssetContent.getTileVideoURL(itemsList.getObject(), AssetContent.getVideoResol(itemsList.getObject().getTags())), layoutPosition, 1, itemsList);
        } else if (itemsList.getObject().getType() == MediaTypeConstant.getProgram(activity)) {
            detailRailClick.detailItemClicked("", layoutPosition, 1, itemsList);
        } else {
            clearPlayerStack(name, itemsList, layoutPosition, layoutType, detailRailClick);
        }
    }

    private void checkCurrentProgram(final Asset itemValue) {
        try {

            ProgramAsset progAsset = (ProgramAsset) itemValue;
            if (progAsset.getLinearAssetId() != null) {
                LinearProgramDataLayer.getLinearFromProgram(activity, progAsset.getLinearAssetId().toString()).observe((LifecycleOwner) activity, railCommonData1 -> {
                    if (railCommonData1.getStatus()) {
                        Intent intent = new Intent(activity, LiveChannel.class);
                        intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railCommonData1);
                        intent.putExtra(AppLevelConstants.PROGRAM_ASSET, itemValue);
                        intent.putExtra("asset_ids", railCommonData1.getObject().getId());
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(activity, "Asset not Found", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Asset not Found", Toast.LENGTH_SHORT).show();

        }
        /*new LiveChannelManager().getLiveProgram(activity, itemValue, asset -> {
            if (asset.getStatus()) {
                if (asset.getLivePrograme()) {
                    getProgramRailCommonData(itemValue);
                    new ActivityLauncher(activity).liveChannelActivity(activity, LiveChannel.class, railCommonData);
                } else {
                    getProgramRailCommonData(asset.getCurrentProgram());
                    if (asset.getProgramTime() == 1) {
                        getProgramRailCommonData(itemValue);

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, activity.getResources().getString(R.string.catchup_error), Toast.LENGTH_SHORT).show();
                            }
                        });

                        // new ActivityLauncher(activity).catchUpActivity(activity, CatchupActivity.class, railCommonData);
                    } else {
                        getProgramRailCommonData(itemValue);
                        // new ActivityLauncher(activity).forwardeEPGActivity(activity, ForwardedEPGActivity.class, railCommonData);
                    }
                }
            }
        });*/
    }


    private void liveManger(final RailCommonData railCommonData) {
        if (railCommonData != null) {
            new LiveChannelManager().getLiveProgram(activity, railCommonData.getObject(), commonResponse -> {
                if (commonResponse.getStatus()) {
                    if (commonResponse.getLivePrograme()) {
                        getProgramRailCommonData(commonResponse.getCurrentProgram());
                        new ActivityLauncher(activity).liveChannelActivity(activity, LiveChannel.class, railCommonData);
                    } else {
                        getProgramRailCommonData(commonResponse.getCurrentProgram());
                        if (commonResponse.getProgramTime() == 1) {
                            getProgramRailCommonData(commonResponse.getCurrentProgram());
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, activity.getResources().getString(R.string.catchup_error), Toast.LENGTH_SHORT).show();
                                }
                            });
                            //   new ActivityLauncher(activity).catchUpActivity(activity, CatchupActivity.class, railCommonData);
                        } else {
                            //  new ActivityLauncher(activity).forwardeEPGActivity(activity, ForwardedEPGActivity.class, railCommonData);
                        }
                    }
                }
            });
        }

    }

    private void getProgramRailCommonData(Asset currentProgram) {
        railCommonData = new RailCommonData();
        railCommonData.setObject(currentProgram);
    }

    public void webSeriesActivity(Activity source, Class<WebSeriesDescriptionActivity> destination, RailCommonData railData, int layoutType) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppLevelConstants.LAYOUT_TYPE, layoutType);
        intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        activity.startActivity(intent);
    }

    public void forwardeEPGActivity(Activity source, Class<ForwardedEPGActivity> destination, RailCommonData commonData) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, commonData);
        activity.startActivity(intent);
    }

    public void liveChannelActivity(Activity source,
                                    Class<LiveChannel> destination, RailCommonData commonData) {
        try {


            MediaAsset mediaAsset = (MediaAsset) commonData.getObject();
            String channelId = mediaAsset.getExternalIds();
            LinearProgramDataLayer.getProgramFromLinear(activity, channelId).observe((LifecycleOwner) activity, programAsset -> {
                if (programAsset != null) {
                    Intent intent = new Intent(source, destination);
                    intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, commonData);
                    intent.putExtra(AppLevelConstants.PROGRAM_ASSET, programAsset);
                    intent.putExtra("asset_ids", commonData.getObject().getId());
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Asset not Found", Toast.LENGTH_SHORT).show();

                }

            });
        } catch (Exception exception) {
            Toast.makeText(activity, "Asset not Found", Toast.LENGTH_SHORT).show();

        }

        /* */
    }

    public void detailActivity(Activity source, Class<MovieDescriptionActivity> destination, RailCommonData railData, int layoutType) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppLevelConstants.LAYOUT_TYPE, layoutType);
        intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        activity.startActivity(intent);
    }

    public void boxSetDetailActivity(Activity source, Class<BoxSetDetailActivity> destination, RailCommonData railData, int layoutType) {
        if (!AssetContent.isSponsored(railData.getObject().getMetas())) {
            Intent intent = new Intent(source, destination);
            intent.putExtra(AppLevelConstants.LAYOUT_TYPE, layoutType);
            intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railData);
            activity.startActivity(intent);
        }
    }


    public void homeActivity(Activity source, Class<HomeActivity> destination) {
        Intent intent = new Intent(source, destination);
        TaskStackBuilder.create(source).addNextIntentWithParentStack(intent).startActivities();
    }

    public void webEpisodeActivity(Activity source, Class<WebEpisodeDescriptionActivity> destination, RailCommonData railData, int layoutType) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppLevelConstants.LAYOUT_TYPE, layoutType);
        intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        activity.startActivity(intent);
    }


    public void resultActivityBundle(Activity source, Class<ResultActivity> destination, SearchModel searchmodel, String searchText) {
        Bundle args = new Bundle();
        searchmodel.setSearchString(searchText);
        args.putParcelable("Search_Show_All", searchmodel);

        Intent intent = new Intent(source, destination);
        intent.putExtra("SearchResult", args);
        activity.startActivity(intent);
    }

    public void deviceManagementActivity(Activity activity, Class<DeviceManagementActivity> deviceManagementActivityClass) {
        Intent intent = new Intent(activity, deviceManagementActivityClass);
        intent.putExtra("from", "login");
        activity.startActivity(intent);
    }


    public void homeScreen(Activity source, Class<HomeActivity> destination) {
        Intent intent = new Intent(source, destination);
        TaskStackBuilder.create(source).addNextIntentWithParentStack(intent).startActivities();
    }

    public void selectAccount(Activity source, Class<SelectDtvAccountActivity> destination, String strPhoneNumber) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("PhoneNumber", strPhoneNumber);
        activity.startActivity(intent);
    }


    public void loginActivity(Activity source, Class<LoginActivity> destination, int pos, String s) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("position", pos);
        intent.putExtra("screenName", s);
        activity.startActivity(intent);
    }

    public void subscriptionActivity(Activity source, Class<SubscriptionActivity> destination, int pos) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void accountSetting(FragmentActivity activity, Class<AccountSettingsActivity> accountSettingsActivityClass) {

        Intent intent = new Intent(activity, accountSettingsActivityClass);
        activity.startActivity(intent);
    }

    public void changePaymentMethod(Activity activity, Class<ChangePaymentMethodActivity> paymentMethodActivityClass) {

        Intent intent = new Intent(activity, paymentMethodActivityClass);
        activity.startActivity(intent);
    }

    public void updatePaymentMethod(Activity activity, Class<UpdatePaymentMethod> updatePaymentMethodClass, String externalIdToCompare, int paymentMethodId) {

        Intent intent = new Intent(activity, updatePaymentMethodClass);
        intent.putExtra("ExternalId", externalIdToCompare);
        intent.putExtra("paymentMethodId", paymentMethodId);
        activity.startActivity(intent);
    }


    public void myPlan(Activity activity, Class<MyPlansActivity> plansActivityClass) {

        Intent intent = new Intent(activity, plansActivityClass);
        activity.startActivity(intent);
    }

    public void playlistActivity(FragmentActivity source, Class<MyPlaylist> destination, String partnerId, String personalListName) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("partnerIdType", partnerId);
        intent.putExtra("personalListName", personalListName);
        activity.startActivity(intent);

    }

    public void multipleplaylistActivity(FragmentActivity source, Class<MultiplePlaylistActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void detailListing(Activity source, Class<DetailListingActivity> destination, String type, AssetCommonBean assetCommonBean) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppLevelConstants.LAYOUT_TYPE, type);
        intent.putExtra(AppLevelConstants.ASSET_COMMON_BEAN, assetCommonBean);
        activity.startActivity(intent);

    }
}

