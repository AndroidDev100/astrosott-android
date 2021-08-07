package com.astro.sott.utils.ksPreferenceKey;

import android.content.Context;

import com.astro.sott.utils.helpers.SharedPrefHelper;
import com.google.gson.Gson;
import com.kaltura.client.types.OTTUser;

public class KsPreferenceKey {

    private static final String USER = "User";
    private static final String DEFAULT_ENTITLEMENT = "DefaultEntitlement";
    private static final String ATBPAYMENTGATEWAYID = "ATBpaymentGatewayId";
    public static final String SUBSCRIPTION_OFFER = "SubscriptionOffer";
    private static final String DOWNLOAD_OVER_WIFI = "downloadOverWifi";
    private static final String AUDIO_LANGUAGE = "audio_language_astr";
    private static final String SUBTITLE_LANGUAGE = "subtitle_language_astr";
    public static final String AUTO_ROTATE = "auto_rotate";
    public static final String AUTO_DURATION = "auto_rotate_duration";
    public static final String GENRE_DATA = "genre_data";
    public static final String FILTER_SELECTED_GENRE = "FILTER_SELECTED_GENRE";
    public static final String FILTER_SELECTED_GENRE_VALUE = "FILTER_SELECTED_GENRE_VALUE";
    public static final String FILTER_SELECTED_LANGUAGE = "FILTER_SELECTED_LANGUAGE";
    public static final String FILTER_SELECTED_LANGUAGE_VALUES = "FILTER_SELECTED_LANGUAGE_VALUES";

    public static final String FILTER_SORT_BY = "FILTER_SORT_BY";
    public static final String FILTER_CONTENT_TYPE = "FILTER_CONTENT_TYPE";
    public static final String FILTER_FREE_PAID = "FILTER_FREE_PAID";
    public static final String FILTER_APPLY = "FILTER_APPLY";
    public static final String SEARCH_KSQL = "SEARCH_KSQL";

    public static final String LOW_BITRATE_MAX_LIMIT = "lowBitRateMaxLimit";
    public static final String MEDIUM_BITRATE_MAX_LIMIT = "mediumBitRateMaxLimit";
    public static final String HIGH_BITRATE_MAX_LIMIT = "highBitRatemaxLimit";

    public static final String AUDIO_LANG_INDEX = "AUDIO_LANG_INDEX";
    public static final String SUBTITLE_LANG_INDEX = "SUBTITLE_LANG_INDEX";


    public static final String AUDIO_LANG_KEY = "AUDIO_LANG_KEY";
    public static final String SUBTITLE_LANG_KEY = "SUBTITLE_LANG_KEY";


    private static KsPreferenceKey mInstance;
    private SharedPrefHelper session;
    private static final String REMINDER_ID = "reminder_id";
    private static final String CATCHUP_ID = "catchup_id";

    public KsPreferenceKey(Context context) {
        if (session == null)
            session = SharedPrefHelper.getInstance(context);
    }

