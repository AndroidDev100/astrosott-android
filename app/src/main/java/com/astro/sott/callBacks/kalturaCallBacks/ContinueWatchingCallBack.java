package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

public interface ContinueWatchingCallBack {
    void response(boolean status,
                  Response<ListResponse<Asset>> listResponseResponse);
}
