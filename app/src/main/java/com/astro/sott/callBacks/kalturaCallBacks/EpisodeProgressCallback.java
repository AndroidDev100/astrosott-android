package com.astro.sott.callBacks.kalturaCallBacks;

import com.kaltura.client.types.Bookmark;

import java.util.List;

public interface EpisodeProgressCallback {
    void getEpisodeProgressList(List<Bookmark> bookmarkList);
}
