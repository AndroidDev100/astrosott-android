package com.dialog.dialoggo.activities.myPlans.models;

public class SubscriptionPlanPackageModel {


    public static final int SUBSCRIPTION_TITLE_TYPE=0;
    public static final int SUBSCRIPTION_DESC_TYPE=1;
    public static final int SUBSCRIPTION_CHANNEL_TYPE=2;

    public int type;
    private String title;
    private String packageTitle,packageCharge,packageDesc,packagestartDate,packageEndDate,channelTitle,channelDesc;
    private String currency;

    public SubscriptionPlanPackageModel(int type, String title, String packageTitle,String currency, String packageCharge, String packageDesc, String packagestartDate, String packageEndDate, String channelTitle, String channelDesc) {
        this.type = type;
        this.title = title;
        this.packageTitle = packageTitle;
        this.packageCharge = packageCharge;
        this.packageDesc = packageDesc;
        this.packagestartDate = packagestartDate;
        this.packageEndDate = packageEndDate;
        this.channelTitle = channelTitle;
        this.channelDesc = channelDesc;
        this.currency = currency;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPackageTitle() {
        return packageTitle;
    }

    public void setPackageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
    }

    public String getPackageCharge() {
        return packageCharge;
    }

    public void setPackageCharge(String packageCharge) {
        this.packageCharge = packageCharge;
    }

    public String getPackageDesc() {
        return packageDesc;
    }

    public void setPackageDesc(String packageDesc) {
        this.packageDesc = packageDesc;
    }

    public String getPackagestartDate() {
        return packagestartDate;
    }

    public void setPackagestartDate(String packagestartDate) {
        this.packagestartDate = packagestartDate;
    }

    public String getPackageEndDate() {
        return packageEndDate;
    }

    public void setPackageEndDate(String packageEndDate) {
        this.packageEndDate = packageEndDate;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getChannelDesc() {
        return channelDesc;
    }

    public void setChannelDesc(String channelDesc) {
        this.channelDesc = channelDesc;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