    public static KsPreferenceKey getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new KsPreferenceKey(context);
        }
        return mInstance;
    }

    public String getKalturaPhoenixUrl() {
        return session.getString("kalturaPhoenixUrl", "");
    }

    public void setKalturaPhoenixUrl(String kalturaPhoenixUrl) {
        session.setString("kalturaPhoenixUrl", kalturaPhoenixUrl);
    }

    public String getKalturaPhoenixUrlForWaterMark() {
        return session.getString("KalturaPhoenixUrlForWaterMark", "");
    }

    public void setKalturaPhoenixUrlForWaterMark(String kalturaPhoenixUrl) {
        session.setString("KalturaPhoenixUrlForWaterMark", kalturaPhoenixUrl);
    }

    public String getHouseHoldId() {
        return session.getString("household_id", "");
    }

    public void setHouseHoldId(String houseHoldId) {
        session.setString("household_id", houseHoldId);
    }

    public String getSubtitleLanguage() {
        return session.getString(SUBTITLE_LANGUAGE, "");
    }

    public void setSubtitleLanguage(String userType) {
        session.setString(SUBTITLE_LANGUAGE, userType);
    }

    public String getClassName() {
        return session.getString("class_name", "");
    }

    public void setClassName(String className) {
        session.setString("class_name", className);
    }

    public void setAudioLanguage(String userType) {
        session.setString(AUDIO_LANGUAGE, userType);
    }

    public String getAudioLanguage() {
        return session.getString(AUDIO_LANGUAGE, "");
    }

    public String getUserType() {
        return session.getString("userType", "");
    }

    public void setUserType(String userType) {
        session.setString("userType", userType);
    }

    public String getHouseHoldDeviceLimit() {
        return session.getString("household_limit", "");
    }

    public void setHouseHoldDeviceLimit(String houseHoldDeviceLimit) {
        session.setString("household_limit", houseHoldDeviceLimit);
    }

    public String getUserLoginKs() {
        return session.getString("userlogin_ks", "");
    }

    public void setUserLoginKs(String userLoginKs) {
        session.setString("userlogin_ks", userLoginKs);
    }

    public String getQualityName() {
        return session.getString("video_quality_name", "");
    }

    public void setQualityName(String qualityName) {
        session.setString("video_quality_name", qualityName);
    }

    public String getAppLangName() {
        return session.getString("app_lang", "en");
    }

    public void setAppLangName(String appLangName) {
        session.setString("app_lang", appLangName);
    }

    public String getAudioLangKey() {
        return session.getString(AUDIO_LANG_KEY, "en");
    }

    public void setAudioLangKey(String appLangName) {
        session.setString(AUDIO_LANG_KEY, appLangName);
    }

    public String getSubTitleLangKey() {
        return session.getString(SUBTITLE_LANG_KEY, "en");
    }

    public void setSubTitleLangKey(String appLangName) {
        session.setString(SUBTITLE_LANG_KEY, appLangName);
    }

    public String getCatchUpId() {
        return session.getString("catchup_id", "");
    }

    public void setCatchUpId(String catchUpId) {
        session.setString("catchup_id", catchUpId);
    }

    public String getTokenId() {
        return session.getString("token_id", "");
    }

    public void setTokenId(String tokenId) {
        session.setString("token_id", tokenId);
    }

    public String getToken() {
        return session.getString("set_token", "");
    }

    public void setToken(String token) {
        session.setString("set_token", token);
    }

    public void setFCMToken(String s) {
        session.setString("fcmRefreshToken", s);
    }

    public boolean getDownloadOverWifi() {
        return session.getBoolean(DOWNLOAD_OVER_WIFI, true);
    }

    public void setDownloadOverWifi(boolean status) {
        session.setBoolean(DOWNLOAD_OVER_WIFI, status);
    }

    public int getContinueWatchingIndex() {
        return session.getInt("c_w_index", 0);
    }

    public void setContinueWatchingIndex(int continueWatchingIndex) {
        session.setInt("c_w_index", continueWatchingIndex);
    }

    public boolean getUserActive() {
        return session.getBoolean("useractive", false);
    }

    public void setUserActive(boolean b) {
        session.setBoolean("useractive", b);
    }

    public boolean getCatchupValue() {
        return session.getBoolean("catchup", false);
    }

    public void setCatchupValue(boolean b) {
        session.setBoolean("catchup", b);
    }


    public String getStartSessionKs() {
        return session.getString("startsession_ks", "");
    }

    public void setStartSessionKs(String startSessionKs) {
        session.setString("startsession_ks", startSessionKs);
    }

    public String getSMSURL() {
        return session.getString("sms_url", "");
    }

    public void setSMSURL(String SMSURL) {
        session.setString("sms_url", SMSURL);
    }

    public String getAnonymousks() {
        return session.getString("anonymousks", "");
    }

    public void setAnonymousks(String anonymousks) {
        session.setString("anonymousks", anonymousks);
    }

    public int getQualityPosition() {
        return session.getInt("video_quality_position", 0);
    }

    public void setQualityPosition(int qualityPosition) {
        session.setInt("video_quality_position", qualityPosition);
    }


    public int getExpiryTime() {
        return session.getInt("expiry_time", 0);
    }

    public void setExpiryTime(int expiryTime) {
        session.setInt("expiry_time", expiryTime);
    }

    public String getJwtToken() {
        return session.getString("jwt_token", "");
    }

    public void setJwtToken(String jwtToken) {
        session.setString("jwt_token", jwtToken);
    }


    public boolean getPrefrenceSelected() {
        return session.getBoolean("p_selected", false);
    }

    public void setPrefrenceSelected(boolean prefrenceSelected) {
        session.setBoolean("p_selected", prefrenceSelected);
    }

    public OTTUser getUser() {

        try {
            Gson gson = new Gson();
            String json = session.getString(USER, "");

            return gson.fromJson(json, OTTUser.class);
        } catch (Exception e) {
            System.out.println("Error : " + e.toString());
            return null;
        }

    }

    public void setUser(OTTUser ottUser) {

        Gson gson = new Gson();
        String json = gson.toJson(ottUser);
        session.setString(USER, json);
    }

    public String getDefaultEntitlement() {
        return session.getString(DEFAULT_ENTITLEMENT, "");
    }

    public void setDefaultEntitlement(String defaultEntitlement) {
        session.setString(DEFAULT_ENTITLEMENT, defaultEntitlement);
    }

    public String getATBpaymentGatewayId() {
        return session.getString(ATBPAYMENTGATEWAYID, "");
    }


    public void setLowBitrateMaxLimit(String lowBitrateMaxLimit) {
        session.setString(LOW_BITRATE_MAX_LIMIT, lowBitrateMaxLimit);
    }

    public String getLowBitrateMaxLimit() {
        return session.getString(LOW_BITRATE_MAX_LIMIT, "");
    }

    public void setMediumBitrateMaxLimit(String mediumBitrateMaxLimit) {
        session.setString(MEDIUM_BITRATE_MAX_LIMIT, mediumBitrateMaxLimit);
    }

    public String getMediumBitrateMaxLimit() {
        return session.getString(MEDIUM_BITRATE_MAX_LIMIT, "");
    }

    public void setHighBitrateMaxLimit(String highBitrateMaxLimit) {
        session.setString(HIGH_BITRATE_MAX_LIMIT, highBitrateMaxLimit);
    }

    public String getHighBitrateMaxLimit() {
        return session.getString(HIGH_BITRATE_MAX_LIMIT, "");
    }

    public void setATBpaymentGatewayId(String ATBpaymentGatewayId) {
        session.setString(ATBPAYMENTGATEWAYID, ATBpaymentGatewayId);
    }

    public String getSubscriptionOffer() {
        return session.getString(SUBSCRIPTION_OFFER, "");
    }

    public void setSubscriptionOffer(String SubscriptionOffer) {
        session.setString(SUBSCRIPTION_OFFER, SubscriptionOffer);
    }

    public String getPinToken() {
        return session.getString("pin_token", "");
    }

    public void setPinToken(String token) {
        session.setString("pin_token", token);
    }

    public String getRoot() {
        return session.getString("root", "");
    }

    public void setRoot(String root) {
        session.setString("root", root);
    }

    public String getFileFormat() {
        return session.getString("file_format", "");
    }

    public void setFileFormat(String fileFormat) {
        session.setString("file_format", fileFormat);
    }


    public void setReminderId(String id, boolean reminderValue) {
        session.setBoolean(REMINDER_ID + id, reminderValue);
    }

    public boolean getReminderId(String id) {
        return session.getBoolean(REMINDER_ID + id, false);
    }


    public void setLiveCatchUpId(String id, boolean catchUpValue) {
        session.setBoolean(CATCHUP_ID + id, catchUpValue);
    }

    public boolean getLiveCatchUpId(String id) {
        return session.getBoolean(CATCHUP_ID + id, false);
    }

    public void setNotificationResponse(String notificationResponse) {
        session.setString("notification_response", notificationResponse);
    }

    public String getNotificationResponse() {
        return session.getString("notification_response", "");
    }

    public void setMsisdn(String finalMsisdn) {
        session.setString("MsisdnNumber", finalMsisdn);
    }

    public String getMsisdn() {
        return session.getString("MsisdnNumber", "");
    }


    public boolean getParentalActive() {
        return session.getBoolean("parentalactive", false);
    }

    public void setParentalActive(boolean b) {
        session.setBoolean("parentalactive", b);
    }

    public void setUserRegistered(boolean isUserRegistered) {
        session.setBoolean("isUserRegistered", isUserRegistered);

    }

    public boolean getUserRegistered() {
        return session.getBoolean("isUserRegistered", false);

    }

    public void setUserSelectedRating(String userSelectedRating) {
        session.setString("userSelectedRating", userSelectedRating);
    }

    public String getUserSelectedRating() {
        return session.getString("userSelectedRating", "");
    }

    public void setCWListSize(int size) {
        session.setInt("cw_list_size", size);
    }

    public int getCWListSize() {
        return session.getInt("cw_list_size", 0);
    }

    public int getAutoDuration() {
        return session.getInt(AUTO_DURATION, 0);
    }

    public void setAutoDuration(int value) {
        session.setInt(AUTO_DURATION, value);
    }

    public boolean getAutoRotation() {
        return session.getBoolean(AUTO_ROTATE, true);
    }

    public void setAutoRotation(boolean value) {
        session.setBoolean(AUTO_ROTATE, value);
    }

    public void setUserProfileData(String profileData) {
        session.setString(GENRE_DATA, profileData);
    }

    public String getUserProfileData() {
        return session.getString(GENRE_DATA, "");
    }


    public void setFilterGenre(String profileData) {
        session.setString(FILTER_SELECTED_GENRE, profileData);
    }

    public String getFilterGenre() {
        return session.getString(FILTER_SELECTED_GENRE, "");
    }


    public void setFilterGenreSelection(String profileData) {
        session.setString(FILTER_SELECTED_GENRE_VALUE, profileData);
    }

    public String getFilterGenreSelection() {
        return session.getString(FILTER_SELECTED_GENRE_VALUE, "");
    }


    public void setFilterLanguage(String profileData) {
        session.setString(FILTER_SELECTED_LANGUAGE, profileData);
    }

    public String getFilterLanguage() {
        return session.getString(FILTER_SELECTED_LANGUAGE, "");
    }

    public void setFilterLanguageSelection(String profileData) {
        session.setString(FILTER_SELECTED_LANGUAGE_VALUES, profileData);
    }

    public String getFilterLanguageSelection() {
        return session.getString(FILTER_SELECTED_LANGUAGE_VALUES, "");
    }

    public void setFilterSortBy(String profileData) {
        session.setString(FILTER_SORT_BY, profileData);
    }

    public String getFilterSortBy() {
        return session.getString(FILTER_SORT_BY, "");
    }


    public void setFilterContentType(String profileData) {
        session.setString(FILTER_CONTENT_TYPE, profileData);
    }

    public String getFilterContentType() {
        return session.getString(FILTER_CONTENT_TYPE, "");
    }

    public void setFilterFreePaid(String profileData) {
        session.setString(FILTER_FREE_PAID, profileData);
    }

    public String getFilterFreePaid() {
        return session.getString(FILTER_FREE_PAID, "");
    }

    public void setFilterApply(String profileData) {
        session.setString(FILTER_APPLY, profileData);
    }

    public String getFilterApply() {
        return session.getString(FILTER_APPLY, "");
    }

    public void setSearchKSQL(String profileData) {
        session.setString(SEARCH_KSQL, profileData);
    }

    public String getSearchKSQL() {
        return session.getString(SEARCH_KSQL, "");
    }

    public void setAudioLanguageIndex(int index) {
        session.setInt(AUDIO_LANG_INDEX, index);
    }

    public int getAudioLanguageIndex() {
        return session.getInt(AUDIO_LANG_INDEX, -1);
    }


    public void setSubtitleLanguageIndex(int index) {
        session.setInt(SUBTITLE_LANG_INDEX, index);
    }

    public int getSubtitleLanguageIndex() {
        return session.getInt(SUBTITLE_LANG_INDEX, -1);
    }

}
