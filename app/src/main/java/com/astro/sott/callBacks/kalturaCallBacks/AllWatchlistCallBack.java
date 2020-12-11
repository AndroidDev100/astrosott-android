package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.List;

public interface AllWatchlistCallBack {
    void getAllWatchlistDetail(Boolean status, String errorCode, String message, List<Response<ListResponse<Asset>>> listResponseResponse);
}
