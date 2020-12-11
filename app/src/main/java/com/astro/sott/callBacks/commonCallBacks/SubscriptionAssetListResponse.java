package com.astro.sott.callBacks.commonCallBacks;

import com.kaltura.client.types.Asset;

import java.util.List;

public interface SubscriptionAssetListResponse {
    void response(boolean status, String message, List<Asset> listResponseResponse);

}
