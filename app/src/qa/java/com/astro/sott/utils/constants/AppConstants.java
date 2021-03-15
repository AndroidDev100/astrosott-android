package com.astro.sott.utils.constants;


public interface AppConstants {

    //home page tab ids
    int POPULAR_SEARCH_CHANNEL_ID = 332536;
    int HOME_TAB_ID = 3232;
    int LIVE_TAB_ID = 3233;
    int VIDEO_TAB_ID = 3235;

    int MOVIE_DETAIL_ID = 3256;
    int SHORT_FILMDETAIL_ID = 3259;
    int DRAMA_DETAILS_ID = 3258;
    int DRAMA_EPISODE_DETAILS_ID = 3257;
    int LIVETV_DETAIL_ID = 3266;

    int TAB_FORWARDED_EPG_DETAIL = 3292;
    String KEY_SEASON_NUMBER = "Season number";
    String EPISODE_NUMBER = "EPISODE NUMBER";


    String END_POINT = "https://rest-sgs1.ott.kaltura.com/";
    String TNC_URL = "https://www.dialog.lk/browse/termsAndConditions.jsp?id=rpb4deb831863ff58e428067028c98d4b50";
    String HELP_URL = "https://astrosott.zendesk.com/hc/en-us";


    String HD = "HD";
    String SD = "SD";
    String Mobile_Dash_HD = "Mobile_Dash_HD";
    String Mobile_Dash_SD = "Mobile_Dash_SD";

    String CON_VIVA_CUSTOMER_KEY = "4d9d8829a9c54ef1b4457fab9508f3e779ba38dd";
    String AF_DEV_KEY = "";
    String TAB_HOME_KEY = "HOME_V1";
    String TAB_HOME_KEY_IGNORE = "Home";
    String TAB_LIVE_TV_KEY = "LiveTV_V1";
    String TAB_VIDEO_KEY = "VIDEO_V1";
    String TAB_MOVIE_DETAILS_KEY = "Movie Detail";
    String TAB_SHORT_FILM_DETAILS_KEY = "Short Film Detail";
    String TAB_DRAMA_DETAILS_KEY = "Drama Detail";
    String TAB_DRAMA_EPISODE_KEY = "Episode Detail";
    String TAB_POPULAR_SEARCH_KEY = "DialogPopularSearch";
    String TAB_LIVE_TV_DETAILS_KEY = "Live TV Detail";
    String TAB_FORWARDED_EPG_DETAIL_KEY = "Forward EPG Detail";

    String FB_PLACEMENT_ID = "367519410486408_367519577153058";
    String SL_COUNTRY_CODE = "94";
    String SMS_API_END_POINT = "v4/send-otp";
    String VERIFY_OTP = "v4/verify-otp";
    String DTV_ACC_NUM = "QA/v4/getDtvContactInfo";
    String CONNETION_DETAILS = "qa/v4/getConDetailsByRefAccount";

    String Vlog_Genre = "Vlog";
    String Science_Genre = "Science";
    String Sports_Genre = "Sports";


    String Crime_Genre = "Crime";
    String Chat_Shows_Genre = "Chat Shows";
    String Biopic_Genre = "Biopic";


    String Family_Genre = "Family";
    String Kids_Genre = "Kids";
    String Documentary_Genre = "Documentary";


    String Reality_Genre = "Reality";
    String Horror_Genre = "Horror";
    String Action_Genre = "Action";

    String Thriller_Genre = "Thriller";
    String Romance_Genre = "Romance";
    String Drama_Genre = "Drama";

    String Lifestyle_Genre = "Lifestyle";
    String Comedy_Genre = "Comedy";

    long Vlog = 332451;
    long Science = 332452;
    long Sports = 332422;


    long Crime = 332421;
    long Chat_Shows = 332362;
    long Biopic = 332361;


    long Family = 332360;
    long Kids = 332355;
    long Documentary = 332344;


    long Reality = 332343;
    long Horror = 332340;
    long Action = 332532;

    long Thriller = 332313;
    long Romance = 332309;
    long Drama = 332307;

    long Lifestyle = 332395;
    long Comedy = 332394;


//    Send OTP
//
//    QA
//
//    https://sus8scm21a.execute-api.ap-southeast-1.amazonaws.com/QA/v4/send-otp
//
//    Verify OTP
//
//    QA
//
//    https://mo98wsla9h.execute-api.ap-southeast-1.amazonaws.com/QA/v4/verify-otp
//
//    Get DTV Contact Info
//
//    QA
//
//    https://pqbjim2r2i.execute-api.ap-southeast-1.amazonaws.com/QA/v4/getDtvContactInfo
//
//    Get Connection Detail by Ref
//
//            QA
//
//    https://bo18wc4nfl.execute-api.ap-southeast-1.amazonaws.com/qa/v4/getConDetailsByRefAccount
//


    String JWT_PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIY1uHmPI7jvjfBo/FrP/ucB/gbzLYFA /n28laR+gfgHbxHlJgUeYiioPjuQAKRL+ko3zOmVx0lNBcs1NKL/Oq8CAwEAAQ==";
    String Platform = "qa";

