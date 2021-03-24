package com.astro.sott.utils.helpers;

import com.astro.sott.BuildConfig;

public interface AppLevelConstants {
    String VOLTE = "VOLTE";
    String HBB = "HBB";
    String MOBILE_MBB = "MOBILE";
    String MBB = "MBB";
    String DTV = "DTV";
    String MBB_ACCOUNT = "MBB";
    String HBB_ACCOUNT_DESCRIPTION = "HBB account";
    String HBB_ACCOUNT = "HBB";
    String PARENTAL_LEVEL = "ParentalLevels";
    String AUDIO_LANGUAGE = "AudioLanguages";
    String ALL = "All";
    String THIRTEEN_PLUS = "13+";
    String EIGHTEEN_PLUS = "18+";
    String DEFAULT_PARENTAL_LEVEL = "DefaultParentalLevel";
    String PARENTAL_DESCRIPTION = "ParentalDescription";
    String PARENTAL_MAPPING = "ParentalMapping";
    String MY_DIALOG_ENCRYPTION_KEY = "MyDialogKey";
    String PROGRAM_NAME = "program_name";
    String ACTICITY_NAME = "ParentalControl";
    String PARENTAL_SWITCH = "ParentalSwitch";
    String TITLE = "title";
    String MESSAGE = "message";
    String POSITIVE_BUTTON_TEXT = "positiveButtonText";
    String FORCE = "force";
    String NO_RESULT_FOUND = "noResultFound";
    String TRUE = "true";
    String MDATE = "mDate";
    String LAYOUT_TYPE = "layouttype";
    String ASSET_COMMON_BEAN = "assetCommonBean";
    String MOVIE_DESCRIPTION_ACTIVITY = "MovieDescriptionActivity";
    String WEB_EPISODE_DESCRIPTION_ACTIVITY = "WebEpisodeDescriptionActivity";
    String BOX_SET_DETAIL = "BoxSetDetailActivity";
    String LIVE_CHANNEL = "LiveChannel";
    String SHORT_FILM_ACTIVITY = "ShortFilmActivity";
    String WEB_SERIES_DESCCRIPTION_ACTIVITY = "WebSeriesDescriptionActivity";
    String OK = "OK";
    String SUCCESS = "SUCCESS";
    String DIALOG_TV = "Dialog TV";
    String BROAD_BAND_HBB = "Home Broadband";
    String BROAD_BAND_HBB_ACCOUNT = "Home Broadband Account";
    String MOBILE_ACCOUNT = "Mobile Account";
    String DASH_WIDEVINE = "Dash_widevine";
    int TAB_HOME = 0;
    int TAB_LIVE = 1;
    int TAB_VIDEO = 2;
    int TAB_MOVIE_DESCRIPTION = 3;
    int TAB_SHORTFILM_DESCRIPTION = 4;
    int TAB_DRAMA_DETAILS = 5;
    int TAB_DRAMA_EPISODES_DETAILS = 6;
    int TAB_LIVETV_DETAIL = 7;
    int TAB_FORWARDED_EPG_DETAIL = 8;
    String EMAIL_MOBILE_KEY = "emailMobile";
    String FROM_KEY = "from";
    String TYPE_KEY="type";
    String FILE_ID_KEY="file_id";
    String OLD_PASSWORD_KEY = "oldPassword";
    String PASSWORD_KEY = "password";

    String SIGN_UP = "SignUp";
    String CONTINUE = "Continue";
    String ALREADY_USER = "alreadyUser";
    String SIGN_IN = "SignInFragment";
    String DONT_HAVE_ACCOUNT = "dont_have_account";

    int SPAN_COUNT_PORTRAIT = 3;
    int SPAN_COUNT_SQUARE_TAB = 5;
    int SPAN_COUNT_LANDSCAPE = 2;
    int SPAN_COUNT_LANDSCAPE_TAB = 4;
    int PORTRAIT_SPACING = 14;
    int PORTRAIT_SPACING_TAB = 14;
    int LANSCAPE_SPACING = 8;
    int LANSCAPE_SPACING_TAB = 8;

    int SPAN_COUNT_SQUARE = 4;
    //Device management
    String DEVICE_EXISTS = "1015";

    //kaltura error codes
    String KS_EXPIRE = "500016";
    String ALREADY_FOLLOW_ERROR = "8013";
    String ALREADY_UNFOLLOW_ERROR = "8012";
    String LOGGED_OUT_ERROR_CODE = "1019";
    String DEVICE_EXIST_ERROR_CODE = "1016";
    String PLAYER = "PLAYER";
    String STATUS_READ = "READ";
    String STATUS_UNREAD = "UNREAD";
    String STATUS_DELETED = "DELETED";

