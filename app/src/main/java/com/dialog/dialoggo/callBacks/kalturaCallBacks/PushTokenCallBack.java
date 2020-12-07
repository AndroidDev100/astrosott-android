package com.dialog.dialoggo.callBacks.kalturaCallBacks;

import com.kaltura.client.utils.response.base.Response;

public interface PushTokenCallBack {
    void result(boolean status, Response<Boolean> result);
}
