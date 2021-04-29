package com.astro.sott.callBacks.commonCallBacks;

public interface ChangePlanCallBack {
    void onClick(String paymentType);
    void onCancel(String serviceId,String type);
}
