package com.astro.sott.thirdParty.CleverTapManager;

import android.content.Context;
import android.os.Build;

import com.astro.sott.R;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.clevertap.android.sdk.CleverTapAPI;
import com.kaltura.client.types.Asset;

import java.util.HashMap;

public class CleverTapManager {
    private static final String SIGN_OUT = "Signed Out";
    private static final String SIGN_IN_ORIGIN = "Signed In Origin";
    private static final String SIGN_IN_TYPE = "Signed In Type";
    private static final String SIGN_IN = "Signed In";
    public static final String DETAIL_PAGE_MY_LIST = "Details Page - My List";
    public static final String DETAIL_PAGE_BECOME_VIP = "Details Page - Become a VIP";
    public static final String DETAIL_PAGE_LOCK = "Details Page - Locked Episodes";
    public static final String PLAYER_LOCK = "Player - Locked Episodes";
    public static final String HOME = "Home";
    public static final String SUBSCRIPTION_PAGE = "Subscription Page";
    public static final String CONTENT_TITLE = "Content Title";
    public static final String MEDIA_WATCHED = "Media Watched";
    public static final String SOCIAL_SHARE = "Social Share";
    public static final String ADD_MY_LIST = "Add My list";

    public static final String CHANNEL_NAME = "Channel Name";
    public static final String CONTENT_ID = "Content ID";
    public static final String CONTENT_LICENSE_EXPIRY = "Content License Expiry";


    public static final String GENRE = "Genre";
    public static final String SUB_GENRE = "Sub-genre";
    public static final String CONTENT_TYPE = "Content Type";
    public static final String AUDIO_LANGUAGE = "Audio Language";
    public static final String SUBTITLE_LANGUAGE = "Subtitle Language";
    public static final String TOTAL_WATCHED_DURATION = "Total Watched Duration";
    public static final String DEVICE = "Device";
    public static final String OS = "OS";
    public static final String OS_VERSIONS = "OS Versions";
    public static final String DEVICE_MANUFACTURER = "Device Manufacturer";
    public static final String NETWORK_CARRIER = "CT Network Carrier";
    public static final String NETWORK_TYPE = "CT Network Type";
    public static final String CONNECTED_TO_WIFI = "CT Connected To WiFi";
    public static final String OFFER_NAME = "Offer Name";
    public static final String OFFER_ID = "Offer ID";
    public static final String OFFER_TYPE = "Offer Type";
    public static final String TRANSACTION_AMOUNT = "Transaction Amount";

    public static final String DATE_PURCHASE = "Date Purchase";
    public static final String PAYMENT_MODE = "Payment Mode";
    public static final String PAYMENT_STATUS = "Payment Status";
    public static final String PURCHASE_ORIGIN = "Purchase Origin";
    public static final String CHARGED = "Charged";


    private String loginOrigin = "";

    public void setLoginOrigin(String loginOrigin) {
        this.loginOrigin = loginOrigin;
    }

    public String getLoginOrigin() {
        return loginOrigin;
    }

    private static CleverTapManager cleverTapManager;

    public static CleverTapManager getInstance() {
        if (cleverTapManager == null)
            cleverTapManager = new CleverTapManager();
        return cleverTapManager;
    }


    public void setSignOutEvent(Context context) {
        CleverTapAPI.getDefaultInstance(context, AppCommonMethods.getDeviceId(context.getContentResolver())).pushEvent(SIGN_OUT);
    }

    public void setSignInEvent(Context context, String origin, String type) {
        HashMap<String, Object> signInAction = new HashMap<String, Object>();
        signInAction.put(SIGN_IN_ORIGIN, origin);
        signInAction.put(SIGN_IN_TYPE, type);
        CleverTapAPI.getDefaultInstance(context, AppCommonMethods.getDeviceId(context.getContentResolver())).pushEvent(SIGN_IN, signInAction);
    }

