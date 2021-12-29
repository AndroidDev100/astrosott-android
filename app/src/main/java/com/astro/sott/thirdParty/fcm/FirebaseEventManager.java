package com.astro.sott.thirdParty.fcm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NavigationItem;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kaltura.client.types.Asset;

public class FirebaseEventManager {
    private static final String VIEW_SCREEN = "view_screen";
    private static final String SCREEN_NAME = "screen_name";
    public static final String SEARCH = "Search";
    public static final String PROFILE = "Profile";
    public static final String LANGUAGE_SETTINGS = "Language Settings";
    public static final String TRANSACTION_HISTORY = "Transaction History";
    public static final String EDIT_PROFILE = "Edit Profile";
    public static final String EDIT_EMAIL = "Edit Email";
    public static final String EDIT_PASSWORD = "Edit Password";
    public static final String APP_LANGUAGE = "App Language";
    public static final String SUBTITLE_LANGUAGE = "Subtitle Language";
    public static final String AUDIO_LANGUAGE = "Audio Language";
    public static final String SIGN_UP = "Sign Up";
    public static final String LOGIN = "Login";
    public static final String ITEM_CHANNEL = "item_channel";
    public static final String PROGRAM_DATE = "program_date";
    public static final String PROGRAM_TIME = "program_time";


    public static final String SHARE = "share";

    public String searchString = "";
    public String sponsorDetailName="";
    private String relatedAssetName = "";
    public long liveEventProgramDateTime=0;

    public void setSponsorDetailName(String sponsorDetailName) {
        this.sponsorDetailName = sponsorDetailName;
    }

    public void setLiveEventProgramDateTime(long liveEventProgramDateTime) {
        this.liveEventProgramDateTime = liveEventProgramDateTime;
    }

    public static final String TRX_VIP = "trx_vip";
    public static final String MANAGE_SUBSCRIPTION = "Manage Subscription";


    public static final String MANAGE_DEVICES = " Manage Devices";

    public static final String ADD_MY_LIST = "add_mylist";

    public static final String LOGOUT = "Log Out";
    public static final String HELP = "Help";


    public static final String MANAGE_SUBSCRIBE = "Manage Subscriptions / Subscribe";


    public static final String FITLER = "Filter";
    public static final String BTN_CLICK = "btn_click";
    public static final String SEARCH_BY_KEYWORD = "Search by Keyword";
    public static final String SEARCH_BY_HISTORY = "Search by History";
    public static final String SEARCH_BY_GENRE = " Search by Genre";
    public static final String WATCH = "watch";
    public static final String TXN_SUCCESS = "trx_success";

    public boolean subscribeClicked = false;

    public static final String VIEW_SEARCH_RESULT = "view_search_results";


    private static FirebaseAnalytics mFirebaseAnalytics;
    private static FirebaseEventManager firebaseEventManager;

    public static FirebaseEventManager getFirebaseInstance(Context context) {
        if (firebaseEventManager == null) {
            firebaseEventManager = new FirebaseEventManager();
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        return firebaseEventManager;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void trackScreenName(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(SCREEN_NAME, screenName);
        mFirebaseAnalytics.logEvent(VIEW_SCREEN, bundle);
    }


    public void navEvent(String itemList, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, itemList);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, title);
        mFirebaseAnalytics.logEvent("navigation", bundle);
    }


