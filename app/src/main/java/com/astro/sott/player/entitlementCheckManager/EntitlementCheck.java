package com.astro.sott.player.entitlementCheckManager;

import android.content.Context;

import com.astro.sott.callBacks.kalturaCallBacks.ProductPriceCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.ProductPriceStatusCallBack;
import com.astro.sott.networking.errorCallBack.ErrorCallBack;
import com.astro.sott.networking.ksServices.KsServices;

public class EntitlementCheck {

    private ProductPriceCallBack productPriceCallBack;
    public static final String SVOD = "SVOD";
    public static final String TVOD = "TVOD";
    public static final String FREE = "FREE";


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
                productPriceCallBack.getProductprice(true, response, response.results.getObjects().get(0).getPurchaseStatus().toString(), "", "");
            } else {
                productPriceCallBack.getProductprice(false, null, "", "", new ErrorCallBack().ErrorMessage(errorCode, message));
            }
        });
    }

    public void checkAssetPurchaseStatus(Context context, String fileId, ProductPriceStatusCallBack callBack) {
        KsServices ksServices = new KsServices(context);

        ksServices.getAssetPurchaseStatus(fileId, (status, response, purchaseKey, errorCode, message) -> {
            if (status) {
                if (purchaseKey.equalsIgnoreCase("for_purchase_subscription_only")) {
                    callBack.getProductprice(true, false, SVOD, response.results.getObjects().get(0).getPurchaseStatus().toString(), "", "");
                } else if (purchaseKey.equalsIgnoreCase("for_purchase")) {
                    callBack.getProductprice(true, false, TVOD, response.results.getObjects().get(0).getPurchaseStatus().toString(), "", "");
                } else if (purchaseKey.equalsIgnoreCase("subscription_purchased")) {
                    callBack.getProductprice(true, true, SVOD, response.results.getObjects().get(0).getPurchaseStatus().toString(), "", "");
                } else if (purchaseKey.equalsIgnoreCase("ppv_purchased")) {
                    callBack.getProductprice(true, true, TVOD, response.results.getObjects().get(0).getPurchaseStatus().toString(), "", "");
                } else {
                    callBack.getProductprice(true, true, FREE, response.results.getObjects().get(0).getPurchaseStatus().toString(), "", "");
                }
            } else {
                callBack.getProductprice(false, false, null, "", "", new ErrorCallBack().ErrorMessage(errorCode, message));
            }
        });
    }
}
