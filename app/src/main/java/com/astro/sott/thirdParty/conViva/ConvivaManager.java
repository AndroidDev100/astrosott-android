package com.astro.sott.thirdParty.conViva;

import android.content.Context;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;

import com.astro.sott.BuildConfig;
import com.astro.sott.activities.movieDescription.ui.MovieDescriptionActivity;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.shimmer.Constants;
import com.astro.sott.utils.userInfo.UserInfo;
import com.conviva.api.ConvivaConstants;
import com.conviva.sdk.ConvivaAdAnalytics;
import com.conviva.sdk.ConvivaAnalytics;
import com.conviva.sdk.ConvivaSdkConstants;
import com.conviva.sdk.ConvivaVideoAnalytics;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.LiveAsset;
import com.kaltura.playkit.Player;
import com.kaltura.playkit.plugins.ads.AdEvent;

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
    private static final String SHOW_TITLE = "c3.cm.showTitle";
    private static final String providerContentTier = "providerContentTier";


    private static final String CATEGORY_TYPE = "c3.cm.categoryType";
    private static final String SERIES_NAME = "c3.cm.seriesName";
    private static final String SERIES_NUMBER = "c3.cm.seasonNumber";
    private static final String EPISODE_NUMBER = "c3.cm.episodeNumber";
    private static final String EPISODE_NAME = "c3.cm.episodeName";

    private static final String APP_NAME = "appName";
    private static final String APP_VERSION = "c3.app.version";
    private static final String CONTENT_LENGTH = "appVersion";
    private static final String CONNECTION_TYPE = "connectionType";
    private static final String ASSET_PROVIDER_NAME = "c3.cm.name";
    private static final String AFFILIATE = "c3.cm.affiliate";
    private static final String RATING = "rating";
    private static final String WIRELESS = "Wireless";
    private static final String MOBILE = "Mobile";
    private static final String LINEAR = "Linear";
    private static final String VOD = "VOD";
    private static final String AUDIO_LANGUAGE = "audioLanguage";


    public static final String AD_ID = "c3.ad.id";
    public static final String AD_IS_SLATE = "c3.ad.isSlate";
    public static final String FIRST_AID_SYSTEM = "c3.ad.firstAdSystem";
    public static final String ACTORS = "actors";
    public static final String DIRECTORS = "directors";
    public static final String PRODUCER = "producer";

    public static final String DEVICE_ID = "deviceId";
    public static final String PRODUCT_ID = "productId";
    public static final String PUB_DATE = "pubDate";


    public static final String STREAM_PROTOCOL = "streamProtocol";
    public static final String CONTENT_PLAYBACK_TYPE = "contentPlaybackType";
    public static final String MEDIA_FILE_FRAMEWORK = "c3.ad.mediaFileApiFramework";
    public static final String AD_POSITION = "c3.ad.position";
    public static final String FIRST_AD_ID = "c3.ad.firstAdId";
    public static final String FIRST_CREATIVE_ID = "c3.ad.firstCreativeId";
    public static final String AD_CREATIVE_ID = "c3.ad.adCreativeId";


    public static final String CARRIER = "carrier";
    public static final String KALTURA_ID = "kalturaAssetId";
    public static final String Year = "Year";


    public static final String AD_STITCHER = "c3.ad.adStitcher";
    public static final String AD_SYSTEM = "c3.ad.system";
    public static final String AD_TECHNOLOGY = "c3.ad.technology";
    public static final String UTM_URL = "c3.cm.utmTrackingUrl";


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

    public static ConvivaAdAnalytics checkAdsInstanse(Context context) {
        return convivaAdAnalytics;
    }

    public static void setreportPlaybackRequested(Context context, Asset railData, String duraton, Boolean isLivePlayer, String streamUrl, Asset programAsset) {
        Asset convivaAsset;
        if (railData.getType() == MediaTypeConstant.getLinear(context)) {
            convivaAsset = programAsset;
        } else {
            convivaAsset = railData;
        }


        Map<String, Object> contentInfo = new HashMap<String, Object>();

        contentInfo.put(ConvivaSdkConstants.ASSET_NAME, (convivaAsset != null) ? convivaAsset.getName() : "NA");
        contentInfo.put(ConvivaSdkConstants.IS_LIVE, isLivePlayer + "");
        contentInfo.put(AFFILIATE, "NA");
        contentInfo.put(ConvivaSdkConstants.FRAMEWORK_NAME, "Kaltura");
        contentInfo.put(ConvivaSdkConstants.FRAMEWORK_VERSION, "4.13.3");

        contentInfo.put(ConvivaSdkConstants.DURATION, (convivaAsset != null) ? AppCommonMethods.getDurationFromUrl(convivaAsset) : 0);
        if (convivaAsset != null && !AssetContent.getActor(convivaAsset.getTags()).equalsIgnoreCase("")) {
            contentInfo.put(ACTORS, AssetContent.getActor(convivaAsset.getTags()));
        } else {
            contentInfo.put(ACTORS, "NA");
        }
        if (convivaAsset != null && !AssetContent.getDirector(convivaAsset.getTags()).equalsIgnoreCase("")) {
            contentInfo.put(DIRECTORS, AssetContent.getDirector(convivaAsset.getTags()));
        } else {
            contentInfo.put(DIRECTORS, "NA");
        }
        if (convivaAsset != null && !AssetContent.getProducer(convivaAsset.getTags()).equalsIgnoreCase("")) {
            contentInfo.put(PRODUCER, AssetContent.getProducer(convivaAsset.getTags()));
        } else {
            contentInfo.put(PRODUCER, "NA");
        }
        if (convivaAsset != null && !AssetContent.getParentalRating(convivaAsset.getTags()).equalsIgnoreCase("")) {
            contentInfo.put(RATING, AssetContent.getParentalRating(convivaAsset.getTags()));
        } else {
            contentInfo.put(RATING, "NA");
        }
        if (railData.getType() != MediaTypeConstant.getLinear(context)) {
            if (convivaAsset != null || !AssetContent.getYear(convivaAsset.getMetas()).equalsIgnoreCase("")) {
                contentInfo.put(Year, "NA");
            } else {
                contentInfo.put(Year, AssetContent.getYear(convivaAsset.getMetas()));
            }
        } else {
            if (convivaAsset != null && !AssetContent.getProgramYear(convivaAsset.getMetas()).equalsIgnoreCase("")) {
                contentInfo.put(Year, AssetContent.getProgramYear(convivaAsset.getMetas()));
            } else {
                contentInfo.put(Year, "NA");
            }
        }

        if (convivaAsset != null && !AssetContent.getProviderContentTier(convivaAsset.getTags()).equalsIgnoreCase("")) {
            contentInfo.put(providerContentTier, AssetContent.getProviderContentTier(convivaAsset.getTags()));
        } else {
            contentInfo.put(providerContentTier, "NA");
        }

        if (convivaAsset != null && convivaAsset.getStartDate() != null) {
            contentInfo.put(PUB_DATE, AppCommonMethods.getPubDate(convivaAsset.getStartDate()));
        } else {
            contentInfo.put(PUB_DATE, "NA");
        }
        if (convivaAsset != null && !AssetContent.getLanguageDataString(convivaAsset.getTags(), context).equalsIgnoreCase("")) {
            contentInfo.put(AUDIO_LANGUAGE, AssetContent.getLanguageDataString(convivaAsset.getTags(), context));
        } else {
            contentInfo.put(AUDIO_LANGUAGE, "NA");
        }

        contentInfo.put(PRODUCT_ID, "Astro-Sooka");
        contentInfo.put(STREAM_PROTOCOL, "DASH");
        contentInfo.put(KALTURA_ID, (convivaAsset != null) ? convivaAsset.getId() : "NA");

        contentInfo.put(DEVICE_ID, AppCommonMethods.getDeviceId(context.getContentResolver()));

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
            contentInfo.put(CHANNEL, Constants.channelName);
            contentInfo.put(CONTENT_PLAYBACK_TYPE, "LIVE");
            if (convivaAsset != null && !AppCommonMethods.getPlayerUrl(convivaAsset).equalsIgnoreCase("")) {
                contentInfo.put(ConvivaSdkConstants.STREAM_URL, AppCommonMethods.getPlayerUrl(convivaAsset));
            } else {
                contentInfo.put(ConvivaSdkConstants.STREAM_URL, "NA");
            }
            contentInfo.put(EPISODE_NAME, (convivaAsset != null) ? convivaAsset.getName() : "NA");

            try {
                LiveAsset liveAsset = (LiveAsset) railData;
                contentInfo.put(ASSET_ID, liveAsset.getExternalIds());
            } catch (Exception e) {
            }
        } else {
            if (!streamUrl.equalsIgnoreCase("")) {
                contentInfo.put(ConvivaSdkConstants.STREAM_URL, streamUrl);
            } else {
                contentInfo.put(ConvivaSdkConstants.STREAM_URL, "NA");
            }
            contentInfo.put(CONTENT_PLAYBACK_TYPE, "VOD");
            if (!AssetContent.getProvider(convivaAsset.getTags()).equalsIgnoreCase("")) {
                contentInfo.put(BRAND, AssetContent.getProvider(convivaAsset.getTags()));
            } else {
                contentInfo.put(BRAND, "NA");
            }
            if (!AssetContent.getKeyworddata(convivaAsset.getTags()).equalsIgnoreCase("")) {
                contentInfo.put(CHANNEL, AssetContent.getKeyworddata(convivaAsset.getTags()));
            } else {
                contentInfo.put(CHANNEL, "NA");

            }
            if (convivaAsset.getType() == MediaTypeConstant.getSeries(context)) {
                contentInfo.put(SERIES_NAME, convivaAsset.getName());
            }
            if (AssetContent.getSeriesNumber(convivaAsset.getMetas()) == -1) {
                contentInfo.put(SERIES_NUMBER, "NA");
            } else {
                contentInfo.put(SERIES_NUMBER, AssetContent.getSeriesNumber(convivaAsset.getMetas()));
            }
            if (AssetContent.getSeriesName(convivaAsset.getTags()).equalsIgnoreCase("")) {
                contentInfo.put(SERIES_NAME, "NA");
            } else {
                contentInfo.put(SERIES_NAME, AssetContent.getSeriesName(convivaAsset.getTags()));

            }
            contentInfo.put(SHOW_TITLE, convivaAsset.getName());
            contentInfo.put(CONTENT_TYPE, VOD);
            contentInfo.put(ASSET_ID, convivaAsset.getExternalId());
        }

        if (convivaAsset != null && !AssetContent.getProvider(convivaAsset.getTags()).equalsIgnoreCase("")) {
            contentInfo.put(ASSET_PROVIDER_NAME, AssetContent.getProvider(convivaAsset.getTags()));
        } else {
            contentInfo.put(ASSET_PROVIDER_NAME, "NA");

        }
        if (convivaAsset != null && convivaAsset.getType() == MediaTypeConstant.getEpisode(context)) {
            contentInfo.put(EPISODE_NUMBER, convivaAsset.getName());
        } else {
            contentInfo.put(EPISODE_NUMBER, "NA");
        }
        contentInfo.put(UTM_URL, "NA");
        TelephonyManager telemamanger = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);

        String simOperatorName = telemamanger.getNetworkOperatorName();
        if (!simOperatorName.equalsIgnoreCase("")) {
            contentInfo.put(CARRIER, simOperatorName);
        } else {
            contentInfo.put(CARRIER, "NA");
        }
        //
        contentInfo.put(CATEGORY_TYPE, (convivaAsset != null) ? AppCommonMethods.getAssetType(convivaAsset.getType(), context) : "NA");
        contentInfo.put(APP_NAME, "Sooka Android");
        contentInfo.put(APP_VERSION, BuildConfig.VERSION_NAME);
        if (convivaAsset != null && !AssetContent.getGenredataString(convivaAsset.getTags()).equals("")) {
            contentInfo.put(GENRE, AssetContent.getGenredataString(convivaAsset.getTags()));
        } else {
            contentInfo.put(GENRE, "NA");
        }

        if (convivaAsset != null && !AssetContent.getSubGenredataString(convivaAsset.getTags()).equals("")) {
            contentInfo.put(GENRE_LIST, AssetContent.getSubGenredataString(convivaAsset.getTags()));
        } else {
            contentInfo.put(GENRE_LIST, "NA");
        }
        contentInfo.put(ConvivaSdkConstants.PLAYER_NAME, "Sooka Android");
        ConvivaManager.getConvivaVideoAnalytics(context).reportPlaybackRequested(contentInfo);
    }

    public static void convivaAdsEvent(BaseActivity baseActivity, AdEvent.AdStartedEvent event) {
        try {
            AdEvent.AdStartedEvent adStartedEvent = event;
            Map<String, Object> contentInfo = new HashMap<String, Object>();
            contentInfo.put(ConvivaManager.AD_ID, adStartedEvent.adInfo.getAdId());
            contentInfo.put(ConvivaManager.FIRST_AD_ID, adStartedEvent.adInfo.getAdId());
            contentInfo.put(ConvivaSdkConstants.DURATION, adStartedEvent.adInfo.getAdDuration() + "");
            contentInfo.put(ConvivaManager.AD_CREATIVE_ID, adStartedEvent.adInfo.getCreativeId());
            contentInfo.put(ConvivaSdkConstants.FRAMEWORK_NAME, "Kaltura IMA");
            contentInfo.put(ConvivaSdkConstants.FRAMEWORK_VERSION, "4.13.3");
            contentInfo.put(ConvivaManager.FIRST_CREATIVE_ID, adStartedEvent.adInfo.getCreativeId());
            contentInfo.put(ConvivaManager.AD_POSITION, adStartedEvent.adInfo.getAdPositionType());
            if (adStartedEvent.adInfo.getMediaBitrate() != 0) {
                int bitRate = adStartedEvent.adInfo.getMediaBitrate() / 1000;
                contentInfo.put(ConvivaSdkConstants.PLAYBACK.BITRATE, bitRate + "");
            }
            contentInfo.put(ConvivaSdkConstants.ASSET_NAME, adStartedEvent.adInfo.getAdTitle());
            contentInfo.put(ConvivaSdkConstants.STREAM_URL, "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpreonly&cmsid=496&vid=short_onecue&correlator=");
            contentInfo.put(ConvivaManager.AD_SYSTEM, "DFP");
            contentInfo.put(ConvivaManager.AD_IS_SLATE, false);
            contentInfo.put(ConvivaManager.MEDIA_FILE_FRAMEWORK, "NA");
            contentInfo.put(ConvivaManager.FIRST_AID_SYSTEM, "NA");
            contentInfo.put(ConvivaManager.AD_TECHNOLOGY, "Client Side");
            contentInfo.put(ConvivaManager.AD_STITCHER, "NA");
            contentInfo.put(ConvivaSdkConstants.IS_LIVE, false);
            ConvivaManager.getConvivaAdAnalytics(baseActivity).reportAdLoaded(contentInfo);
            ConvivaManager.getConvivaAdAnalytics(baseActivity).reportAdStarted();
            ConvivaManager.getConvivaAdAnalytics(baseActivity).reportAdMetric(ConvivaSdkConstants.PLAYBACK.PLAYER_STATE, ConvivaSdkConstants.PlayerState.PLAYING);
        } catch (Exception e) {

        }
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