    String CHANGE_STATUS_READ = "Read";
    String CHANGE_STATUS_DELETED = "Deleted";

    String KSQL_GENRE_END = "' Genre='";
    String PROMO_ASSET_NAME = "name";
    String PARENTAL_RATING = "ParentalRating";
    String BILLING_ID = "BillingId";

    String KEY_SEASON_NUMBER = "SeasonNumber";
    String KEY_LONG_DESCRIPTION = "LongDescription";
    String KEY_SHORT_DESCRIPTION = "ShortDescription";

    String KEY_EPISODE_NUMBER = "EpisodeNumber";
    String KEY_TITLE_SORT = "TitleSortName";


    String KEY_EXTERNAL_URL = "External URL";
    String KEY_PROGRAM_ID = "Program Id";
    String KEY_PROMO_MEDIA_ID = "Promo Media Id";
    String KEY_BASE_ID = "Base ID";

    String KEY_EXTERNAL_REF_ID = "ExternalReferenceId";

    String KEY_REF_ID = "Ref Id";
    String YEAR = "Year";
    String IS_SPONSORED = "IsSponsored";
    String PLAYBACK_START_DATE = "PlaybackStartDate";
    String PLAYBACK_END_DATE = "PlaybackEndDate";

    String NOADS = "NoAds";

    String XOFFERWINDOW = "XOfferWindow";
    String RAIL_DATA_OBJECT = "railData";
    String PROGRAM_ASSET = "program_asset";


    String FORWARDED_EPG = "FORWARDED_EPG";
    String KSQL_GENRE = "Genre='";
    //
    String KEY_SERIES_ID = "SeriesID";
    int PARTNER_ID = 3209;

    String HLS = "HLS_Main";
    String KEY_GENRE = "Genre";
    String KEY_KEYWORD = "Keywords";

    String KEY_SUB_GENRE = "SubGenre";


    String KEY_MAINCAST = "Main Cast";
    String KEY_LANGUAGE = "Languages";
    String KEY_PARENT_REF_ID = "ParentRefId";


    String KEY_SUBTITLE_LANGUAGE = "SubtitleLanguages";
    String KEY_BILLING_ID = "BillingId";

    String KEY_CAST_ACTOR = "Actors";
    String KEY_PARENTREF_ID = "TrailerParentRefId";
    String KEY_VIDEO_RESOLUTION = "VideoResolution";
    String KEY_DIRECTOR = "Director";
    String PROVIDER = "Provider";
    String RIBBON = "Ribbon";
    String WATCHLIST_PARTNER_TYPE = "1";
    String MEDIATYPE_SEARCH_MOVIE = "Movie";
    String MEDIATYPE_SERIES = "Series";
    String MEDIATYPE_EPISODE = "Episode";
    String MEDIATYPE_COLLECTION = "Collection";
    String MEDIATYPE_SEARCH_WEBEPISODE = "WebEpisode";
    String MEDIATYPE_SEARCH_LINEAR = "Linear";
    String MEDIATYPE_SEARCH_SHORTFILM = "ShortFilm";
    String C_W_PREFERENCES = "c_watching";
    String C_W_IDS_PREFERENCES = "c_ids_watching";
    String KEY_CONTINUE_WATCHING = "ContinueWatching";
    String KEY_RECOMMENDED = "RECOMMENDED";
    String WIDTH = "/width/";
    String HEIGHT = "/height/";
    String QUALITY = "/quality/100";
    String KEY_PROVIDER = "Provider";
    String KEY_PROVIDER_EXTERNAL_CONTENT_ID = "ProviderExternalContentID";


    String TYPE1 = "CAROUSEL";
    String TYPE2 = "CIRCLE";
    String TYPE3 = "PORTRAIT";
    String TYPE4 = "SQUARE";
    String TYPE5 = "LANDSCAPE";
    String TYPELDS = "LDS";
    String TYPE6 = "CONTINUE_WATCHING";
    String TYPE7 = "RECOMMENDED";
    String TYPEPOSTER = "POSTER";
    String TYPEPR2 = "PR2";
    int Rail1 = 0;
    int Rail2 = 1;
    int Rail3 = 2;
    int Rail4 = 3;
    int Rail5 = 4;
    int Rail6 = 5;
    int Rail7 = 6;


