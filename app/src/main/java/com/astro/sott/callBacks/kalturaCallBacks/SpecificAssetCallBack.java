package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.Asset;

public interface SpecificAssetCallBack {
    void getAsset(boolean status, Asset asset);
}
