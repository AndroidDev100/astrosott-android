package com.astro.sott.callBacks.commonCallBacks;

import com.astro.sott.db.search.SearchedKeywords;

import java.util.List;

public interface RecentSearchCallBack {
    void recentSearches(List<SearchedKeywords> searchedKeywords);
}
