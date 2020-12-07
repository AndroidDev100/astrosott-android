package com.dialog.dialoggo.callBacks.kalturaCallBacks;

import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.UserAssetRule;
import com.kaltura.client.utils.response.base.Response;

public interface AssetRuleCallback {
    void getAssetrule(boolean status,Response<ListResponse<UserAssetRule>> response,int totalCount, String errorCode, String message);
}
