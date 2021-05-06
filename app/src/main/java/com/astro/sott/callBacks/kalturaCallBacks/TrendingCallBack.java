package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;

import java.util.List;

public interface TrendingCallBack {
    void getList(boolean status, List<Asset> assetListResponse,int totalCount);
}