    public void itemListEvent(String itemList, String title, String eventName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, itemList);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, title);
        mFirebaseAnalytics.logEvent(eventName, bundle);
    }

    public String getRelatedAssetName() {
        return relatedAssetName;
    }

    public void setRelatedAssetName(String relatedAssetName) {
        this.relatedAssetName = relatedAssetName;
    }

    public void languageBtnEvent(String lang, String category) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, " Language Settings");
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, lang);
        mFirebaseAnalytics.logEvent(BTN_CLICK, bundle);
    }

    public void userLoginEvent(String customerId, String userType, String signupMethod) {
        Bundle bundle = new Bundle();
        if (!signupMethod.equalsIgnoreCase("Mobile")) {
            bundle.putString("sign_up_method", signupMethod);
        } else {
            bundle.putString("sign_up_method", "Mobile Number");
        }
        bundle.putString("user_id", customerId);
        bundle.putString("user_type", userType); // e.g VIP,  Registered User etc
        mFirebaseAnalytics.logEvent("login", bundle);
        mFirebaseAnalytics.setUserProperty("user_id", customerId);
        mFirebaseAnalytics.setUserId(customerId);
    }

    public void viewItemEvent(String itemList, Asset asset, Context context) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, itemList); //e.g TV Shows Top Slider
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, AssetContent.getGenredataString(asset.getTags()));
            bundle.putString("item_subgenre", AssetContent.getSubGenredataString(asset.getTags()));
            bundle.putString("item_language", AssetContent.getLanguageDataStringForCleverTap(asset.getTags(), context));
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, asset.getId() + "");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, asset.getName());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
        } catch (Exception e) {

        }
    }

    public void sponsorViewItem(String itemList, Asset asset, Context context) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, AssetContent.getGenredataString(asset.getTags()));
            bundle.putString("item_subgenre", AssetContent.getSubGenredataString(asset.getTags()));
            bundle.putString("item_language", AssetContent.getLanguageDataStringForCleverTap(asset.getTags(), context));
            bundle.putString("screen_name",sponsorDetailName);
            bundle.putString("rail_name", itemList);
            if (asset.getType()==MediaTypeConstant.getLinear(context)&&AssetContent.isLiveEvent(asset.getMetas())){
                bundle.putString(ITEM_CHANNEL, asset.getName());
                bundle.putString(PROGRAM_DATE, AppCommonMethods.getLiveEventDate(liveEventProgramDateTime));
                bundle.putString(PROGRAM_TIME, AppCommonMethods.getLiveEventTime(liveEventProgramDateTime));
            }
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, asset.getId() + "");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, asset.getName());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
        } catch (Exception e) {

        }
    }

    public void searchViewItemEvent(String itemList, Asset asset, Context context) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, itemList); //e.g TV Shows Top Slider
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, AssetContent.getGenredataString(asset.getTags()));
            bundle.putString("item_subgenre", AssetContent.getSubGenredataString(asset.getTags()));
            bundle.putString("search_variant", searchString);
            bundle.putString("item_language", AssetContent.getLanguageDataStringForCleverTap(asset.getTags(), context));
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, asset.getId() + "");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, asset.getName());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
        } catch (Exception e) {

        }
    }

    public void clickButtonEvent(String eventName, Asset asset, Context context) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, "Content Action"); //e.g TV Shows Top Slider
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, AssetContent.getGenredataString(asset.getTags()));
            bundle.putString("item_subgenre", AssetContent.getSubGenredataString(asset.getTags()));
            bundle.putString("item_language", AssetContent.getLanguageDataStringForCleverTap(asset.getTags(), context));
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, asset.getId() + "");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, asset.getName());
            mFirebaseAnalytics.logEvent(eventName, bundle);
        } catch (Exception e) {

        }
    }

    public void packageEvent(String packageTitle, Long price, String eventName, String customerId) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, "Rental Subscription");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, packageTitle);
            Long amount = price / 1000000;
            bundle.putDouble("item_price", amount);
            bundle.putString("user_id", customerId);
            mFirebaseAnalytics.logEvent(eventName, bundle);
        } catch (Exception e) {
            Log.w("exc", e);
        }
    }

    public void liveButtonEvent(String eventName, Asset asset, Context context, String channelName) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, "Content Action"); //e.g TV Shows Top Slider
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, AssetContent.getGenredataString(asset.getTags()));
            bundle.putString("item_subgenre", AssetContent.getSubGenredataString(asset.getTags()));
            bundle.putString("item_language", AssetContent.getLanguageDataStringForCleverTap(asset.getTags(), context));
            if (!channelName.equalsIgnoreCase(""))
                bundle.putString("item_channel”", channelName);
            bundle.putString("item_date", AppCommonMethods.getFirebaseDate(asset.getStartDate()));
            bundle.putString("item_time", AppCommonMethods.getEndTime(asset.getStartDate()) + "-" + AppCommonMethods.getEndTime(asset.getEndDate()));
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, asset.getId() + "");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, asset.getName());
            mFirebaseAnalytics.logEvent(eventName, bundle);
        } catch (Exception e) {

        }
    }


    @SuppressLint("InvalidAnalyticsName")
    public void ralatedTabEvent(String eventName, Asset asset, Context context, String channelName) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, "Related-" + FirebaseEventManager.getFirebaseInstance(context).getRelatedAssetName()); //e.g TV Shows Top Slider
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, AssetContent.getGenredataString(asset.getTags()));
            bundle.putString("item_subgenre", AssetContent.getSubGenredataString(asset.getTags()));
            bundle.putString("item_language", AssetContent.getLanguageDataStringForCleverTap(asset.getTags(), context));
            if (!channelName.equalsIgnoreCase(""))
                bundle.putString("item_channel”", channelName);
            bundle.putString("item_date”", AppCommonMethods.getFirebaseDate(asset.getStartDate()));
            bundle.putString("item_time”", AppCommonMethods.getEndTime(asset.getStartDate()) + "-" + AppCommonMethods.getEndTime(asset.getEndDate()));
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, asset.getId() + "");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, asset.getName());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
        } catch (Exception e) {

        }
    }

    public void showTabEvent(Asset asset, Context context) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, "Shows-" + asset.getName()); //e.g TV Shows Top Slider
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, AssetContent.getGenredataString(asset.getTags()));
            bundle.putString("item_subgenre", AssetContent.getSubGenredataString(asset.getTags()));
            bundle.putString("item_language", AssetContent.getLanguageDataStringForCleverTap(asset.getTags(), context));
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, asset.getId() + "");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, asset.getName());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
        } catch (Exception e) {

        }
    }

    public void shareEvent(Asset asset, Context context) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, "Content Action"); //e.g TV Shows Top Slider
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, AssetContent.getGenredataString(asset.getTags()));
            bundle.putString("item_subgenre", AssetContent.getSubGenredataString(asset.getTags()));
            bundle.putString("item_language", AssetContent.getLanguageDataStringForCleverTap(asset.getTags(), context));
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, asset.getId() + "");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, asset.getName());
            mFirebaseAnalytics.logEvent(SHARE, bundle);
        } catch (Exception e) {

        }
    }

}
