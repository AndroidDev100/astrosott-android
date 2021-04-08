package com.astro.sott.thirdParty.conViva;

import android.content.Context;

import com.astro.sott.BuildConfig;
import com.astro.sott.activities.movieDescription.ui.MovieDescriptionActivity;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.userInfo.UserInfo;
import com.conviva.api.ConvivaConstants;
import com.conviva.sdk.ConvivaAdAnalytics;
import com.conviva.sdk.ConvivaAnalytics;
import com.conviva.sdk.ConvivaSdkConstants;
import com.conviva.sdk.ConvivaVideoAnalytics;
import com.kaltura.playkit.Player;

import java.util.HashMap;
import java.util.Map;

public class ConvivaManager {
    private static ConvivaVideoAnalytics convivaVideoAnalytics;
    private static ConvivaAdAnalytics convivaAdAnalytics;
    private static final String CONTENT_TYPE = "c3.cm.contentType";
    private static final String CHANNEL = "c3.cm.channel";
    private static final String BRAND = "c3.cm.brand";
    private static final String GENRE = "c3.cm.genre";
    private static final String GENRE_LIST = "c3.cm.genreList";
    private static final String ASSET_ID = "c3.cm.id";


    private static final String CATEGORY_TYPE = "c3.cm.categoryType";
    private static final String SERIES_NAME = "c3.cm.seriesName";
    private static final String SERIES_NUMBER = "c3.cm.seasonNumber";
    private static final String EPISODE_NUMBER = "c3.cm.episodeNumber";
    private static final String APP_NAME = "appName";
    private static final String APP_VERSION = "appVersion";
    private static final String CONTENT_LENGTH = "appVersion";
    private static final String CONNECTION_TYPE = "connectionType";
    private static final String ASSET_PROVIDER_NAME = "assetProviderName";

    private static final String WIRELESS = "Wireless";
    private static final String MOBILE = "Mobile";
    private static final String LINEAR = "Linear";
    private static final String VOD = "VOD";
    public static final String AD_STITCHER = "adStitcherSystem";
    public static final String AD_SYSTEM = "adSystem";
    public static final String AD_TECHNOLOGY = "adTechnology";
    public static final String UTM_URL = " c3.cm.utmTrackingUrl";


    private static Context mcontext;

    public static void initConvivaAnalytics(Context context) {
        mcontext = context;
        if (BuildConfig.FLAVOR.equalsIgnoreCase("QA")) {
            Map<String, Object> settings = new HashMap<String, Object>();
            String gatewayUrl = "https://astro-sott-test.testonly.conviva.com";
            settings.put(ConvivaSdkConstants.GATEWAY_URL, gatewayUrl);
            settings.put(ConvivaSdkConstants.LOG_LEVEL, ConvivaSdkConstants.LogLevel.DEBUG);
            ConvivaAnalytics.init(context, AppConstants.CON_VIVA_CUSTOMER_KEY, settings);
        } else {
            //production release
            ConvivaAnalytics.init(context, AppConstants.CON_VIVA_CUSTOMER_KEY);
        }
    }


    public static ConvivaVideoAnalytics getConvivaVideoAnalytics(Context context) {
        if (convivaVideoAnalytics == null)
            convivaVideoAnalytics = ConvivaAnalytics.buildVideoAnalytics(context);
        return convivaVideoAnalytics;
    }

    public static ConvivaAdAnalytics getConvivaAdAnalytics(Context context) {
        if (convivaAdAnalytics == null) {
            if (convivaVideoAnalytics != null)
                convivaAdAnalytics = ConvivaAnalytics.buildAdAnalytics(context, convivaVideoAnalytics);
        }
        return convivaAdAnalytics;
    }

