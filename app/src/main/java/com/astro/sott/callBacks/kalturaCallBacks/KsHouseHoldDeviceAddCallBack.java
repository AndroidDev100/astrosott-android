package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.HouseholdDevice;
import com.kaltura.client.utils.response.base.Response;

public interface KsHouseHoldDeviceAddCallBack {

    void success(boolean sucess, Response<HouseholdDevice> result);
    void failure(boolean failure, String errorCode, String message, Response<HouseholdDevice> result);
}
