package com.dialog.dialoggo.callBacks.commonCallBacks;

import com.kaltura.client.types.Entitlement;

import java.util.List;

public interface CancelRenewalResponseCallBack {
    void response(boolean status, String errorCode, String message);
}
