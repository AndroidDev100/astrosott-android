package com.dialog.dialoggo.callBacks.kalturaCallBacks;

import com.kaltura.client.types.HouseholdDevice;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

public interface KsHouseHoldDevice {

    void success(boolean sucess, Response<ListResponse<HouseholdDevice>> result);
    void failure(boolean failure, Response<ListResponse<HouseholdDevice>> result);
}
