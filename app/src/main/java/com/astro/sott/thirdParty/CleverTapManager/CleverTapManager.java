package com.astro.sott.thirdParty.CleverTapManager;

import android.content.Context;

import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.clevertap.android.sdk.CleverTapAPI;

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
}
