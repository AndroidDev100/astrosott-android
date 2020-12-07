package com.dialog.dialoggo.callBacks.commonCallBacks;

import java.util.List;

public interface SubscriptionResponseCallBack {
    void response(boolean status, String errorCode, String message, List<com.kaltura.client.types.Subscription> subscriptionListRespone);
    default void response2(boolean status, String errorCode, String message,List<com.kaltura.client.types.Subscription> list){}
}
