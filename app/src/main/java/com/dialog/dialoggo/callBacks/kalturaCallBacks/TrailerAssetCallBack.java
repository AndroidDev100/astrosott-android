package com.dialog.dialoggo.callBacks.kalturaCallBacks;

import com.kaltura.client.types.Asset;

import java.util.List;

public interface TrailerAssetCallBack {
    void getTrailorAsset(boolean status, List<Asset> asset);
}
