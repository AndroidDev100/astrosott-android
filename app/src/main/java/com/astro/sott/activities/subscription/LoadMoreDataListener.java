package com.astro.sott.activities.subscription;

import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;

import java.util.List;

public interface LoadMoreDataListener {
    void onLoadMore(List<RailCommonData> moreChannelList);
}