    String CERT_SEND_BASE_URL = "sus8scm21a.execute-api.ap-southeast-1.amazonaws.com:443";
    String CERT_SEND_PUBLIC_KEY = "sha256/Gkqn2BwGNCm0SDBqw9AEfpG8msmFcgA3DLlFXFVmRWo=";
    String CERT_VERIFY_BASE_URL = "mo98wsla9h.execute-api.ap-southeast-1.amazonaws.com:443";
    String CERT_VERTIFY_PUBLIC_KEY = "sha256/Gkqn2BwGNCm0SDBqw9AEfpG8msmFcgA3DLlFXFVmRWo=";
    String CERT_GET_DTV_INFO = "pqbjim2r2i.execute-api.ap-southeast-1.amazonaws.com:443";
    String CERT_CONNECTION_DETAILS = "bo18wc4nfl.execute-api.ap-southeast-1.amazonaws.com:443";


    //Experience manager related changes
    public static int TAB_FIRST = 0;
    public static int TAB_SECOND = 1;
    public static int TAB_THIRD = 2;
    public static int TAB_FOUR = 3;
    public static int TAB_FIVE = 4;
    public static int TAB_SIX = 5;
    public static final int TAB_MOVIE_DESCRIPTION = 6;
    public static final int TAB_SHORTFILM_DESCRIPTION = 7;
    public static final int TAB_WEBSERIES_DETAIL = 8;
    public static final int TAB_WEBEPISODE_DETAIL = 9;
    public static final int TAB_SPOTLIGHTSERIES = 10;
    public static final int TAB_SPOTLIGHTEPISODE = 11;
    public static final int TAB_LIVETV_DETAIL = 12;
    public static final int TAB_UGC_CREATORPROFILE_DETAIL = 13;
    public static final int TAB_UGC_VIDEO_DETAIL = 14;
    public static final int TAB_CATCHUP_DETAIL = 15;
    public static final int TAB_CLIP_DETAIL = 17;

    public static final int DFP_MREC_BANNER = 300;
    public static final int DFP_BANNER = 320;
    public static final int DFP_TABLET_SMALL = 468;
    public static final int DFP_TABLET_LARGE = 728;


    //Experience manager

    /*******
     *Layout Constants
     ******/
    public static String WIDGET_TYPE_AD = "ADS";
    public static String WIDGET_TYPE_CONTENT = "CNT";
    public static final String KEY_MY_WATCHLIST = "My Watchlist";
    public static String KEY_RAIL_DATA = "Rail Data";
    public static String WIDGET_TYPE_HERO = "HRO";
    public static final int ADS_BANNER = 41;
    public static final int ADS_MREC = 42;
    public static final int CAROUSEL_LDS_BANNER = 10;
    public static final int CAROUSEL_LDS_LANDSCAPE = 11;
    public static final int CAROUSEL_PR_POTRAIT = 12;
    public static final int CAROUSEL_PR_POSTER = 13;
    public static final int CAROUSEL_SQR_SQUARE = 14;
    public static final int CAROUSEL_CIR_CIRCLE = 15;
    public static final int CAROUSEL_CST_CUSTOM = 16;

    public static final int HERO_LDS_LANDSCAPE = 20;
    public static final int HERO_LDS_BANNER = 21;
    public static final int HERO_RCG_BANNER = 27;

    public static final int HERO_PR_POTRAIT = 22;
    public static final int HERO_PR_POSTER = 23;
    public static final int HERO_SQR_SQUARE = 24;
    public static final int HERO_CIR_CIRCLE = 25;

    public static final int HERO_CST_CUSTOM = 26;

    public static final int HORIZONTAL_LDS_LANDSCAPE = 31;
    public static final int HORIZONTAL_LDS_BANNER = 32;
    public static final int HORIZONTAL_PR_POTRAIT = 33;
    public static final int HORIZONTAL_PR_POSTER = 34;
    public static final int HORIZONTAL_SQR_SQUARE = 35;
    public static final int HORIZONTAL_CIR_CIRCLE = 36;
    public static final int HORIZONTAL_CST_CUSTOM = 37;

    public static final int RAIL_CONTINUE_WATCHING = 61;
    public static final int RAIL_MY_WATCHLIST = 62;

    public static final int PDF_TARGET_LGN = 51;
    public static final int PDF_TARGET_IFP = 52;
    public static final int PDF_TARGET_SRH = 53;


    public static final int RAIL_FAN_TABLET = 71;
    public static final int RAIL_FAN_MOBILE = 72;

    public static final int RAIL_RECOMMENDED = 81;

    public static final String CAROUSEL_LANDSCAPE = "CAROUSEL_LANDSCAPE";
    public static final String CAROUSEL_POTRAIT = "CAROUSEL_POTRAIT";
    public static final String CAROUSEL_SQUARE = "CAROUSEL_SQUARE";
    public static final String CAROUSEL_CUSTOM = "CAROUSEL_CUSTOM";


