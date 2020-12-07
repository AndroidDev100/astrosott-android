package com.dialog.dialoggo.activities.subscription.manager;

import com.dialog.dialoggo.activities.subscription.callback.ChannelDataUpdateListener;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;

import java.util.List;

public class AllChannelManager {

    private static AllChannelManager mAllChannelManager;
    private List<RailCommonData> mRailCommonDataList;
    private List<PaymentItemDetail> paymentItemDetails;

    public List<PaymentItemDetail> getPaymentItemDetails() {
        return paymentItemDetails;
    }

    public void setPaymentItemDetails(List<PaymentItemDetail> paymentItemDetails) {
        this.paymentItemDetails = paymentItemDetails;
    }

    private ChannelDataUpdateListener mDataUpdateListener;
    private RailCommonData railCommonData;
    private String productId;
    private String currency;
    private String price;
    private String channelId;
    private String paymentType;
    private String transactionId;
    private String paymentTitle;

    public String getPaymentTitle() {
        return paymentTitle;
    }

    public void setPaymentTitle(String paymentTitle) {
        this.paymentTitle = paymentTitle;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private AllChannelManager(){

    }

    public static synchronized AllChannelManager getInstance(){
        if(mAllChannelManager == null){
            mAllChannelManager = new AllChannelManager();
        }
        return mAllChannelManager;
    }

    public List<RailCommonData> getRailCommonDataList() {
        return mRailCommonDataList;
    }

    public void setRailCommonDataList(List<RailCommonData> mRailCommonDataList) {
        this.mRailCommonDataList = mRailCommonDataList;
    }

    public ChannelDataUpdateListener getDataUpdateListener() {
        return mDataUpdateListener;
    }

    public void setDataUpdateListener(ChannelDataUpdateListener dataUpdateListener) {
        this.mDataUpdateListener = dataUpdateListener;
    }

    public RailCommonData getRailCommonData() {
        return railCommonData;
    }

    public void setRailCommonData(RailCommonData railCommonData) {
        this.railCommonData = railCommonData;
    }

    public void clearTempAllChannelsData(){
        mRailCommonDataList = null;
        mDataUpdateListener = null;
    }
}
