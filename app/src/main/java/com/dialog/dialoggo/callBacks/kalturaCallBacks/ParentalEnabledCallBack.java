package com.dialog.dialoggo.callBacks.kalturaCallBacks;

import com.kaltura.client.types.APIException;

public interface ParentalEnabledCallBack {
    void onParentalEnableSuccess();
    void onParentalEnableFailure(APIException error);
}
