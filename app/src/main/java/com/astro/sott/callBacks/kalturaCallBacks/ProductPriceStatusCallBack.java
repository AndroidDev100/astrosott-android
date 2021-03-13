package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.ProductPrice;
import com.kaltura.client.utils.response.base.Response;

public interface ProductPriceStatusCallBack {
    void getProductprice(boolean apiStatus,boolean purchasedStatus,String vodType,String purchaseKey, String errorCode, String message);

}
