
package com.dialog.dialoggo.modelClasses.dmsResponse;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Params {
    @SerializedName("updatedVersion")
    @Expose
    private String updatedVersion;
    @SerializedName("FilesFormat")
    @Expose
    private FilesFormat filesFormat;
    @SerializedName("Gateways")
    @Expose
    private Gateways gateways;

    public String getATBpaymentGatewayId() {
        return ATBpaymentGatewayId;
    }

    public void setATBpaymentGatewayId(String ATBpaymentGatewayId) {
        this.ATBpaymentGatewayId = ATBpaymentGatewayId;
    }

    @SerializedName("Categories")
    @Expose
    private Categories categories;
    @SerializedName("DefaultEntitlement")
    @Expose
    private String defaultEntitlement;

    @SerializedName("ATBpaymentGatewayId")
    @Expose
    private String ATBpaymentGatewayId;

    @SerializedName("SubscriptionOffer")
    @Expose
    private String SubscriptionOffer;

    public String getSubscriptionOffer() {
        return SubscriptionOffer;
    }

    public void setSubscriptionOffer(String subscriptionOffer) {
        SubscriptionOffer = subscriptionOffer;
    }

    @SerializedName("MediaTypes")
    @Expose
    private MediaTypes mediaTypes;

    @SerializedName("ParentalLevels")
    @Expose
    private JsonObject parentalLevels;

    public JsonObject getParentalLevels() {
        return parentalLevels;
    }

    @SerializedName("ParentalRating")
    @Expose
    private JsonObject parentalRatings;

    public JsonObject getParentalRatings() {
        return parentalRatings;
    }

//    public void setParentalLevels(ParentalLevels parentalLevels) {
//        this.parentalLevels = parentalLevels;
//    }

    @SerializedName("DefaultParentalLevel")
    @Expose
    private String defaultParentalLevel;

    public String getDefaultParentalLevel() {
        return defaultParentalLevel;
    }

    public void setDefaultParentalLevel(String defaultParentalLevel) {
        this.defaultParentalLevel = defaultParentalLevel;
    }

    private ParentalDefaultRule ParentalDefaultRule;

    public FilesFormat getFilesFormat() {
        return filesFormat;
    }

    public void setFilesFormat(FilesFormat filesFormat) {
        this.filesFormat = filesFormat;
    }

    public Gateways getGateways() {
        return gateways;
    }

    public void setGateways(Gateways gateways) {
        this.gateways = gateways;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public String getDefaultEntitlement() {
        return defaultEntitlement;
    }

    public void setDefaultEntitlement(String defaultEntitlement) {
        this.defaultEntitlement = defaultEntitlement;
    }

    public String getUpdatedVersion() {
        return updatedVersion;
    }

    public void setUpdatedVersion(String updatedVersion) {
        this.updatedVersion = updatedVersion;
    }

    public MediaTypes getMediaTypes() {
        return mediaTypes;
    }

    public void setMediaTypes(MediaTypes mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

    public ParentalDefaultRule getParentalDefaultRule() {
        return ParentalDefaultRule;
    }

    public void setParentalDefaultRule(ParentalDefaultRule parentalDefaultRule) {
        this.ParentalDefaultRule = parentalDefaultRule;
    }
}