    public static void setreportPlaybackRequested(Context context, RailCommonData railData, String duraton, Boolean isLivePlayer) {
        Map<String, Object> contentInfo = new HashMap<String, Object>();

        if (!AppCommonMethods.getPlayerUrl(railData.getObject()).equalsIgnoreCase("")) {
            contentInfo.put(ConvivaSdkConstants.STREAM_URL, AppCommonMethods.getPlayerUrl(railData.getObject()));
        } else {
            contentInfo.put(ConvivaSdkConstants.STREAM_URL, "NA");
        }
        contentInfo.put(ConvivaSdkConstants.ASSET_NAME, railData.getObject().getName());
        contentInfo.put(ConvivaSdkConstants.IS_LIVE, isLivePlayer + "");
        contentInfo.put(ConvivaSdkConstants.FRAMEWORK_NAME, "Kaltura");
        contentInfo.put(ConvivaSdkConstants.DURATION, AppCommonMethods.getDurationFromUrl(railData.getObject()));
        contentInfo.put(ConvivaSdkConstants.FRAMEWORK_VERSION, "Kaltura 4.8.3");
        if (UserInfo.getInstance(context).isActive()) {
            if (UserInfo.getInstance(context).getCpCustomerId() != null) {
                contentInfo.put(ConvivaSdkConstants.VIEWER_ID, UserInfo.getInstance(context).getCpCustomerId());
            } else {
                contentInfo.put(ConvivaSdkConstants.VIEWER_ID, AppCommonMethods.getDeviceId(context.getContentResolver()));
            }
        } else {
            contentInfo.put(ConvivaSdkConstants.VIEWER_ID, AppCommonMethods.getDeviceId(context.getContentResolver()));
        }
        if (NetworkConnectivity.isWifiConnected(context)) {
            contentInfo.put(CONNECTION_TYPE, WIRELESS);
        } else {
            contentInfo.put(CONNECTION_TYPE, MOBILE);
        }
        if (isLivePlayer) {
            contentInfo.put(CONTENT_TYPE, LINEAR);
            contentInfo.put(CHANNEL, railData.getObject().getName());

        } else {
            if (AssetContent.getProvider(railData.getObject().getTags()).equalsIgnoreCase("")) {
                contentInfo.put(BRAND, AssetContent.getProvider(railData.getObject().getTags()));
            } else {
                contentInfo.put(BRAND, "NA");
            }
            if (!AssetContent.getKeyworddata(railData.getObject().getTags()).equalsIgnoreCase("")) {
                contentInfo.put(CHANNEL, AssetContent.getKeyworddata(railData.getObject().getTags()));
            } else {
                contentInfo.put(CHANNEL, "NA");

            }
            contentInfo.put(CONTENT_TYPE, VOD);
            contentInfo.put(ASSET_ID, railData.getObject().getExternalId());
        }

        if (AssetContent.getProvider(railData.getObject().getTags()).equalsIgnoreCase("")) {
            contentInfo.put(ASSET_PROVIDER_NAME, AssetContent.getProvider(railData.getObject().getTags()));
        } else {
            contentInfo.put(ASSET_PROVIDER_NAME, "NA");

        }
        if (railData.getObject().getType() == MediaTypeConstant.getEpisode(context)) {
            contentInfo.put(EPISODE_NUMBER, railData.getObject().getName());
        } else {
            contentInfo.put(EPISODE_NUMBER, "NA");

        }
        contentInfo.put(UTM_URL, "NA");
        contentInfo.put(CATEGORY_TYPE, AppCommonMethods.getAssetType(railData.getObject().getType(), context));
        contentInfo.put(APP_NAME, "SOTT Android");
        contentInfo.put(APP_VERSION, BuildConfig.VERSION_NAME);
        if (AssetContent.getGenredataString(railData.getObject().getTags()).equals("")) {
            contentInfo.put(GENRE, "NA");
        } else {
            contentInfo.put(GENRE, AssetContent.getGenredataString(railData.getObject().getTags()));
        }

        if (AssetContent.getSubGenredataString(railData.getObject().getTags()).equals("")) {
            contentInfo.put(GENRE_LIST, "NA");
        } else {
            contentInfo.put(GENRE_LIST, AssetContent.getSubGenredataString(railData.getObject().getTags()));
        }
        contentInfo.put(ConvivaSdkConstants.PLAYER_NAME, "SOTT Android");
        ConvivaManager.getConvivaVideoAnalytics(context).reportPlaybackRequested(contentInfo);
    }

    public static void convivaPlayerPlayReportRequest() {
        ConvivaManager.getConvivaVideoAnalytics(mcontext).reportPlaybackMetric(ConvivaSdkConstants.PLAYBACK.PLAYER_STATE, ConvivaSdkConstants.PlayerState.PLAYING);
    }

    public static void convivaPlayerPauseReportRequest() {
        ConvivaManager.getConvivaVideoAnalytics(mcontext).reportPlaybackMetric(ConvivaSdkConstants.PLAYBACK.PLAYER_STATE, ConvivaSdkConstants.PlayerState.PAUSED);
    }

    public static void convivaPlayerBufferReportRequest() {
        ConvivaManager.getConvivaVideoAnalytics(mcontext).reportPlaybackMetric(ConvivaSdkConstants.PLAYBACK.PLAYER_STATE, ConvivaSdkConstants.PlayerState.BUFFERING);
    }

    public static void convivaPlayerSetBitRate(long bitrate) {
        ConvivaManager.getConvivaVideoAnalytics(mcontext).reportPlaybackMetric(ConvivaSdkConstants.PLAYBACK.BITRATE, Integer.parseInt(bitrate + ""));
    }

    public static void convivaPlayerStoppedReportRequest() {
        ConvivaManager.getConvivaVideoAnalytics(mcontext).reportPlaybackMetric(ConvivaSdkConstants.PLAYBACK.PLAYER_STATE, ConvivaSdkConstants.PlayerState.STOPPED);
    }

    public static void convivaPlayerSeekStartedReportRequest(Context context) {
        ConvivaManager.getConvivaVideoAnalytics(mcontext).reportPlaybackMetric(ConvivaSdkConstants.PLAYBACK.SEEK_STARTED, 0);
    }

    public static void convivaPlayerSeekStoppedReportRequest(Context context) {
        ConvivaManager.getConvivaVideoAnalytics(mcontext).reportPlaybackMetric(ConvivaSdkConstants.PLAYBACK.SEEK_ENDED, 0);
    }

    public static void convivaPlayerAppBackgrounded(Context context) {
        ConvivaAnalytics.reportAppBackgrounded();
    }

    public static void convivaPlayerAppForegrounded(Context context) {
        ConvivaAnalytics.reportAppForegrounded();
    }

    public static void removeConvivaSession() {
        if (ConvivaManager.getConvivaVideoAnalytics(mcontext) != null) {
            ConvivaManager.getConvivaVideoAnalytics(mcontext).release();
            convivaVideoAnalytics = null;
        }

    }

    public static void removeConvivaAdsSession() {
        if (ConvivaManager.getConvivaAdAnalytics(mcontext) != null) {
            ConvivaManager.getConvivaAdAnalytics(mcontext).release();
            convivaAdAnalytics = null;
        }
    }

    public static void convivaUserWaitStarted() {
        ConvivaManager.getConvivaVideoAnalytics(mcontext).reportPlaybackEvent(String.valueOf(ConvivaSdkConstants.Events.USER_WAIT_STARTED));
    }

    public static void convivaUserWaitStopped() {
        ConvivaManager.getConvivaVideoAnalytics(mcontext).reportPlaybackEvent(String.valueOf(ConvivaSdkConstants.Events.USER_WAIT_ENDED));
    }


}

