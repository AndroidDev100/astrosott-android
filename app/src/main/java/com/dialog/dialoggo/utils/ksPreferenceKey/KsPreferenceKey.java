package com.dialog.dialoggo.utils.ksPreferenceKey;

import android.content.Context;

import com.dialog.dialoggo.utils.helpers.SharedPrefHelper;
import com.google.gson.Gson;
import com.kaltura.client.types.OTTUser;

public class KsPreferenceKey {

    private static final String USER = "User";
    private static final String DEFAULT_ENTITLEMENT = "DefaultEntitlement";
    private static final String ATBPAYMENTGATEWAYID ="ATBpaymentGatewayId";
    public static final String  SUBSCRIPTION_OFFER = "SubscriptionOffer";
    private static KsPreferenceKey mInstance;
    private SharedPrefHelper session;
    private static final String REMINDER_ID="reminder_id";
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

    public String getHouseHoldId() {
        return session.getString("household_id", "");
    }

    public void setHouseHoldId(String houseHoldId) {
        session.setString("household_id", houseHoldId);
    }

    public String getUserType(){
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

    public boolean getPrefrenceSelected() {
        return session.getBoolean("p_selected", false);
    }

    public void setPrefrenceSelected(boolean prefrenceSelected) {
        session.setBoolean("p_selected", prefrenceSelected);
    }

    public OTTUser getUser() {

        try{
            Gson gson = new Gson();
            String json = session.getString(USER, "");

            return gson.fromJson(json, OTTUser.class);
        }catch (Exception e){
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
        session.setBoolean(REMINDER_ID+id, reminderValue);
    }

    public boolean getReminderId(String id)
    {
        return session.getBoolean(REMINDER_ID+id,false);
    }


    public void setLiveCatchUpId(String id, boolean catchUpValue) {
        session.setBoolean(CATCHUP_ID+id, catchUpValue);
    }

    public boolean getLiveCatchUpId(String id)
    {
        return session.getBoolean(CATCHUP_ID+id,false);
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
    public String getMsisdn(){
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
      return   session.getBoolean("isUserRegistered", false);

    }

    public void setUserSelectedRating(String userSelectedRating) {
        session.setString("userSelectedRating", userSelectedRating);
    }
    public String getUserSelectedRating(){
        return session.getString("userSelectedRating", "");
    }

    public void setCWListSize(int size) {
        session.setInt("cw_list_size", size);
    }

    public int getCWListSize() {
        return session.getInt("cw_list_size", 0);
    }
}
