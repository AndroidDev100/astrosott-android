package com.dialog.dialoggo.callBacks.kalturaCallBacks;

import com.kaltura.client.types.AppToken;
import com.kaltura.client.utils.response.base.Response;

public interface KsAppTokenCallBack {

    void success(boolean sucess, Response<AppToken> result);
    void failure(boolean failure, Response<AppToken> result);
}
