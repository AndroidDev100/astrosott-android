package com.astro.sott.callBacks.commonCallBacks;

public interface PurchaseSubscriptionCallBack {
    void response(boolean status, String errorCode, String message, String paymentGatewayReferenceId);
}
