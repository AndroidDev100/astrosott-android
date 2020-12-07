package com.dialog.dialoggo.callBacks.kalturaCallBacks;

import com.dialog.dialoggo.beanModel.VIUChannel;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.List;

public interface ContinueWatchingCallBack {
    void response(boolean status,
                  Response<ListResponse<Asset>> listResponseResponse);
}
