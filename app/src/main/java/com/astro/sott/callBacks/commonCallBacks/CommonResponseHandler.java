package com.astro.sott.callBacks.commonCallBacks;

import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

public interface CommonResponseHandler {
    void response(boolean status, String errorCode, String message, Response<ListResponse<Asset>> assetList);
}
