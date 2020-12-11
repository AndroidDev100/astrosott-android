package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.ProductPrice;
import com.kaltura.client.utils.response.base.Response;

public interface ProductPriceCallBack {
    void getProductprice(boolean status,Response<ListResponse<ProductPrice>> response,String purchaseKey, String errorCode, String message);

}
