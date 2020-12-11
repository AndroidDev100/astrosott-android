package com.astro.sott.callBacks.commonCallBacks;

import com.kaltura.client.types.HouseholdPaymentMethod;

import java.util.List;

public interface HouseholdpaymentResponseCallBack {
    void response(boolean status, String errorCode, String message, List<HouseholdPaymentMethod> householdPaymentMethodList);
}
