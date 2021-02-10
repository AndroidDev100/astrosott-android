package com.astro.sott.utils.userInfo;

import android.content.Context;

import com.astro.sott.utils.helpers.SharedPrefHelper;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;

public class UserInfo {
    private String accessToken;
    private String refreshToken;
    private String externalSessionToken;

    public String getAccessToken() {
        return session.getString("accessToken", "");
    }

    public void setAccessToken(String accessToken) {
        session.setString("accessToken", accessToken);
    }

    public String getRefreshToken() {
        return session.getString("refreshToken", "");

    }

    public void setRefreshToken(String refreshToken) {
        session.setString("refreshToken", refreshToken);
    }

    public String getExternalSessionToken() {
        return session.getString("externalSessionToken", "");

    }

    public void setExternalSessionToken(String externalSessionToken) {
        session.setString("externalSessionToken", externalSessionToken);

    }

    private SharedPrefHelper session;
    private static UserInfo mInstance;


    public UserInfo(Context context) {
        if (session == null)
            session = SharedPrefHelper.getInstance(context);
    }


    public static UserInfo getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserInfo(context);
        }
        return mInstance;
    }
}