    String MOVIE = "Movie";
    String WEB_SERIES = "WebSeries";
    String WEB_EPISODE_STRING = "WebEpisode";
    String SHORT_FILM = "ShortFilm";
    String LINEAR = "Linear";
    String TRAILER = "Trailer";
    String ENLGISH = "English";
    String CATCHUP = "Catchup";
    String SELF = "Self";
    String PAPARE = "http://www.thepapare.com/";
    String BALAJI = "com.balaji.alt.dialog";
    String DIALOG_MY_TV = "com.dialog.aptv";
    String EROS_NOW = "https://go.onelink.me/app/enviu";
    String GURU = "https://guru.lk/";
    String BONGO = "com.bongo.bongosl";
    String IFLIX = "https://play.iflix.com/?utm_source=dialog";
    String ZEE_5 = "com.graymatrix.did";
    String MUSIC_SAFARI = "com.atechnos.musicsafari";
    String KIKI = "lk.mobilevisions.kiki";
    String VINDANA = "com.evoke.vindanaSL";
    String VIU_TUBE = "http://www.viutube.lk";
    String YUPP_TV = "https://play.google.com/store/apps/details?id=com.tru";
    String GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=";
    String MARKET_DETAIL_ID = "market://details?id=";
    String DA_VINCI = "http://viu.lk/uc.php";
    String HOPSTER = "http://viu.lk/uc.php";
    String HUNGAMA = "com.hungama.movies";
    String HTTP = "http";
    String APP_SETTINGS = "App Settings";
    String DEVICE_MANAGEMENT = "Device Management";

    String LOGOUT = "Logout";
    String LOGIN = "Login";
    String TNC = "Terms & conditions";
    String WEBVIEW = "Webview";
    String HELP = "Help";
    String ACCOUNT_SETTINGS = "Account Settings";
    String PLAYLIST = "PlayList";
    String KEY_CONTENT_PREFRENCES = "ContentPrefrences";
    String DTV_ACCOUNT = "DTV";
    String DTV_ACCOUNT_DESCRIPTION = "DTV account";
    String MBB_ACCOUNT_DESCRIPTION = "MBB account";
    String DELETE = "Delete";
    String EDIT = "Edit";
    String MY_FAVORITE = "My Favorite";
    int FOR_PURCHASED_ERROR = 1001;
    int USER_ACTIVE_ERROR = 1002;
    int NO_ERROR = 0;
    int GEO_LOCATION_ERROR = 1003;
    int PARENTAL_BLOCK = 1004;
    int NO_MEDIA_FILE = 1005;

    String DEVICE_NAME = "device_name";
    String SMART_PHONE = "Smart Phone";
    String FCM_TOKEN = "fcmRefreshToken";
    String YOUBORA = "Youbora";
    String PHOENIX_ANALYTICS = "PhoenixAnalyticsPlugin.factory.getName()";
    String AUTO = "Auto";
    String LOW = "Low Quality";
    String DEFAULT = "Default";
    String MEDIUM = "Medium Quality";
    String HIGH = "High Quality";
    String MOBILE = "Mobile";
    String TAG_FRAGMENT_ALERT = "fragment_alert";
    String PROGRAM_CLICKED = "asdas";
    String PROGRAM_VIDEO_CLICLKED = "Program VideoItemClicked";
    String NEGATIVE_BUTTON_TEXT = "negativeButtonText";
    long RESEND_PIN_DELAY = 30000;
    long SHARE_DIALOG_DELAY = 3000;
    String DMS_RESPONSE = "DMS_Response";
    String FOR_PURCHASE = "for_purchase";
    String SUBSCRIPTION_PURCHASE = "subscription_purchased";
    String PAYMENT_GATEWAY_EXTERNAL_ID = "DPG";
    String FRAGMENT_NAME = "fragmentName";
    String FRAGMENT_TYPE = "fragmentType";
    String USER_NAME = "userName";
    String BEARER = "Bearer ";
    //https://rest-sgs1.ott.kaltura.com/restful_v5_1/api_v3/

    String QA_PHOENIX_URL = "https://rest-sgs1.ott.kaltura.com/restful_v5_1/api_v3/";


    int YOU_MAY_LIKE = 1;
    int SIMILAR_MOVIES = 2;
    int CONTINUE_WATCHING = 3;
    int CATEGORY = 4;
    int WEB_EPISODE = 5;
    int SPOTLIGHT_EPISODE = 6;
    int PROMO = 0;

