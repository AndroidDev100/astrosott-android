package com.dialog.dialoggo.player.geoBlockingManager;

import android.content.Context;

import com.dialog.dialoggo.callBacks.kalturaCallBacks.AssetRuleCallback;
import com.dialog.dialoggo.networking.errorCallBack.ErrorCallBack;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.kaltura.client.types.Asset;

public class GeoBlockingCheck {
    private AssetRuleCallback assetRuleCallback;

    public void aseetAvailableOrNot(Context context, Asset asset, AssetRuleCallback callback) {
        assetRuleCallback = callback;
        KsServices ksServices = new KsServices(context);

        ksServices.assetRuleApi(asset.getId(), (status, response, count,errorcode,message) -> {

            if (status) {
                int totalCount = response.results.getTotalCount();
                assetRuleCallback.getAssetrule(true, response, totalCount,"","");
            } else {
                assetRuleCallback.getAssetrule(false, response, -1,"",new ErrorCallBack().ErrorMessage(errorcode,message));
            }
        });
    }
}
