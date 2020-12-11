package com.astro.sott.callBacks.commonCallBacks;

import com.kaltura.client.types.FollowTvSeries;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

public interface FollowTvSeriesCallBack {
    void getSeriesFollowList(Boolean status, String errorCode, String message, Response<ListResponse<FollowTvSeries>> result);
}
