
package com.astro.sott.modelClasses.dmsResponse;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    @SerializedName("FilterLanguageValues")
    @Expose
    private JsonObject filterLanguages;

    @SerializedName("AudioLanguages")
    @Expose
    private JsonObject AudioLanguages;

    @SerializedName("SubtitleLanguages")
    @Expose
    private JsonObject SubtitleLanguages;

    @SerializedName("AssetHistoryDays")
    @Expose
    private AssetHistoryDays assetHistoryDays;

    @SerializedName("FilterValues")
    @Expose
    private JsonObject FilterValues;

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

    @SerializedName("download_limit_days")
    @Expose
    private String download_limit_days;

    public void setDownload_limit_days(String download_limit_days) {
        this.download_limit_days = download_limit_days;
    }

    public String getDownload_limit_days() {
        return download_limit_days;
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


    @SerializedName("ParentalRatingLevels")
    @Expose
    private JsonObject ParentalRatingLevels;

    public void setParentalRatingLevels(JsonObject parentalRatingLevels) {
        ParentalRatingLevels = parentalRatingLevels;
    }

    public JsonObject getParentalRatingLevels() {
        return ParentalRatingLevels;
    }

    public JsonObject getParentalRatings() {
        return parentalRatings;
    }

//    public void setParentalLevels(ParentalLevels parentalLevels) {
//        this.parentalLevels = parentalLevels;
//    }

    @SerializedName("DefaultParentalLevel")
    @Expose
    private String defaultParentalLevel;


    @SerializedName("apiProxyUrlKaltura")
    @Expose
    private String apiProxyUrlKaltura;

    @SerializedName("apiProxyUrlEvergent")
    @Expose
    private String apiProxyUrlEvergent;

    @SerializedName("apiProxyUrlExpManager")
    @Expose
    private String apiProxyUrlExpManager;

    @SerializedName("AdTagURL")
    @Expose
    private AdTagURL AdTagURL;


    @SerializedName("lowBitRateMaxLimit")
    @Expose
    private String lowBitRateMaxLimit;

    @SerializedName("mediumBitRateMaxLimit")
    @Expose
    private String mediumBitRateMaxLimit;

    public String getLowBitRateMaxLimit() {
        return lowBitRateMaxLimit;
    }

    public void setLowBitRateMaxLimit(String lowBitRateMaxLimit) {
        this.lowBitRateMaxLimit = lowBitRateMaxLimit;
    }

    public String getMediumBitRateMaxLimit() {
        return mediumBitRateMaxLimit;
    }

    public void setMediumBitRateMaxLimit(String mediumBitRateMaxLimit) {
        this.mediumBitRateMaxLimit = mediumBitRateMaxLimit;
    }

    public String getHighBitRatemaxLimit() {
        return highBitRatemaxLimit;
    }

    public void setHighBitRatemaxLimit(String highBitRatemaxLimit) {
        this.highBitRatemaxLimit = highBitRatemaxLimit;
    }

    @SerializedName("highBitRatemaxLimit")
    @Expose
    private String highBitRatemaxLimit;


    public com.astro.sott.modelClasses.dmsResponse.AdTagURL getAdTagURL() {
        return AdTagURL;
    }

    public String getApiProxyUrlEvergent() {
        return apiProxyUrlEvergent;
    }

    public String getApiProxyUrlExpManager() {
        return apiProxyUrlExpManager;
    }

    public String getApiProxyUrlKaltura() {
        return apiProxyUrlKaltura;
    }

    public String getDefaultParentalLevel() {
        return defaultParentalLevel;
    }

    public void setDefaultParentalLevel(String defaultParentalLevel) {
        this.defaultParentalLevel = defaultParentalLevel;
    }

    public AssetHistoryDays getAssetHistoryDays() {
        return assetHistoryDays;
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

    public void setFilterLanguages(JsonObject filterLanguages) {
        this.filterLanguages = filterLanguages;
    }

    public JsonObject getFilterLanguages() {
        return filterLanguages;
    }

    public JsonObject getAudioLanguages() {
        return AudioLanguages;
    }

    public void setAudioLanguages(JsonObject parentalDefaultRule) {
        this.AudioLanguages = parentalDefaultRule;
    }

    public void setSubtitleLanguages(JsonObject subtitleLanguages) {
        SubtitleLanguages = subtitleLanguages;
    }

    public JsonObject getSubtitleLanguages() {
        return SubtitleLanguages;
    }

    public void setFilterValues(JsonObject filterValues) {
        FilterValues = filterValues;
    }

    public JsonObject getFilterValues() {
        return FilterValues;
    }
}
