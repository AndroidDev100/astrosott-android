package com.dialog.dialoggo.networking.ksServices;

import com.dialog.dialoggo.utils.helpers.PrintLogging;

class KSQL {
    private static String _one;
    private static String _tw0;
    private static String _three;
    private static String _four;
    private static String KSQL;

    public static String forParticularProgram(String externalId, String startDate, String endDate) {
        _one = "(and epg_channel_id='";
        _tw0 = "' start_date<='";
        _three = "' end_date>='";
        _four = "')";
        KSQL = _one + externalId + _tw0 + startDate + _three + endDate + _four;
        printKSQL(KSQL);
        return KSQL;
    }


    public static String forCatchUpPreviousProgram(String externalId, String startDate) {
        _one = "(and epg_channel_id='";
        _tw0 = "' start_date<'";
        _four = "')";
        KSQL = _one + externalId + _tw0 + startDate + _four;
        printKSQL(KSQL);
        return KSQL;

    }

    public static String forCatchUpNextProgram(String externalId, String startDate) {
        _one = "(and epg_channel_id='";
        _tw0 = "' start_date>'";
        _four = "')";
        KSQL = _one + externalId + _tw0 + startDate + _four;
        printKSQL(KSQL);
        return KSQL;

    }



    public static String forEPGListing(String externalId, String startDate, String endDate) {
        _one = "(and epg_channel_id='";
        _tw0 = "' start_date>='";
        _three = "' start_date<'";
        _four = "')";
        KSQL = _one + externalId + _tw0 + startDate + _three + endDate + _four;
        printKSQL(KSQL);
        return KSQL;
    }

    private static void printKSQL(String ksql) {
       PrintLogging.printLog("KSQL","", "KSQL" + ksql);
    }

    public static String forvideoContent(String AssetId) {
        _one = "media_id:'";
        _tw0 = AssetId;
        _three = "'";
        KSQL = _one + _tw0 + _three;
        return KSQL;
    }

    public static String forFollowSeries(String ksql) {
        _one = "media_id:'";
        _tw0 = "'";
        KSQL = _one + ksql + _tw0;
        return KSQL;
    }

    public static String watchList(String ksql) {
        _one = "media_id:'";
        _tw0 = "'";
        KSQL = _one + ksql + _tw0;
        return KSQL;
    }

    public static String followList(String ksql) {
        _one = "media_id:'";
        _tw0 = "'";
        KSQL = _one + ksql + _tw0;
        return KSQL;
    }

    public static String forSimilarChannel(Integer type, String genre) {
        _one = "(and asset_type='";
        _tw0 = "' (or Genre='";
        _three = "'))";
        KSQL = _one + type + _tw0 + genre + _three;
        printKSQL(KSQL);
        return KSQL;
    }
    public static String forsimillarMovie(int assetType) {
        String one = "(and asset_type='";
        String two = "')";
        KSQL = one + String.valueOf(assetType) + two;
        printKSQL(KSQL);
        return KSQL;
    }

    public static String foryouMayLikeMovie(String genreSkl,int assetType) {
        String one = "(and asset_type='";
        String two = "' (or " + genreSkl + "))";
        String third = String.valueOf(assetType);
        KSQL = one + third + two;
        printKSQL(KSQL);
        return KSQL;
    }
    public static String forContinueWatchingAssets(String ksql) {
        _one = "media_id:'";
        _tw0 = "'";
        KSQL = _one + ksql + _tw0;
        printKSQL(KSQL);
        return KSQL;
    }

    public static String forNumberOfEpisodes(String seriesId) {
        String one = "(and SeriesId='";
        String two = "')";
        KSQL = one + seriesId + two;
        printKSQL(KSQL);
        return KSQL;
    }
}