    int LIVE_CHANNEL_LIST = 9;
    int SIMILLAR_CHANNEL_LIST = 10;
    String OTP = "otp";
    String DTV_ACCOUNT_NUMBER = "dtv";
    String MBB_ACCOUNT_NUMBER = "mbb";
    String ACCOUNT_NUMBER = "accountnumber";
    long COUNT_DOWN_TIMER = 1000;
    String KEY_RECOMMENDED_TITLE = "Recommended";
    String KEY_CONTENT_PREFRENCES_F_PARENTAL = "ParentalRestrictions";
    String KEY_USER_TYPE = "userOrigin";

    String KEY_PARENTAL_STATUS_ACTIVE = "active";
    String KEY_PARENTAL_STATUS_INACTIVE = "inactive";
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


    String SPECTRUM = "com.spectrum.moviebox";
    String ID = "id";
    String Title = "name";
    String DESCRIPTION = "description";
    String SCREEN_NAME = "screenname";
    String PROGRAM = "Program";
    String KEY_DVR_ENABLED = "LongTSTVEnabled";
    String KEY_PURCHASE_ALLOWED = "OTTPurchaseAllowed";

    String DTVFRAGMENT = "dtvFragment";
    String MBB_FRAGMENT = "mbbFragment";
    String CHANGEFRAGMENT = "changeFragment";
    String DELETEFRAGMENT = "deleteFragment";
    String FRAGMENTTYPE = "type";
    String DTV_ACCOUNT_NUM = "dtvAccountNum";
    String MBB_ACCOUNT_NUM = "mbbAccountNum";
    String CHANNEL = "Horizon";
    String REQUESTED_USER = "HorUsr";


    String CHANGE = "Change";
    String YES = "yes";
    String DELETE_SUCCESS = "deletesuccess";
    String SCREENFROM = "successmessage";
    String ADDSUCCESS = "AddSuccess";
    String CHANGESUCCESS = "ChangeScuuess";
    String CHANGESUCCESSDIALOG = "ChangeSuccessDialog;";
    String JWT_ISSUER = "dialog.com";
    String JWT_ALGO = "RSA";
    String DEVICE = "android";
    String TXN_ID = "txnId";
    String DELETE_FRAGMENT = "DeleteFragment";
    String FIREBASE_SCREEN = "firebase_screen";
    String CHANGE_FRAGMENT = "ChangeFragment";
    String OTP_FRAGMENT = "DTVAccountOtpFragment";
    String SUCCESS_FRAGMENT = "SuccessFragment";

    String DTV_TYPE_FRAGMENT = "dtv";
    String MBB_TYPE_FRAGMENT = "mbb";
    String HBB_TYPE_FRAGMENT = "hbb";
    String MOBILE_NUMBER = "Mobile Number";
    String DTV_NUMBER = "DTV Number";
    String ADD_TO_BILL = "Pay via";
    String DEDUCT_FROM_BILL = "Deduct From Bill";
    String NOT_ENTITLED = "3032";
    String RECORDING_NOT_FOUND = "3039";
    String PROGRAM_NOT_EXIST = "4022";
    String DEVICE_NOT_IN_DOMAIN = "1003";
    String RECORDING_STATUS_NOT_VALID = "3043";
    String ADAPTER_APP_FAILURE = "6012";
    String ADAPETR_NOT_EXIST = "10000";
    String ADAPETR_URL_REQUIRED = "5013";
    String USER_SUSPENDED = "2001";
    String COUCURENCY_LIMITATION = "4001";
    String INVALID_ASSET_TYPE = "4021";
    String PROGRAM_DOES_NOT_EXIST = "4022";
    String ACTION_NOT_RECOGNISED = "4023";
    String INVALID_ASSET_ID = "4024";
    String USER_NOT_EXIST_IN_DOMAIN = "1020";
    String INVALID_USER = "1026";
    String INVALID_ASSET_TYPEE = "4021";
    String INVALID_KS = "500015";
    String KS_EXPIRED = "500016";

    String NON_DIALOG = "Non-Dialog";
    String DIALOG = "Dialog";
    String BASE_ID = "Base ID";
    String REASEN_CODE = "reasonCode";

    String SEARCH_GENRE_CONSTATNT = "FilterGenre='";
    String SEARCH_ISSPONSORED_CONSTATNT = "IsSponsored";
    String FILTER_LANGUAGE_CONSTANT = "FilterLanguage='";


}
