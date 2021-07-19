package com.astro.sott.thirdParty.fcm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

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
    public static final String SHARE = "share";


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

    public void languageBtnEvent(String lang, String category) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, " Language Settings");
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, lang);
        mFirebaseAnalytics.logEvent(BTN_CLICK, bundle);
    }

    public void userLoginEvent(String customerId, String userType, String signupMethod) {
        Bundle bundle = new Bundle();
       /* if (signupMethod.equalsIgnoreCase("Facebook")||signupMethod.equalsIgnoreCase("Google")) {
            bundle.putString("sign_up_method", signupMethod);
        }else {*/
        bundle.putString("sign_up_method", "Evergent");

        /* }*/
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

            bundle.putString("item_language", AssetContent.getLanguageDataString(asset.getTags(), context));
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

            bundle.putString("item_language", AssetContent.getLanguageDataString(asset.getTags(), context));

            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, asset.getId() + "");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, asset.getName());
            mFirebaseAnalytics.logEvent(eventName, bundle);
        } catch (Exception e) {

        }
    }

    public void packageEvent(String packageTitle, String price, String eventName, String customerId) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, "Rental Subscription");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, packageTitle);
            bundle.putDouble("item_price", Double.parseDouble(price));
            bundle.putString("user_id", customerId);
            mFirebaseAnalytics.logEvent(eventName, bundle);
        } catch (Exception e) {

        }
    }

    public void liveButtonEvent(String eventName, Asset asset, Context context, String channelName) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, "Content Action"); //e.g TV Shows Top Slider
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, AssetContent.getGenredataString(asset.getTags()));
            bundle.putString("item_subgenre", AssetContent.getSubGenredataString(asset.getTags()));
            bundle.putString("item_language", AssetContent.getLanguageDataString(asset.getTags(), context));
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
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, "Related-" + channelName + ":" + asset.getName()); //e.g TV Shows Top Slider
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, AssetContent.getGenredataString(asset.getTags()));
            bundle.putString("item_subgenre", AssetContent.getSubGenredataString(asset.getTags()));
            bundle.putString("item_language", AssetContent.getLanguageDataString(asset.getTags(), context));
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
            bundle.putString("item_language", AssetContent.getLanguageDataString(asset.getTags(), context));
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
            bundle.putString("item_language", AssetContent.getLanguageDataString(asset.getTags(), context));
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, asset.getId() + "");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, asset.getName());
            mFirebaseAnalytics.logEvent(SHARE, bundle);
        } catch (Exception e) {

        }
    }

}
