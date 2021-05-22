package com.astro.sott.thirdParty.fcm;

import android.content.Context;
import android.os.Bundle;

import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NavigationItem;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kaltura.client.types.Asset;

public class FirebaseEventManager {
    private static final String VIEW_SCREEN = "view_screen";
    private static final String SCREEN_NAME = "screen_name";


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

    public void viewItemEvent(String itemList, Asset asset, Context context) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, itemList); //e.g TV Shows Top Slider
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, AssetContent.getGenredataString(asset.getTags()));
            bundle.putString("item_subgenre", AssetContent.getSubGenredataString(asset.getTags()));
            if (asset.getType() == MediaTypeConstant.getProgram(context) || asset.getType() == MediaTypeConstant.getLinear(context)) {
                bundle.putString("item_language", AssetContent.getChannelLanguage(asset));
            } else {
                bundle.putString("item_language", AssetContent.getLanguageDataString(asset.getTags(), context));
            }
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, asset.getId() + "");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, asset.getName());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
        } catch (Exception e) {

        }
    }

    public void clickMyListButtonEvent(String itemList, Asset asset, Context context) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, itemList); //e.g TV Shows Top Slider
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, AssetContent.getGenredataString(asset.getTags()));
            bundle.putString("item_subgenre", AssetContent.getSubGenredataString(asset.getTags()));
            if (asset.getType() == MediaTypeConstant.getProgram(context) || asset.getType() == MediaTypeConstant.getLinear(context)) {
                bundle.putString("item_language", AssetContent.getChannelLanguage(asset));
            } else {
                bundle.putString("item_language", AssetContent.getLanguageDataString(asset.getTags(), context));
            }
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, asset.getId() + "");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, asset.getName());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
        } catch (Exception e) {

        }
    }
}
