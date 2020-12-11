package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.PersonalList;
import com.kaltura.client.utils.response.base.Response;

public interface WatchlistCallBack {
    void getWatchlistDetail(Boolean status, String errorCode, Response<ListResponse<PersonalList>> result);
}
