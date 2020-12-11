package com.astro.sott.callBacks.commonCallBacks;

import com.kaltura.client.types.APIException;

public interface CommonResponseCallBack {
    void onSuccess();
    void onFailure(APIException error);
}