    public static final String HORIZONTAL_LANDSCAPE = "HORIZONTAL_LANDSCAPE";
    public static final String HORIZONTAL_POTRAIT = "HORIZONTAL_POTRAIT";
    public static final String HORIZONTAL_POSTER = "HORIZONTAL_POSTER";
    public static final String HORIZONTAL_SQUARE = "HORIZONTAL_SQUARE";
    public static final String HORIZONTAL_CIRCLE = "HORIZONTAL_CIRCLE";
    public static final String KEY_DFP_ADS = "DFP";
    public static final String KEY_MREC = "MREC";
    public static final String KEY_BANNER = "BANNER";
    public static final String KEY_TABLET = "TAB";
    public static final String KEY_PHONE = "PHONE";
    public static final String KEY_CONTENT_PREFRENCES = "ContentPrefrences";
    public static final String KEY_CONTINUE_WATCHING = "ContinueWatching";
    public static final String KEY_RECOMMENDED = "RECOMMENDED";
    public static final String KEY_FB_FAN = "FAN-ANDROID-NATIVE-";
    public static final String KEY_FB_FANWEB = "FAN-WEB-NATIVE-";
    public static final String KEY_FB_FANIOS = "FAN-IOS-NATIVE-";
    public static final String KEY_FB_FANTABLET = "FAN-TABLET-NATIVE-";


    public static String TYPE1 = "CAROUSEL";
    public static String TYPE2 = "CIRCLE";
    public static String TYPE3 = "PORTRAIT";
    public static String TYPE4 = "SQUARE";
    public static String TYPE5 = "LANDSCAPE";
    public static String TYPE6 = "CONTINUE_WATCHING";
    public static String TYPE7 = "RECOMMENDED";
    public static String TYPE8 = "UGCIFP";
    public static final String TYPE9 = "FAN";
    public static final String TYPE10 = "SERIESBANNER";
    public static final String TYPE11 = "DFP_BANNER";
    public static String POSTER = "POSTER";

    public static final int Rail1 = 0;
    public static final int Rail2 = 1;
    public static final int Rail3 = 2;
    public static final int Rail4 = 3;
    public static final int Rail5 = 4;
    public static final int Rail6 = 5;
    public static final int Rail7 = 6;
    public static final int Rail8 = 7;
    public static final int Rail9 = 8;
    public static final int Rail10 = 9;
    public static final int Rail11 = 10;
    public static final int Rail12 = 11;

    public static String WIDTH = "/width/";
    public static String HEIGHT = "/height/";
    public static String QUALITY = "/quality/100";
    public static String WEBP_URL = "https://d33ziwki8ny9c1.cloudfront.net/filters:format(webp):quality(60)/";

    public static int YOU_MAY_LIKE = 1;
    public static int SIMILAR_MOVIES = 2;
    public static int CONTINUE_WATCHING = 3;
    public static final int CATEGORY = 4;
    public static final int WEB_EPISODE = 5;

    public static final int SPOTLIGHT_EPISODE = 6;
    public static final int SIMILLAR_UGC_VIDEOS = 7;
    public static final int SIMILLAR_UGC_CREATOR = 8;
    public static final int LIVE_CHANNEL_LIST = 9;
    public static final int SIMILLAR_CHANNEL_LIST = 10;

    /*public static String OVP_API_KEY = "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE1NjUzNjMwNzMsInN1YiI6IkFwcC1ob2VqbWNncGRvIiwiaXNzIjoiU1lTVEVNIn0.fL3BzqDsIQK98rUr0m5DSGkLbXP-G0p2xPP9MvuebHSGnpT_K5wsQA16MvflOpjGxJg0Y3zxjNkIVtOebZfNLA";
    public static String BASE_URL = "https://167yl6nndd.execute-api.ap-south-1.amazonaws.com/Prod/";
    public static String OVP_BASE_URL = "";
    public static String SUBSCRIPTION_BASE_URL = "";
    public static String API_KEY_MOB = "CqZXGMdFAE1vnBnR3IAycaDGmYlKZa5I2aa6ol0F";*/
    public static String OVP_API_KEY = "emrdlkycyjzwzzsgeeeliudfcthlzeiabrogpiwf";
    public static String BASE_URL = "https://tddsy70es4.execute-api.ap-southeast-1.amazonaws.com/enveu_prod/";
    public static String OVP_BASE_URL = "";
    public static String SUBSCRIPTION_BASE_URL = "";
    public static String API_KEY_MOB = "bxwlpovuvcefasfegfonsxtzgzzyggrelwoyqebo";
    public static String API_KEY_TAB = "pdmlwglmihraeslyzcbluzxgthrlvjlpllgqumsc";
    public static final String KS_EXPIRE = "500016";
    public static final String WATCHLIST_PARTNER_TYPE = "1";
    public static final String FOLLOW_USER_PARTNER_TYPE = "2";
    public static final int INDICATOR_BOTTOM = 25;
    public static String SORT_VALUE = "";
    public static final int PORTRAIT_SPACING = 6;
    public static final int LANSCAPE_SPACING = 8;
    public static final int SPAN_COUNT_PORTRAIT = 3;
    public static final int SPAN_COUNT_LANDSCAPE = 2;

    public static final int TYPE_LANGUAGE =1;
    public static final int TYPE_GENRE =2;


}
