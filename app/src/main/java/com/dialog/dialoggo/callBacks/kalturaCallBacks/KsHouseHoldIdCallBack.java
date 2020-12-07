package com.dialog.dialoggo.callBacks.kalturaCallBacks;

import com.kaltura.client.types.Household;
import com.kaltura.client.utils.response.base.Response;

public interface KsHouseHoldIdCallBack {

    void success(boolean sucess, Response<Household> result);
    void failure(boolean failure, Response<Household> result);
}
