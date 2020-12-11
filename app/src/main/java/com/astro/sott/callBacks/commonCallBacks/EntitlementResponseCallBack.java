package com.astro.sott.callBacks.commonCallBacks;

import com.kaltura.client.types.Entitlement;

import java.util.List;

public interface EntitlementResponseCallBack {
    void response(boolean status, String errorCode, String message, List<Entitlement> entitlementListRespone);
}
