package com.dialog.dialoggo.player.entitlementCheckManager;

import android.content.Context;

import com.dialog.dialoggo.callBacks.kalturaCallBacks.ProductPriceCallBack;
import com.dialog.dialoggo.networking.errorCallBack.ErrorCallBack;
import com.dialog.dialoggo.networking.ksServices.KsServices;

public class EntitlementCheck {

    private ProductPriceCallBack productPriceCallBack;

    public void checkAssetType(Context context, String fileId, ProductPriceCallBack callBack) {
        productPriceCallBack = callBack;
        KsServices ksServices = new KsServices(context);

//        ksServices.getAssetPurchaseStatus(fileId, (status, response, purchaseKey) -> {
//            if (status) {
//                productPriceCallBack.getProductprice(true, response, response.results.getObjects().get(0).getPurchaseStatus().toString(),"","");
//            }
//        });

        ksServices.getAssetPurchaseStatus(fileId, (status, response, purchaseKey, errorCode, message) -> {
            if (status) {
                productPriceCallBack.getProductprice(true, response, response.results.getObjects().get(0).getPurchaseStatus().toString(),"","");
            }else {
                productPriceCallBack.getProductprice(false, null, "","",new ErrorCallBack().ErrorMessage(errorCode,message));
            }
        });
    }
}
