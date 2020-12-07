package com.dialog.dialoggo.callBacks.kalturaCallBacks;

import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

public interface PopularSearchCallBack {
    void response(boolean status, Response<ListResponse<Asset>> result);
}
