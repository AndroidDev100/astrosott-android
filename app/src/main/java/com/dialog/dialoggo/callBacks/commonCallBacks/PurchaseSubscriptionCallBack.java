package com.dialog.dialoggo.callBacks.commonCallBacks;

public interface PurchaseSubscriptionCallBack {
    void response(boolean status, String errorCode, String message, String paymentGatewayReferenceId);
}
