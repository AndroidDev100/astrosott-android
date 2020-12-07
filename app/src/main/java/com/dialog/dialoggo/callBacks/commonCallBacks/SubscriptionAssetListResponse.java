package com.dialog.dialoggo.callBacks.commonCallBacks;

import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.List;

public interface SubscriptionAssetListResponse {
    void response(boolean status, String message, List<Asset> listResponseResponse);

}
