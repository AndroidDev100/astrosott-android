package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.AssetHistory;

public interface NextEpisodeCallBack {

    void getNextEpisode(boolean status,AssetHistory assetHistory);
}
