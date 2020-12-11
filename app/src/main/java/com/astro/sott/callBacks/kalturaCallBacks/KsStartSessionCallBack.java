package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.SessionInfo;
import com.kaltura.client.utils.response.base.Response;

public interface KsStartSessionCallBack {
    void success(boolean sucess, Response<SessionInfo> result);
    void failure(boolean failure, Response<SessionInfo> result);
}