    public void mediaWatched(Context context, Asset asset, Boolean isLive) {
        HashMap<String, Object> mediaWatched = new HashMap<String, Object>();
        mediaWatched.put(CONTENT_TITLE, asset.getName());
        mediaWatched.put(CONTENT_ID, asset.getExternalId());
        mediaWatched.put(CHANNEL_NAME, asset.getName());
        mediaWatched.put(CONTENT_LICENSE_EXPIRY, AppCommonMethods.getDateCleverTap(asset.getEndDate()));
        mediaWatched.put(GENRE, AssetContent.getGenredataString(asset.getTags()));
        mediaWatched.put(SUB_GENRE, AssetContent.getSubGenredataString(asset.getTags()));
        mediaWatched.put(CONTENT_TYPE, AppCommonMethods.getAssetType(asset.getType(), context));
        if (isLive) {
            mediaWatched.put(AUDIO_LANGUAGE, AssetContent.getChannelLanguage(asset));
        } else {
            mediaWatched.put(AUDIO_LANGUAGE, AssetContent.getLanguageDataStringForCleverTap(asset.getTags(), context));
        }

        mediaWatched.put(SUBTITLE_LANGUAGE, AssetContent.getSubTitleLanguageDataString(asset.getTags(), context));
        if (!isLive)
            mediaWatched.put(TOTAL_WATCHED_DURATION, AppCommonMethods.getDurationFromUrl(asset));
        if (context.getResources().getBoolean(R.bool.is_tablet)) {
            mediaWatched.put(DEVICE, "Android");
        } else {
            mediaWatched.put(DEVICE, "Android_Tablet");
        }
        mediaWatched.put(OS, "Android");
        int currentVer = Build.VERSION.SDK_INT;
        mediaWatched.put(OS_VERSIONS, currentVer);
        mediaWatched.put(DEVICE_MANUFACTURER, Build.MANUFACTURER);

        mediaWatched.put(NETWORK_CARRIER, AppCommonMethods.getCarrier(context));
        if (NetworkConnectivity.isWifiConnected(context)) {
            mediaWatched.put(CONNECTED_TO_WIFI, true);
        } else {
            mediaWatched.put(CONNECTED_TO_WIFI, false);
        }
        CleverTapAPI.getDefaultInstance(context, AppCommonMethods.getDeviceId(context.getContentResolver())).pushEvent(MEDIA_WATCHED, mediaWatched);
    }

    public void charged(Context context, String offerName, String offerId, String offerType, Long transactionAmount, String paymentMode, String paymentStatus, String origin) {
        HashMap<String, Object> charged = new HashMap<String, Object>();
        charged.put(OFFER_NAME, offerName);
        charged.put(OFFER_ID, offerId);
        //  charged.put(OFFER_TYPE, offerType);
        charged.put(DATE_PURCHASE, AppCommonMethods.getCurrentDate());
        //1000000
        Long amount = transactionAmount / 1000000;
        charged.put(TRANSACTION_AMOUNT, amount);
        charged.put(PAYMENT_MODE, paymentMode);
        charged.put(PAYMENT_STATUS, paymentStatus);
        charged.put(PURCHASE_ORIGIN, origin);
        CleverTapAPI.getDefaultInstance(context, AppCommonMethods.getDeviceId(context.getContentResolver())).pushEvent(CHARGED, charged);
    }

    public void socialShare(Context context, Asset asset, Boolean isLive) {
        HashMap<String, Object> mediaWatched = new HashMap<String, Object>();
        mediaWatched.put(CONTENT_TITLE, asset.getName());
        mediaWatched.put(CONTENT_ID, asset.getExternalId());
        mediaWatched.put(GENRE, AssetContent.getGenredataString(asset.getTags()));
        mediaWatched.put(SUB_GENRE, AssetContent.getSubGenredataString(asset.getTags()));
        mediaWatched.put(CONTENT_TYPE, AppCommonMethods.getAssetType(asset.getType(), context));
        CleverTapAPI.getDefaultInstance(context, AppCommonMethods.getDeviceId(context.getContentResolver())).pushEvent(SOCIAL_SHARE, mediaWatched);
    }

    public void addMyList(Context context, Asset asset, Boolean isLive) {
        HashMap<String, Object> mediaWatched = new HashMap<String, Object>();
        mediaWatched.put(CONTENT_TITLE, asset.getName());
        mediaWatched.put(CONTENT_ID, asset.getExternalId());
        mediaWatched.put(CONTENT_LICENSE_EXPIRY, AppCommonMethods.getDateCleverTap(asset.getEndDate()));
        mediaWatched.put(GENRE, AssetContent.getGenredataString(asset.getTags()));
        mediaWatched.put(SUB_GENRE, AssetContent.getSubGenredataString(asset.getTags()));
        CleverTapAPI.getDefaultInstance(context, AppCommonMethods.getDeviceId(context.getContentResolver())).pushEvent(ADD_MY_LIST, mediaWatched);
    }
}
