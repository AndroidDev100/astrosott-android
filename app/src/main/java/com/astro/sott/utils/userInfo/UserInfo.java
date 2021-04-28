package com.astro.sott.utils.userInfo;

import android.content.Context;

import com.astro.sott.utils.helpers.SharedPrefHelper;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;

public class UserInfo {
    private String accessToken;
    private String refreshToken;
    private String externalSessionToken;
    private boolean active;

    private boolean passwordExists;

    private String firstName;
    private String lastName;
    private String userName;
    private String alternateUserName;


    private String email;
    private String cpCustomerId;
    private String mobileNumber;

    public String getMobileNumber() {
        return session.getString("mobileNumber", "");
    }

    public void setMobileNumber(String mobileNumber) {
        session.setString("mobileNumber", mobileNumber);
    }

    public boolean isPasswordExists() {
        return session.getBoolean("passwordExists", false);
    }

    public void setPasswordExists(boolean passwordExists) {
        session.setBoolean("passwordExists", passwordExists);
    }


    public String getCpCustomerId() {
        return cpCustomerId;
    }

    public void setCpCustomerId(String cpCustomerId) {
        this.cpCustomerId = cpCustomerId;
    }

    public String getEmail() {
        return session.getString("AstroEmail", "");

    }


    public void setEmail(String email) {
        session.setString("AstroEmail", email);
    }


    public String getUserName() {
        return session.getString("AstroUserName", "");
    }

    public void setUserName(String userName) {
        session.setString("AstroUserName", userName);
    }

    public String getAlternateUserName() {
        return session.getString("AstroAlternateUserName", "");
    }

    public void setAlternateUserName(String alternateUserName) {
        session.setString("AstroAlternateUserName", alternateUserName);
    }

    public String getLastName() {
        return session.getString("AstrolastName", "");

    }

    public void setLastName(String lastName) {
        session.setString("AstrolastName", lastName);
    }

    public String getFirstName() {
        return session.getString("AstrofirstName", "");
    }

    public void setFirstName(String firstName) {
        session.setString("AstrofirstName", firstName);
    }

    public boolean isActive() {
        return session.getBoolean("astroUserActive", false);
    }

    public void setActive(boolean activeUser) {
        session.setBoolean("astroUserActive", activeUser);
    }

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
