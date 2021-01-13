package com.astro.sott.thirdParty.conViva;

import android.content.Context;

import com.astro.sott.BuildConfig;
import com.astro.sott.activities.movieDescription.ui.MovieDescriptionActivity;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.utils.constants.AppConstants;
import com.conviva.sdk.ConvivaAdAnalytics;
import com.conviva.sdk.ConvivaAnalytics;
import com.conviva.sdk.ConvivaSdkConstants;
import com.conviva.sdk.ConvivaVideoAnalytics;

import java.util.HashMap;
import java.util.Map;

public class ConvivaManager {
    private static ConvivaVideoAnalytics convivaVideoAnalytics;
    private static ConvivaAdAnalytics convivaAdAnalytics;
    private static Context mcontext;

    public static void initConvivaAnalytics(Context context) {
        mcontext = context;
        if (BuildConfig.DEBUG) {
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

    public static void setreportPlaybackRequested(Context context, RailCommonData railData, String duraton) {
        Map<String, Object> contentInfo = new HashMap<String, Object>();
        contentInfo.put(ConvivaSdkConstants.STREAM_URL, "");
        contentInfo.put(ConvivaSdkConstants.ASSET_NAME, railData.getObject().getName());
        contentInfo.put(ConvivaSdkConstants.IS_LIVE, ConvivaSdkConstants.StreamType.VOD);
        contentInfo.put(ConvivaSdkConstants.VIEWER_ID, "NA");
        //   contentInfo.put(ConvivaSdkConstants.DURATION, duraton);
        contentInfo.put("c3.cm.id", railData.getObject().getId());
        contentInfo.put("c3.cm.contentType", "Movie");
        contentInfo.put(ConvivaSdkConstants.PLAYER_NAME, "SOTT Android");
        //  videoAnalytics.reportPlaybackRequested(contentInfo);
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
        if (ConvivaManager.getConvivaAdAnalytics(mcontext) != null) {
            ConvivaManager.getConvivaAdAnalytics(mcontext).release();
            convivaAdAnalytics = null;
        }
        ConvivaAnalytics.release();
    }

    public static void convivaUserWaitStarted() {
        ConvivaManager.getConvivaVideoAnalytics(mcontext).reportPlaybackEvent(String.valueOf(ConvivaSdkConstants.Events.USER_WAIT_STARTED));
    }

    public static void convivaUserWaitStopped() {
        ConvivaManager.getConvivaVideoAnalytics(mcontext).reportPlaybackEvent(String.valueOf(ConvivaSdkConstants.Events.USER_WAIT_ENDED));
    }


}

